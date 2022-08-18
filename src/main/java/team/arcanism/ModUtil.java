package team.arcanism;

import com.google.common.collect.ImmutableList;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
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

	public static double bezierSwingEase(float t, float strength) {
		t = Mth.clamp(t, 0f, 1f);
		strength = Mth.clamp(strength, 0, 1);
		Vec2 start = new Vec2(0, 0);
		Vec2 end = new Vec2(1, 1);
		Vec2 controlX = new Vec2(strength, -0.25f);
		Vec2 controlY = new Vec2(1f - strength, 1);

		Vec2 lbottom = lerpVec2(start, controlX, t);
		Vec2 lcenter = lerpVec2(controlX, controlY, t);
		Vec2 ltop = lerpVec2(controlY, end, t);

		Vec2 li1 = lerpVec2(lbottom, lcenter, t);
		Vec2 li2 = lerpVec2(lcenter, ltop, t);

		Vec2 lfinal = lerpVec2(li1, li2, t);

		return lfinal.y;
	}

	public static float bezierFadeOut(float t) {
		t = Mth.clamp(t, 0f, 1f);
		Vec2 start = new Vec2(0, 0);
		Vec2 end = new Vec2(1, 1);
		Vec2 control = new Vec2(0, 1f);

		Vec2 l1 = lerpVec2(start, control, t);
		Vec2 l2 = lerpVec2(control, end, t);
		Vec2 lfinal = lerpVec2(l1, l2, t);
		return lfinal.y;
	}

	public static Vec2 lerpVec2(Vec2 s, Vec2 e, float time) {
		time = Mth.clamp(time, 0f, 1f);
		float x = Mth.lerp(time, s.x, e.x);
		float y = Mth.lerp(time, s.y, e.y);
		return new Vec2(x, y);
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
