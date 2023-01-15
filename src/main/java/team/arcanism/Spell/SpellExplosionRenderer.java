package team.arcanism.Spell;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import team.arcanism.Arcanism;

import java.awt.*;

public class SpellExplosionRenderer extends EntityRenderer<SpellExplosionEntity> {
	private static final ResourceLocation texture = new ResourceLocation(Arcanism.MODID, "textures/entity/spell_explosion.png");
	private static final RenderType rendertype = SpellRenderHelper.renderType(texture);

	public SpellExplosionRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.15F;
		this.shadowStrength = 0.75F;
	}

	@Override
	protected int getBlockLightLevel(SpellExplosionEntity spell, BlockPos pos) {
		return 15;
	}

	@Override
	public void render(SpellExplosionEntity spell, float a, float partialTick, PoseStack stack, MultiBufferSource buf, int light) {
		Color c = new Color(255, 255, 255, 100);

		stack.pushPose();
		float scale = 2f;
		stack.scale(scale, scale, scale);
		stack.translate(0, 0.25f, 0);

		stack.mulPose(this.entityRenderDispatcher.cameraOrientation());
		stack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
		stack.mulPose(Vector3f.ZN.rotationDegrees((((float) spell.tickCount + partialTick) / 2.0F) * 10));

		VertexConsumer consumer = buf.getBuffer(rendertype);
		PoseStack.Pose pose = stack.last();

		float completion = (float) spell.age / spell.lifetime;
		float texture_w = 288;
		float texture_h = 32;
		float index_size = 1f / (texture_w / texture_h);
		int index = (int) ((texture_w / texture_h) * completion);
		float x = index_size * index;
		SpellRenderHelper.vertex(consumer, pose, c, -1, -1, 0 + x, 1);
		SpellRenderHelper.vertex(consumer, pose, c, 1, -1, index_size + x, 1);
		SpellRenderHelper.vertex(consumer, pose, c, 1, 1, index_size + x, 0);
		SpellRenderHelper.vertex(consumer, pose, c, -1, 1, 0f + x, 0);
		stack.popPose();

		super.render(spell, a, partialTick, stack, buf, light);
	}

	@Override
	public ResourceLocation getTextureLocation(SpellExplosionEntity entity) {
		return texture;
	}
}
