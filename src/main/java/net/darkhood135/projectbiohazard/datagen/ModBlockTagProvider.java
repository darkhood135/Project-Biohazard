package net.darkhood135.projectbiohazard.datagen;

import com.jcraft.jorbis.Block;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.tag.ModTags;
import net.minecraft.core.Holder;
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
                .add(ModBlocks.TRONA_ORE.get())
                .add(ModBlocks.SANDSTONE_TRONA_ORE.get())
                .add(ModBlocks.RED_SANDSTONE_TRONA_ORE.get())
                .add(ModBlocks.TRONA_BLOCK.get())
                .add(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get())
                .add(ModBlocks.BAUXITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FLESH_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.UNDEAD_PLANKS.get())
                .add(ModBlocks.UNDEAD_STAIRS.get())
                .add(ModBlocks.UNDEAD_SLAB.get())
                .add(ModBlocks.UNDEAD_BUTTON.get())
                .add(ModBlocks.UNDEAD_PRESSURE_PLATE.get())
                .add(ModBlocks.UNDEAD_FENCE.get())
                .add(ModBlocks.UNDEAD_FENCE_GATE.get())
                .add(ModBlocks.UNDEAD_DOOR.get())
                .add(ModBlocks.UNDEAD_TRAPDOOR.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TRONA_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.BAUXITE_ORE.get())
                .add(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());

        tag(BlockTags.WOODEN_STAIRS)
                .add(ModBlocks.UNDEAD_STAIRS.get());
        tag(BlockTags.STAIRS)
                .add(ModBlocks.UNDEAD_STAIRS.get());

        tag(BlockTags.WOODEN_SLABS)
                .add(ModBlocks.UNDEAD_SLAB.get());
        tag(BlockTags.SLABS)
                .add(ModBlocks.UNDEAD_SLAB.get());

        tag(BlockTags.WOODEN_PRESSURE_PLATES)
                .add(ModBlocks.UNDEAD_PRESSURE_PLATE.get());
        tag(BlockTags.PRESSURE_PLATES)
                .add(ModBlocks.UNDEAD_PRESSURE_PLATE.get());

        tag(BlockTags.WOODEN_BUTTONS)
                .add(ModBlocks.UNDEAD_BUTTON.get());
        tag(BlockTags.BUTTONS)
                .add(ModBlocks.UNDEAD_BUTTON.get());

        tag(BlockTags.WOODEN_FENCES)
                .add(ModBlocks.UNDEAD_FENCE.get());
        tag(BlockTags.FENCES)
                .add(ModBlocks.UNDEAD_FENCE.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.UNDEAD_FENCE_GATE.get());

        tag(BlockTags.WOODEN_DOORS)
                .add(ModBlocks.UNDEAD_DOOR.get());
        tag(BlockTags.DOORS)
                .add(ModBlocks.UNDEAD_DOOR.get());

        tag(BlockTags.WOODEN_TRAPDOORS)
                .add(ModBlocks.UNDEAD_TRAPDOOR.get());
        tag(BlockTags.TRAPDOORS)
                .add(ModBlocks.UNDEAD_TRAPDOOR.get());

        tag(ModTags.Blocks.NEEDS_ALUMINUM_TOOL)
                .addTag(BlockTags.NEEDS_IRON_TOOL);
        tag(ModTags.Blocks.INCORRECT_FOR_ALUMINUM_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_ALUMINUM_TOOL);

        tag(ModTags.Blocks.HATCHET_MINEABLE)
                .addTag(BlockTags.SWORD_EFFICIENT);
    }
}
