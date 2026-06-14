package net.darkhood135.projectbiohazard.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;

public class ModFoodProperties {
    public static final FoodProperties HERB = new FoodProperties.Builder()
            .alwaysEdible()
            .nutrition(1)
            .saturationModifier(0.6f)
            .build();

    public static final Consumable BASIC_HERB_EFFECT = Consumables
            .defaultFood().consumeSeconds(0.4f)
            .build();
}
