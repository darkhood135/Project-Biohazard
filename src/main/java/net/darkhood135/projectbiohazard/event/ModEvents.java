package net.darkhood135.projectbiohazard.event;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.block.custom.TypewriterBlock;
import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.darkhood135.projectbiohazard.entity.ModEntities;
import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.HatchetItem;
import net.darkhood135.projectbiohazard.item.custom.SyringeItem;
import net.darkhood135.projectbiohazard.networking.ServerboundPackets;
import net.darkhood135.projectbiohazard.networking.packet.TestPacketC2S;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = ProjectBiohazard.MOD_ID)
public class ModEvents {
    private static final Identifier yellowHerbHPID = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID,"yellow_herb_hp");

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(TestPacketC2S.TYPE, TestPacketC2S.STREAM_CODEC, ServerboundPackets::handleTestPacket);
    }

    // Vial Potions
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        event.getBuilder().addContainer(ModItems.WATER_VIAL.get());
        // event.getBuilder().addMix(Potions.AWKWARD, Blocks.HERE.asItem(), ModPotions.POTION);
    }
    @SubscribeEvent
    public static void addVialsToVanillaTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {   // the tab vanilla potions live in (confirm key)
            event.getParameters().holders().lookupOrThrow(Registries.POTION).listElements().forEach(potion ->
                    event.accept(PotionContents.createItemStack(ModItems.WATER_VIAL.get(), potion)));
        }
    }

    // Typewriter
    private static boolean sameIgnoringDamage(ItemStack a, ItemStack b) {
        if (!a.is(b.getItem())) return false;          // cheap early-out, also skips the copies below
        ItemStack ca = a.copy(); ca.remove(DataComponents.DAMAGE);
        ItemStack cb = b.copy(); cb.remove(DataComponents.DAMAGE);
        return ItemStack.isSameItemSameComponents(ca, cb);
    }
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        List<ItemStack> snapshot = player.getData(ModAttachmentTypes.SAVE_SNAPSHOT);
        GlobalPos tPos = player.getData(ModAttachmentTypes.SAVE_TYPEWRITER_POS);
        if (tPos != null) {
            ServerLevel tLevel = player.level().getServer().getLevel(tPos.dimension());
            if (tLevel != null && tLevel.isLoaded(tPos.pos())
                    && !(tLevel.getBlockState(tPos.pos()).getBlock() instanceof TypewriterBlock)) {
                // chunk is loaded AND the typewriter is confirmed gone -> void the save
                player.sendSystemMessage(Component.literal("Save location was destroyed or obstructed."));
                player.removeData(ModAttachmentTypes.SAVE_SNAPSHOT);
                player.removeData(ModAttachmentTypes.SAVE_TYPEWRITER_POS);
                player.setRespawnPosition(null, false);
                return;   // death proceeds normally, items drop
            }
        }
        // otherwise fall through to the count-capped protection from Step 2
        if (snapshot.isEmpty()) return;                 // no active save

        List<ItemStack> budget = new ArrayList<>();      // mutable count budget from the snapshot
        for (ItemStack s : snapshot) budget.add(s.copy());

        List<ItemStack> protect = new ArrayList<>();
        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack invStack = inv.getItem(i);
            if (invStack.isEmpty()) continue;
            int keep = 0;
            for (ItemStack b : budget) {
                if (b.getCount() > 0 && sameIgnoringDamage(b, invStack)) {
                    int take = Math.min(invStack.getCount() - keep, b.getCount());
                    keep += take; b.shrink(take);
                    if (keep >= invStack.getCount()) break;
                }
            }
            if (keep > 0) {
                protect.add(invStack.copyWithCount(keep));
                invStack.shrink(keep);                   // pulled out before the drop -> won't drop
            }
        }
        player.setData(ModAttachmentTypes.RESTORE_STASH, protect);
    }
    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        List<ItemStack> protect = event.getOriginal().getData(ModAttachmentTypes.RESTORE_STASH);
        if (!protect.isEmpty()) {
            Player p = event.getEntity();
            for (ItemStack s : protect) {
                if (!p.getInventory().add(s)) p.drop(s, false);   // overflow drops at feet
            }
        }
    }


    // Syringe Functionality
    @SubscribeEvent
    public static void injectionTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LivingEntity entity) || entity.level().isClientSide()) return;
        SyringeItem.SyringeInjection inj = entity.getData(ModAttachmentTypes.SYRINGE_INJECTION);
        if (inj == null) return;

        int timer = inj.tickTimer() - 1;
        if (timer > 0) {
            entity.setData(ModAttachmentTypes.SYRINGE_INJECTION,
                    new SyringeItem.SyringeInjection(inj.contents(), inj.chargesDone(), timer));
            return;
        }
        int charge = inj.chargesDone() + 1;                       // 1, 2, 3
        for (MobEffectInstance base : inj.contents().getAllEffects()) {
            int dur = base.getDuration() * charge / 3;            // 1/3, 2/3, full
            int amp = Math.min(charge - 1, base.getAmplifier());  // 0,1,2 capped at the potion's amp
            entity.addEffect(new MobEffectInstance(base.getEffect(), dur, amp));
        }
        if (charge >= 3) entity.removeData(ModAttachmentTypes.SYRINGE_INJECTION);
        else entity.setData(ModAttachmentTypes.SYRINGE_INJECTION, new SyringeItem.SyringeInjection(inj.contents(), charge, 60));
    }

    // Immunity Function
    @SubscribeEvent
    public static void blockHarmfulWhileImmune(MobEffectEvent.Applicable event) {
        if (!event.getEntity().hasEffect(ModEffects.CONSTITUTION)) return;
        if (event.getEffectInstance().getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);  // verify the enum value name
        }
    }
    @SubscribeEvent
    public static void immunityDefense(LivingIncomingDamageEvent event) {
        MobEffectInstance imm = event.getEntity().getEffect(ModEffects.CONSTITUTION);
        if (imm == null) return;
        float reduction = Math.min(0.9f, 0.2f * (imm.getAmplifier() + 1));  // amp0=20%, amp1=40%; capped at 90%
        event.setAmount(event.getAmount() * (1f - reduction));
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

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.T_VIRUS_ZOMBIE.get(), TZombieEntity.createTZombieAttributes().build());
    }
}
