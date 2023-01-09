package team.arcanism;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import team.arcanism.Registry.*;

@Mod(Arcanism.MODID)
public class Arcanism {

	public static final String MODID = "arcanism";
	private static final Logger LOGGER = LogUtils.getLogger();

	public Arcanism() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		EffectRegistry.EFFECTS.register(eventBus);
		ItemRegistry.ITEMS.register(eventBus);
		BlockRegistry.BLOCKS.register(eventBus);
		EntityRegistry.ENTITIES.register(eventBus);
		ParticleRegistry.PARTICLES.register(eventBus);
		BlockEntityRegistry.BLOCK_ENTITIES.register(eventBus);
		ContainerRegistry.CONTAINERS.register(eventBus);
		RecipeRegistry.SERIALIZERS.register(eventBus);
		RecipeRegistry.TYPES.register(eventBus);
		SoundRegistry.SOUNDS.register(eventBus);
	}

	public static final CreativeModeTab CREATIVETAB = new CreativeModeTab(MODID) {
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(BlockRegistry.arcane_infuser.get());
		}
	};
}
