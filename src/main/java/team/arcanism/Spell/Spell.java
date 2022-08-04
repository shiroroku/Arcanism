package team.arcanism.Spell;

public abstract class Spell {

	public enum Shape {
		PROJECTILE,
		TOUCH,
		SELF,
		NOVA,
		BEAM
	}

	public enum Aspect {
		AETHER,
		FIRE,
		WATER,
		ICE,
		LIGHTNING,
		EARTH
	}

	public enum Effect {
		BURST,
	}

	public final Shape shape;
	public final Effect effect;
	public final Aspect aspect;

	public Spell(Shape shape, Effect effect, Aspect aspect) {
		this.shape = shape;
		this.effect = effect;
		this.aspect = aspect;
	}

	public Shape getShape() {
		return shape;
	}

	public Effect getEffect() {
		return effect;
	}

	public Aspect getAspect() {
		return aspect;
	}

}
