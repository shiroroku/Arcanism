package team.arcanism.Spell;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class SpellRenderHelper extends RenderType {

	//Ignored
	public SpellRenderHelper(
		String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
		super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
	}

	public static void vertex(VertexConsumer consumer, PoseStack.Pose pose, Color c, float x, float y, float u, float v) {
		consumer.vertex(pose.pose(), x, y, 0.0F).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(pose.normal(), 0, 0, 0).endVertex();
	}
	
	public static RenderType renderType(ResourceLocation texture) {
		CompositeState compositeState = CompositeState.builder()
			.setShaderState(RENDERTYPE_ENTITY_ALPHA_SHADER)
			.setTextureState(new TextureStateShard(texture, false, false))
			.setCullState(CULL)
			.createCompositeState(true);
		return RenderType.create("full_bright", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 256, false, true, compositeState);
	}

}
