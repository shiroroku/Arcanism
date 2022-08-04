package team.arcanism.Compat.JEI;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserContainer;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserRecipe;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserScreen;
import team.arcanism.Block.MortarPestle.MortarPestleRecipe;
import team.arcanism.Registry.BlockRegistry;
import team.arcanism.Registry.ItemRegistry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@JeiPlugin
public class JEIPlugin implements IModPlugin {

	public static final RecipeType arcane_infuser = RecipeType.create(Arcanism.MODID, "arcane_infuser", JEIArcaneInfuserCategory.Wrapper.class);
	public static final RecipeType mortar_and_pestle = RecipeType.create(Arcanism.MODID, "mortar_and_pestle", JEIMortarPestleCategory.Wrapper.class);

	private static final TagKey<Item> elixir_ingredients = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Arcanism.MODID, "elixir_ingredients"));

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Arcanism.MODID, "recipes");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		registry.addRecipeCategories(new JEIArcaneInfuserCategory(registry.getJeiHelpers().getGuiHelper()));
		registry.addRecipeCategories(new JEIMortarPestleCategory(registry.getJeiHelpers().getGuiHelper()));
	}

	@Override
	public void registerRecipes(IRecipeRegistration registry) {
		registry.addRecipes(arcane_infuser, convertInfuserRecipes());
		registry.addRecipes(mortar_and_pestle, convertMortarRecipes());
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.arcane_infuser.get()), arcane_infuser);
		registry.addRecipeCatalyst(new ItemStack(BlockRegistry.mortar_and_pestle.get()), mortar_and_pestle);
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registry) {
		registry.addRecipeTransferHandler(ArcaneInfuserContainer.class, arcane_infuser, 0, 9, 10, 36);
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registry) {
		registry.addRecipeClickArea(ArcaneInfuserScreen.class, 90, 34, 24, 17, arcane_infuser);
	}

	private List<JEIArcaneInfuserCategory.Wrapper> convertInfuserRecipes() {
		List<JEIArcaneInfuserCategory.Wrapper> recipesconverted = new ArrayList<>();
		for (final ArcaneInfuserRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(ArcaneInfuserRecipe.TYPE)) {
			recipesconverted.add(new JEIArcaneInfuserCategory.Wrapper(recipe.getInputs(), recipe.getOutput(), recipe.getAetherCost()));
		}
		return recipesconverted;
	}

	private List<JEIMortarPestleCategory.Wrapper> convertMortarRecipes() {
		List<JEIMortarPestleCategory.Wrapper> recipesconverted = new ArrayList<>();
		for (final MortarPestleRecipe recipe : Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(MortarPestleRecipe.TYPE)) {
			recipesconverted.add(new JEIMortarPestleCategory.Wrapper(recipe.getInputs(), recipe.getOutput()));
		}

		recipesconverted.add(new JEIMortarPestleCategory.Wrapper(List.of(Ingredient.of(elixir_ingredients), Ingredient.of(elixir_ingredients), Ingredient.of(elixir_ingredients)), new ItemStack(ItemRegistry.elixir.get())));

		return recipesconverted;
	}

}
