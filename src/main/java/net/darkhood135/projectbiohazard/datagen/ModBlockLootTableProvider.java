package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.fml.common.Mod;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {

    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        // dropSelf(ModBlocks.NAME_HERE.get());
        dropSelf(ModBlocks.FLESH_BLOCK.get());

        dropSelf(ModBlocks.TYPEWRITER.get());

        dropSelf(ModBlocks.UNDEAD_PLANKS.get());
        dropSelf(ModBlocks.UNDEAD_STAIRS.get());
        dropSelf(ModBlocks.UNDEAD_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.UNDEAD_BUTTON.get());
        add(ModBlocks.UNDEAD_SLAB.get(), this::createSlabItemTable);
        dropSelf(ModBlocks.UNDEAD_FENCE.get());
        dropSelf(ModBlocks.UNDEAD_FENCE_GATE.get());
        dropSelf(ModBlocks.UNDEAD_TRAPDOOR.get());
        add(ModBlocks.UNDEAD_DOOR.get(), this::createDoorTable);
        dropOther(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get(), Blocks.REDSTONE_BLOCK);

        add(ModBlocks.BAUXITE_ORE.get(),
                block -> createOreDrop(block, ModItems.BAUXITE.get()));
        add(ModBlocks.DEEPSLATE_BAUXITE_ORE.get(),
                block -> createOreDrop(block, ModItems.BAUXITE.get()));

        add(ModBlocks.TRONA_ORE.get(),
                block -> createMultipleOreDrops(block, ModItems.TRONA.get(), 3, 6));
        add(ModBlocks.SANDSTONE_TRONA_ORE.get(),
                block -> createMultipleOreDrops(block, ModItems.TRONA.get(), 3, 6));
        add(ModBlocks.RED_SANDSTONE_TRONA_ORE.get(),
                block -> createMultipleOreDrops(block, ModItems.TRONA.get(), 3, 6));
        add(ModBlocks.TRONA_BLOCK.get(),
                block -> createMultipleDrops(block, ModItems.TRONA.get(), 6, 9));

        add(ModBlocks.BOROSILICATE_GLASS.get(), this::createSilkTouchOnlyTable);
        add(ModBlocks.BOROSILICATE_GLASS_PANE.get(), this::createSilkTouchOnlyTable);
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))
                ));
    }

    protected LootTable.Builder createMultipleDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                ));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
