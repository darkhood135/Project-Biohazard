package net.darkhood135.projectbiohazard.sound;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LightLayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.SelectMusicEvent;

@EventBusSubscriber(modid = ProjectBiohazard.MOD_ID, value = Dist.CLIENT)
public class ModMusic {
    private static final Music SAFE_MUSIC = new Music(ModSounds.SECURE, 9000, 18000, false); // replace = FALSE
    private static int tickCounter = 0;
    private static boolean musicArmed = false;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        if (++tickCounter < 20) return;          // ~1s — responsive enough to cut on danger
        tickCounter = 0;

        if (!isSafe(mc.level, mc.player)) {
            if (musicArmed) {
                mc.getMusicManager().stopPlaying(SAFE_MUSIC);   // actively stop OUR track
                musicArmed = false;
            }
            return;
        }
        if (!musicArmed && Math.random() < 0.10) {
            musicArmed = true;
        }
    }


    @SubscribeEvent
    public static void onSelectMusic(SelectMusicEvent event) {
        if (musicArmed) event.setMusic(SAFE_MUSIC);   // cheap — just reads the cached flag
    }

    private static boolean isSafe(ClientLevel level, LocalPlayer player) {
        BlockPos pos = player.blockPosition();
        if (level.getBrightness(LightLayer.BLOCK, pos) < 10) return false;
        return level.getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(30.0)).isEmpty();
    }
}