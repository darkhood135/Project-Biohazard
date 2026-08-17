package net.darkhood135.projectbiohazard.sound.client;

import net.darkhood135.projectbiohazard.item.custom.FirstAidSprayItem;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

public class AidSpraySoundInstance extends AbstractTickableSoundInstance {
    private static final int FADE_TICKS = 6;
    private final Player player;
    private int fade = 0;

    public AidSpraySoundInstance(Player player) {
        super(ModSounds.SPRAY_HISS.get(), SoundSource.PLAYERS, player.getRandom());
        this.player = player;
        this.looping = false;
        this.volume = 1.0f;
        this.x = player.getX(); this.y = player.getY(); this.z = player.getZ();
    }

    @Override
    public void tick() {
        if (player.isRemoved()) { this.stop(); return; }
        this.x = player.getX(); this.y = player.getY(); this.z = player.getZ();   // stay centered on the player
        boolean spraying = player.isUsingItem() && player.getUseItem().getItem() instanceof FirstAidSprayItem;
        if (spraying) { this.fade = 0; this.volume = 1.0f; }
        else {
            this.fade++;
            this.volume = Math.max(0f, 1.0f - (float) this.fade / FADE_TICKS);
            if (this.fade >= FADE_TICKS) this.stop();
        }
    }
}