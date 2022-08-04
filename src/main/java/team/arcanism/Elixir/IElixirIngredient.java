package team.arcanism.Elixir;

import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;

import java.util.TreeMap;

public interface IElixirIngredient {
	public TreeMap<Integer, Tuple<MobEffect, Integer>> getEffect();
}
