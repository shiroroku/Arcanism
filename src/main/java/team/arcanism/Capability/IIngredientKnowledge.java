package team.arcanism.Capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IIngredientKnowledge extends INBTSerializable<CompoundTag> {

	boolean knows(Item ingredient, int slot);

	void setKnows(Item ingredient, int slot, boolean knows);

	Map<Item, Map<Integer, Boolean>> getKnowledge();

	void setKnowledge(Map<Item, Map<Integer, Boolean>> knowledge);

	void clear();
}
