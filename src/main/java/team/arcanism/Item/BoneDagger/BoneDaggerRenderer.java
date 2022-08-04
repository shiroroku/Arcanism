package team.arcanism.Item.BoneDagger;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import team.arcanism.Registry.ItemRegistry;

public class BoneDaggerRenderer extends EntityRenderer<BoneDaggerEntity> {

	public BoneDaggerRenderer(EntityRendererProvider.Context manager) {
		super(manager);
	}

	public void render(BoneDaggerEntity entity, float f1, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int lighting) {
		if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.camera.getEntity().distanceToSqr(entity) < 12.25D)) {
			matrix.pushPose();

			matrix.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotOCapture, entity.yRotCapture) - 90F));
			matrix.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(partialTicks, entity.xRotOCapture, entity.xRotCapture) + 225F));

			Minecraft.getInstance().getItemRenderer().renderStatic(new ItemStack(ItemRegistry.bone_dagger.get()), ItemTransforms.TransformType.FIXED, lighting, OverlayTexture.NO_OVERLAY, matrix, buffer, 0);
			matrix.popPose();
			super.render(entity, f1, partialTicks, matrix, buffer, lighting);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public ResourceLocation getTextureLocation(BoneDaggerEntity entity) {
		return TextureAtlas.LOCATION_BLOCKS;
	}
}
