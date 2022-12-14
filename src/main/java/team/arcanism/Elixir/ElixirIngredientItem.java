package team.arcanism.Elixir;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;

public class ElixirIngredientItem extends Item implements IElixirIngredient {

	private final Supplier<TreeMap<Integer, Tuple<MobEffect, Integer>>> effectMap;

	public ElixirIngredientItem(Properties prop, final Supplier<TreeMap<Integer, Tuple<MobEffect, Integer>>> effects) {
		super(prop);
		effectMap = effects;

	}

	public TreeMap<Integer, Tuple<MobEffect, Integer>> getEffect() {
		return effectMap.get();
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		if (world != null && world.isClientSide) {
			ElixirUtilClient.ingredientHoverText(effectMap.get(), stack, world, tooltip, flag);
		}
	}
}
