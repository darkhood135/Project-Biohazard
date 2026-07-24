package net.darkhood135.projectbiohazard.item.custom;

import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;

public class SyringeItem extends Item {
    public SyringeItem(Properties properties) { super(properties); }

    public record SyringeInjection(PotionContents contents, int chargesDone, int tickTimer) {}

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!target.level().isClientSide()) {
            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
            if (contents != null) {
                if (target.hasEffect(ModEffects.CONSTITUTION) || target.hasEffect(MobEffects.RESISTANCE)) {
                    return;   // "bounce off" — reject; syringe stays filled (don't empty it)
                }
                target.setData(ModAttachmentTypes.SYRINGE_INJECTION, new SyringeInjection(contents, 0, 60)); // first charge in 3s
                target.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        ModSounds.SYRINGE_PIERCE.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
                if (!(attacker instanceof Player p && p.getAbilities().instabuild)) {   // skip only creative players
                    attacker.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.EMPTY_SYRINGE.get()));
                }
            }
        }
        super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public Component getName(ItemStack stack) {
        PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
        if (contents != null) {
            var it = contents.getAllEffects().iterator();
            if (it.hasNext()) {
                Component effectName = it.next().getEffect().value().getDisplayName();  // e.g. "Poison"
                return Component.translatable("item.projectbiohazard.syringe.named", effectName);
            }
        }
        return super.getName(stack);
    }
}