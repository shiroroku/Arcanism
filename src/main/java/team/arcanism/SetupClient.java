package team.arcanism;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserScreen;
import team.arcanism.Block.MortarPestle.MortarPestleRenderer;
import team.arcanism.Elixir.ElixirUtil;
import team.arcanism.Item.BoneDagger.BoneDaggerRenderer;
import team.arcanism.Particle.StabAttackParticle;
import team.arcanism.Registry.*;
import team.arcanism.Render.AetherBarRender;

import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

@Mod.EventBusSubscriber(modid = Arcanism.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SetupClient {

	@SubscribeEvent
	public static void setup(final FMLClientSetupEvent event) {
		MinecraftForge.EVENT_BUS.addListener(SetupClient::onRenderGameOverlay);
		event.enqueueWork(() -> {
			ItemBlockRenderTypes.setRenderLayer(BlockRegistry.blackwood_trapdoor.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(BlockRegistry.blackwood_door.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ItemRegistry.blightshade.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ItemRegistry.blazel.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ItemRegistry.wise_hazel.get(), RenderType.cutout());

			MenuScreens.register(ContainerRegistry.arcanist_workbench.get(), ArcaneInfuserScreen::new);

			BlockEntityRenderers.register(BlockEntityRegistry.mortar_and_pestle.get(), MortarPestleRenderer::new);

			ItemProperties.register(ItemRegistry.blackwood_bow.get(), new ResourceLocation("pull"), (stack, level, living, id) -> {
				if (living == null) {
					return 0.0F;
				} else {
					return living.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - living.getUseItemRemainingTicks()) / 20.0F;
				}
			});
			ItemProperties.register(ItemRegistry.blackwood_bow.get(), new ResourceLocation("pulling"), (stack, level, living, id) -> {
				return living != null && living.isUsingItem() && living.getUseItem() == stack ? 1.0F : 0.0F;
			});
		});
	}

	@SubscribeEvent
	public static void onItemColorHandler(ColorHandlerEvent.Item event) {
		event.getItemColors().register(new ItemColor() {
			@Override
			public int getColor(ItemStack stack, int tintIndex) {
				if (tintIndex == 0) {
					AtomicInteger badEffectCount = new AtomicInteger();
					ElixirUtil.getFromNBT(stack).forEach((mobEffect, values) -> {
						if (!mobEffect.isBeneficial()) {
							badEffectCount.getAndIncrement();
						}
					});

					return ModUtil.blendColor(new Color(60, 60, 255), new Color(255, 60, 60), badEffectCount.get() <= 0 ? 0 : (float) badEffectCount.get() / 3f).getRGB();
					//return new Color(255, 255, 255).getRGB();
				}
				return 16777215;
			}

		}, ItemRegistry.elixir.get());
	}

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
		if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
			return;
		}
		event.addSprite(MortarPestleRenderer.pestle_texture);
	}

	@SubscribeEvent
	public static void registerRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(EntityRegistry.bone_dagger.get(), BoneDaggerRenderer::new);
	}

	@SubscribeEvent
	public static void registerParticles(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(ParticleRegistry.stab_attack.get(), StabAttackParticle.Provider::new);
	}

	public static void onRenderGameOverlay(RenderGameOverlayEvent.Pre e) {
		AetherBarRender.render(e);
	}
}
