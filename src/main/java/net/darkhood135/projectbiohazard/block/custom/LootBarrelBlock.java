package net.darkhood135.projectbiohazard.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public class LootBarrelBlock extends Block {
    public LootBarrelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel serverLevel) {
            double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;

            // The "burst" cloud
            serverLevel.sendParticles(ParticleTypes.POOF,
                    x, y, z,
                    12,              // count
                    0.4, 0.4, 0.4,   // x/y/z spread
                    0.02);           // speed

            // Wood fragments flung outward (carry the barrel's own texture)
            serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state),
                    x, y, z,
                    50,              // count
                    0.3, 0.3, 0.3,   // spread
                    0.25);           // speed
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.playerDestroy(level, player, pos, state, blockEntity, tool);   // rolls + spawns the loot

        if (level instanceof ServerLevel serverLevel) {
            AABB area = new AABB(pos).inflate(0.5);
            for (ItemEntity drop : serverLevel.getEntitiesOfClass(ItemEntity.class, area, e -> true)) {
                double angle    = serverLevel.getRandom().nextDouble() * Math.PI * 2.0;  // random compass direction
                double sideways = 0.05 + serverLevel.getRandom().nextDouble() * 0.08;    // gentle horizontal push
                double upward   = 0.18 + serverLevel.getRandom().nextDouble() * 0.10;    // light pop up
                drop.setDeltaMovement(Math.cos(angle) * sideways, upward, Math.sin(angle) * sideways);
            }
        }
    }
}