package team.arcanism.Capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.arcanism.Registry.CapabilityRegistry;

import java.util.HashMap;
import java.util.Map;

public class IngredientKnowledgeCapability implements IIngredientKnowledge {

	Map<Item, Map<Integer, Boolean>> knowledge;

	public IngredientKnowledgeCapability() {
		this(new CompoundTag());
	}

	public IngredientKnowledgeCapability(CompoundTag nbt) {
		deserializeNBT(nbt);
	}

	@Override
	public boolean knows(Item ingredient, int slot) {
		if (!knowledge.containsKey(ingredient) || !knowledge.get(ingredient).containsKey(slot)) {
			return false;
		}

		return knowledge.get(ingredient).get(slot);
	}

	@Override
	public void setKnows(Item ingredient, int slot, boolean knows) {
		if (!knowledge.containsKey(ingredient)) {
			knowledge.put(ingredient, Map.of(slot, knows));
		} else {
			Map<Integer, Boolean> ingredients = new HashMap<>(knowledge.get(ingredient));
			ingredients.put(slot, knows);
			knowledge.put(ingredient, ingredients);
		}
	}

	@Override
	public Map<Item, Map<Integer, Boolean>> getKnowledge() {
		return knowledge;
	}

	@Override
	public void setKnowledge(Map<Item, Map<Integer, Boolean>> knowledge) {
		this.knowledge = knowledge;
	}

	@Override
	public void clear() {
		knowledge = new HashMap<>();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();

		knowledge.forEach((key, value) -> {
			CompoundTag iTag = new CompoundTag();
			value.forEach((slot, knows) -> {
				CompoundTag sTag = new CompoundTag();
				sTag.putBoolean("knows", knows);
				iTag.put(String.valueOf(slot), sTag);
			});
			tag.put(key.getRegistryName().toString(), iTag);
		});

		/*
		{
			"someitem":{
				"1":{
					"knows":true
				},
				"3":{
					"knows":true
				}
			}
		}*/

		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		Map<Item, Map<Integer, Boolean>> knowledgeLoaded = new HashMap<>();

		for (String ingredientRegName : nbt.getAllKeys()) {
			Item ingredient = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(ingredientRegName));
			CompoundTag iTag = nbt.getCompound(ingredientRegName);

			Map<Integer, Boolean> knownSlots = new HashMap<>();
			iTag.getAllKeys().forEach((slot) -> {
				CompoundTag sTag = iTag.getCompound(slot);
				boolean knows = sTag.getBoolean("knows");
				knownSlots.put(Integer.valueOf(slot), knows);
			});

			knowledgeLoaded.put(ingredient, knownSlots);
		}

		setKnowledge(knowledgeLoaded);
	}

	public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
		private final IIngredientKnowledge knowledge = new IngredientKnowledgeCapability();
		private final LazyOptional<IIngredientKnowledge> optionalData = LazyOptional.of(() -> knowledge);

		@Override
		public CompoundTag serializeNBT() {
			return this.knowledge.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.knowledge.deserializeNBT(nbt);
		}

		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			return CapabilityRegistry.ingredient_knowledge.orEmpty(cap, this.optionalData);
		}
	}
}
