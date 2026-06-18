package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.tag.ModTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

public class HatchetItem extends Item {

    // Most Hatchet logic is handled by parry events (see ModEvents)

    public HatchetItem(ToolMaterial toolMaterial, float attackDamageBaseLine,
            float attackSpeedBaseline, Properties properties) {
        super(properties.tool(toolMaterial, ModTags.Blocks.HATCHET_MINEABLE, attackDamageBaseLine, attackSpeedBaseline, 0.5f));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (player.getCooldowns().isOnCooldown(item)) return InteractionResult.FAIL;
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if (entity instanceof Player player) {
            if (!player.getCooldowns().isOnCooldown(stack)) player.getCooldowns().addCooldown(stack, 40);
        }
        super.onStopUsing(stack, entity, count);
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 8; // 0.4 seconds
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.BLOCK;
    }
}
