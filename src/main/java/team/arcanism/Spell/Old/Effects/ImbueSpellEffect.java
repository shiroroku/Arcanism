package team.arcanism.Spell.Old.Effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import team.arcanism.Spell.Old.Aspects.SpellAspect;

import java.util.List;

public class ImbueSpellEffect extends SpellEffect {
	public ImbueSpellEffect(ResourceLocation id, float cost) {
		super(id, cost);
	}

	@Override
	public void createHitEffects(Player player, List<Object> target, SpellAspect aspect, float duration, float magnitude, float radius) {
		for (Object o : target) {
			if (o instanceof BlockPos pos && player.level.getBlockState(pos).getBlock() == Blocks.WATER) {
				for (int x = 1; x <= 3; x++) {
					player.level.addParticle(ParticleTypes.FLAME, pos.getX() + (1f / x), pos.getY() + 2f, pos.getZ() + 0.5f, 0, 0, 0);
				}
			}
		}
	}
}
