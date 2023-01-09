package team.arcanism.Registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserBlockEntity;
import team.arcanism.Block.MortarPestle.MortarPestleBlockEntity;

public class BlockEntityRegistry {

	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Arcanism.MODID);

	public static final RegistryObject<BlockEntityType<ArcaneInfuserBlockEntity>> arcane_infuser = BLOCK_ENTITIES.register("arcane_infuser", () -> BlockEntityType.Builder.of(ArcaneInfuserBlockEntity::new, BlockRegistry.arcane_infuser.get()).build(null));
	public static final RegistryObject<BlockEntityType<MortarPestleBlockEntity>> mortar_and_pestle = BLOCK_ENTITIES.register("mortar_and_pestle", () -> BlockEntityType.Builder.of(MortarPestleBlockEntity::new, BlockRegistry.mortar_and_pestle.get()).build(null));

}
