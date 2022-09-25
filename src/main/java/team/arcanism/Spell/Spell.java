package team.arcanism.Spell;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.arcanism.Spell.Aspects.SpellAspect;
import team.arcanism.Spell.Effects.SpellEffect;
import team.arcanism.Spell.Forms.SpellForm;

public class Spell {

	private final SpellForm form;
	private final SpellEffect effect;
	private final SpellAspect aspect;

	private final float duration;
	private final float magnitude;
	private final float radius;

	public Spell(SpellForm form, SpellEffect effect, SpellAspect aspect, float duration, float magnitude, float radius) {
		this.form = form;
		this.effect = effect;
		this.aspect = aspect;
		this.duration = duration;
		this.magnitude = magnitude;
		this.radius = radius;
	}

	public void cast(Player player, ItemStack castingItem) {
		form.collectTargetAndCast(player, castingItem, effect, aspect, duration, magnitude, radius);
	}

	public float getCost(Player player, ItemStack castingItem) {
		return form.getCost() + effect.getCost() + aspect.getCost();
	}

	public SpellForm getForm() {
		return form;
	}

	public SpellEffect getEffect() {
		return effect;
	}

	public SpellAspect getAspect() {
		return aspect;
	}

}
