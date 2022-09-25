package team.arcanism.Spell.Aspects;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.arcanism.Spell.Effects.SpellEffect;

import java.util.List;

public abstract class SpellAspect {

	private final ResourceLocation id;
	private final float cost;

	public SpellAspect(ResourceLocation id, float cost) {
		this.id = id;
		this.cost = cost;
	}

	public ResourceLocation getId() {
		return id;
	}

	public float getCost() {
		return cost;
	}

	public abstract boolean cast(Player player, List<Object> target, SpellEffect effect, float duration, float magnitude, float radius);

	public abstract ParticleOptions getParticle();

}
