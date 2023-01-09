package team.arcanism.Block.MortarPestle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import team.arcanism.Registry.RecipeRegistry;

import javax.annotation.Nullable;

public class MortarPestleRecipe implements Recipe<Container> {

	private final ResourceLocation ID;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;

	public MortarPestleRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output) {
		ID = id;
		this.inputs = inputs;
		this.output = output;
	}

	public NonNullList<Ingredient> getInputs() {
		return inputs;
	}

	public ItemStack getOutput() {
		return output;
	}

	@Override
	public boolean matches(Container container, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(Container container) {
		return null;
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return this.output;
	}

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeRegistry.mortar_and_pestle.get();
	}

	@Override
	public RecipeType<?> getType() {
		return RecipeRegistry.mortar_and_pestle_type.get();
	}

	public static class Serializer implements RecipeSerializer<MortarPestleRecipe> {

		@Override
		public MortarPestleRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			try {
				NonNullList<Ingredient> inputStacks = NonNullList.create();
				for (int i = 0; i < GsonHelper.getAsJsonArray(json, "ingredients").size(); ++i) {
					Ingredient ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredients").get(i));
					if (!ingredient.isEmpty()) {
						inputStacks.add(ingredient);
					}
				}

				if (inputStacks.isEmpty()) {
					throw new JsonParseException("No ingredients for mortar pestle recipe.");
				} else {
					ItemStack outputStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
					return new MortarPestleRecipe(recipeId, inputStacks, outputStack);
				}
			} catch (JsonSyntaxException e) {
				return null;
			}
		}

		@Nullable
		@Override
		public MortarPestleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			int inputSize = buffer.readVarInt();
			NonNullList<Ingredient> inputStacks = NonNullList.withSize(inputSize, Ingredient.EMPTY);
			inputStacks.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
			ItemStack outputStack = buffer.readItem();
			return new MortarPestleRecipe(id, inputStacks, outputStack);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, MortarPestleRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());
			for (Ingredient ingredient : recipe.inputs) {
				ingredient.toNetwork(buffer);
			}
			buffer.writeItem(recipe.output);
		}
	}
}
