package net.darkhood135.projectbiohazard.entity;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.createEntities(ProjectBiohazard.MOD_ID);

    public static final ResourceKey<EntityType<?>> TZOMBIE_KEY = ResourceKey.create(Registries.ENTITY_TYPE,
            Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "t-virus_zombie"));

    public static final Supplier<EntityType<TZombieEntity>> T_VIRUS_ZOMBIE = ENTITY_TYPES.register("t-virus_zombie",
            () -> EntityType.Builder.of(TZombieEntity::new, MobCategory.MONSTER).sized(0.6f, 2.0f).build(TZOMBIE_KEY));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
