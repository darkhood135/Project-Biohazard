package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class EMFItem extends Item {
    private static final Map<Block, Block> EMF_MAP =
            Map.of(
                    Blocks.REDSTONE_BLOCK, ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get(),
                    ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get(), Blocks.REDSTONE_BLOCK
            );

    private static final double RANGE = 25;

    public EMFItem(Properties properties) {
        super(properties);
    }

    /* Original Code
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Block clickedBlock = level.getBlockState(context.getClickedPos()).getBlock();

        if (EMF_MAP.containsKey(clickedBlock) && !level.isClientSide()) {
            level.setBlockAndUpdate(context.getClickedPos(), EMF_MAP.get(clickedBlock).defaultBlockState());
            context.getItemInHand().hurtAndBreak(1,(ServerLevel) level, context.getPlayer(),
                    item -> context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
            level.playSound(null, context.getClickedPos(), SoundEvents.LEVER_CLICK, SoundSource.PLAYERS);
        }

        return InteractionResult.CONSUME;
    } */

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookDir = player.getViewVector(1f);
        Vec3 endPoint = eyePos.add(lookDir.scale(RANGE));

        BlockHitResult hit = level.clip(new ClipContext(eyePos, endPoint, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

        if (hit.getType() == HitResult.Type.BLOCK) {
            BlockPos blockPos = hit.getBlockPos();
            Block clickedBlock = level.getBlockState(blockPos).getBlock();
            if (EMF_MAP.containsKey(clickedBlock) && !level.isClientSide()) {
                level.setBlockAndUpdate(blockPos, EMF_MAP.get(clickedBlock).defaultBlockState());
                player.getItemInHand(hand).hurtAndBreak(1,(ServerLevel) level, player,
                        item -> player.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
                level.playSound(null, blockPos, SoundEvents.LEVER_CLICK, SoundSource.PLAYERS);
                return InteractionResult.CONSUME;
            }
        }

        return InteractionResult.PASS;
    }
}
