package team.arcanism.Network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import team.arcanism.Capability.AetherCapability;
import team.arcanism.Capability.IAether;
import team.arcanism.Registry.CapabilityRegistry;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class AetherPacket {

	public IAether aether;
	UUID player;

	public AetherPacket(IAether aether, UUID player) {
		this.aether = aether;
		this.player = player;
	}

	public static AetherPacket decode(FriendlyByteBuf buf) {
		float max = buf.readFloat();
		float amount = buf.readFloat();
		UUID player = buf.readUUID();
		return new AetherPacket(new AetherCapability(amount, max), player);
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeFloat(aether.getMax());
		buf.writeFloat(aether.get());
		buf.writeUUID(player);
	}

	public static class Handler {
		public static void onMessageReceived(final AetherPacket message, Supplier<NetworkEvent.Context> ctxSupplier) {
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

		private static void processMessage(Level worldClient, AetherPacket message) {
			CapabilityRegistry.getAether(worldClient.getPlayerByUUID(message.player)).ifPresent(cap -> {
				cap.setMax(message.aether.getMax());
				cap.set(message.aether.get());
			});
		}
	}

}
