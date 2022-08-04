package team.arcanism.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import team.arcanism.Registry.CapabilityRegistry;

public class AetherCommand {

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> aether = Commands.literal("aether").requires((commandSource) -> commandSource.hasPermission(2)).then(Commands.literal("set").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((commandSource -> {
			CapabilityRegistry.getAether(EntityArgument.getPlayer(commandSource, "player")).ifPresent(cap -> cap.set(IntegerArgumentType.getInteger(commandSource, "amount")));
			CapabilityRegistry.sendAetherPacket(EntityArgument.getPlayer(commandSource, "player"));
			return 0;
		}))))).then(Commands.literal("set_max").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((commandSource -> {
			CapabilityRegistry.getAether(EntityArgument.getPlayer(commandSource, "player")).ifPresent(cap -> cap.setMax(IntegerArgumentType.getInteger(commandSource, "amount")));
			CapabilityRegistry.sendAetherPacket(EntityArgument.getPlayer(commandSource, "player"));
			return 0;
		}))))).then(Commands.literal("add").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((commandSource -> {
			CapabilityRegistry.getAether(EntityArgument.getPlayer(commandSource, "player")).ifPresent(cap -> cap.add(IntegerArgumentType.getInteger(commandSource, "amount")));
			CapabilityRegistry.sendAetherPacket(EntityArgument.getPlayer(commandSource, "player"));
			return 0;
		}))))).then(Commands.literal("spend").then(Commands.argument("player", EntityArgument.player()).then(Commands.argument("amount", IntegerArgumentType.integer(0)).executes((commandSource -> {
			CapabilityRegistry.getAether(EntityArgument.getPlayer(commandSource, "player")).ifPresent(cap -> cap.spend(IntegerArgumentType.getInteger(commandSource, "amount")));
			CapabilityRegistry.sendAetherPacket(EntityArgument.getPlayer(commandSource, "player"));
			return 0;
		})))));
		dispatcher.register(aether);
	}

}
