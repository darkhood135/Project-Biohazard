package net.darkhood135.projectbiohazard.event;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.darkhood135.projectbiohazard.item.custom.HatchetItem;
import net.darkhood135.projectbiohazard.networking.ServerboundPackets;
import net.darkhood135.projectbiohazard.networking.packet.TestPacketC2S;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ProjectBiohazard.MOD_ID)
public class ModEvents {
    private static final Identifier yellowHerbHPID = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID,"yellow_herb_hp");

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(TestPacketC2S.TYPE, TestPacketC2S.STREAM_CODEC, ServerboundPackets::handleTestPacket);
    }

    // Immunity Function
    @SubscribeEvent
    public static void blockHarmfulWhileImmune(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.IMMUNITY)) return;
        if (event.getEffectInstance().getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);  // verify the enum value name
        }
    }

    // Adrenaline Function
    @SubscribeEvent
    public static void adrenalineCrit(CriticalHitEvent event) {
        Player attacker = event.getEntity();
        MobEffectInstance adr = attacker.getEffect(ModEffects.ADRENALINE);
        if (adr == null || !event.isCriticalHit()) return;     // only boost actual crits
        float factor = 1.0f + 0.2f * (adr.getAmplifier() + 1);  // amp0 = 1.2x, amp1 = 1.4x
        event.setDamageMultiplier(event.getDamageMultiplier() * factor);
    }

    // Yellow Herb HP
    public static void grantYellowHerbHP (Player player) {
        int boost = player.getData(ModAttachmentTypes.YELLOW_HERB_HP);
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        maxHealth.removeModifier(yellowHerbHPID);   // Clearing the old one first
        maxHealth.addPermanentModifier(
                new AttributeModifier(yellowHerbHPID, boost, AttributeModifier.Operation.ADD_VALUE));
    }

    // TESTING ONLY!!!!
    /*
    @SubscribeEvent
    public static void setYellow(PlayerEvent.PlayerLoggedInEvent event) {
        Player newPlayer = event.getEntity();
        newPlayer.setData(ModAttachmentTypes.YELLOW_HERB_HP, 0);
    }
    */

    @SubscribeEvent
    public static void setYellowHerbProgressOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        newPlayer.setData(ModAttachmentTypes.YELLOW_HERB_HP, event.getOriginal().getData(ModAttachmentTypes.YELLOW_HERB_HP));
        grantYellowHerbHP(newPlayer);
    }
    @SubscribeEvent
    public static void setYellowHerbProgressOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.setData(ModAttachmentTypes.YELLOW_HERB_HP, player.getData(ModAttachmentTypes.YELLOW_HERB_HP));
        grantYellowHerbHP(player);
        player.heal(1000f);
    }

    // Parry System
    @SubscribeEvent
    public static void onParry(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!(player.getMainHandItem().getItem() instanceof HatchetItem)) return;
        if (!(player.isUsingItem())) return;

        int pT = player.getTicksUsingItem();
        if (pT <= 0) return;
        boolean perfect = pT <= 3;

        ItemStack item = player.getItemInHand(player.getUsedItemHand());
        DamageSource source = event.getSource();
        if(source.is(DamageTypeTags.IS_EXPLOSION)) return;
        if (!(source.getEntity() instanceof LivingEntity attacker)) return;

        double reach = player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE);
        Vec3 look = player.getLookAngle();
        Vec3 toAttacker = attacker.position().subtract(player.position());
        look = new Vec3(look.x, 0, look.z).normalize();
        toAttacker = new Vec3(toAttacker.x, 0, toAttacker.z).normalize();
        if (look.dot(toAttacker) < 0.5) return;   // ~60° cone, height-independent

        if (player.level() instanceof ServerLevel server) {
            event.setCanceled(true);
            if (player.distanceToSqr(attacker) <= reach * reach) {
                // Play parry sound
                // Perfect vs. Normal Parry
                if (perfect) {
                    server.playSound(
                            null,                     // Pass 'null' so the sound isn't hidden from the originating player
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.SPEAR_USE, // Replace with your custom SoundEvent if needed
                            SoundSource.PLAYERS,         // The category of the sound
                            1.0F,                        // Volume
                            1.0F                         // Pitch
                    );
                    player.attack(attacker);
                    player.addEffect(new MobEffectInstance(ModEffects.ADRENALINE, 60, 0));   // 3s = 60 ticks, amp 0
                } else {
                    server.playSound(
                            null,                     // Pass 'null' so the sound isn't hidden from the originating player
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            SoundEvents.PLAYER_ATTACK_SWEEP, // Replace with your custom SoundEvent if needed
                            SoundSource.PLAYERS,         // The category of the sound
                            1.0F,                        // Volume
                            1.0F                         // Pitch
                    );
                    attacker.knockback(1.0, player.getX() - attacker.getX(), player.getZ() - attacker.getZ());
                }
            }
            player.stopUsingItem();

            float blocked = event.getAmount();
            item.hurtAndBreak((int) Math.max(1, blocked), server, player,
                    hurtItem -> player.onEquippedItemBroken(hurtItem, EquipmentSlot.MAINHAND));
            int cd = blocked > 10 ? 100 : 20;   // heavy hit (>10) = 5s, else 1s
            player.getCooldowns().addCooldown(item, cd);

        }
    }
    /*
    @SubscribeEvent
    public static void noAttackWhileParrying(AttackEntityEvent event) {
        Player p = event.getEntity();
        if (p.isUsingItem() && p.getUseItem().getItem() instanceof HatchetItem) event.setCanceled(true);
    } */

}
