package team.arcanism.Spell.Effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.arcanism.Spell.Aspects.SpellAspect;

import java.util.List;

public abstract class SpellEffect {

	private final ResourceLocation id;
	private final float cost;

	public SpellEffect(ResourceLocation id, float cost) {
		this.id = id;
		this.cost = cost;
	}

	public ResourceLocation getId() {
		return id;
	}

	public float getCost() {
		return cost;
	}

	public abstract void createHitEffects(Player player, List<Object> target, SpellAspect aspect, float duration, float magnitude, float radius);

}
