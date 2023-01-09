package team.arcanism.Spell;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import team.arcanism.Registry.EntityRegistry;

public class SpellEntity extends Projectile {

	public int lifetime = 400;
	public boolean hasCollision = true;
	public int age;

	public SpellEntity(EntityType<SpellEntity> spell, Level level) {
		super(spell, level);
	}

	public SpellEntity(Level level, double x, double y, double z) {
		super(EntityRegistry.spell.get(), level);
		this.setPos(x, y, z);
	}

	public boolean hasCollison() {
		return hasCollision;
	}

	public void tick() {
		Entity entity = this.getOwner();
		if (this.level.isClientSide || (entity == null || entity.isAlive()) && this.level.hasChunkAt(new BlockPos(this.position()))) {
			super.tick();

			HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
			if (hitresult.getType() != HitResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
				this.onHit(hitresult);
			}

			this.checkInsideBlocks();
			Vec3 vec3 = this.getDeltaMovement();
			double d0 = this.getX() + vec3.x;
			double d1 = this.getY() + vec3.y;
			double d2 = this.getZ() + vec3.z;

			//this.level.addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);

			this.setPos(d0, d1, d2);
		} else {
			this.discard();
		}
		//End of life
		++this.age;
		if (this.age >= lifetime) {
			this.discard();
		}

	}

	protected ParticleOptions getTrailParticle() {
		return ParticleTypes.CRIT;
	}

	@Override
	protected MovementEmission getMovementEmission() {
		return MovementEmission.NONE;
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.age = tag.getShort("Age");
		this.lifetime = tag.getShort("Lifetime");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putShort("Age", (short) this.age);
		tag.putShort("Lifetime", (short) this.lifetime);
	}

	@Override
	protected void onHit(HitResult result) {
		super.onHit(result);
		if (result.getType() == HitResult.Type.ENTITY) {
			if (((EntityHitResult) result).getEntity() == this.getOwner()) {
				return;
			}
		}
		this.discard();
		SpellExplosionEntity explosionEntity = new SpellExplosionEntity(level, this.position().x, this.position().y, this.position().z);
		level.addFreshEntity(explosionEntity);
		level.explode((Entity) null, this.getX(), this.getY(), this.getZ(), 1.0F, Explosion.BlockInteraction.NONE);

	}
}

