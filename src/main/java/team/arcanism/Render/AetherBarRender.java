package team.arcanism.Render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import team.arcanism.Arcanism;
import team.arcanism.Capability.IAether;
import team.arcanism.Registry.CapabilityRegistry;

import java.awt.*;

public class AetherBarRender {
	private static final ResourceLocation bar = new ResourceLocation(Arcanism.MODID, "textures/gui/aether_bar.png");
	private static float last_aether = 0f;
	private static float lerp_aether = 0f;
	private static float time = 0;
	private static float fade_timer = 0;

	public static void render(RenderGuiOverlayEvent.Pre e) {
		Minecraft client = Minecraft.getInstance();
		IAether cap = CapabilityRegistry.getAether(client.player).orElse(null);
		if (cap == null) {
			return;
		}
		float aether = cap.get();
		float max_aether = cap.getMax();

		if (last_aether != aether) {
			if (lerp_aether == 0) {
				lerp_aether = last_aether;
			}
			time = Math.min(time + client.getDeltaFrameTime() * 0.002f, 1f);
			last_aether = Mth.lerp(time, lerp_aether, aether);
			fade_timer = 20;
		} else {
			lerp_aether = 0;
			time = 0;
		}

		if (fade_timer > 0) {
			PoseStack stack = e.getPoseStack();

			int width = 44;
			int height = 182;
			int y = client.getWindow().getGuiScaledHeight() / 2 - height / 2;
			int x = 3;

			RenderSystem.setShaderTexture(0, bar);
			RenderSystem.enableBlend();
			RenderSystem.setShaderColor(1, 1, 1, 0.5f);//todo hook fade timer to this

			//border
			GuiComponent.blit(stack, x, y, 0, 0, 22, height, width, height);

			//bar
			int space = 13;
			float perc = last_aether / max_aether;
			int val = (int) ((1 - perc) * 182);
			int rate = 60;
			if (last_aether > aether) {
				rate *= 4;
			}
			float v = Mth.sin(client.player.tickCount / 360f * rate) * 0.10f + 1f;

			RenderSystem.setShaderColor(1 * v, 1 * v, 1 * v, 1f);
			GuiComponent.blit(stack, x, y + val, 22, 0, 22, height - val - space / 2, width, height);

			stack.pushPose();
			String text = String.valueOf(Math.round(last_aether));
			stack.translate(x + width / 6f, y + height - 8, 0);
			stack.mulPose(Vector3f.ZP.rotationDegrees(-90));
			Color c = last_aether > aether ? new Color(175, 0, 0) : last_aether < aether ? new Color(0, 175, 0) : Color.black;
			client.font.draw(stack, text, 0, 0, c.getRGB());
			stack.popPose();
			RenderSystem.disableBlend();

			fade_timer -= client.getDeltaFrameTime() * 0.01;
		}
	}

}
