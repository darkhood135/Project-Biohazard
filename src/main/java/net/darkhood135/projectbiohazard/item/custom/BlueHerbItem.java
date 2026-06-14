package net.darkhood135.projectbiohazard.item.custom;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BlueHerbItem extends Item {
    public BlueHerbItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        entity.removeEffect(MobEffects.POISON);
        entity.removeEffect(MobEffects.HUNGER);
        entity.removeEffect(MobEffects.NAUSEA);

        return super.finishUsingItem(itemStack, level, entity);
    }
}
