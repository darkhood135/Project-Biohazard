package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.tag.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, ProjectBiohazard.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.STONE_TILES.get())
                .add(ModBlocks.STONE_TILE_SLAB.get())
                .add(ModBlocks.STONE_TILE_STAIRS.get())
                .add(ModBlocks.MOSSY_STONE_TILES.get())
                .add(ModBlocks.MOSSY_STONE_TILE_SLAB.get())
                .add(ModBlocks.MOSSY_STONE_TILE_STAIRS.get())
                .add(ModBlocks.TRONA_ORE.get())
                .add(ModBlocks.SANDSTONE_TRONA_ORE.get())
                .add(ModBlocks.RED_SANDSTONE_TRONA_ORE.get())
                .add(ModBlocks.TRONA_BLOCK.get())
                .add(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get())
                .add(ModBlocks.BAUXITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_BAUXITE_ORE.get())
                .add(ModBlocks.WEATHERED_BRICKS.get())
                .add(ModBlocks.WEATHERED_BRICK_SLAB.get())
                .add(ModBlocks.WEATHERED_BRICK_STAIRS.get())
                .add(ModBlocks.WEATHERED_BRICK_WALL.get());


        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FLESH_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.BEECH_PLANKS.get())
                .add(ModBlocks.BEECH_STAIRS.get())
                .add(ModBlocks.BEECH_SLAB.get())
                .add(ModBlocks.BEECH_BUTTON.get())
                .add(ModBlocks.BEECH_PRESSURE_PLATE.get())
                .add(ModBlocks.BEECH_FENCE.get())
                .add(ModBlocks.BEECH_FENCE_GATE.get())
                .add(ModBlocks.BEECH_DOOR.get())
                .add(ModBlocks.BEECH_TRAPDOOR.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TRONA_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BAUXITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.BEECH_STAIRS.get());
        tag(BlockTags.STAIRS)
                .add(ModBlocks.BEECH_STAIRS.get())
                .add(ModBlocks.STONE_TILE_STAIRS.get())
                .add(ModBlocks.MOSSY_STONE_TILE_STAIRS.get())
                .add(ModBlocks.WEATHERED_BRICK_STAIRS.get());

        tag(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.BEECH_SLAB.get());
        tag(BlockTags.SLABS)
                .add(ModBlocks.BEECH_SLAB.get())
                .add(ModBlocks.STONE_TILE_SLAB.get())
                .add(ModBlocks.MOSSY_STONE_TILE_SLAB.get())
                .add(ModBlocks.WEATHERED_BRICK_SLAB.get());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.BEECH_PRESSURE_PLATE.get());
        tag(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.BEECH_PRESSURE_PLATE.get());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.BEECH_BUTTON.get());
        tag(BlockTags.BUTTONS)
                .add(ModBlocks.BEECH_BUTTON.get());

        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.BEECH_FENCE.get());
        tag(BlockTags.FENCES)
                .add(ModBlocks.BEECH_FENCE.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.BEECH_FENCE_GATE.get());

        tag(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.BEECH_DOOR.get());
        tag(BlockTags.DOORS)
                .add(ModBlocks.BEECH_DOOR.get());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.BEECH_TRAPDOOR.get());
        tag(BlockTags.TRAPDOORS)
                .add(ModBlocks.BEECH_TRAPDOOR.get());

        tag(ModTags.Blocks.NEEDS_ALUMINUM_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL);
        tag(ModTags.Blocks.INCORRECT_FOR_ALUMINUM_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_ALUMINUM_TOOL);

        tag(ModTags.Blocks.HATCHET_MINEABLE)
                .addTag(BlockTags.SWORD_EFFICIENT);

        tag(BlockTags.WALLS)
                .add(ModBlocks.WEATHERED_BRICK_WALL.get());

    }
}
