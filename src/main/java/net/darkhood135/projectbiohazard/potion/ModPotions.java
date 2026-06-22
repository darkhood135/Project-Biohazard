package net.darkhood135.projectbiohazard.potion;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(BuiltInRegistries.POTION, ProjectBiohazard.MOD_ID);

    /* EXAMPLE CODE
    public static final Holder<Potion> POTION = POTIONS.register("adrenaline_potion",
            () -> new Potion("adrenaline_potion", new MobEffectInstance(ModEffects.ADRENALINE, 1200, 0)));
     */

    // INCLUDE LONG AND STRONG VARIATIONS
    // CHECK VANILLA POTIONS FOR EXISTING FEATURES

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
