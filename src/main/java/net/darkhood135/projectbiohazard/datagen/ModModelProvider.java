package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ProjectBiohazard.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        // Items
        itemModels.generateFlatItem(ModItems.TRONA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_BADGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_INSIGNIA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.EMF_VISUALIZER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RED_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUE_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.YELLOW_HERB.get(), ModelTemplates.FLAT_ITEM);

        // Blocks
        blockModels.createTrivialCube(ModBlocks.TRONA_ORE.get());
        blockModels.createTrivialCube(ModBlocks.TRONA_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.FLESH_BLOCK.get());
    }
}
