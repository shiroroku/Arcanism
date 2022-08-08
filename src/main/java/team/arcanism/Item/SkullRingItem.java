package team.arcanism.Item;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import team.arcanism.Arcanism;
import team.arcanism.ModUtil;
import team.arcanism.Registry.ItemRegistry;

public class SkullRingItem extends Item {
	private static final TagKey<Item> repairable = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Arcanism.MODID, "skullring_repairable"));

	public SkullRingItem(Item.Properties prop) {
		super(prop);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

		return super.use(level, player, hand);
	}

	public static void handlePlayerTick(TickEvent.PlayerTickEvent event) {
		Player player = event.player;
		if (!event.side.isServer() && event.phase != TickEvent.Phase.START && player.tickCount % (20 * 5) != 0) {
			return;
		}

		if (ModUtil.hasItem(player, ItemRegistry.skull_ring.get(), true) && ModUtil.hasItem(player, Items.BONE_MEAL, false)) {
			ModUtil.getFirstItem(player, Items.BONE_MEAL, false).ifPresent(bonemeal -> {
				ModUtil.getItemsOfTag(player, repairable).stream().filter(ItemStack::isDamaged).findFirst().ifPresent(item -> {
					if (item.getDamageValue() >= 15) {
						bonemeal.shrink(1);
						item.setDamageValue(item.getDamageValue() - 15);
					}
				});
			});
		}

	}
}
