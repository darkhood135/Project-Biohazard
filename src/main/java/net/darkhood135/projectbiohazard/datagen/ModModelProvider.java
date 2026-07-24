package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ProjectBiohazard.MOD_ID);
    }

    @Override
    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems()
                .filter(holder -> holder.value() != ModItems.GLASS_VIAL.get())
                .filter(holder -> holder.value() != ModItems.WATER_VIAL.get())
                .filter(holder -> holder.value() != ModItems.SYRINGE.get())
                .filter(holder -> holder.value() != ModItems.EMF_VISUALIZER.get())
                .filter(holder -> holder.value() != ModBlocks.TYPEWRITER.asItem());

    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        // Items
        itemModels.generateFlatItem(ModItems.TRONA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_BADGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_INSIGNIA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RED_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUE_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.YELLOW_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BORON_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SODA_ASH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAUXITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HONEY_VIAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SECURE_MUSIC_DISC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.EMPTY_SYRINGE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Materials
        itemModels.generateFlatItem(ModItems.INK_RIBBON.get(), ModelTemplates.FLAT_ITEM);

        // Aluminum Tools
        itemModels.generateFlatItem(ModItems.ALUMINUM_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Hatchets
        itemModels.generateFlatItem(ModItems.ALUMINUM_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.WOODEN_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Blocks
        blockModels.createTrivialCube(ModBlocks.TRONA_ORE.get());
        blockModels.createTrivialCube(ModBlocks.BAUXITE_ORE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());
        blockModels.createTrivialBlock(
                ModBlocks.SANDSTONE_TRONA_ORE.get(),
                TexturedModel.createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP)
        );
        blockModels.createTrivialBlock(
                ModBlocks.RED_SANDSTONE_TRONA_ORE.get(),
                TexturedModel.createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP)
        );
        blockModels.createTrivialCube(ModBlocks.TRONA_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.FLESH_BLOCK.get());

        blockModels.createGlassBlocks(ModBlocks.BOROSILICATE_GLASS.get(), ModBlocks.BOROSILICATE_GLASS_PANE.get());
        blockModels.createGlassBlocks(ModBlocks.DIRTY_GLASS.get(), ModBlocks.DIRTY_GLASS_PANE.get());

        blockModels.family(ModBlocks.STONE_TILES.get())
                .stairs(ModBlocks.STONE_TILE_STAIRS.get())
                .slab(ModBlocks.STONE_TILE_SLAB.get());

        blockModels.family(ModBlocks.WEATHERED_BRICKS.get())
                .stairs(ModBlocks.WEATHERED_BRICK_STAIRS.get())
                .slab(ModBlocks.WEATHERED_BRICK_SLAB.get())
                .wall(ModBlocks.WEATHERED_BRICK_WALL.get());

        blockModels.family(ModBlocks.MOSSY_STONE_TILES.get())
                .stairs(ModBlocks.MOSSY_STONE_TILE_STAIRS.get())
                .slab(ModBlocks.MOSSY_STONE_TILE_SLAB.get());

        blockModels.family(ModBlocks.BEECH_PLANKS.get())
                .stairs(ModBlocks.BEECH_STAIRS.get())
                .slab(ModBlocks.BEECH_SLAB.get())
                .button(ModBlocks.BEECH_BUTTON.get())
                .pressurePlate(ModBlocks.BEECH_PRESSURE_PLATE.get())
                .fence(ModBlocks.BEECH_FENCE.get())
                .fenceGate(ModBlocks.BEECH_FENCE_GATE.get())
                .door(ModBlocks.BEECH_DOOR.get())
                .trapdoor(ModBlocks.BEECH_TRAPDOOR.get());

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.TYPEWRITER.get(),
                BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/typewriter")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }
}
