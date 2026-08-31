package net.darkhood135.projectbiohazard.entity.custom.goal;

import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class TZombieAttackGoal extends Goal {
    private final TZombieEntity zombie;
    private LivingEntity target;

    private int strikeTimer = -1;      // -1 = pursuing; 0..RECOVER_END = mid-strike cycle
    private boolean hasHit;
    private String currentStrike;

    private static final int    HIT_TICK    = 5;    // ~0.2s: damage connects
    private static final int    STRIKE_END  = 11;   // ~0.55s: strike animation length (standstill)
    private static final int    RECOVER_END = 16;   // +~0.4s idle recovery before acting again
    private static final double REACH_SQR   = 4.0;  // ~2 blocks (1-block gap + hitboxes)

    public TZombieAttackGoal(TZombieEntity zombie) {
        this.zombie = zombie;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity t = this.zombie.getTarget();
        if (t == null || !t.isAlive()) return false;
        this.target = t;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.strikeTimer >= 0) return true;                 // never abandon a swing mid-cycle
        LivingEntity t = this.zombie.getTarget();
        return t != null && t.isAlive();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void stop() {
        this.target = null;
        this.strikeTimer = -1;
        this.hasHit = false;
        this.zombie.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.target == null) return;

        if (this.zombie.isStaggering()) {                       // stagger interrupts everything
            this.strikeTimer = -1;
            this.hasHit = false;
            this.zombie.getNavigation().stop();
            return;
        }

        this.zombie.getLookControl().setLookAt(this.target, 30.0f, 30.0f);

        // --- mid-strike: committed, no movement ---
        if (this.strikeTimer >= 0) {
            this.zombie.getNavigation().stop();
            this.strikeTimer++;

            if (!this.hasHit && this.strikeTimer >= HIT_TICK) {
                this.hasHit = true;
                if (inReach() && this.zombie.level() instanceof ServerLevel level) {
                    this.zombie.doHurtTarget(level, this.target);
                }
            }
            if (this.strikeTimer == STRIKE_END) {
                this.zombie.stopTriggeredAnim("controller", this.currentStrike);
            }
            if (this.strikeTimer >= RECOVER_END) this.strikeTimer = -1;
            return;
        }

        // --- pursuit ---
        if (inReach()) {
            if (!this.zombie.canStrike()) {                // still settling from a stagger — hold, don't swing
                this.zombie.getNavigation().stop();
                return;
            }
            this.strikeTimer = 0;
            this.hasHit = false;
            this.zombie.getNavigation().stop();
            this.currentStrike = this.zombie.getRandom().nextBoolean() ? "strike" : "strike2";
            this.zombie.triggerAnim("controller", this.currentStrike);
        } else {
            this.zombie.getNavigation().moveTo(this.target, 1.0);
        }
    }

    private boolean inReach() {
        return this.zombie.distanceToSqr(this.target) <= REACH_SQR
                && this.zombie.getSensing().hasLineOfSight(this.target);
    }
}