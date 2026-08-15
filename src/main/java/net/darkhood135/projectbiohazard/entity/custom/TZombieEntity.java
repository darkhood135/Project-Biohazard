package net.darkhood135.projectbiohazard.entity.custom;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class TZombieEntity extends PathfinderMob implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TZombieEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, state -> {
            if (state.isMoving()) {
                return state.setAndContinue(RawAnimation.begin().thenLoop("walk"));
            }
            return state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void registerGoals() {
        // This determines the AI of the mob
        goalSelector.addGoal(0,new FloatGoal(this));
        goalSelector.addGoal(1,new TemptGoal(this, 1d, stack -> stack.is(ItemTags.MEAT), false));
        goalSelector.addGoal(3,new LookAtPlayerGoal(this, Player.class, 6f));

        super.registerGoals();
    }

    public static AttributeSupplier.Builder createTZombieAttributes() {
        return PathfinderMob.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50d)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1d)
                .add(Attributes.MOVEMENT_SPEED, 0.2d)
                .add(Attributes.TEMPT_RANGE, 8d)
                .add(Attributes.FOLLOW_RANGE, 8d);
    }
}
