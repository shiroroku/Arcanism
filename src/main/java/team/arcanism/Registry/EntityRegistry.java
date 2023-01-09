package team.arcanism.Registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import team.arcanism.Arcanism;
import team.arcanism.Item.BoneDagger.BoneDaggerEntity;
import team.arcanism.Spell.SpellEntity;
import team.arcanism.Spell.SpellExplosionEntity;

public class EntityRegistry {

	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Arcanism.MODID);

	public static final RegistryObject<EntityType<BoneDaggerEntity>> bone_dagger = ENTITIES.register("bone_dagger", () -> EntityType.Builder.<BoneDaggerEntity>of(BoneDaggerEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4).build("bone_dagger"));
	public static final RegistryObject<EntityType<SpellEntity>> spell = ENTITIES.register("spell", () -> EntityType.Builder.<SpellEntity>of(SpellEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4).build("spell"));
	public static final RegistryObject<EntityType<SpellExplosionEntity>> spell_explosion = ENTITIES.register("spell_explosion", () -> EntityType.Builder.<SpellExplosionEntity>of(SpellExplosionEntity::new, MobCategory.MISC).sized(0.8f, 0.8f).clientTrackingRange(4).build("spell_explosion"));

}
