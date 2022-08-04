package team.arcanism.Compat;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import team.arcanism.Arcanism;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod.EventBusSubscriber(modid = Arcanism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CuriosCompat {

	@SubscribeEvent
	public static void enqueueIMC(InterModEnqueueEvent evt) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().size(2).build());
	}
}
