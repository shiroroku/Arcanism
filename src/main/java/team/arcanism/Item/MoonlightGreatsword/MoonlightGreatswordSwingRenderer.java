package team.arcanism.Item.MoonlightGreatsword;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import team.arcanism.ModUtil;
import team.arcanism.Registry.ItemRegistry;

public class MoonlightGreatswordSwingRenderer {

	public static int swingTime = 160;
	public static int swingtick = 0;

	public static void onRenderHandEvent(RenderHandEvent e) {
		if (Minecraft.getInstance().player.getMainHandItem().is(ItemRegistry.moonlight_greatsword.get()) && e.getEquipProgress() == 0) {

			if (swingtick > 0) {
				if (Minecraft.getInstance().player.swinging) {
					swingtick = 0;
					return;
				}

				PoseStack stack = e.getPoseStack();
				float speed = 15f;
				swingtick--;
				float wave = (float) ModUtil.bezierSwingEase(1 - swingtick / (float) swingTime, 0.2f);

				float slide = 0;
				stack.translate((-1 - slide / 2) - (wave * slide), -1, 0);

				float p = -0.5f;
				float z = 1f;
				stack.translate(z, 0, p);
				stack.mulPose(Vector3f.YP.rotationDegrees(180 + 180 * wave));
				stack.translate(-z, 0, -p);
				stack.mulPose(Vector3f.ZP.rotationDegrees(90));
				stack.mulPose(Vector3f.XN.rotationDegrees(20));

			}
		}

	}

	public static void onRenderPlayerEvent(RenderPlayerEvent.Post e) {
		PoseStack stack = e.getPoseStack();

	}

}
