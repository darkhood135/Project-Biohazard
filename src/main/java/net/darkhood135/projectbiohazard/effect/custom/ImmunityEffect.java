package net.darkhood135.projectbiohazard.effect.custom;

import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ImmunityEffect extends MobEffect {
    public ImmunityEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static void applyImmunity(LivingEntity entity, int duration, int amplifier) {
        for (MobEffectInstance effect : List.copyOf(entity.getActiveEffects())) {
            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                entity.removeEffect(effect.getEffect());
            }
        }
        entity.addEffect(new MobEffectInstance(ModEffects.IMMUNITY, duration, amplifier));
    }
}
