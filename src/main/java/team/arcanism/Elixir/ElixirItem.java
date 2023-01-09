package team.arcanism.Elixir;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class ElixirItem extends Item {

	public ElixirItem(Properties prop) {
		super(prop);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity playerIn) {
		Player player = playerIn instanceof Player ? (Player) playerIn : null;
		if (player instanceof ServerPlayer) {
			CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
		}

		if (!level.isClientSide) {
			ElixirUtil.getFromNBT(stack).forEach((mobEffect, value) -> {
				if (mobEffect.isInstantenous()) {
					mobEffect.applyInstantenousEffect(player, player, playerIn, value.getA() - 1, 1);
				} else {
					playerIn.addEffect(new MobEffectInstance(mobEffect, value.getB() * 20, value.getA() - 1));
				}
			});
		}

		if (player != null) {
			if (!player.getAbilities().instabuild) {
				stack.shrink(1);
			}
		}

		return stack;
	}

	@Override
	public int getUseDuration(ItemStack p_43001_) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack p_42997_) {
		return UseAnim.DRINK;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level p_42993_, Player p_42994_, InteractionHand p_42995_) {
		return ItemUtils.startUsingInstantly(p_42993_, p_42994_, p_42995_);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		ElixirUtil.getFromNBT(stack).forEach((mobEffect, values) -> {
			int seconds = values.getB();
			String formatted = String.format("%d:%02d", (seconds % 3600) / 60, seconds = seconds % 60);
			String duration = mobEffect.isInstantenous() ? "" : " (" + formatted + ")";
			String potency = values.getA() > 1 ? " " + values.getA() : "";
			tooltip.add(Component.literal("- ").append(Component.translatable(mobEffect.getDescriptionId())).append(potency + duration).withStyle(mobEffect.isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED));
		});
	}

}
