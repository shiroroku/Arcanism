package team.arcanism.Particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class StabAttackParticle extends TextureSheetParticle {
	private final SpriteSet sprites;

	StabAttackParticle(ClientLevel level, double x, double y, double z, double xv, SpriteSet sprites) {
		super(level, x, y, z, 0.0D, 0.0D, 0.0D);
		this.sprites = sprites;
		this.lifetime = 4;
		float f = this.random.nextFloat() * 0.6F + 0.4F;
		this.rCol = f;
		this.gCol = f;
		this.bCol = f;
		this.quadSize = 1.0F - (float) xv * 0.5F;
		this.setSpriteFromAge(sprites);
	}

	public int getLightColor(float p_105562_) {
		return 15728880;
	}

	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			this.setSpriteFromAge(this.sprites);
		}
	}

	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_LIT;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xv, double yv, double zv) {
			return new StabAttackParticle(level, x, y, z, xv, this.sprites);
		}
	}
}
