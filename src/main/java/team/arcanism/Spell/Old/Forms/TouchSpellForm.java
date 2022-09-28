package team.arcanism.Spell.Old.Forms;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import team.arcanism.ModUtil;
import team.arcanism.Spell.Old.Aspects.SpellAspect;
import team.arcanism.Spell.Old.Effects.SpellEffect;

import java.util.ArrayList;
import java.util.List;

public class TouchSpellForm extends SpellForm {
	public TouchSpellForm(ResourceLocation id, float cost) {
		super(id, cost);
	}

	@Override
	public boolean collectTargetAndCast(Player player, ItemStack castingItem, SpellEffect effect, SpellAspect aspect, float duration, float magnitude, float radius) {
		if (player != null) {
			List<Object> targets = new ArrayList<>();
			HitResult hit = ModUtil.getPlayerRaycast(player.level, player, ClipContext.Fluid.SOURCE_ONLY);

			if (hit.getType() == HitResult.Type.ENTITY) {
				EntityHitResult entityHit = (EntityHitResult) hit;
				if (!(entityHit.getEntity() instanceof LivingEntity)) {
					return false;
				}

				targets.add(entityHit.getEntity());
				if (radius > 0) {
					for (Entity le : ModUtil.getEntitiesInRange(player.level, entityHit.getEntity().blockPosition(), radius)) {
						if (le != entityHit.getEntity() && le instanceof LivingEntity) {
							targets.add(le);
						}
					}
				}
			} else {
				if (hit.getType() == HitResult.Type.BLOCK) {
					BlockHitResult blockHit = (BlockHitResult) hit;
					targets.add(blockHit.getBlockPos());
					if (radius > 0) {
						radius = radius * 2;
						for (int x = (int) -radius; x <= radius; x++) {
							for (int y = (int) -radius; y <= radius; y++) {
								for (int z = (int) -radius; z <= radius; z++) {
									BlockPos newpos = blockHit.getBlockPos().offset(x, y, z);
									if (blockHit.getBlockPos().distToCenterSqr(newpos.getX() + 0.5, newpos.getY() + 0.5, newpos.getZ() + 0.5) < radius) {
										targets.add(blockHit.getBlockPos().offset(x, y, z));
									}
								}
							}
						}
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


