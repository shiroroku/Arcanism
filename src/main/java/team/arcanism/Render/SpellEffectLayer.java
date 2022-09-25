package team.arcanism.Render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import team.arcanism.Arcanism;

public class SpellEffectLayer extends RenderLayer<LivingEntity, EntityModel<LivingEntity>> {

	private static final ResourceLocation texture = new ResourceLocation(Arcanism.MODID, "textures/entity/spell_effect.png");
	private final EntityModel model;

	public SpellEffectLayer(RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> parent, EntityModel model) {
		super(parent);
		this.model = model;
	}

	public void render(PoseStack stack, MultiBufferSource buffer, int light, LivingEntity entity, float p_116974_, float p_116975_, float p_116976_, float p_116977_, float p_116978_, float p_116979_) {

		float f = (float) entity.tickCount + p_116976_;
		EntityModel<LivingEntity> entitymodel = model;
		entitymodel.prepareMobModel(entity, p_116974_, p_116975_, p_116976_);
		this.getParentModel().copyPropertiesTo(entitymodel);
		VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.energySwirl(this.getTextureLocation(), this.xOffset(f) % 1.0F, f * 0.01F % 1.0F));
		entitymodel.setupAnim(entity, p_116974_, p_116975_, p_116977_, p_116978_, p_116979_);
		entitymodel.renderToBuffer(stack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 0.5F, 0.5F, 0.5F, 1.0F);

	}

	protected float xOffset(float x) {
		return x * 0.01F;
	}

	protected ResourceLocation getTextureLocation() {
		return texture;
	}

}
