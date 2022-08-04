package team.arcanism.Block.ArcaneInfuser;

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
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class ArcaneInfuserRecipe implements Recipe<Container> {

	public static final RecipeType<ArcaneInfuserRecipe> TYPE = RecipeType.register("arcane_infuser");
	public static final Serializer SERIALIZER = new Serializer();

	private final ResourceLocation ID;
	private final NonNullList<Ingredient> inputs;
	private final ItemStack output;
	private final float aetherCost;

	public ArcaneInfuserRecipe(ResourceLocation id, NonNullList<Ingredient> inputs, ItemStack output, float aetherCost) {
		ID = id;
		this.inputs = inputs;
		this.output = output;
		this.aetherCost = aetherCost;
	}

	public NonNullList<Ingredient> getInputs() {
		return inputs;
	}

	public ItemStack getOutput() {
		return output;
	}

	public float getAetherCost() {
		return aetherCost;
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
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return TYPE;
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ArcaneInfuserRecipe> {

		@Override
		public ArcaneInfuserRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			try {
				NonNullList<Ingredient> inputStacks = NonNullList.create();
				for (int i = 0; i < GsonHelper.getAsJsonArray(json, "ingredients").size(); ++i) {
					Ingredient ingredient = Ingredient.EMPTY;
					try {
						ingredient = Ingredient.fromJson(GsonHelper.getAsJsonArray(json, "ingredients").get(i));
					} catch (Exception ignored) {

					}
					inputStacks.add(ingredient);

				}

				if (inputStacks.isEmpty()) {
					throw new JsonParseException("No ingredients for crafting arcane infuser recipe.");
				} else {
					ItemStack outputStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output"));
					float aethercost = GsonHelper.getAsFloat(json, "aether_cost");
					return new ArcaneInfuserRecipe(recipeId, inputStacks, outputStack, aethercost);
				}
			} catch (JsonSyntaxException e) {
				return null;
			}
		}

		@Nullable
		@Override
		public ArcaneInfuserRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
			int inputSize = buffer.readVarInt();
			NonNullList<Ingredient> inputStacks = NonNullList.withSize(inputSize, Ingredient.EMPTY);
			inputStacks.replaceAll(ignored -> Ingredient.fromNetwork(buffer));
			float aethercost = buffer.readFloat();
			ItemStack outputStack = buffer.readItem();
			return new ArcaneInfuserRecipe(id, inputStacks, outputStack, aethercost);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, ArcaneInfuserRecipe recipe) {
			buffer.writeVarInt(recipe.inputs.size());
			for (Ingredient ingredient : recipe.inputs) {
				ingredient.toNetwork(buffer);
			}
			buffer.writeFloat(recipe.aetherCost);
			buffer.writeItem(recipe.output);
		}
	}
}
