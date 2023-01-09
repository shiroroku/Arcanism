package team.arcanism.Elixir;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import team.arcanism.Registry.CapabilityRegistry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ElixirUtilClient {
	public static void ingredientHoverText(TreeMap<Integer, Tuple<MobEffect, Integer>> effectMap, ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(Component.translatable("item.arcanism.ingredient").withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC));
		CapabilityRegistry.getIngredientKnowledge(Minecraft.getInstance().player).ifPresent((cap) -> {
			AtomicInteger slot = new AtomicInteger(1);
			effectMap.values().forEach((mobeffect) -> {
				if (cap.knows(stack.getItem(), slot.get())) {
					int seconds = mobeffect.getB();
					String formatted = String.format("%d:%02d", (seconds % 3600) / 60, seconds = seconds % 60);
					String duration = mobeffect.getA().isInstantenous() ? "" : " (" + formatted + ")";
					tooltip.add(Component.literal("- ").append(Component.translatable(mobeffect.getA().getDescriptionId())).append(duration).withStyle(mobeffect.getA().isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED));
				} else {
					tooltip.add(Component.literal("- ").append("???").withStyle(ChatFormatting.BLUE).withStyle(ChatFormatting.BOLD));
				}
				slot.getAndIncrement();
			});
		});
	}
}
