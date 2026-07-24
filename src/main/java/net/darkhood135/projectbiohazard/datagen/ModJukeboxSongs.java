package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;

public class ModJukeboxSongs {

    public static final ResourceKey<JukeboxSong> SECURE_KEY = createKey("secure");

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        register(context, SECURE_KEY, ((Holder.Reference<SoundEvent>) ModSounds.SECURE_CD.getDelegate()), 79, 2);
    }

    private static ResourceKey<JukeboxSong> createKey(String name) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name));
    }

    private static void register(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> registryKey,
                                 final Holder.Reference<SoundEvent> soundEvent, int lengthInSeconds, int comparatorOutput) {
        context.register(registryKey, new JukeboxSong(soundEvent,
                Component.translatable(Util.makeDescriptionId("jukebox_song", registryKey.identifier())), lengthInSeconds, comparatorOutput));
    }
}
