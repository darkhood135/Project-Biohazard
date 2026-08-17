package net.darkhood135.projectbiohazard.item.custom;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.util.GeckoLibUtil;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.item.client.FirstAidSprayRenderer;
import net.darkhood135.projectbiohazard.item.custom.shared.OneHandedAimableItem;
import net.darkhood135.projectbiohazard.particle.ModParticles;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.darkhood135.projectbiohazard.sound.client.AidSpraySoundInstance;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Consumer;

public class FirstAidSprayItem extends Item implements GeoItem, OneHandedAimableItem {
    private static final int   HEAL_INTERVAL  = 2;    // ticks between pulses
    private static final float HEAL_PER_PULSE = 2.0f; // half a heart per pulse
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FirstAidSprayItem(Properties properties) {
        super(properties);
        GeoItem.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 1, state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("default")))
                .triggerableAnim("selfUse",   RawAnimation.begin().thenPlayAndHold("selfUse"))
                .triggerableAnim("selfUseOH", RawAnimation.begin().thenPlayAndHold("selfUseOH"))
                .triggerableAnim("use",       RawAnimation.begin().thenPlayAndHold("use")));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private FirstAidSprayRenderer renderer;
            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null) this.renderer = new FirstAidSprayRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return Boolean.TRUE.equals(stack.get(ModDataComponentTypes.SELF_SPRAY_MODE.get()))
                ? ItemUseAnimation.BLOCK       // self-heal: shield pose
                : ItemUseAnimation.NONE;        // spray-others: no pose override → arm keeps its normal held position
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity user) {
        return 72000;                                // "hold indefinitely"; we stop it ourselves
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getDamageValue() >= stack.getMaxDamage()) return InteractionResult.FAIL;  // empty can

        boolean selfMode = player.isShiftKeyDown();
        if (selfMode && player.getHealth() >= player.getMaxHealth()) return InteractionResult.FAIL;

        stack.set(ModDataComponentTypes.SELF_SPRAY_MODE.get(), selfMode);   // drives getUseAnimation; runs both sides

        boolean offhand = hand == InteractionHand.OFF_HAND;
        String anim = selfMode ? (offhand ? "selfUseOH" : "selfUse") : "use";

        if (level instanceof ServerLevel serverLevel) {
            long id = GeoItem.getOrAssignId(stack, serverLevel);
            this.triggerAnim(player, id, "controller", anim);
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int ticksRemaining) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        boolean selfMode = entity.isShiftKeyDown();

        emitMist(serverLevel, entity, selfMode);                 // toward chest vs outward
        if (ticksRemaining % HEAL_INTERVAL != 0) return;

        if (selfMode) {
            if (entity.getHealth() >= entity.getMaxHealth()) {
                this.stopTriggeredAnim(entity, GeoItem.getId(stack), "controller", null); // reset pose ourselves
                entity.stopUsingItem();
                return;
            }
            entity.heal(HEAL_PER_PULSE);
            stack.hurtAndBreak(1, entity, entity.getUsedItemHand());
        } else {
            for (LivingEntity t : findConeTargets(serverLevel, entity)) {
                if (t.getHealth() < t.getMaxHealth()) {
                    t.heal(HEAL_PER_PULSE);
                    spawnHealParticles(serverLevel, t);       // (see #3)
                }
            }
            stack.hurtAndBreak(1, entity, entity.getUsedItemHand());   // consume every pulse
        }
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int remainingTime) {
        if (level instanceof ServerLevel) {
            this.stopTriggeredAnim(entity, GeoItem.getId(stack), "controller", "selfUse");
            this.stopTriggeredAnim(entity, GeoItem.getId(stack), "controller", null);
        }
        return super.releaseUsing(stack, level, entity, remainingTime);
    }

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity mob, LivingEntity attacker) {
        super.hurtEnemy(stack, mob, attacker);
        if (attacker.level() instanceof ServerLevel level) {
            level.playSound(null, mob.getX(), mob.getY(), mob.getZ(),
                    ModSounds.SPRAY_CAN_THUNK.get(), SoundSource.PLAYERS, 5.0f, 1.0f);
        }
    }

    private java.util.List<LivingEntity> findConeTargets(ServerLevel level, LivingEntity user) {
        double range = 4.0;
        double minDot = 0.5;                       // ~60° half-angle cone
        Vec3 eye = user.getEyePosition();
        Vec3 look = user.getViewVector(1.0f);
        return level.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(range), t ->
                t != user
                        && t.isAlive()
                        && !(t instanceof Enemy)                                   // players + passive/tamed, no hostiles
                        && t.distanceToSqr(user) <= range * range
                        && t.getEyePosition().subtract(eye).normalize().dot(look) > minDot);  // inside the cone
    }

    private void emitMist(ServerLevel level, LivingEntity user, boolean towardSelf) {
        SimpleParticleType mist = ModParticles.AID_MIST_PARTICLES.get();
        Vec3 look = user.getViewVector(1.0f);

        Vec3 origin;
        Vec3 vel;
        double speed;

        if (towardSelf) {
            // self-heal: puff back onto the chest
            Vec3 chest = user.position().add(0, user.getBbHeight() * 0.65, 0);
            Vec3 nozzle = chest.add(new Vec3(look.x, 0, look.z).normalize().scale(0.35));
            origin = nozzle;
            vel = chest.subtract(nozzle).normalize().scale(0.03);
            speed = 0.01;
        } else {
            InteractionHand usedHand = user.getUsedItemHand();
            HumanoidArm arm = usedHand == InteractionHand.MAIN_HAND ? user.getMainArm() : user.getMainArm().getOpposite();
            double side = arm == HumanoidArm.RIGHT ? 0.3 : -0.3;   // + = player's right, - = left

            Vec3 right = new Vec3(-look.z, 0.0, look.x).normalize();
            origin = user.getEyePosition()
                    .add(look.scale(0.4))
                    .add(right.scale(side))
                    .add(0.0, -0.25, 0.0);   // was -0.15 — lowered onto the nozzle
            vel = look;
            speed = 1d;
        }

        for (int i = 0; i < 2; i++) {
            double jx = (level.getRandom().nextDouble() - 0.5) * 0.004;
            double jy = (level.getRandom().nextDouble() - 0.5) * 0.004;
            double jz = (level.getRandom().nextDouble() - 0.5) * 0.004;
            level.sendParticles(mist, origin.x, origin.y, origin.z, 0,
                    vel.x + jx, vel.y + jy, vel.z + jz, speed);
        }
    }

    private void spawnHealParticles(ServerLevel level, LivingEntity target) {
        level.sendParticles(ParticleTypes.HAPPY_VILLAGER,        // the green "positive" sparkle
                target.getX(), target.getY() + target.getBbHeight() * 0.6, target.getZ(),
                3,
                target.getBbWidth() * 0.4, target.getBbHeight() * 0.4, target.getBbWidth() * 0.4,
                0.1);
    }
}