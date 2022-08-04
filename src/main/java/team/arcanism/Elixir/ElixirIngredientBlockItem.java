package team.arcanism.Elixir;

import net.minecraft.network.chat.Component;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;
import java.util.TreeMap;

public class ElixirIngredientBlockItem extends BlockItem implements IElixirIngredient {

	private final TreeMap<Integer, Tuple<MobEffect, Integer>> effectMap;

	public ElixirIngredientBlockItem(Block block, Properties prop, TreeMap<Integer, Tuple<MobEffect, Integer>> effects) {
		super(block, prop);
		effectMap = effects;

	}

	public TreeMap<Integer, Tuple<MobEffect, Integer>> getEffect() {
		return effectMap;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
		if (world != null && world.isClientSide) {
			ElixirUtilClient.ingredientHoverText(effectMap, stack, world, tooltip, flag);
		}
	}
}
