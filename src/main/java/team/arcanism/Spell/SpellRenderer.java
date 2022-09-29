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

public class SpellRenderer extends EntityRenderer<SpellEntity> {

	private static final ResourceLocation texture = new ResourceLocation(Arcanism.MODID, "textures/entity/spell.png");
	private static final RenderType rendertype = RenderType.entityTranslucentCull(texture);

	public SpellRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	@Override
	protected int getBlockLightLevel(SpellEntity spell, BlockPos pos) {
		return Mth.clamp(super.getBlockLightLevel(spell, pos) + 7, 0, 15);
	}

	@Override
	public void render(SpellEntity spell, float a, float partialTick, PoseStack stack, MultiBufferSource buf, int light) {

		float f8 = ((float) spell.tickCount + partialTick) / 2.0F;
		int R = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int B = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int G = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);
		int A = (int) ((Mth.sin(f8 + 0.0F) + 1.0F) * 0.5F * 255.0F);

		float fade = 1f - (float) spell.age / spell.lifetime;
		this.shadowStrength = 0.75F * fade;

		R = 255;
		G = 255;
		B = 255;
		A = (int) (200f * fade);

		float scale = (spell.getBbWidth() - 0.2f) * fade;

		//Main
		stack.pushPose();
		stack.scale(scale, scale, scale);
		stack.translate(0, spell.getEyeHeight(), 0);
		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Vector3f.ZN.rotationDegrees(f8 * 30));
		stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		VertexConsumer consumer = buf.getBuffer(rendertype);
		PoseStack.Pose pose = stack.last();
		float x = 0f;
		vertex(consumer, pose.pose(), pose.normal(), -1, -1, R, G, B, A, 0 + x, 1, light);
		vertex(consumer, pose.pose(), pose.normal(), 1, -1, R, G, B, A, 0.5f + x, 1, light);
		vertex(consumer, pose.pose(), pose.normal(), 1, 1, R, G, B, A, 0.5f + x, 0, light);
		vertex(consumer, pose.pose(), pose.normal(), -1, 1, R, G, B, A, 0f + x, 0, light);
		stack.popPose();

		//Trails
		float scaledefault = scale;
		for (int set = 0; set < 2; set++) {
			scale = scaledefault;
			if (true) {
				scale *= 0.5f;
				for (int p = 2; p < 12; p++) {
					float p2 = p * 0.4f;
					float spacing = -3;
					float freq = 20;
					float amp = 0.6f;
					float sin = (float) Math.sin(Math.toRadians((spell.tickCount + partialTick) * freq + (set * 180)) - p2) * p2 * amp;
					float cos = (float) Math.cos(Math.toRadians((spell.tickCount + partialTick) * freq + (set * 180)) - p2) * p2 * amp;

					stack.pushPose();
					stack.scale(scale, scale, scale);
					stack.translate(0, spell.getEyeHeight() + 1, 0f);

					double distance = (p * spacing);

					double sinX = spell.getDeltaMovement().x * distance;
					double vX = Math.abs(spell.getDeltaMovement().normalize().x);
					double sinY = spell.getDeltaMovement().y * distance;
					double vY = Math.abs(spell.getDeltaMovement().normalize().y);
					double sinZ = spell.getDeltaMovement().z * distance;
					double vZ = Math.abs(spell.getDeltaMovement().normalize().z);

					sinX = sinX + Mth.lerp(vX, sin, 0);
					sinY = sinY + Mth.lerp(vY, Mth.lerp(vX, cos, sin), 0);
					sinZ = sinZ + Mth.lerp(vZ, cos, 0);

					stack.translate(sinX, sinY, sinZ);

					//Forwardback
					// (-1, 0, 0)
					// (1, 0, 0)
					//stack.translate(spell.getDeltaMovement().x * distance, spell.getDeltaMovement().y * distance + sin, spell.getDeltaMovement().z * distance + cos);

					//Updown
					//stack.translate(spell.getDeltaMovement().x * distance + sin, spell.getDeltaMovement().y * distance, spell.getDeltaMovement().z * distance + cos);

					//Leftright
					// (0, 0, 1)
					// (0, 0, -1)
					//stack.translate(spell.getDeltaMovement().x * distance + sin, spell.getDeltaMovement().y * distance + cos, spell.getDeltaMovement().z * distance);

					//System.out.println(spell.getDeltaMovement());
					stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
					stack.mulPose(Vector3f.ZP.rotationDegrees(f8 * 10));
					stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));

					VertexConsumer consumer2 = buf.getBuffer(rendertype);
					PoseStack.Pose pose2 = stack.last();

					x = p % 2 == 0 ? 0.5f : 0.75f;
					vertex(consumer2, pose2.pose(), pose2.normal(), -1, -1, R, G, B, A, 0 + x, 0.5f, light);
					vertex(consumer2, pose2.pose(), pose2.normal(), 1, -1, R, G, B, A, 0.25f + x, 0.5f, light);
					vertex(consumer2, pose2.pose(), pose2.normal(), 1, 1, R, G, B, A, 0.25f + x, 0, light);
					vertex(consumer2, pose2.pose(), pose2.normal(), -1, 1, R, G, B, A, 0f + x, 0, light);

					stack.popPose();

					scale *= 0.95f;
				}
			}
		}

		super.render(spell, a, partialTick, stack, buf, light);
	}

	private void vertex(VertexConsumer consumer, Matrix4f matrix4, Matrix3f matrix3, float x, float y, int r, int g, int b, int a, float u, float v, int light) {
		consumer.vertex(matrix4, x, y, 0.0F).color(r, g, b, a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3, 0, 0, 1).endVertex();
	}

	@Override
	public ResourceLocation getTextureLocation(SpellEntity entity) {
		return texture;
	}
}
