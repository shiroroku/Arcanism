package team.arcanism.Spell.Effects;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import team.arcanism.Spell.Aspects.SpellAspect;

import java.util.List;

public class ImbueSpellEffect extends SpellEffect {
	public ImbueSpellEffect(ResourceLocation id, float cost) {
		super(id, cost);
	}

	@Override
	public void createHitEffects(Player player, List<Object> target, SpellAspect aspect, float duration, float magnitude, float radius) {

	}
}
