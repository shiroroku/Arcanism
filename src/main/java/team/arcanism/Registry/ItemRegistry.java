package team.arcanism.Registry;

import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import team.arcanism.Arcanism;
import team.arcanism.Elixir.ElixirIngredientBlockItem;
import team.arcanism.Elixir.ElixirIngredientItem;
import team.arcanism.Elixir.ElixirItem;
import team.arcanism.Item.BoneDagger.BoneDaggerItem;
import team.arcanism.Item.BoneSwordItem;
import team.arcanism.Item.SkullRingItem;

import java.util.TreeMap;
import java.util.function.Supplier;

public class ItemRegistry {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Arcanism.MODID);

	public static final RegistryObject<Item> bone_sword = ITEMS.register("bone_sword", () -> new BoneSwordItem(ToolTiers.bone, 3, -1.8f, defProp()));
	public static final RegistryObject<Item> bone_axe = ITEMS.register("bone_axe", () -> new AxeItem(ToolTiers.bone, 7, -3.2f, defProp()));
	public static final RegistryObject<Item> bone_pickaxe = ITEMS.register("bone_pickaxe", () -> new PickaxeItem(ToolTiers.bone, 1, -2.8f, defProp()));
	public static final RegistryObject<Item> bone_shovel = ITEMS.register("bone_shovel", () -> new ShovelItem(ToolTiers.bone, 1.5f, -3f, defProp()));
	public static final RegistryObject<Item> bone_hoe = ITEMS.register("bone_hoe", () -> new HoeItem(ToolTiers.bone, -1, -2f, defProp()));
	public static final RegistryObject<Item> bone_dagger = ITEMS.register("bone_dagger", () -> new BoneDaggerItem(defProp().stacksTo(16)));

	public static final RegistryObject<Item> aethersteel_sword = ITEMS.register("aethersteel_sword", () -> new SwordItem(ToolTiers.aethersteel, 3, -1.8f, defProp()));
	public static final RegistryObject<Item> aethersteel_axe = ITEMS.register("aethersteel_axe", () -> new AxeItem(ToolTiers.aethersteel, 7, -3.2f, defProp()));
	public static final RegistryObject<Item> aethersteel_pickaxe = ITEMS.register("aethersteel_pickaxe", () -> new PickaxeItem(ToolTiers.aethersteel, 1, -2.8f, defProp()));
	public static final RegistryObject<Item> aethersteel_shovel = ITEMS.register("aethersteel_shovel", () -> new ShovelItem(ToolTiers.aethersteel, 1.5f, -3f, defProp()));
	public static final RegistryObject<Item> aethersteel_hoe = ITEMS.register("aethersteel_hoe", () -> new HoeItem(ToolTiers.aethersteel, -1, -2f, defProp()));

	public static final RegistryObject<Item> blackwood_bow = ITEMS.register("blackwood_bow", () -> new BowItem(defProp().durability(400)));
	public static final RegistryObject<Item> aurum_ring = simpleItem("aurum_ring", defProp().stacksTo(1));
	public static final RegistryObject<Item> blackwood_wand = simpleItem("blackwood_wand", defProp().stacksTo(1));
	public static final RegistryObject<Item> aurum_blend = simpleItem("aurum_blend");
	public static final RegistryObject<Item> aurum_ingot = simpleItem("aurum_ingot");
	public static final RegistryObject<Item> aurum_nugget = simpleItem("aurum_nugget");
	public static final RegistryObject<Item> skull_ring = ITEMS.register("skull_ring", () -> new SkullRingItem(defProp().stacksTo(1)));
	public static final RegistryObject<Item> aethersteel_ingot = ITEMS.register("aethersteel_ingot", () -> new Item(defProp()));
	public static final RegistryObject<Item> blackwood_twig = ITEMS.register("blackwood_twig", () -> new Item(defProp()) {
		@Override
		public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
			return 200;
		}
	});

	public static final RegistryObject<Item> elixir = ITEMS.register("elixir", () -> new ElixirItem(defProp()));
	public static final RegistryObject<Item> arcane_powder = regElixirIngredient("arcane_powder", new TreeMap<>() {{
		put(1, new Tuple<>(MobEffects.CONFUSION, 30));
		put(2, new Tuple<>(MobEffects.LEVITATION, 15));
		put(3, new Tuple<>(MobEffects.REGENERATION, 120));//AETHER REGEN!!!!!!!!!!!!!!
	}});
	public static final RegistryObject<Block> wise_hazel = regElixirIngredient("wise_hazel", () -> new FlowerBlock(MobEffects.REGENERATION, 10, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), new TreeMap<>() {{
		put(1, new Tuple<>(MobEffects.REGENERATION, 60));
		put(2, new Tuple<>(MobEffects.CONFUSION, 10));
		put(3, new Tuple<>(MobEffects.WEAKNESS, 30));
	}});
	public static final RegistryObject<Block> blazel = regElixirIngredient("blazel", () -> new FlowerBlock(MobEffects.WITHER, 10, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), new TreeMap<>() {{
		put(1, new Tuple<>(MobEffects.WITHER, 10));
		put(2, new Tuple<>(MobEffects.DAMAGE_BOOST, 60));
		put(3, new Tuple<>(MobEffects.FIRE_RESISTANCE, 120));
	}});
	public static final RegistryObject<Block> blightshade = regElixirIngredient("blightshade", () -> new FlowerBlock(MobEffects.BLINDNESS, 10, BlockBehaviour.Properties.of(Material.PLANT).noCollission().instabreak().sound(SoundType.GRASS)), new TreeMap<>() {{
		put(1, new Tuple<>(MobEffects.BLINDNESS, 30));
		put(2, new Tuple<>(MobEffects.POISON, 60));
		put(3, new Tuple<>(MobEffects.HARM, 0));
	}});

	private static <I extends Item> RegistryObject<Item> regElixirIngredient(final String id, TreeMap<Integer, Tuple<MobEffect, Integer>> effects) {
		return ITEMS.register(id, () -> new ElixirIngredientItem(new Item.Properties().tab(Arcanism.CREATIVETAB), effects));
	}

	private static <I extends Block> RegistryObject<I> regElixirIngredient(final String id, final Supplier<? extends I> supplier, TreeMap<Integer, Tuple<MobEffect, Integer>> effects) {
		RegistryObject<I> createdBlock = BlockRegistry.BLOCKS.register(id, supplier);
		ITEMS.register(id, () -> new ElixirIngredientBlockItem(createdBlock.get(), new Item.Properties().tab(Arcanism.CREATIVETAB), effects));
		return createdBlock;
	}

	private static Item.Properties defProp() {
		return new Item.Properties().tab(Arcanism.CREATIVETAB);
	}

	private static RegistryObject<Item> simpleItem(String ID) {
		return ITEMS.register(ID, () -> new Item(defProp()));
	}

	private static RegistryObject<Item> simpleItem(String ID, Item.Properties properties) {
		return ITEMS.register(ID, () -> new Item(properties));
	}

	public static class ToolTiers {
		private static final Tier bone = new Tier() {
			@Override
			public int getUses() {
				return 131;
			}

			@Override
			public float getSpeed() {
				return 4.0f;
			}

			@Override
			public float getAttackDamageBonus() {
				return 1.0f;
			}

			@Override
			public int getLevel() {
				return 1;
			}

			@Override
			public int getEnchantmentValue() {
				return 22;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(Tags.Items.BONES);
			}
		};

		private static final Tier aethersteel = new Tier() {
			@Override
			public int getUses() {
				return 300;
			}

			@Override
			public float getSpeed() {
				return 6.0f;
			}

			@Override
			public float getAttackDamageBonus() {
				return 2.0f;
			}

			@Override
			public int getLevel() {
				return 2;
			}

			@Override
			public int getEnchantmentValue() {
				return 14;
			}

			@Override
			public Ingredient getRepairIngredient() {
				return Ingredient.of(aethersteel_ingot.get());
			}
		};
	}
}
