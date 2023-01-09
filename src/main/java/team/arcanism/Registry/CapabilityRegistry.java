package team.arcanism.Registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.PacketDistributor;
import team.arcanism.Arcanism;
import team.arcanism.Capability.AetherCapability;
import team.arcanism.Capability.IAether;
import team.arcanism.Capability.IIngredientKnowledge;
import team.arcanism.Capability.IngredientKnowledgeCapability;
import team.arcanism.Network.AetherPacket;
import team.arcanism.Network.IngredientKnowledgePacket;
import team.arcanism.SetupNetwork;

@Mod.EventBusSubscriber(modid = Arcanism.MODID)
public class CapabilityRegistry {

	public static final Capability<IAether> aether = CapabilityManager.get(new CapabilityToken<>() {});
	public static final Capability<IIngredientKnowledge> ingredient_knowledge = CapabilityManager.get(new CapabilityToken<>() {});

	@SubscribeEvent
	public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Player) {
			event.addCapability(new ResourceLocation(Arcanism.MODID, "aether"), new AetherCapability.Provider());
			event.addCapability(new ResourceLocation(Arcanism.MODID, "ingredient_knowledge"), new IngredientKnowledgeCapability.Provider());
		}
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.register(IAether.class);
		event.register(IIngredientKnowledge.class);
	}

	public static LazyOptional<IAether> getAether(LivingEntity entity) {
		return entity.getCapability(aether);
	}

	public static LazyOptional<IIngredientKnowledge> getIngredientKnowledge(LivingEntity entity) {
		return entity.getCapability(ingredient_knowledge);
	}

	@SubscribeEvent
	public static void playerClone(PlayerEvent.Clone event) {
		Player oldPlayer = event.getOriginal();
		oldPlayer.revive();
		getAether(oldPlayer).ifPresent(oldAether -> getAether(event.getEntity()).ifPresent(newAether -> {
			newAether.setMax(oldAether.getMax());
			newAether.set(oldAether.get());
		}));
		getIngredientKnowledge(oldPlayer).ifPresent(oldKnowledge -> getIngredientKnowledge(event.getEntity()).ifPresent(knowledge -> knowledge.setKnowledge(oldKnowledge.getKnowledge())));
		event.getOriginal().invalidateCaps();
	}

	@SubscribeEvent
	public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
		sendAetherPacket(event.getEntity());
		sendIngredientKnowledgePacket(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		sendAetherPacket(event.getEntity());
		sendIngredientKnowledgePacket(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerStartTracking(PlayerEvent.StartTracking event) {
		sendAetherPacket(event.getEntity());
		sendIngredientKnowledgePacket(event.getEntity());
	}

	@SubscribeEvent
	public static void onPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		sendAetherPacket(event.getEntity());
		sendIngredientKnowledgePacket(event.getEntity());
	}

	public static void sendAetherPacket(Player player) {
		if (EffectiveSide.get() == LogicalSide.SERVER && player instanceof ServerPlayer serverPlayer) {
			SetupNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new AetherPacket(CapabilityRegistry.getAether(player).orElse(new AetherCapability()), player.getUUID()));
		}
	}

	public static void sendIngredientKnowledgePacket(Player player) {
		if (EffectiveSide.get() == LogicalSide.SERVER && player instanceof ServerPlayer serverPlayer) {
			SetupNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new IngredientKnowledgePacket(CapabilityRegistry.getIngredientKnowledge(player).orElse(new IngredientKnowledgeCapability()), player.getUUID()));
		}
	}
}
