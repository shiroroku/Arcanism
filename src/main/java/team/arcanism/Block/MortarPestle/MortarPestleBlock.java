package team.arcanism.Block.MortarPestle;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.CapabilityItemHandler;
import org.jetbrains.annotations.Nullable;
import team.arcanism.ModUtil;
import team.arcanism.Registry.BlockEntityRegistry;

public class MortarPestleBlock extends Block implements EntityBlock {

	private static final VoxelShape shape = Block.box(4, 0, 4, 12, 6, 12);

	public MortarPestleBlock(Properties prop) {
		super(prop);
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

		if (level.getBlockEntity(pos) instanceof MortarPestleBlockEntity mortar && !level.isClientSide && mortar.spinTime <= 0) {
			if (player.getItemInHand(hand).isEmpty()) {
				if (player.isCrouching()) {
					mortar.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
						for (int i = handler.getSlots() - 1; i >= 0; i--) {
							if (!handler.getStackInSlot(i).isEmpty()) {
								ItemEntity outputEntity = new ItemEntity(level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, handler.extractItem(i, 1, false));
								level.addFreshEntity(outputEntity);
								level.sendBlockUpdated(pos, state, state, 2);
								break;
							}
						}
					});
				} else {
					if (!level.isClientSide) {
						mortar.canCraft(level).ifPresent(recipe -> {
							mortar.startSpin();
						});
					}
				}
			} else {
				mortar.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
					for (int i = 0; i < handler.getSlots(); i++) {
						if (handler.getStackInSlot(i).isEmpty()) {
							player.setItemInHand(hand, handler.insertItem(i, player.getItemInHand(hand).copy(), false));
							level.sendBlockUpdated(pos, state, state, 2);
							break;
						}
					}
				});
			}

		}
		return InteractionResult.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState state2, boolean b) {
		if (!state.is(state2.getBlock())) {
			ModUtil.dropItemHandlerInWorld(level.getBlockEntity(pos));
			super.onRemove(state, level, pos, state2, b);
		}
	}

	@Nullable
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> blockEntity) {
		return blockEntity == BlockEntityRegistry.mortar_and_pestle.get() ? MortarPestleBlockEntity::tick : null;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MortarPestleBlockEntity(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter reader, BlockPos pos) {
		return shape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return shape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return shape;
	}

	@SuppressWarnings("deprecation")
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}
