package team.arcanism.Spell;

import net.minecraft.resources.ResourceLocation;
import team.arcanism.Arcanism;
import team.arcanism.Spell.Aspects.FireSpellAspect;
import team.arcanism.Spell.Aspects.SpellAspect;
import team.arcanism.Spell.Effects.ImbueSpellEffect;
import team.arcanism.Spell.Effects.SpellEffect;
import team.arcanism.Spell.Forms.SelfSpellForm;
import team.arcanism.Spell.Forms.SpellForm;
import team.arcanism.Spell.Forms.TouchSpellForm;

public class SpellRegistry {

	public static class Forms {

		public static final SpellForm self = new SelfSpellForm(new ResourceLocation(Arcanism.MODID, "self"), 1);
		public static final SpellForm touch = new TouchSpellForm(new ResourceLocation(Arcanism.MODID, "touch"), 1);
	}

	public static class Effects {

		public static final SpellEffect imbue = new ImbueSpellEffect(new ResourceLocation(Arcanism.MODID, "imbue"), 1);
	}

	public static class Aspects {

		public static final SpellAspect fire = new FireSpellAspect(new ResourceLocation(Arcanism.MODID, "fire"), 1);
	}
}
