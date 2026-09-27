package net.darkhood135.projectbiohazard.entity.custom.goal;

import net.darkhood135.projectbiohazard.entity.custom.TZombieEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class TZombieInvestigateGoal extends Goal {
    private final TZombieEntity zombie;
    private static final double ARRIVE_SQR = 2.25;   // ~1.5 blocks
    private static final double SPEED = 1.0;

    public TZombieInvestigateGoal(TZombieEntity zombie) {
        this.zombie = zombie;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.zombie.getTarget() == null && !this.zombie.isCorpse()
                && !this.zombie.isGettingUp() && this.zombie.getInvestigatePos() != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.zombie.getTarget() == null
                && this.zombie.getInvestigatePos() != null
                && this.zombie.getInvestigateTicks() > 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() { return true; }

    @Override
    public void start() { pathToNoise(); }

    @Override
    public void stop() {
        this.zombie.clearInvestigation();
        this.zombie.getNavigation().stop();
    }

    @Override
    public void tick() {
        BlockPos p = this.zombie.getInvestigatePos();
        if (p == null) return;
        Vec3 c = Vec3.atCenterOf(p);
        this.zombie.getLookControl().setLookAt(c.x, c.y, c.z);

        if (this.zombie.distanceToSqr(c) <= ARRIVE_SQR) {
            this.zombie.getNavigation().stop();          // arrived — wait/look until the timer lapses
        } else if (this.zombie.getNavigation().isDone()) {
            pathToNoise();                               // re-path if blocked or the noise moved
        }
    }

    private void pathToNoise() {
        BlockPos p = this.zombie.getInvestigatePos();
        if (p != null) this.zombie.getNavigation().moveTo(p.getX() + 0.5, p.getY(), p.getZ() + 0.5, SPEED);
    }
}