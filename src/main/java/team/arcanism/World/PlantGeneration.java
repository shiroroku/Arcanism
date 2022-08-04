package team.arcanism.World;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import team.arcanism.Arcanism;
import team.arcanism.Registry.ItemRegistry;

import java.util.List;

public class PlantGeneration {

	public static Holder<PlacedFeature> wise_hazel_patch;
	public static Holder<PlacedFeature> blazel_patch;
	public static Holder<PlacedFeature> blightshade_patch;

	public static RandomPatchConfiguration getWildCropConfiguration(Block block, int tries, int xzSpread, BlockPredicate plantedOn) {
		return new RandomPatchConfiguration(tries, xzSpread, 3, PlacementUtils.filtered(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(block)), BlockPredicate.allOf(BlockPredicate.ONLY_IN_AIR_PREDICATE, plantedOn)));
	}

	private static Holder<PlacedFeature> registerPlacement(ResourceLocation id, Holder<? extends ConfiguredFeature<?, ?>> feature, PlacementModifier... modifiers) {
		return BuiltinRegistries.register(BuiltinRegistries.PLACED_FEATURE, id, new PlacedFeature(Holder.hackyErase(feature), List.of(modifiers)));
	}

	private static <FC extends FeatureConfiguration, F extends Feature<FC>> Holder<ConfiguredFeature<FC, ?>> register(ResourceLocation id, FC featureConfig) {
		return register(id, new ConfiguredFeature<>((F) Feature.RANDOM_PATCH, featureConfig));
	}

	private static <V extends T, T> Holder<V> register(ResourceLocation id, V value) {
		return (Holder<V>) BuiltinRegistries.register((Registry<T>) BuiltinRegistries.CONFIGURED_FEATURE, id, value);
	}

	public static void registerPlantGeneration() {
		Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> wise_hazel = register(new ResourceLocation(Arcanism.MODID, "wise_hazel"), getWildCropConfiguration(ItemRegistry.wise_hazel.get(), 32, 8, BlockPredicate.matchesTag(BlockTags.DIRT, new BlockPos(0, -1, 0))));
		wise_hazel_patch = registerPlacement(new ResourceLocation("wise_hazel"), wise_hazel, RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

		Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> blazel = register(new ResourceLocation(Arcanism.MODID, "blazel"), getWildCropConfiguration(ItemRegistry.blazel.get(), 32, 8, BlockPredicate.matchesTag(BlockTags.DIRT, new BlockPos(0, -1, 0))));
		blazel_patch = registerPlacement(new ResourceLocation("blazel"), blazel, RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

		Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> blightshade = register(new ResourceLocation(Arcanism.MODID, "blightshade"), getWildCropConfiguration(ItemRegistry.blightshade.get(), 32, 8, BlockPredicate.matchesTag(BlockTags.DIRT, new BlockPos(0, -1, 0))));
		blightshade_patch = registerPlacement(new ResourceLocation("blightshade"), blightshade, RarityFilter.onAverageOnceEvery(12), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome());
	}
}
