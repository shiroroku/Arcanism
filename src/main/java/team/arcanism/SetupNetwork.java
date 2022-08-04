package team.arcanism;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import team.arcanism.Network.AetherPacket;
import team.arcanism.Network.IngredientKnowledgePacket;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Arcanism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SetupNetwork {

	public static SimpleChannel CHANNEL;

	@SubscribeEvent
	public static void setup(final FMLCommonSetupEvent event) {
		CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(Arcanism.MODID, "channel"), () -> "1.0", s -> true, s -> true);
		int id = 0;
		CHANNEL.registerMessage(id++, AetherPacket.class, AetherPacket::encode, AetherPacket::decode, AetherPacket.Handler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
		CHANNEL.registerMessage(id++, IngredientKnowledgePacket.class, IngredientKnowledgePacket::encode, IngredientKnowledgePacket::decode, IngredientKnowledgePacket.Handler::onMessageReceived, Optional.of(NetworkDirection.PLAY_TO_CLIENT));

	}

}
