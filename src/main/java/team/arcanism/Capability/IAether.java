package team.arcanism.Capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAether extends INBTSerializable<CompoundTag> {

	float getMax();

	void setMax(float amount);

	float get();

	void set(float amount);

	boolean canSpend(float amount);

	void spend(float amount);

	void add(float amount);
}
