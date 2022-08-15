package team.arcanism.Block.MortarPestle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import team.arcanism.Arcanism;

public class MortarPestleRenderer implements BlockEntityRenderer<MortarPestleBlockEntity> {

	public static final ResourceLocation pestle_texture = new ResourceLocation(Arcanism.MODID, "entity/pestle");
	@SuppressWarnings("deprecation")
	private static final Material material = new Material(TextureAtlas.LOCATION_BLOCKS, pestle_texture);
	private final ModelPart pestle;

	public MortarPestleRenderer(BlockEntityRendererProvider.Context context) {
		pestle = createPestleLayer().bakeRoot();
	}

	public static @NotNull LayerDefinition createPestleLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition bb_main = partdefinition.addOrReplaceChild("pestle", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -8.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0f, 0.0F));
		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void render(MortarPestleBlockEntity blockEntity, float ptick, PoseStack stack, MultiBufferSource bufferSource, int p_112311_, int p_112312_) {
		//dont render unless close for performance
		if(Minecraft.getInstance().player.distanceToSqr(Vec3.atCenterOf(blockEntity.getBlockPos())) < Mth.square(16)) {
			stack.pushPose();
			stack.translate(0.5, 0.27, 0.5);
			stack.mulPose(Vector3f.ZP.rotationDegrees(90));
			if (blockEntity.spinTime > 0) {
				stack.mulPose(Vector3f.XP.rotationDegrees((blockEntity.spinTime - ptick) / 10f * 360f));
			}
			stack.mulPose(Vector3f.ZP.rotationDegrees(135));
			VertexConsumer vertexconsumer = material.buffer(bufferSource, RenderType::entitySolid);
			this.pestle.render(stack, vertexconsumer, p_112311_, p_112312_);
			stack.popPose();
			blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> renderItems(handler, blockEntity, ptick, stack, bufferSource, p_112311_, p_112312_));
		}

	}

	private void renderItems(IItemHandler handler, MortarPestleBlockEntity blockEntity, float ptick, PoseStack stack, MultiBufferSource bufferSource, int p_112311_, int p_112312_) {
		for (int slot = 0; slot < handler.getSlots(); slot++) {
			stack.pushPose();
			stack.translate(0.5, 0.2, 0.5);
			stack.mulPose(Vector3f.ZP.rotationDegrees(90));
			stack.scale(0.3f, 0.3f, 0.3f);
			float spin = blockEntity.spinTime > 0 ? ((blockEntity.spinTime - ptick) / 20f * 360f) : 0;
			stack.mulPose(Vector3f.XP.rotationDegrees(spin + (slot * 90)));
			stack.translate(0.2, 0, 0.25);
			stack.mulPose(Vector3f.YP.rotationDegrees(135));
			Minecraft.getInstance().getItemRenderer().renderStatic(handler.getStackInSlot(slot), ItemTransforms.TransformType.FIXED, p_112311_, p_112312_, stack, bufferSource, 0);
			stack.popPose();
		}
	}
}
