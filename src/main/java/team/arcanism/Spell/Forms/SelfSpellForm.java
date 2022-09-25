package team.arcanism.Spell.Forms;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.arcanism.ModUtil;
import team.arcanism.Spell.Aspects.SpellAspect;
import team.arcanism.Spell.Effects.SpellEffect;

import java.util.ArrayList;
import java.util.List;

public class SelfSpellForm extends SpellForm {
	public SelfSpellForm(ResourceLocation id, float cost) {
		super(id, cost);
	}

	@Override
	public boolean collectTargetAndCast(Player player, ItemStack castingItem, SpellEffect effect, SpellAspect aspect, float duration, float magnitude, float radius) {
		if (player != null) {
			List<Object> targets = new ArrayList<>();
			targets.add(player);
			if (radius > 0) {
				for (Entity le : ModUtil.getEntitiesInRange(player.level, player.blockPosition(), radius)) {
					if (le != player && le instanceof LivingEntity) {
						targets.add(le);
					}
				}
			}
			createSpawnEffects(player, targets, aspect, duration, magnitude, radius);
			boolean completed = aspect.cast(player, targets, effect, duration, magnitude, radius);
			if (completed) {
				effect.createHitEffects(player, targets, aspect, duration, magnitude, radius);
			}
			return completed;
		}
		return false;
	}

	@Override
	public void createSpawnEffects(Player player, List<Object> target, SpellAspect aspect, float duration, float magnitude, float radius) {

	}

}
