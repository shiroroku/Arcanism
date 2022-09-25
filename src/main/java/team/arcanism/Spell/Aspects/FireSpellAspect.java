package team.arcanism.Spell.Aspects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import team.arcanism.Spell.Effects.SpellEffect;
import team.arcanism.Spell.SpellRegistry;

import java.util.List;

public class FireSpellAspect extends SpellAspect {
	public FireSpellAspect(ResourceLocation id, float cost) {
		super(id, cost);
	}

	@Override
	public boolean cast(Player player, List<Object> targets, SpellEffect aspect, float duration, float magnitude, float radius) {
		if (aspect == SpellRegistry.Effects.imbue) {
			boolean completed = false;
			for (Object target : targets) {
				if (target instanceof LivingEntity entity && !entity.fireImmune()) {
					entity.setSecondsOnFire((int) duration);
					entity.hurt(DamageSource.IN_FIRE, magnitude);
					completed = true;
				}

				if (target instanceof BlockPos pos) {
					//water to obsidian
					if (player.level.getBlockState(pos).getBlock() == Blocks.WATER) {
						player.level.setBlock(pos, Blocks.OBSIDIAN.defaultBlockState(), 3);
						completed = true;
					}

					//burn logs into charcoal
					if (player.level.getBlockState(pos).getTags().anyMatch(t -> (t.equals(BlockTags.LOGS_THAT_BURN)))) {
						player.level.destroyBlock(pos, false);
						player.level.addFreshEntity(new ItemEntity(player.level, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, new ItemStack(Items.CHARCOAL)));
						completed = true;
					}
				}
			}
			return completed;
		}
		return false;
	}

	@Override
	public ParticleOptions getParticle() {
		return ParticleTypes.FLAME;
	}
}
