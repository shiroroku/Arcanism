package team.arcanism;

import com.google.common.collect.ImmutableList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModUtil {

	/**
	 * Returns if the player has the specified item in their inventory.
	 *
	 * @param alsoCurios Should Curios also be counted.
	 */
	public static boolean hasItem(Player player, Item item, boolean alsoCurios) {
		if (player == null) {
			return false;
		}
		if (alsoCurios) {
			if (CuriosApi.getCuriosHelper().findFirstCurio(player, item).isPresent()) {
				return true;
			}
		}
		Inventory inv = player.getInventory();
		return inv.contains(new ItemStack(item));
	}

	public static void dropItemHandlerInWorld(BlockEntity block) {
		block.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
			for (int i = 0; i < handler.getSlots(); i++) {
				ItemEntity outputEntity = new ItemEntity(block.getLevel(), block.getBlockPos().getX() + 0.5f, block.getBlockPos().getY() + 0.5f, block.getBlockPos().getZ() + 0.5f, handler.getStackInSlot(i));
				block.getLevel().addFreshEntity(outputEntity);
			}
		});
	}

	/**
	 * Returns the first item in the players in their inventory.
	 *
	 * @param alsoCurios Should Curios also be counted.
	 */
	public static Optional<ItemStack> getFirstItem(Player player, Item item, boolean alsoCurios) {
		if (player == null) {
			return Optional.empty();
		}

		if (alsoCurios) {
			Optional<SlotResult> res = CuriosApi.getCuriosHelper().findFirstCurio(player, item);
			if (res.isPresent()) {
				return Optional.of(res.get().stack());
			}
		}

		Inventory inv = player.getInventory();
		for (List<ItemStack> list : ImmutableList.of(inv.items, inv.armor, inv.offhand)) {
			for (ItemStack itemstack : list) {
				if (itemstack.getItem().equals(item)) {
					return Optional.of(itemstack);
				}
			}
		}

		return Optional.empty();
	}

	/**
	 * Returns list of any items with the given tag in the player's inventory.
	 */
	public static List<ItemStack> getItemsOfTag(Player player, TagKey<Item> tag) {
		List<ItemStack> items = new ArrayList<>();
		if (player == null) {
			return items;
		}

		Inventory inv = player.getInventory();
		for (List<ItemStack> list : ImmutableList.of(inv.items, inv.armor, inv.offhand)) {
			for (ItemStack itemstack : list) {
				if (itemstack.getTags().anyMatch(t -> (t.equals(tag)))) {
					items.add(itemstack);
				}
			}
		}

		return items;
	}

	public static Color blendColor(Color c1, Color c2, float ratio) {
		float iRatio = 1.0f - Math.min(1, Math.max(0, ratio));

		int rgb1 = c1.getRGB();
		int a1 = (rgb1 >> 24 & 0xff);
		int r1 = ((rgb1 & 0xff0000) >> 16);
		int g1 = ((rgb1 & 0xff00) >> 8);
		int b1 = (rgb1 & 0xff);

		int rgb2 = c2.getRGB();
		int a2 = (rgb2 >> 24 & 0xff);
		int r2 = ((rgb2 & 0xff0000) >> 16);
		int g2 = ((rgb2 & 0xff00) >> 8);
		int b2 = (rgb2 & 0xff);

		int a = (int) ((a1 * iRatio) + (a2 * ratio));
		int r = (int) ((r1 * iRatio) + (r2 * ratio));
		int g = (int) ((g1 * iRatio) + (g2 * ratio));
		int b = (int) ((b1 * iRatio) + (b2 * ratio));

		return new Color(a << 24 | r << 16 | g << 8 | b);
	}
}
