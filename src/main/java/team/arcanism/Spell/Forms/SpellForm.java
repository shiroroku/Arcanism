package team.arcanism.Spell.Forms;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import team.arcanism.Spell.Aspects.SpellAspect;
import team.arcanism.Spell.Effects.SpellEffect;

import java.util.List;

public abstract class SpellForm {

	private final ResourceLocation id;
	private final float cost;

	public SpellForm(ResourceLocation id, float cost) {
		this.id = id;
		this.cost = cost;
	}

	public ResourceLocation getId() {
		return id;
	}

	public float getCost() {
		return cost;
	}

	public abstract boolean collectTargetAndCast(Player player, ItemStack castingItem, SpellEffect effect, SpellAspect aspect, float duration, float magnitude, float radius);

	public abstract void createSpawnEffects(Player player, List<Object> target, SpellAspect aspect, float duration, float magnitude, float radius);

}
