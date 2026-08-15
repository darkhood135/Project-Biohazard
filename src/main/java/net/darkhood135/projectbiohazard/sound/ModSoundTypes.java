package net.darkhood135.projectbiohazard.sound;   // sits fine next to ModSounds

import net.minecraft.world.level.block.SoundType;

public class ModSoundTypes {
    public static final SoundType LOOT_BARREL = new SoundType(
            1.0f, 1.0f,                          // volume, pitch
            ModSounds.BARREL_DESTROY.get(),   // break — your custom sound
            SoundType.WOOD.getStepSound(),
            SoundType.WOOD.getPlaceSound(),
            SoundType.WOOD.getHitSound(),
            SoundType.WOOD.getFallSound());
}