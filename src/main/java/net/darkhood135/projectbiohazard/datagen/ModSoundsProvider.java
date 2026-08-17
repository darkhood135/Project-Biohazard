package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.sound.ModSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class ModSoundsProvider extends SoundDefinitionsProvider {

    public ModSoundsProvider(PackOutput output) {
        super(output, ProjectBiohazard.MOD_ID);
    }

    @Override
    public void registerSounds() {
        add(ModSounds.SYRINGE_PIERCE.get(), definition().subtitle("sounds.projectbiohazard.syringe_pierce")
                .with(sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "syringe_pierce1")),
                    sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "syringe_pierce2")),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "syringe_pierce3"))));

        add(ModSounds.SECURE_CD.get(), definition().subtitle("sounds.projectbiohazard.secure_cd")
                .with(sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "secure_cd")).stream()));

        add(ModSounds.TYPEWRITER_SAVE.get(), definition().subtitle("sounds.projectbiohazard.typewriter_save")
                .with(sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "typewriter_save")).stream()));

        add(ModSounds.BARREL_DESTROY.get(), definition().subtitle("sounds.projectbiohazard.barrel_destroy")
                .with(
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "barrel_destroy")).pitch(0.9f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "barrel_destroy")).pitch(1.0f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "barrel_destroy")).pitch(1.1f)
                ));

        add(ModSounds.SPRAY_CAN_THUNK.get(), definition().subtitle("sounds.projectbiohazard.spray_can_thunk")
                .with(
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk1")).pitch(0.9f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk1")).pitch(1.0f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk1")).pitch(1.1f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk2")).pitch(0.9f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk2")).pitch(1.0f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "thunk2")).pitch(1.1f)
                ));

        add(ModSounds.SPRAY_HISS.get(), definition().subtitle("sounds.projectbiohazard.spray_hiss")
                .with(
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "spray")).pitch(0.9f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "spray")).pitch(1.0f),
                        sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "spray")).pitch(1.1f)
                ));

        add(ModSounds.SECURE.get(), definition().subtitle("sounds.projectbiohazard.secure")
                .with(sound(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "secure")).stream()));
    }
}
