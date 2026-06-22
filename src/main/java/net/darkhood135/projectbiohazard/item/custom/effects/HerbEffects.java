package net.darkhood135.projectbiohazard.item.custom.effects;

import net.darkhood135.projectbiohazard.Config;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.darkhood135.projectbiohazard.effect.custom.ImmunityEffect;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class HerbEffects {

    private static final Identifier yellowHP_ID = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID,"yellow_herb_hp");

    public static void useGreenHerb(Player player, int greens, boolean red) {
        float hearts;
        if (red) {
            hearts = (greens == 1) ? 12 : 20;
        } else {
            hearts = switch (greens) {
                case 1 -> 3;    // single green herb
                case 2 -> 7;
                default -> 11;  // 3 greens
            };
        }
        player.heal(hearts * 2f);   // hearts -> health points
    }

    public static void useBlueHerb(Player player, boolean red) {
        player.removeEffect(MobEffects.POISON);
        player.removeEffect(MobEffects.HUNGER);
        player.removeEffect(MobEffects.NAUSEA);
        if (red) {
            ImmunityEffect.applyImmunity(player, 600, 1);
        }
    }

    public static void useYellowHerb(Player player, boolean red) {
        int originalHPBoost = player.getData(ModAttachmentTypes.YELLOW_HERB_HP);
        int updatedHPBoost = originalHPBoost + 2;
        if(updatedHPBoost <= Config.YELLOW_HERB_HEALTH_BONUS.get()) {
            player.setData(ModAttachmentTypes.YELLOW_HERB_HP, updatedHPBoost);
            AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
            maxHealth.removeModifier(yellowHP_ID);   // Clearing the old one first
            maxHealth.addPermanentModifier(
                    new AttributeModifier(yellowHP_ID, updatedHPBoost, AttributeModifier.Operation.ADD_VALUE));
        } else {
            int amp = 0;
            if(red) {
                amp = 2;
            }
            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 6000, amp));
        }
    }
}
