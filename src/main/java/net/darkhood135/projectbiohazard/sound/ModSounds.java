package net.darkhood135.projectbiohazard.sound;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, ProjectBiohazard.MOD_ID);

    public static final Supplier<SoundEvent> SYRINGE_PIERCE = registerSoundEvent("syringe_pierce");

    public static final Supplier<SoundEvent> BARREL_DESTROY = registerSoundEvent("barrel_destroy");

    public static final Supplier<SoundEvent> TYPEWRITER_SAVE = registerSoundEvent("typewriter_save");

    public static final Supplier<SoundEvent> SPRAY_CAN_THUNK = registerSoundEvent("spray_can_thunk");
    public static final Supplier<SoundEvent> SPRAY_HISS = registerSoundEvent("spray_hiss");

    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_GROAN = registerSoundEvent("female_t-zombie_groan");
    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_NOTICE = registerSoundEvent("female_t-zombie_notice");
    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_STAGGER = registerSoundEvent("female_t-zombie_stagger");
    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_LUNGE = registerSoundEvent("female_t-zombie_lunge");
    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_DEATH = registerSoundEvent("female_t-zombie_death");
    public static final Supplier<SoundEvent> FEMALE_T_ZOMBIE_FEAST = registerSoundEvent("female_t-zombie_feast");

    public static final DeferredHolder<SoundEvent, SoundEvent> SECURE_CD = registerJukeboxSong("secure_cd");

    public static final DeferredHolder<SoundEvent, SoundEvent> SECURE = registerJukeboxSong("secure");

    private static Supplier<SoundEvent> registerSoundEvent(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    private static DeferredHolder<SoundEvent, SoundEvent> registerJukeboxSong(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
