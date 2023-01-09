package team.arcanism.Block.ArcaneInfuser;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import team.arcanism.Capability.IAether;
import team.arcanism.Registry.BlockEntityRegistry;
import team.arcanism.Registry.CapabilityRegistry;
import team.arcanism.Registry.RecipeRegistry;
import team.arcanism.Registry.SoundRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ArcaneInfuserBlockEntity extends BlockEntity {

	private final ItemStackHandler itemHandler = createItemHandler();
	private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

	public ArcaneInfuserBlockEntity(BlockPos pos, BlockState state) {
		super(BlockEntityRegistry.arcane_infuser.get(), pos, state);
	}

	public void tryCraft(Player player) {
		for (final ArcaneInfuserRecipe recipe : player.level.getRecipeManager().getAllRecipesFor(RecipeRegistry.arcane_infuser_type.get())) {
			List<ItemStack> items = new ArrayList<>();
			List<Ingredient> requirements = recipe.getInputs();

			for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
				items.add(itemHandler.getStackInSlot(slot).copy());
			}

			//ingredient 0 is actually center slot for table, make sure its correct
			if (requirements.get(0).test(itemHandler.getStackInSlot(0))) {
				//check if we have all inputs
				if (RecipeMatcher.findMatches(items, requirements) != null) {
					//make sure we have aether for this craft
					IAether aether = CapabilityRegistry.getAether(player).orElseGet(null);
					if (aether != null) {
						if (aether.canSpend(recipe.getAetherCost())) {

							//spawn output in world, and update block for blockentity renderer
							ItemEntity outputItem = new ItemEntity(level, getBlockPos().getX() + 0.5f, getBlockPos().getY() + 1, getBlockPos().getZ() + 0.5f, recipe.getOutput().copy());
							level.addFreshEntity(outputItem);
							level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);

							//remove 1 from each input (ingredients dont support count (I THINK?))
							for (int slot = 0; slot < itemHandler.getSlots() - 1; slot++) {
								itemHandler.extractItem(slot, 1, false);
							}
							aether.spend(recipe.getAetherCost());
							CapabilityRegistry.sendAetherPacket(player);
							level.playSound(null, getBlockPos(), SoundRegistry.arcane_infuser.get(), SoundSource.BLOCKS, 0.5f, 1f - level.random.nextFloat() * 0.2f);
							for (int i = 0; i < 10; i++) {
								((ServerLevel) level).sendParticles(ParticleTypes.FIREWORK, getBlockPos().getX() + level.random.nextFloat(), getBlockPos().getY() + 1 + level.random.nextFloat() * 0.5, getBlockPos().getZ() + level.random.nextFloat(), 1, 0.0D, 0D, 0.0D, 0.01);
							}
						} else {
							player.displayClientMessage(Component.translatable("status.arcanism.no_aether").withStyle(ChatFormatting.RED), true);
						}
						return;
					}
				}
			}
		}
		player.displayClientMessage(Component.translatable("status.arcanism.wrong").withStyle(ChatFormatting.RED), true);
	}

	public boolean hasValidRecipe() {
		for (final ArcaneInfuserRecipe recipe : level.getRecipeManager().getAllRecipesFor(RecipeRegistry.arcane_infuser_type.get())) {
			List<ItemStack> items = new ArrayList<>();
			List<Ingredient> requirements = recipe.getInputs();

			for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
				items.add(itemHandler.getStackInSlot(slot).copy());
			}

			//ingredient 0 is actually center slot for table, make sure its correct
			if (requirements.get(0).test(itemHandler.getStackInSlot(0))) {
				//check if we have all inputs
				if (RecipeMatcher.findMatches(items, requirements) != null) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public void setRemoved() {
		super.setRemoved();
		handler.invalidate();
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER) {
			return handler.cast();
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void load(CompoundTag tag) {
		if (tag.contains("items")) {
			itemHandler.deserializeNBT(tag.getCompound("items"));
		}
		super.load(tag);
	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		tag.put("items", itemHandler.serializeNBT());
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		this.saveAdditional(nbt);
		return nbt;
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	private ItemStackHandler createItemHandler() {
		return new ItemStackHandler(9) {

			@Override
			protected void onContentsChanged(int slot) {
				setChanged();
			}

			@Override
			public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
				return true;
			}

			@Nonnull
			@Override
			public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
				return super.insertItem(slot, stack, simulate);
			}
		};
	}
}
