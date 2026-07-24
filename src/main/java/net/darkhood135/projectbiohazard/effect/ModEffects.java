package net.darkhood135.projectbiohazard.effect;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.effect.custom.AdrenalineEffect;
import net.darkhood135.projectbiohazard.effect.custom.ConstitutionEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ProjectBiohazard.MOD_ID);

    public static final Holder<MobEffect> CONSTITUTION = MOB_EFFECTS.register("constitution",
            () -> new ConstitutionEffect(MobEffectCategory.BENEFICIAL, 0xd1a347));
    public static final Holder<MobEffect> ADRENALINE = MOB_EFFECTS.register("adrenaline",
            () -> new AdrenalineEffect(MobEffectCategory.BENEFICIAL, 0x4a0207)   // pick a color
                    .addAttributeModifier(
                            Attributes.ATTACK_SPEED,
                            Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "adrenaline_attack_speed"),
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                            amp -> 0.3 * (amp + 1)));        // amp0 = +30% (1.3x), amp1 = +60% (1.6x)

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
