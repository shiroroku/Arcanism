package team.arcanism.Block.MortarPestle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.arcanism.Arcanism;
import team.arcanism.Capability.IIngredientKnowledge;
import team.arcanism.Elixir.ElixirUtil;
import team.arcanism.Elixir.IElixirIngredient;
import team.arcanism.Registry.BlockEntityRegistry;
import team.arcanism.Registry.CapabilityRegistry;
import team.arcanism.Registry.ItemRegistry;
import team.arcanism.Registry.RecipeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class MortarPestleBlockEntity extends BlockEntity {

	private final ItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
	public static final int spinTimeDefault = 20;
	public static final int spinAmounts = 4;

	public int spinTime = 0;
	public int spinsLeft = 0;

	public MortarPestleBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.mortar_and_pestle.get(), pos, state);
	}

	public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState blockState, T t) {
		if (t instanceof MortarPestleBlockEntity mortar) {
			if (mortar.spinTime > 0) {
				mortar.spinTime--;
				if (mortar.spinTime % 5 == 0) {
					for (int i = 0; i < mortar.itemHandler.getSlots(); i++) {
						level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.1f, pos.getZ() + 0.5, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.25f, 0.5f, false);
						if (!mortar.itemHandler.getStackInSlot(i).isEmpty()) {
							level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, mortar.itemHandler.getStackInSlot(i)), pos.getX() + 0.5, pos.getY() + 0.1f, pos.getZ() + 0.5, (-0.5D + level.random.nextDouble()) * 0.1, (level.random.nextDouble()) * 0.2, (-0.5D + level.random.nextDouble()) * 0.1);
						}
					}
				}
				if (mortar.spinTime == 0 && !level.isClientSide) {
					mortar.spinsLeft--;

					if (mortar.spinsLeft == 0) {
						mortar.tryCraft(level);
						mortar.spinsLeft = spinAmounts;
					}
				}
			}
		}
	}

	public void tryCraft(Level level) {
		if (!level.isClientSide) {
			canCraft(level).ifPresent(recipe -> {
				ItemEntity outputEntity = new ItemEntity(level, this.getBlockPos().getX() + 0.5f, this.getBlockPos().getY() + 0.5f, this.getBlockPos().getZ() + 0.5f, recipe.getOutput().copy());
				level.addFreshEntity(outputEntity);
				for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
					itemHandler.extractItem(slot, 1, false);
				}
				level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);

				//ingredient knowledge
				if (recipe.getOutput().getItem() == ItemRegistry.elixir.get()) {
					int slot = 1;
					Player learnedPlayer = level.getNearestPlayer(this.getBlockPos().getX() + 0.5f, this.getBlockPos().getY() + 0.5f, this.getBlockPos().getZ() + 0.5f, 5, false);
					for (Ingredient I : recipe.getInputs()) {
						ItemStack ingredient = Arrays.stream(I.getItems()).findFirst().orElseGet(null);
						if (ingredient != null && ingredient.getItem() instanceof IElixirIngredient) {
							IIngredientKnowledge knowledge = CapabilityRegistry.getIngredientKnowledge(learnedPlayer).orElseGet(null);
							if (knowledge != null) {
								knowledge.setKnows(ingredient.getItem(), slot, true);
							}
						}
						slot++;
					}
					CapabilityRegistry.sendIngredientKnowledgePacket(learnedPlayer);
				}
			});
		}
	}

	public Optional<MortarPestleRecipe> canCraft(Level level) {
		for (final MortarPestleRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeRegistry.mortar_and_pestle_type.get())) {
			List<ItemStack> items = new ArrayList<>();
			List<Ingredient> requirements = recipe.getInputs();

			//todo we probably dont need this remove later pls
			for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
				if (!itemHandler.getStackInSlot(slot).isEmpty()) {
					ItemStack slotstack = itemHandler.getStackInSlot(slot).copy();
					slotstack.setCount(1);
					items.add(slotstack);
				}
			}

			if (RecipeMatcher.findMatches(items, requirements) != null) {
				return Optional.of(recipe);
			}
		}

		//Elixir mixing
		//todo make ingredients data bound <---

		//make sure we only have 3 items
		if (itemHandler.getStackInSlot(3).isEmpty()) {
			List<IElixirIngredient> ingredients = new ArrayList<>();
			NonNullList<Ingredient> recipeIng = NonNullList.create();
			for (int i = 0; i < itemHandler.getSlots() - 1; i++) {
				//only ingredients accepted
				if (!(itemHandler.getStackInSlot(i).getItem() instanceof IElixirIngredient)) {
					return Optional.empty();
				}
				recipeIng.add(Ingredient.of(itemHandler.getStackInSlot(i).getItem()));
				ingredients.add((IElixirIngredient) itemHandler.getStackInSlot(i).getItem());
			}

			//only different ingredients accepted
			int match = 0;
			for (IElixirIngredient ingredient1 : ingredients) {
				for (IElixirIngredient ingredient2 : ingredients) {
					if (ingredient1.equals(ingredient2)) {
						match++;
						if (match > 1) {
							return Optional.empty();
						}
					}
				}
				match = 0;
			}

			ItemStack elixir = ElixirUtil.createElixir(new ItemStack(ItemRegistry.elixir.get()), ingredients);
			return Optional.of(new MortarPestleRecipe(new ResourceLocation(Arcanism.MODID, "elixir_recipe"), recipeIng, elixir));

		}

		return Optional.empty();
	}

	public void startSpin() {
		if (spinTime <= 0) {
			spinTime = spinTimeDefault;
			level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("items")) {
			itemHandler.deserializeNBT(tag.getCompound("items"));
		}
		spinTime = tag.getInt("spin_time");
		spinsLeft = tag.getInt("spins_left");
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("items", itemHandler.serializeNBT());
		tag.putInt("spin_time", spinTime);
		tag.putInt("spins_left", spinsLeft);
	}

	private ItemStackHandler createItemHandler() {
		return new ItemStackHandler(4) {

			@Override
			protected void onContentsChanged(int slot) {
				spinTime = 0;
				spinsLeft = spinAmounts;
				setChanged();
			}

			@Override
			protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
				return 1;
			}
		};
	}

}
