package net.darkhood135.projectbiohazard.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FleshBlock extends Block {
    public FleshBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {

        // If entity does not have boots on, inflicts poison
        if (!level.isClientSide() && entity instanceof LivingEntity living) {
            if (living.getItemBySlot(EquipmentSlot.FEET).isEmpty() && !living.hasEffect(MobEffects.POISON)) {
                living.addEffect(new MobEffectInstance(MobEffects.POISON, 20, 1));
            }
        }

        super.stepOn(level, pos, onState, entity);
    }
}
