package team.arcanism.Spell;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import team.arcanism.Registry.EntityRegistry;

public class SpellExplosionEntity extends Entity {

	public int lifetime = 10;
	public boolean hasCollision = false;
	public int age;

	public SpellExplosionEntity(EntityType<SpellExplosionEntity> spell, Level level) {
		super(spell, level);
	}

	public SpellExplosionEntity(Level level, double x, double y, double z) {
		super(EntityRegistry.spell_explosion.get(), level);
		this.setPos(x, y, z);
		this.setNoGravity(true);
	}

	public boolean hasCollison() {
		return hasCollision;
	}

	public void setHasCollison(boolean bool) {
		hasCollision = bool;
	}

	public void tick() {
		super.tick();
		this.xo = this.getX();
		this.yo = this.getY();
		this.zo = this.getZ();

		//debug movement
		//this.setDeltaMovement(this.getDeltaMovement().add(0.005f, 0, 0));

		//Gravity
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.0001D, 0.0D));
		}
		/*//Squish out of block
		if (hasCollison() && !this.level.noCollision(this.getBoundingBox())) {
			this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.getZ());
		}*/
		//this.move(MoverType.SELF, this.getDeltaMovement());
		//Friction
		float f = 0.98F;
		if (this.onGround) {
			BlockPos pos = new BlockPos(this.getX(), this.getY() - 1.0D, this.getZ());
			f = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98F;
		}
		this.setDeltaMovement(this.getDeltaMovement().multiply((double) f, 0.98D, (double) f));
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, -0.9D, 1.0D));
		}

		//End of life
		++this.age;
		if (this.age >= lifetime) {
			this.discard();
		}

	}

	@Override
	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	@Override
	protected void defineSynchedData() {

	}

	@Override
	protected void readAdditionalSaveData(CompoundTag tag) {
		this.age = tag.getShort("Age");
		this.lifetime = tag.getShort("Lifetime");
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.putShort("Age", (short) this.age);
		tag.putShort("Lifetime", (short) this.lifetime);
	}

	@Override
	public net.minecraft.network.protocol.Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this);
	}
}
