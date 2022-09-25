package team.arcanism.Item.MoonlightGreatsword;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeTier;
import team.arcanism.Registry.ItemRegistry;
import team.arcanism.Spell.Spell;
import team.arcanism.Spell.SpellRegistry;

public class MoonlightGreatswordItem extends SwordItem {
	public MoonlightGreatswordItem(Properties prop) {
		super(new ForgeTier(1, 1000, 4, 1, 20, null, null), 7, -3.2f, prop);
	}

	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);

		player.startUsingItem(hand);

		new Spell(SpellRegistry.Forms.touch, SpellRegistry.Effects.imbue, SpellRegistry.Aspects.fire, 3, 3, 3).cast(player, player.getItemInHand(hand));

		return super.use(level, player, hand);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		if (level.isClientSide) {
			MoonlightGreatswordSwingRenderer.swingtick = MoonlightGreatswordSwingRenderer.swingTime;
		}
		((Player) entity).getCooldowns().addCooldown(ItemRegistry.moonlight_greatsword.get(), 20);
		return super.finishUsingItem(stack, level, entity);
	}

	public int getUseDuration(ItemStack p_40680_) {
		return 10;
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

}
