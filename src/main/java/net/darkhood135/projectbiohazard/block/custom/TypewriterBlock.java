package net.darkhood135.projectbiohazard.block.custom;

import com.mojang.serialization.MapCodec;
import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TypewriterBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<TypewriterBlock> CODEC = simpleCodec(TypewriterBlock::new);
    private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 9, 14);

    public TypewriterBlock(Properties properties) {
        super(properties);
    }



    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
                                          Player player, InteractionHand hand, BlockHitResult hit) {
        if (!stack.is(ModItems.INK_RIBBON.get())) {
            return InteractionResult.PASS;          // not an ink ribbon -> normal behavior
        }
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setRespawnPosition(
                    new ServerPlayer.RespawnConfig(
                            LevelData.RespawnData.of(
                                    serverPlayer.level().dimension(),
                                    serverPlayer.blockPosition(),
                                    serverPlayer.getYRot(),
                                    serverPlayer.getXRot()),
                            true),                   // forced = true (reliable save-point respawn)
                    false);                          // showMessage = false (we'll give our own feedback)
            stack.consume(1, player);                // spend one ink ribbon
            level.playSound(null, pos, ModSounds.TYPEWRITER_SAVE.get(), SoundSource.BLOCKS, 1f, 1f);
            List<ItemStack> snapshot = new ArrayList<>();
            Inventory inv = serverPlayer.getInventory();
            for (int i = 0; i < inv.getContainerSize(); i++) {
                ItemStack s = inv.getItem(i);
                if (!s.isEmpty()) snapshot.add(s.copy());
            }
            serverPlayer.setData(ModAttachmentTypes.SAVE_SNAPSHOT, snapshot);
            serverPlayer.setData(ModAttachmentTypes.SAVE_TYPEWRITER_POS, GlobalPos.of(level.dimension(), pos));
            player.sendSystemMessage(Component.literal("Progress saved successfully."));
        }
        return InteractionResult.SUCCESS;
    }
}
