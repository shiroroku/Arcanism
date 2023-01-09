package team.arcanism.Block.ArcaneInfuser;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import team.arcanism.Registry.BlockRegistry;
import team.arcanism.Registry.ContainerRegistry;

public class ArcaneInfuserContainer extends AbstractContainerMenu {

	private final BlockEntity blockEntity;

	public ArcaneInfuserContainer(int id, BlockPos pos, Inventory playerInventory, Player playerIn) {
		super(ContainerRegistry.arcanist_workbench.get(), id);
		blockEntity = playerIn.getCommandSenderWorld().getBlockEntity(pos);

		if (blockEntity != null) {
			blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
				addSlot(new SlotItemHandler(handler, 0, 80, 35));
				addSlot(new SlotItemHandler(handler, 1, 80, 8));
				addSlot(new SlotItemHandler(handler, 2, 102, 13));
				addSlot(new SlotItemHandler(handler, 3, 107, 35));
				addSlot(new SlotItemHandler(handler, 4, 102, 57));
				addSlot(new SlotItemHandler(handler, 5, 80, 62));
				addSlot(new SlotItemHandler(handler, 6, 58, 57));
				addSlot(new SlotItemHandler(handler, 7, 53, 35));
				addSlot(new SlotItemHandler(handler, 8, 58, 13));
			});
		}

		IItemHandler playerItemhandler = new InvWrapper(playerInventory);
		int slotIndex = 0;
		int invX = 8;
		int invY = 142;
		for (int x = 0; x < 9; x++) {
			addSlot(new SlotItemHandler(playerItemhandler, slotIndex, invX + 18 * x, invY));
			slotIndex++;
		}
		invY -= 58;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < 9; x++) {
				addSlot(new SlotItemHandler(playerItemhandler, slotIndex, invX + 18 * x, invY + 18 * y));
				slotIndex++;
			}

		}
	}

	public boolean hasRecipe() {
		return ((ArcaneInfuserBlockEntity) blockEntity).hasValidRecipe();
	}

	@Override
	public ItemStack quickMoveStack(Player playerIn, int index) {
		ItemStack copy = ItemStack.EMPTY;
		Slot clickedSlot = this.slots.get(index);
		if (clickedSlot != null && clickedSlot.hasItem()) {
			ItemStack clickedItem = clickedSlot.getItem();
			copy = clickedItem.copy();
			//is in table
			if (index < 9) {
				//try move to inventory
				if (!this.moveItemStackTo(clickedItem, 9, this.slots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else {
				//try move to table
				if (!this.moveItemStackTo(clickedItem, 0, 9, false)) {
					return ItemStack.EMPTY;
				}
			}

			if (clickedItem.isEmpty()) {
				clickedSlot.set(ItemStack.EMPTY);
			} else {
				clickedSlot.setChanged();
			}
		}

		return copy;
	}

	@Override
	public boolean stillValid(Player player) {
		return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), player, BlockRegistry.arcane_infuser.get());
	}
}
