package net.darkhood135.projectbiohazard.entity.custom;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.object.PlayState;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.util.GeckoLibUtil;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.entity.custom.goal.TZombieAttackGoal;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class TZombieEntity extends PathfinderMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TZombieEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    private LivingEntity lastTarget = null;
    private int pursuitGroanCooldown = 100;
    private boolean wasPursuing = false;
    private int reviveCount = 0;
    private int reviveTicks = 0;
    private int resurrectTicks = 0;
    private static final int   MAX_REVIVES = 2;
    private static final float REVIVE_CHANCE = 0.5f;
    private static final int   RESURRECT_LENGTH_TICKS = 96;   // match your resurrect animation

    // Animations
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 3, state -> {
            TZombieEntity zombie = (TZombieEntity) state.animatable();
            if (zombie.isCorpse()) return state.setAndContinue(RawAnimation.begin().thenLoop("corpse"));
            boolean alreadyWalking = state.isCurrentAnimationStage("walk");
            boolean walk = state.isMoving() && (alreadyWalking || zombie.hurtTime <= 0);
            return state.setAndContinue(RawAnimation.begin().thenLoop(walk ? "walk" : "idle"));
        })
                .triggerableAnim("death", RawAnimation.begin().thenPlayAndHold("death"))
                .triggerableAnim("stagger", RawAnimation.begin().thenPlayAndHold("stagger"))
                .triggerableAnim("resurrect", RawAnimation.begin().thenPlayAndHold("resurrect"))
                .triggerableAnim("strike", RawAnimation.begin().thenPlayAndHold("strike"))
                .triggerableAnim("strike2", RawAnimation.begin().thenPlayAndHold("strike2")));
        controllers.add(new AnimationController<>("flinch", 0, state -> PlayState.STOP)
                .additiveAnimations()
                .triggerableAnim("flinch1", RawAnimation.begin().thenPlay("flinch1"))
                .triggerableAnim("flinch2", RawAnimation.begin().thenPlay("flinch2")));
        controllers.add(new AnimationController<>("groan", 2, state -> PlayState.STOP)
                .additiveAnimations()
                .triggerableAnim("passiveGroan", RawAnimation.begin().thenPlay("passiveGroan"))
                .triggerableAnim("injureGroan",  RawAnimation.begin().thenPlay("injureGroan")));
        controllers.add(new AnimationController<>("leftArm", 20, state -> PlayState.STOP)
                .triggerableAnim("leftArmStretch", RawAnimation.begin().thenPlay("leftArmStretch")));
        controllers.add(new AnimationController<>("rightArm", 20, state -> PlayState.STOP)
                .triggerableAnim("rightArmStretch", RawAnimation.begin().thenPlay("rightArmStretch")));

    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    // Forced Facing the Attacker
    private void faceEntity(Entity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        float yaw = (float) (Mth.atan2(dz, dx) * (180.0 / Math.PI)) - 90.0F;
        this.setYRot(yaw);      this.yRotO = yaw;
        this.setYBodyRot(yaw);  this.yBodyRotO = yaw;
        this.setYHeadRot(yaw);  this.yHeadRotO = yaw;
    }
    private void faceKiller(DamageSource source) {
        if (source.getEntity() instanceof Entity killer) faceEntity(killer);
    }

    // Variants
    public enum Variant {
        REMAKE(90, "t-virus_zombie.png"),
        CLASSIC(10, "t-virus_zombie_classic.png");   // RE2 easter egg — rare

        public final int weight;
        public final Identifier texture;
        Variant(int weight, String file) {
            this.weight = weight;
            this.texture = Identifier.fromNamespaceAndPath(
                    ProjectBiohazard.MOD_ID, "textures/entity/t-virus_zombie/" + file);
        }
        private static final int TOTAL;
        static { int t = 0; for (Variant v : values()) t += v.weight; TOTAL = t; }

        public static Variant byId(int id) {
            return (id >= 0 && id < values().length) ? values()[id] : REMAKE;
        }
        public static Variant weightedRandom(RandomSource r) {
            int roll = r.nextInt(TOTAL);
            for (Variant v : values()) if ((roll -= v.weight) < 0) return v;
            return REMAKE;
        }
    }
    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(TZombieEntity.class, EntityDataSerializers.INT);
    public int getVariant() { return this.entityData.get(DATA_VARIANT); }
    public void setVariant(int id) { this.entityData.set(DATA_VARIANT, id); }
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                        EntitySpawnReason reason, @Nullable SpawnGroupData data) {
        this.setVariant(Variant.weightedRandom(this.random).ordinal());
        return super.finalizeSpawn(level, difficulty, reason, data);
    }


    // Forced Knockback
    public void forcedKnockback(Entity from, double strength) {
        double dx = this.getX() - from.getX();
        double dz = this.getZ() - from.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);
        if (dist < 1.0e-4) return;
        dx /= dist; dz /= dist;
        Vec3 v = this.getDeltaMovement();
        this.setDeltaMovement(v.x / 2 + dx * strength, Math.min(0.4, v.y / 2 + 0.25), v.z / 2 + dz * strength);
        this.hurtMarked = true;
    }
    private void applyKnockback(DamageSource source, double strength) {
        if (source.getEntity() instanceof Entity attacker) forcedKnockback(attacker, strength);
    }

    // Staggering
    private void startStagger(DamageSource source) {
        doStagger(source.getEntity() instanceof Entity e ? e : null);
    }
    /** Guaranteed stagger from an explicit attacker (e.g. a perfect parry), bypassing the damage threshold. */
    public void forceStagger(Entity from) {
        doStagger(from);
    }
    private void doStagger(@Nullable Entity from) {
        this.staggerTicks = STAGGER_DURATION;
        this.damageSinceStagger = 0f;
        this.staggerThreshold = rollStaggerThreshold();
        this.getNavigation().stop();
        if (from != null) {
            faceEntity(from);
            forcedKnockback(from, 0.3);
        }
        this.triggerAnim("controller", "stagger");
        this.triggerAnim("groan", "injureGroan");
        this.playSound(ModSounds.FEMALE_T_ZOMBIE_STAGGER.get(), 1.0f, 1.0f);
    }

    // Death
    private static final int DEATH_LENGTH_TICKS = 16;   // selected death anim ticks (20 = 1s)
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FEMALE_T_ZOMBIE_DEATH.get();
    }
    @Override
    public void die(DamageSource source) {
        super.die(source);
        if (!this.level().isClientSide() && this.getHealth() <= 0.0F) {
            faceKiller(source);
            applyKnockback(source, 0.3);
            this.triggerAnim("controller", "death");     // full-body death
            this.triggerAnim("groan", "injureGroan");     // groan overlay
        }
    }
    @Override
    protected void tickDeath() {
        this.deathTime++;
        if (this.deathTime >= DEATH_LENGTH_TICKS && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte) 60);   // death poof
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    // Passive Groans
    private int groanCooldown = 300 + this.random.nextInt(1000);
    private Entity pendingKnockbackFrom;
    @Override
    public void tick() {
        super.tick();

        if (!(this.level() instanceof ServerLevel)) return;   // all of this is server-authoritative

        if (this.pendingKnockbackFrom != null) {
            this.forcedKnockback(this.pendingKnockbackFrom, 0.3);
            this.pendingKnockbackFrom = null;
        }

        if (this.isCorpse()) {
            this.setTarget(null);

            if (this.collapseAnimTicks > 0 && --this.collapseAnimTicks <= 0) {
                this.stopTriggeredAnim("controller", "death");        // fall → corpse loop
            }
            if (this.corpseBurnTicks > 0) {                            // burning out — revival already denied
                if (--this.corpseBurnTicks <= 0) this.finalizeCorpse();
                return;
            }
            if (this.reviveTicks > 0) {                                // waiting to rise
                if (--this.reviveTicks <= 0) this.resurrect();
                return;
            }
            if (--this.corpseTicks <= 0) this.finalizeCorpse();        // fully dead — expire
            return;
        }
        if (this.resurrectTicks > 0) {
            if (--this.resurrectTicks <= 0) {
                this.stopTriggeredAnim("controller", "resurrect");    // up → normal walk/idle
            }
            return;                                                    // still rising — no pursuit/groans
        }

        LivingEntity target = this.getTarget();

        // notice: once on acquiring a target
        if (target != null && this.lastTarget == null) {
            this.playSound(ModSounds.FEMALE_T_ZOMBIE_NOTICE.get(), 1.0f, 1.0f);
            this.triggerAnim("groan", "passiveGroan");
            this.pursuitGroanCooldown = 40 + this.random.nextInt(60);
        }
        this.lastTarget = target;

        // stagger countdown + settle window
        if (this.staggerTicks > 0 && --this.staggerTicks <= 0) {
            this.stopTriggeredAnim("controller", "stagger");
            this.postStaggerLock = POST_STAGGER_LOCK;
        }
        if (this.postStaggerLock > 0) this.postStaggerLock--;

        boolean pursuing = target != null && this.staggerTicks <= 0 && !this.getNavigation().isDone();

        if (pursuing) {
            if (--this.pursuitGroanCooldown <= 0) {
                this.pursuitGroanCooldown = 120 + this.random.nextInt(120);
                this.playSound(ModSounds.FEMALE_T_ZOMBIE_NOTICE.get(), 1.0f, 1.0f);
                this.triggerAnim("groan", "passiveGroan");
                boolean left  = this.random.nextBoolean();
                boolean right = this.random.nextBoolean();
                if (!left && !right) { if (this.random.nextBoolean()) left = true; else right = true; }
                if (left)  this.triggerAnim("leftArm",  "leftArmStretch");
                if (right) this.triggerAnim("rightArm", "rightArmStretch");
            }
        } else if (this.staggerTicks <= 0) {
            if (--this.groanCooldown <= 0) {
                this.groanCooldown = 60 + this.random.nextInt(200);
                this.playSound(ModSounds.FEMALE_T_ZOMBIE_GROAN.get(), 1.0f, 1.0f);
                this.triggerAnim("groan", "passiveGroan");
            }
        }

        if (this.wasPursuing && !pursuing) {
            this.stopTriggeredAnim("leftArm", "leftArmStretch");
            this.stopTriggeredAnim("rightArm", "rightArmStretch");
        }
        this.wasPursuing = pursuing;
    }

    // Corpse
    private static final EntityDataAccessor<Boolean> DATA_CORPSE =
            SynchedEntityData.defineId(TZombieEntity.class, EntityDataSerializers.BOOLEAN);
    private int corpseTicks = 0;
    private int collapseAnimTicks = 0;
    private int corpseBurnTicks = 0;
    private static final int CORPSE_BURN_TICKS = 60;   // ~3s linger when set on fire
    private DamageSource collapseSource;                 // transient, for loot crediting
    private static final int CORPSE_LIFETIME = 3000;     // ~300s is 3000
    @Override
    public boolean isPushable() {
        return !this.isCorpse() && super.isPushable();
    }
    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        // in defineSynchedData:
        builder.define(DATA_VARIANT, 0);
        builder.define(DATA_CORPSE, false);
    }
    public boolean isCorpse() { return this.entityData.get(DATA_CORPSE); }
    private void setCorpse(boolean v) { this.entityData.set(DATA_CORPSE, v); }
    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("Corpse", this.isCorpse());
        output.putInt("CorpseTicks", this.corpseTicks);
        output.putInt("ReviveCount", this.reviveCount);
        output.putInt("ReviveTicks", this.reviveTicks);
        output.putInt("Variant", this.getVariant());
    }
    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.corpseTicks = input.getIntOr("CorpseTicks", 0);
        if (input.getBooleanOr("Corpse", false)) {
            this.setCorpse(true);
            this.refreshDimensions();
            this.reviveCount = input.getIntOr("ReviveCount", 0);
            this.reviveTicks = input.getIntOr("ReviveTicks", 0);
            this.setVariant(input.getIntOr("Variant", 0));
        }
    }
    public boolean isPermanentDeath(DamageSource source) {
        return source.is(DamageTypeTags.IS_FIRE);                  // + hatchet later
    }
    private void finalizeCorpse() {
        if (this.level() instanceof ServerLevel sl) {
            DamageSource src = this.collapseSource != null ? this.collapseSource : this.damageSources().generic();
            this.dropAllDeathLoot(sl, src);
            sl.broadcastEntityEvent(this, (byte) 60);     // death poof particles
        }
        this.discard();
    }
    public void collapseToCorpse(DamageSource source) {
        this.collapseSource = source;
        this.setHealth(this.getMaxHealth());                      // it's "down," not gone — reserve for revival
        this.setCorpse(true);
        this.setTarget(null);
        this.getNavigation().stop();
        if (this.reviveCount < MAX_REVIVES && this.random.nextFloat() < REVIVE_CHANCE) {
            this.reviveTicks = rollReviveWait();          // it'll get back up after this wait
        } else {
            this.reviveTicks = 0;                         // fully dead
            this.corpseTicks = CORPSE_LIFETIME;           // just expires
        }
        if (source.getEntity() instanceof Entity killer) {
            faceEntity(killer);
            this.pendingKnockbackFrom = killer;   // apply next tick, clear of the hit's own velocity handling
        }
        this.triggerAnim("controller", "death");                   // the fall
        this.triggerAnim("groan", "injureGroan");
        this.collapseAnimTicks = DEATH_LENGTH_TICKS;               // then settle into the corpse loop
        this.refreshDimensions();
    }
    @Override
    protected EntityDimensions getDefaultDimensions(Pose pose) {
        if (this.isCorpse()) {
            return EntityDimensions.scalable(0.8f, 0.3f);   // wide + short = a body on the floor
        }
        return super.getDefaultDimensions(pose);
    }
    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (DATA_CORPSE.equals(accessor)) {
            this.refreshDimensions();
        }
    }

    // Resurrection
    private void resurrect() {
        this.reviveCount++;
        this.setCorpse(false);                       // fires onSyncedDataUpdated → box stands back up
        this.refreshDimensions();                    // server side
        this.setHealth(this.getMaxHealth());
        this.resurrectTicks = RESURRECT_LENGTH_TICKS;
        this.triggerAnim("controller", "resurrect");
        this.playSound(ModSounds.FEMALE_T_ZOMBIE_GROAN.get(), 1.0f, 1.0f);   // swap if you add a rise sound
    }
    private int rollReviveWait() {
        float r = this.random.nextFloat();
        if (r < 0.20f) return 100  + this.random.nextInt(100);    // short  5–10s  (20%)
        if (r < 0.70f) return 600  + this.random.nextInt(400);    // medium 30–50s (50%)
        return             3600 + this.random.nextInt(2400);      // long   180–300s (30%)
    }

    // Goals
    @Override
    protected void registerGoals() {
        // This determines the AI of the mob
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(2, new TZombieAttackGoal(this));
        this.goalSelector.addGoal(3,new RandomStrollGoal(this, 1d));

        super.registerGoals();
    }

    // Flinching and Staggering
    private int postStaggerLock = 0;
    private static final int POST_STAGGER_LOCK = 4;   // ~0.2s no-strike window after a stagger
    public boolean canStrike() {
        return this.staggerTicks <= 0 && this.postStaggerLock <= 0;
    }
    private int   staggerTicks = 0;
    private float damageSinceStagger = 0f;
    private float staggerThreshold = 0f;                 // rolled lazily (needs maxHealth)
    private static final int STAGGER_DURATION = 30;      // stun length in ticks — match your stagger animation
    public boolean isStaggering() { return this.staggerTicks > 0; }
    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        if (this.isCorpse()) {
            if (isPermanentDeath(source) && this.corpseBurnTicks <= 0) {
                this.reviveTicks = 0;                        // fire = permanent, no getting up
                this.corpseBurnTicks = CORPSE_BURN_TICKS;   // burn out over ~3s instead of vanishing
            }
            return false;
        }
        float before = this.getHealth();
        boolean hurt = super.hurtServer(level, source, damage);     // a lethal hit collapses it in here
        if (this.isCorpse()) return hurt;                           // just collapsed — skip flinch/stagger
        if (hurt) {
            this.triggerAnim("flinch", this.getRandom().nextBoolean() ? "flinch1" : "flinch2");

            if (this.staggerTicks <= 0 && this.isAlive()) {                 // not already staggering, not a killing blow
                if (this.staggerThreshold <= 0f) this.staggerThreshold = rollStaggerThreshold();
                this.damageSinceStagger += before - this.getHealth();       // actual HP lost, not raw damage
                if (this.damageSinceStagger >= this.staggerThreshold) startStagger(source);
            }
        }
        return hurt;
    }

    private float rollStaggerThreshold() {
        float base = this.getMaxHealth() / 3.0f;
        float mult = 1.0f + (this.random.nextFloat() * 2f - 1f) * 0.4f;   // ±40% earlier/later
        return base * mult;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.staggerTicks > 0 || this.isCorpse() || this.resurrectTicks > 0;
    }
    public boolean isGettingUp() { return this.resurrectTicks > 0; }

    public static AttributeSupplier.Builder createTZombieAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1d)
                .add(Attributes.MOVEMENT_SPEED, 0.16d)
                .add(Attributes.TEMPT_RANGE, 16d)
                .add(Attributes.FOLLOW_RANGE, 16d)
                .add(Attributes.ATTACK_DAMAGE, 3.0); // basic strike ~1.5 hearts; tune later
    }
}
