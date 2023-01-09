package team.arcanism.Item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ToolActions;
import team.arcanism.Registry.ParticleRegistry;

import java.util.List;

public class BoneSwordItem extends SwordItem {
	public BoneSwordItem(Tier tier, int damage, float speed, Properties prop) {
		super(tier, damage, speed, prop);
	}

	public boolean hurtEnemy(ItemStack stack, LivingEntity hurt, LivingEntity attacker) {
		int range = 4;
		List<LivingEntity> entities = attacker.level.getEntitiesOfClass(LivingEntity.class, attacker.getBoundingBoxForCulling().inflate(range));
		for (LivingEntity e : entities) {
			if (e.distanceToSqr(attacker) < range * range && e != hurt && e != attacker && !e.isAlliedTo(attacker)) {
				Vec3 vec3 = attacker.getViewVector(1.0F).normalize();
				Vec3 vec31 = new Vec3(e.getX() - attacker.getX(), e.getEyeY() - attacker.getEyeY(), e.getZ() - attacker.getZ());
				double d0 = vec31.length();
				vec31 = vec31.normalize();
				double d1 = vec3.dot(vec31);
				if (d1 > 0.95D - 0.025D / d0 && attacker.hasLineOfSight(e)) {
					e.hurt(DamageSource.mobAttack(attacker), 2f);
				}
			}
		}

		double d0 = (double) (-Mth.sin(attacker.getYRot() * ((float) Math.PI / 180F))) * 1.5;
		double d1 = (double) Mth.cos(attacker.getYRot() * ((float) Math.PI / 180F)) * 1.5;
		if (attacker.level instanceof ServerLevel) {
			((ServerLevel) attacker.level).sendParticles(ParticleRegistry.stab_attack.get(), attacker.getX() + d0, attacker.getY(0.5D), attacker.getZ() + d1, 0, 0, 0.0D, 3, 0.0D);
		}
		return super.hurtEnemy(stack, hurt, attacker);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, net.minecraftforge.common.ToolAction toolAction) {
		return net.minecraftforge.common.ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction) && toolAction != ToolActions.SWORD_SWEEP;
	}
}
