package team.arcanism.Capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.arcanism.Registry.CapabilityRegistry;

public class AetherCapability implements IAether {

	private float maxAether;
	private float aether;

	public AetherCapability(float amt, float maxAmt) {
		this.setMax(maxAmt);
		this.set(amt);
	}

	public AetherCapability(float maxAmt) {
		this(0, maxAmt);
	}

	public AetherCapability() {
		this(0, 25);
	}

	public float getMax() {
		return maxAether;
	}

	public void setMax(float amount) {
		this.maxAether = Math.max(0, amount);
	}

	public float get() {
		return this.aether;
	}

	public void set(float amount) {
		this.aether = Math.min(Math.max(0, amount), maxAether);
	}

	public boolean canSpend(float amount) {
		return get() - amount > 0;
	}

	public void spend(float amount) {
		this.set(get() - amount);
	}

	public void add(float amount) {
		this.set(get() + amount);
	}

	public static void passiveRegen(TickEvent.PlayerTickEvent event) {
		if (event.player.tickCount % 80 == 0 && event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.START) {

			CapabilityRegistry.getAether(event.player).ifPresent(cap -> {
				float passiveCombined = cap.getMax() * 0.01f;

				cap.add(passiveCombined);
			});
			CapabilityRegistry.sendAetherPacket(event.player);
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putFloat("max_aether", getMax());
		tag.putFloat("aether", get());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		setMax(nbt.getFloat("max_aether"));
		set(nbt.getFloat("aether"));
	}

	public static class Provider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
		private final IAether aether = new AetherCapability();
		private final LazyOptional<IAether> optionalData = LazyOptional.of(() -> aether);

		@Override
		public CompoundTag serializeNBT() {
			return this.aether.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.aether.deserializeNBT(nbt);
		}

		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			return CapabilityRegistry.aether.orEmpty(cap, this.optionalData);
		}
	}
}
