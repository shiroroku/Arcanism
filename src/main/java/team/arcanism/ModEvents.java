package team.arcanism;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import team.arcanism.Capability.AetherCapability;
import team.arcanism.Command.AetherCommand;
import team.arcanism.Command.IngredientKnowledgeCommand;
import team.arcanism.Item.SkullRingItem;
import team.arcanism.Registry.BlockRegistry;
import team.arcanism.World.PlantGeneration;

import java.util.Map;

public class ModEvents {

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		SkullRingItem.handlePlayerTick(event);
		AetherCapability.passiveRegen(event);
	}

	@SubscribeEvent
	public static void onRegisterCommand(RegisterCommandsEvent event) {
		AetherCommand.register(event.getDispatcher());
		IngredientKnowledgeCommand.register(event.getDispatcher());
	}

	@SubscribeEvent
	public static void onBiomeLoad(BiomeLoadingEvent event) {
		if (event.getClimate().temperature > 0f && event.getClimate().temperature < 1f) {
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlantGeneration.blazel_patch);
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlantGeneration.blightshade_patch);
			event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PlantGeneration.wise_hazel_patch);
		}
	}

	public static final Map<Block, Block> BlockStripMap = Map.of(BlockRegistry.blackwood_log.get(), BlockRegistry.stripped_blackwood_log.get());

	@SubscribeEvent
	public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
		if (event.getItemStack().getItem() instanceof AxeItem) {
			Level level = event.getWorld();
			Block block = BlockStripMap.get(level.getBlockState(event.getPos()).getBlock());
			if (block == null) {
				return;
			}

			Player playerentity = event.getPlayer();
			level.playSound(playerentity, event.getPos(), SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
			if (!level.isClientSide()) {
				level.setBlock(event.getPos(), block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, level.getBlockState(event.getPos()).getValue(RotatedPillarBlock.AXIS)), 11);
				if (playerentity != null) {
					event.getItemStack().hurtAndBreak(1, event.getPlayer(), (player) -> {
						player.broadcastBreakEvent(event.getHand());
					});
				}
			}
		}
	}

}
