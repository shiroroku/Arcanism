package team.arcanism.Item.BoneDagger;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import team.arcanism.Registry.EntityRegistry;
import team.arcanism.Registry.ItemRegistry;

public class BoneDaggerEntity extends ThrowableItemProjectile {

	boolean isOnGround = false;
	int onGroundTime = 0;

	float yRotOCapture;
	float yRotCapture;
	float xRotOCapture;
	float xRotCapture;
	BlockPos collidedBlock;

	public BoneDaggerEntity(EntityType<? extends BoneDaggerEntity> dagger, Level level) {
		super(dagger, level);
	}

	public BoneDaggerEntity(Level level, LivingEntity entity) {
		super(EntityRegistry.bone_dagger.get(), entity, level);
	}

	public BoneDaggerEntity(Level level, double x, double y, double z) {
		super(EntityRegistry.bone_dagger.get(), x, y, z, level);
	}

	@Override
	protected Item getDefaultItem() {
		return ItemRegistry.bone_dagger.get();
	}

	@Override
	public void tick() {
		super.tick();
		if (isOnGround) {
			onGroundTime++;
			if (onGroundTime >= 20 * 60 * 30) {
				this.remove(RemovalReason.DISCARDED);
			}

			if (this.level.getBlockState(collidedBlock).isAir()) {
				isOnGround = false;
				this.setNoGravity(false);
				this.setDeltaMovement(0, 0.02F, 0);
			}
		} else {
			this.yRotOCapture = yRotO;
			this.yRotCapture = getYRot();
			this.xRotOCapture = xRotO;
			this.xRotCapture = getXRot();
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte event) {
		if (event == 3) {
			float speed = 0.5f;
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(ItemRegistry.bone_dagger.get())), this.getX(), this.getY(), this.getZ(), (-0.5D + this.random.nextDouble()) * speed, (-0.5D + this.random.nextDouble()) * speed, (-0.5D + this.random.nextDouble()) * speed);
			}
		}

	}

	@Override
	public void onHitEntity(EntityHitResult result) {
		super.onHitEntity(result);
		result.getEntity().hurt(DamageSource.thrown(this, this.getOwner()), 3.0f);
	}

	@Override
	public void playerTouch(Player player) {
		if (!this.level.isClientSide && this.isOnGround) {
			if (!player.isCreative()) {
				this.level.addFreshEntity(new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), new ItemStack(ItemRegistry.bone_dagger.get())));
			}
			this.remove(RemovalReason.DISCARDED);
		}
	}

	@Override
	public void onHitBlock(BlockHitResult result) {
		isOnGround = true;
		float distance = 0.5f;
		this.setPos(Mth.lerp(distance, result.getLocation().x, this.getX()), Mth.lerp(distance, result.getLocation().y, this.getY()), Mth.lerp(distance, result.getLocation().z, this.getZ()));
		this.setDeltaMovement(0, 0, 0);
		this.setNoGravity(true);
		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte) 3);
		}
		this.collidedBlock = result.getBlockPos();
		this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WOOD_BREAK, SoundSource.NEUTRAL, 0.25F, random.nextFloat() * 0.5F + 2F);
	}

}
