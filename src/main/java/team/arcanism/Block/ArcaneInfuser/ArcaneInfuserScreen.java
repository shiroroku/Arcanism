package team.arcanism.Block.ArcaneInfuser;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import team.arcanism.Arcanism;

public class ArcaneInfuserScreen extends AbstractContainerScreen<ArcaneInfuserContainer> {

	private final ResourceLocation GUI = new ResourceLocation(Arcanism.MODID, "textures/gui/arcane_infuser.png");

	public ArcaneInfuserScreen(ArcaneInfuserContainer container, Inventory inv, Component name) {
		super(container, inv, name);
	}

	@Override
	public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack matrixStack, int mouseX, int mouseY) {
		//TranslatableComponent text = new TranslatableComponent("block.arcanism.arcane_infuser");
		//drawString(matrixStack, Minecraft.getInstance().font, text, this.imageWidth / 2 - Minecraft.getInstance().font.width(text) / 2, 8, 0xfff699);
	}

	@Override
	protected void renderBg(PoseStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, GUI);
		int relX = (this.width - this.imageWidth) / 2;
		int relY = (this.height - this.imageHeight) / 2;
		this.blit(matrixStack, relX, relY, 0, 0, this.imageWidth, this.imageHeight);

		if (this.menu.hasRecipe()) {
			matrixStack.pushPose();
			RenderSystem.enableBlend();
			float color = Mth.sin((float) Math.toRadians(Minecraft.getInstance().player.tickCount + partialTicks) * 4f) * 0.25f + 0.80f;
			RenderSystem.setShaderColor(0, 0, 0, 0.25f);
			matrixStack.translate(relX + 52 + 36f, relY + 7 + 36f, 0);
			matrixStack.mulPose(Vector3f.ZP.rotationDegrees(1.5f * Minecraft.getInstance().player.tickCount + partialTicks));
			matrixStack.translate(-36, -36, 0);
			this.blit(matrixStack, 0, 0, 176, 0, 72, 72);
			RenderSystem.disableBlend();
			matrixStack.popPose();
		}

	}
}
