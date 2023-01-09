package team.arcanism.Block.ArcaneInfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;
import team.arcanism.Arcanism;
import team.arcanism.ModUtil;

public class ArcaneInfuserBlock extends Block implements EntityBlock {

	private static final TagKey<Item> activation_items = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Arcanism.MODID, "infuser_activators"));

	public ArcaneInfuserBlock(Properties prop) {
		super(prop);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ArcaneInfuserBlockEntity(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean b) {
		if (!state.is(state2.getBlock())) {
			ModUtil.dropItemHandlerInWorld(level.getBlockEntity(pos));
			super.onRemove(state, level, pos, state2, b);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!level.isClientSide) {
			if (level.getBlockEntity(pos) instanceof ArcaneInfuserBlockEntity infuser) {
				if (player.getItemInHand(hand).getTags().anyMatch(t -> (t.equals(activation_items)))) {
					infuser.tryCraft(player);
				} else {
					MenuProvider menuProvider = new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.translatable("block.arcanism.arcane_infuser");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
							return new ArcaneInfuserContainer(id, pos, playerInventory, player);
						}
					};
					NetworkHooks.openScreen((ServerPlayer) player, menuProvider, infuser.getBlockPos());
				}
			}
		}
		return InteractionResult.SUCCESS;
	}
}
