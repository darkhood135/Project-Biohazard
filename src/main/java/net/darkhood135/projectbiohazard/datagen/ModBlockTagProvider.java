package net.darkhood135.projectbiohazard.datagen;

import com.jcraft.jorbis.Block;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
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
                .add(ModBlocks.TRONA_BLOCK.get())
                .add(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get());

        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FLESH_BLOCK.get());

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.TRONA_ORE.get());
    }
}
