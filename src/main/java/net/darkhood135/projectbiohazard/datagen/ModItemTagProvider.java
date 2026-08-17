package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.tag.ModTags;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ProjectBiohazard.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.EMF_TOOLS)
                .add(ModItems.EMF_VISUALIZER.get());

        tag(ModTags.Items.ALUMINUM_REPAIRABLES)
                .add(ModItems.ALUMINUM_INGOT.get());

        //Keys
        tag(ModTags.Items.DOOR_KEYS)
                .add(ModItems.CLUB_KEY.get())
                .add(ModItems.HEART_KEY.get())
                .add(ModItems.SPADE_KEY.get())
                .add(ModItems.DIAMOND_KEY.get());


        // Aluminum Tools
        tag(ItemTags.SWORDS).add(ModItems.ALUMINUM_SWORD.get());
        tag(ItemTags.PICKAXES).add(ModItems.ALUMINUM_PICKAXE.get());
        tag(ItemTags.AXES).add(ModItems.ALUMINUM_AXE.get());
        tag(ItemTags.SHOVELS).add(ModItems.ALUMINUM_SHOVEL.get());
        tag(ItemTags.HOES).add(ModItems.ALUMINUM_HOE.get());


        // Hatchets
        tag(ModTags.Items.HATCHETS)
                .add(ModItems.ALUMINUM_HATCHET.get())
                .add(ModItems.COPPER_HATCHET.get())
                .add(ModItems.WOODEN_HATCHET.get())
                .add(ModItems.IRON_HATCHET.get())
                .add(ModItems.STONE_HATCHET.get())
                .add(ModItems.DIAMOND_HATCHET.get())
                .add(ModItems.GOLDEN_HATCHET.get())
                .add(ModItems.NETHERITE_HATCHET.get());
    }
}
