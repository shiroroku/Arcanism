package team.arcanism.Compat.JEI;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.arcanism.Arcanism;
import team.arcanism.Registry.BlockRegistry;

import java.util.List;

public class JEIMortarPestleCategory implements IRecipeCategory<JEIMortarPestleCategory.Wrapper> {

	private final IDrawable bg, icon;
	public static final ResourceLocation screen = new ResourceLocation(Arcanism.MODID, "textures/gui/mortar_and_pestle.png");

	@SuppressWarnings("removal")
	public JEIMortarPestleCategory(IGuiHelper guihelper) {
		this.bg = guihelper.createDrawable(screen, 0, 0, 162, 92);
		this.icon = guihelper.createDrawableIngredient(new ItemStack(BlockRegistry.mortar_and_pestle.get()));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, Wrapper recipe, IFocusGroup focuses) {
		int inputs = recipe.getInputs().size();

		int ox = 73;
		int oy = 40;
		int radius = 32;

		float step = 40;

		for (int i = 0; i < inputs; i++) {
			double rad = Math.toRadians(step * i - 90 - ((inputs - 1) * step / 2));
			int x = ox + (int) (radius * Math.cos(rad));
			int y = oy + (int) (radius * Math.sin(rad));
			builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(recipe.getInputs().get(i));
		}
		builder.addSlot(RecipeIngredientRole.OUTPUT, ox, oy - 5).addItemStack(recipe.getOutput());

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
		return JEIPlugin.mortar_and_pestle;
	}

	@Override
	public Component getTitle() {
		return new TranslatableComponent("block.arcanism.mortar_and_pestle");
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

		public Wrapper(List<Ingredient> input, ItemStack output) {
			INPUTS = input;
			OUTPUT = output;
		}

		public List<Ingredient> getInputs() {
			return INPUTS;
		}

		public ItemStack getOutput() {
			return OUTPUT;
		}
	}
}
