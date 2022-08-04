package team.arcanism.Registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import team.arcanism.Arcanism;
import team.arcanism.Block.ArcaneInfuser.ArcaneInfuserBlock;
import team.arcanism.Block.MortarPestle.MortarPestleBlock;

import java.util.function.Supplier;

public class BlockRegistry {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Arcanism.MODID);

	public static final RegistryObject<Block> blackwood_log = regBlockItemWithBurntime("blackwood_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD)), 1600);
	public static final RegistryObject<Block> stripped_blackwood_log = regBlockItemWithBurntime("stripped_blackwood_log", () -> new RotatedPillarBlock(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F).sound(SoundType.WOOD)), 1600);
	public static final RegistryObject<Block> blackwood_planks = regBlockItemWithBurntime("blackwood_planks", () -> new Block(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(2.0F, 3.0F).sound(SoundType.WOOD)), 400);
	public static final RegistryObject<Block> blackwood_trapdoor = regBlockItem("blackwood_trapdoor", () -> new TrapDoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> blackwood_door = regBlockItem("blackwood_door", () -> new DoorBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).strength(3.0F).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> blackwood_slab = regBlockItem("blackwood_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(blackwood_planks.get())));
	public static final RegistryObject<Block> blackwood_stairs = regBlockItem("blackwood_stairs", () -> new StairBlock(() -> blackwood_planks.get().defaultBlockState(), BlockBehaviour.Properties.copy(blackwood_planks.get())));
	public static final RegistryObject<Block> blackwood_fence = regBlockItem("blackwood_fence", () -> new FenceBlock(BlockBehaviour.Properties.copy(blackwood_planks.get())));
	public static final RegistryObject<Block> arcane_infuser = regBlockItem("arcane_infuser", () -> new ArcaneInfuserBlock(BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_GRAY).strength(4.0f).sound(SoundType.WOOD).noOcclusion()));
	public static final RegistryObject<Block> mortar_and_pestle = regBlockItem("mortar_and_pestle", () -> new MortarPestleBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).strength(4.0f).sound(SoundType.STONE)));
	public static final RegistryObject<Block> aurum_block = regBlockItem("aurum_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD).strength(3.0F, 6.0F).sound(SoundType.METAL)));

	/**
	 * Creates and returns a Block while also creating an Item for that block.
	 *
	 * @param id Block id
	 * @param supplier Block factory
	 * @return Registry object of supplied Block
	 */
	private static <I extends Block> RegistryObject<I> regBlockItem(final String id, final Supplier<? extends I> supplier) {
		RegistryObject<I> createdBlock = BLOCKS.register(id, supplier);
		ItemRegistry.ITEMS.register(id, () -> new BlockItem(createdBlock.get(), new Item.Properties().tab(Arcanism.CREATIVETAB)));
		return createdBlock;
	}

	/**
	 * Same as {@link #regBlockItem(String, Supplier)} but also sets Item Burntime.
	 *
	 * @param id Block Id
	 * @param supplier Block factory
	 * @param burntime Burntime
	 * @return Registry object of supplied Block
	 */
	private static <I extends Block> RegistryObject<I> regBlockItemWithBurntime(final String id, final Supplier<? extends I> supplier, int burntime) {
		RegistryObject<I> createdBlock = BLOCKS.register(id, supplier);
		ItemRegistry.ITEMS.register(id, () -> new BlockItem(createdBlock.get(), new Item.Properties().tab(Arcanism.CREATIVETAB)) {
			@Override
			public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
				return burntime;
			}
		});
		return createdBlock;
	}

}