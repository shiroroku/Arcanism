package team.arcanism.Block.ArcaneInfuser;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.CapabilityItemHandler;
import team.arcanism.Arcanism;

public class ArcaneInfuserRenderer implements BlockEntityRenderer<ArcaneInfuserBlockEntity> {

	public static final ResourceLocation octogram = new ResourceLocation(Arcanism.MODID, "blocks/arcane_infuser_octogram");
	private boolean showOctogram = false;

	public ArcaneInfuserRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(ArcaneInfuserBlockEntity infuser, float ptick, PoseStack stack, MultiBufferSource bufferSource, int overlay, int light) {
		//dont render unless close for performance
		if(Minecraft.getInstance().player.distanceToSqr(Vec3.atCenterOf(infuser.getBlockPos())) < Mth.square(16)) {

			//render item on infuser
			stack.pushPose();
			stack.translate(0.5D, 1.03125, 0.5D);
			stack.scale(0.5F, 0.5F, 0.5F);
			stack.mulPose(Vector3f.XP.rotationDegrees(90));
			infuser.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
				Minecraft.getInstance().getItemRenderer().renderStatic(handler.getStackInSlot(0), ItemTransforms.TransformType.FIXED, overlay, light, stack, bufferSource, light);
			});
			stack.popPose();

			//render octogram with valid recipe
			if (showOctogram) {
				TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(octogram);
				VertexConsumer builder = bufferSource.getBuffer(RenderType.cutout());

				float a = 1f;
				float r = 1f;
				float g = 1f;
				float b = 1f;

				stack.pushPose();
				stack.translate(0.5D, 0D, 0.5D);
				stack.mulPose(Vector3f.YN.rotationDegrees((infuser.getLevel().getGameTime() + ptick) * 2f));
				stack.translate(-0.5D, 0D, -0.5D);
				float hover = Mth.sin((float) Math.toRadians((infuser.getLevel().getGameTime() + ptick) * 4)) * 0.05f;
				Vector3f pos = new Vector3f(0f, 1.1f + hover, 0f);
				add(builder, stack, 0f + pos.x(), pos.y(), 1f - pos.z(), sprite.getU0(), sprite.getV1(), r, g, b, a);
				add(builder, stack, 1f - pos.x(), pos.y(), 1f - pos.z(), sprite.getU1(), sprite.getV1(), r, g, b, a);
				add(builder, stack, 1f - pos.x(), pos.y(), 0f + pos.z(), sprite.getU1(), sprite.getV0(), r, g, b, a);
				add(builder, stack, 0f + pos.x(), pos.y(), 0f + pos.z(), sprite.getU0(), sprite.getV0(), r, g, b, a);
				stack.popPose();
			}

			//update octogram every second instead of every tick
			if (infuser.getLevel().getGameTime() % 20 == 0){
				showOctogram = infuser.hasValidRecipe();
			}
		}
	}

	private void add(VertexConsumer renderer, PoseStack stack, float x, float y, float z, float u, float v, float r, float g, float b, float a) {
		renderer.vertex(stack.last().pose(), x, y, z).color(r, g, b, a).uv(u, v).uv2(0, 240).normal(1, 0, 0).endVertex();
	}
}
