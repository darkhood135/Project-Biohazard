package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.effects.HerbEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HerbItem extends Item {
    public HerbItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity entity) {
        if(!level.isClientSide() && entity instanceof Player player) {
            if(itemStack.is(ModItems.GREEN_HERB)) {
                HerbEffects.useGreenHerb(player, 1, false);
            } else if (itemStack.is(ModItems.BLUE_HERB)) {
                HerbEffects.useBlueHerb(player, false);
            } else if (itemStack.is(ModItems.YELLOW_HERB)) {
                HerbEffects.useYellowHerb(player, false);
            }
        }

        return super.finishUsingItem(itemStack, level, entity);
    }
}
