package team.arcanism.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import team.arcanism.Arcanism;
import team.arcanism.Capability.IIngredientKnowledge;
import team.arcanism.Registry.CapabilityRegistry;

public class IngredientKnowledgeCommand {
	private static final TagKey<Item> ingredients = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Arcanism.MODID, "elixir_ingredients"));

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> aether = Commands.literal("ingredient_knowledge").requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.argument("player", EntityArgument.player()).then(Commands.literal("clear").executes((commandSource -> {
			Player p = EntityArgument.getPlayer(commandSource, "player");
			CapabilityRegistry.getIngredientKnowledge(p).ifPresent(IIngredientKnowledge::clear);
			CapabilityRegistry.sendIngredientKnowledgePacket(p);
			return 0;
		}))).then(Commands.literal("fill").executes((commandSource -> {
			Player p = EntityArgument.getPlayer(commandSource, "player");
			ForgeRegistries.ITEMS.getValues().stream().filter(item -> new ItemStack(item).getTags().anyMatch(t -> (t.equals(ingredients)))).forEach((item) -> {
				CapabilityRegistry.getIngredientKnowledge(p).ifPresent((cap) -> {
					for (int i = 1; i <= 3; i++) {
						cap.setKnows(item, i, true);
					}
				});
			});
			CapabilityRegistry.sendIngredientKnowledgePacket(EntityArgument.getPlayer(commandSource, "player"));
			return 0;
		}))));
		dispatcher.register(aether);
	}

}
