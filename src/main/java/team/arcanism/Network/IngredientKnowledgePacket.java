package team.arcanism.Network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import team.arcanism.Capability.IIngredientKnowledge;
import team.arcanism.Capability.IngredientKnowledgeCapability;
import team.arcanism.Registry.CapabilityRegistry;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class IngredientKnowledgePacket {

	public IIngredientKnowledge knowledge;
	public UUID player;

	public IngredientKnowledgePacket(IIngredientKnowledge knowledge, UUID player) {
		this.knowledge = knowledge;
		this.player = player;
	}

	public static IngredientKnowledgePacket decode(FriendlyByteBuf buf) {
		return new IngredientKnowledgePacket(new IngredientKnowledgeCapability(buf.readNbt()), buf.readUUID());
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeNbt(knowledge.serializeNBT());
		buf.writeUUID(player);
	}

	public static class Handler {
		public static void onMessageReceived(final IngredientKnowledgePacket message, Supplier<NetworkEvent.Context> ctxSupplier) {
			NetworkEvent.Context ctx = ctxSupplier.get();
			LogicalSide sideReceived = ctx.getDirection().getReceptionSide();
			ctx.setPacketHandled(true);
			if (sideReceived != LogicalSide.CLIENT) {
				return;
			}
			Optional<Level> clientWorld = LogicalSidedProvider.CLIENTWORLD.get(sideReceived);
			if (clientWorld.isEmpty()) {
				return;
			}
			ctx.enqueueWork(() -> processMessage(clientWorld.get(), message));
		}

		private static void processMessage(Level worldClient, IngredientKnowledgePacket message) {
			CapabilityRegistry.getIngredientKnowledge(worldClient.getPlayerByUUID(message.player)).ifPresent(cap -> {
				cap.setKnowledge(message.knowledge.getKnowledge());
			});
		}
	}

}
