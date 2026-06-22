package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;

public class PotionVialItem extends PotionItem {
    public PotionVialItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (!level.isClientSide() && contents != null) {
            contents.forEachEffect(entity::addEffect, 1.0f);   // applies every effect in the potion
        }
        return new ItemStack(ModItems.GLASS_VIAL.get());            // mirror potion -> glass bottle
    }
}
