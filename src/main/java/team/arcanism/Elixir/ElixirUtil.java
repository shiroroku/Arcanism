package team.arcanism.Elixir;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ElixirUtil {

	public static ItemStack createElixir(ItemStack elixirItem, List<IElixirIngredient> ingredients) {
		//effect <potency, duration>
		LinkedHashMap<MobEffect, Tuple<Integer, Integer>> elixirEffects = new LinkedHashMap<>();

		int slot = 1;
		for (IElixirIngredient ingredient : ingredients) {
			MobEffect effect = ingredient.getEffect().get(slot).getA();

			//stack duration vertically
			int stackedDuration = ingredient.getEffect().get(slot).getB();
			for (IElixirIngredient i : ingredients) {

				//instantenous effects dont have time
				if (i.getEffect().get(slot).getA().isInstantenous()) {
					break;
				}

				if (i != ingredient) {
					//Get other ingredients of the same slot
					Tuple<MobEffect, Integer> effectCurrent = ingredient.getEffect().get(slot);
					Tuple<MobEffect, Integer> effectOther = i.getEffect().get(slot);

					if (effectCurrent.getA().equals(effectOther.getA())) {
						//If they match, add their duration
						stackedDuration += effectOther.getB();
					}
				}
			}

			//stack potency and duration horizontally
			if (elixirEffects.containsKey(effect)) {
				elixirEffects.replace(effect, new Tuple<>(elixirEffects.get(effect).getA() + 1, elixirEffects.get(effect).getB() + stackedDuration));
			} else {
				elixirEffects.put(effect, new Tuple<>(1, stackedDuration));
			}
			slot++;
		}

		CompoundTag elixirTag = new CompoundTag();
		slot = 1;
		for (Map.Entry<MobEffect, Tuple<Integer, Integer>> ingredient : elixirEffects.entrySet()) {
			CompoundTag iTag = new CompoundTag();
			iTag.putString("effect", ForgeRegistries.MOB_EFFECTS.getKey(ingredient.getKey()).toString());
			iTag.putInt("potency", ingredient.getValue().getA());
			iTag.putInt("duration", ingredient.getValue().getB());

			elixirTag.put(String.valueOf(slot), iTag);
			slot++;
		}
		elixirItem.getOrCreateTag().put("elixir", elixirTag);
		return elixirItem;
	}

	public static LinkedHashMap<MobEffect, Tuple<Integer, Integer>> getFromNBT(ItemStack elixirItem) {
		//effect <potency, duration>
		LinkedHashMap<MobEffect, Tuple<Integer, Integer>> effectMap = new LinkedHashMap<>();

		if (elixirItem.hasTag()) {
			CompoundTag elixirTag = elixirItem.getOrCreateTagElement("elixir");

			for (String key : elixirTag.getAllKeys()) {
				CompoundTag iTag = elixirTag.getCompound(key);
				MobEffect e = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation(iTag.getString("effect")));
				Tuple<Integer, Integer> effect = new Tuple<>(iTag.getInt("potency"), iTag.getInt("duration"));
				effectMap.put(e, effect);
			}

		}
		return effectMap;
	}
}
