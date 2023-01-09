package team.arcanism.Registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserRecipe;
import team.arcanism.Block.MortarPestle.MortarPestleRecipe;

public class RecipeRegistry {

	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Arcanism.MODID);

	public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Arcanism.MODID);

	public static final RegistryObject<RecipeType<ArcaneInfuserRecipe>> arcane_infuser_type = TYPES.register("arcane_infuser", () -> RecipeType.simple(new ResourceLocation(Arcanism.MODID, "arcane_infuser")));
	public static final RegistryObject<RecipeType<MortarPestleRecipe>> mortar_and_pestle_type = TYPES.register("mortar_and_pestle", () -> RecipeType.simple(new ResourceLocation(Arcanism.MODID, "mortar_and_pestle")));

	public static final RegistryObject<RecipeSerializer<ArcaneInfuserRecipe>> arcane_infuser = SERIALIZERS.register("arcane_infuser", ArcaneInfuserRecipe.Serializer::new);
	public static final RegistryObject<RecipeSerializer<MortarPestleRecipe>> mortar_and_pestle = SERIALIZERS.register("mortar_and_pestle", MortarPestleRecipe.Serializer::new);

}
