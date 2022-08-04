package team.arcanism.Registry;

import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserRecipe;
import team.arcanism.Block.MortarPestle.MortarPestleRecipe;

public class RecipeRegistry {

	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Arcanism.MODID);

	public static final RegistryObject<RecipeSerializer<ArcaneInfuserRecipe>> arcane_infuser = SERIALIZERS.register("arcane_infuser", () -> ArcaneInfuserRecipe.SERIALIZER);
	public static final RegistryObject<RecipeSerializer<MortarPestleRecipe>> mortar_and_pestle = SERIALIZERS.register("mortar_and_pestle", () -> MortarPestleRecipe.SERIALIZER);

}
