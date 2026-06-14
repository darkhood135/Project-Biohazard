package net.darkhood135.projectbiohazard.item.custom;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GreenHerbItem extends Item {
    public GreenHerbItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        entity.heal(6f);
        return super.finishUsingItem(itemStack, level, entity);
    }
}
