package team.arcanism.Spell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import team.arcanism.Arcanism;

public class SpellExplosionRenderer extends EntityRenderer<SpellExplosionEntity> {
	private static final ResourceLocation texture = new ResourceLocation(Arcanism.MODID, "textures/entity/spell_explosion.png");
	private static final RenderType rendertype = RenderType.entityTranslucentCull(texture);

	public SpellExplosionRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	@Override
	protected int getBlockLightLevel(SpellExplosionEntity spell, BlockPos pos) {
		return Mth.clamp(super.getBlockLightLevel(spell, pos) + 7, 0, 15);
	}

	@Override
	public void render(SpellExplosionEntity spell, float a, float partialTick, PoseStack stack, MultiBufferSource buf, int light) {

		float fade = 1f - (float) spell.age / spell.lifetime;
		this.shadowStrength = 0.75F * fade;

		int R = 255;
		int G = 255;
		int B = 255;
		int A = 240;//(int) (255f * fade);

		float scale = 2f;// (spell.getBbWidth() - 0.2f) * fade;

		//Main
		stack.pushPose();
		stack.scale(scale, scale, scale);
		stack.translate(0, 0.25f, 0);
		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		VertexConsumer consumer = buf.getBuffer(rendertype);
		PoseStack.Pose pose = stack.last();

		float completion = (float) spell.age / spell.lifetime;
		float texture_w = 288;
		float texture_h = 32;
		float index_size = 1f / (texture_w / texture_h);
		int index = (int) ((texture_w / texture_h) * completion);
		float x = index_size * index;
		vertex(consumer, pose.pose(), pose.normal(), -1, -1, R, G, B, A, 0 + x, 1, light);
		vertex(consumer, pose.pose(), pose.normal(), 1, -1, R, G, B, A, index_size + x, 1, light);
		vertex(consumer, pose.pose(), pose.normal(), 1, 1, R, G, B, A, index_size + x, 0, light);
		vertex(consumer, pose.pose(), pose.normal(), -1, 1, R, G, B, A, 0f + x, 0, light);
		stack.popPose();

		super.render(spell, a, partialTick, stack, buf, light);
	}

	private void vertex(VertexConsumer consumer, Matrix4f matrix4, Matrix3f matrix3, float x, float y, int r, int g, int b, int a, float u, float v, int light) {
		consumer.vertex(matrix4, x, y, 0.0F).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3, 0, 0, 1).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(SpellExplosionEntity entity) {
		return texture;
	}
}
