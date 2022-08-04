package team.arcanism.Registry;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;

public class ParticleRegistry {

	public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Arcanism.MODID);

	public static final RegistryObject<SimpleParticleType> stab_attack = PARTICLES.register("stab_attack", () -> new SimpleParticleType(true));

}
