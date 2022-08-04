package team.arcanism.Registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;

public class SoundRegistry {

	public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Arcanism.MODID);

	public static final RegistryObject<SoundEvent> arcane_infuser = SOUNDS.register("arcane_infuser", () -> new SoundEvent(new ResourceLocation(Arcanism.MODID, "arcane_infuser")));

}
