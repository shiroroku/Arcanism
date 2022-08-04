package team.arcanism.Compat.JEI;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.arcanism.Arcanism;
import team.arcanism.Registry.BlockRegistry;

import java.util.List;

public class JEIArcaneInfuserCategory implements IRecipeCategory<JEIArcaneInfuserCategory.Wrapper> {

	private final IDrawable bg, icon;
	public static final ResourceLocation screen = new ResourceLocation(Arcanism.MODID, "textures/gui/arcane_infuser.png");

	@SuppressWarnings("removal")
	public JEIArcaneInfuserCategory(IGuiHelper guihelper) {
		this.bg = guihelper.createDrawable(screen, 7, 3, 162, 78);
		this.icon = guihelper.createDrawableIngredient(new ItemStack(BlockRegistry.arcane_infuser.get()));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, Wrapper recipe, IFocusGroup focuses) {
		int slotIndex = 0;
		//input 0 is middle slot
		builder.addSlot(RecipeIngredientRole.INPUT, 37, 32).addIngredients(recipe.getInputs().get(0));
		builder.addSlot(RecipeIngredientRole.INPUT, 37, 5).addIngredients(recipe.getInputs().get(1));
		builder.addSlot(RecipeIngredientRole.INPUT, 59, 10).addIngredients(recipe.getInputs().get(2));
		builder.addSlot(RecipeIngredientRole.INPUT, 64, 32).addIngredients(recipe.getInputs().get(3));
		builder.addSlot(RecipeIngredientRole.INPUT, 59, 54).addIngredients(recipe.getInputs().get(4));
		builder.addSlot(RecipeIngredientRole.INPUT, 37, 59).addIngredients(recipe.getInputs().get(5));
		builder.addSlot(RecipeIngredientRole.INPUT, 15, 54).addIngredients(recipe.getInputs().get(6));
		builder.addSlot(RecipeIngredientRole.INPUT, 10, 32).addIngredients(recipe.getInputs().get(7));
		builder.addSlot(RecipeIngredientRole.INPUT, 15, 10).addIngredients(recipe.getInputs().get(8));

		builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 32).addItemStack(recipe.getOutput());

	}

	@Override
	public void draw(Wrapper recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
		Font f = Minecraft.getInstance().font;
		MutableComponent cost = new TranslatableComponent("jei.arcanism.aether_cost", recipe.getAetherCost()).withStyle(ChatFormatting.UNDERLINE, ChatFormatting.WHITE);
		f.drawShadow(stack, cost, 125 - (f.width(cost) / 2f), 10, 4210752);
	}

	@SuppressWarnings("removal")
	@Override
	public Class<? extends Wrapper> getRecipeClass() {
		return null;
	}

	@SuppressWarnings("removal")
	@Override
	public ResourceLocation getUid() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RecipeType<Wrapper> getRecipeType() {
		return JEIPlugin.arcane_infuser;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("block.arcanism.arcane_infuser");
	}

	@Override
	public IDrawable getBackground() {
		return bg;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	public static class Wrapper {
		private final List<Ingredient> INPUTS;
		private final ItemStack OUTPUT;
		private final float AETHER_COST;

		public Wrapper(List<Ingredient> input, ItemStack output, float aether_cost) {
			INPUTS = input;
			OUTPUT = output;
			AETHER_COST = aether_cost;
		}

		public List<Ingredient> getInputs() {
			return INPUTS;
		}

		public ItemStack getOutput() {
			return OUTPUT;
		}

		public float getAetherCost() {
			return AETHER_COST;
		}
	}
}
