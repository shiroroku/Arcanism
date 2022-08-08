package team.arcanism.Registry;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;

import java.awt.*;

public class EffectRegistry {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Arcanism.MODID);

	public static final RegistryObject<MobEffect> aether_regen = EFFECTS.register("aether_regen", () -> {
		return new MobEffect(MobEffectCategory.BENEFICIAL, 666001) {
			@Override
			public int getColor() {
				return Color.decode("0xceffea").getRGB();
			}
		};
	});

}
