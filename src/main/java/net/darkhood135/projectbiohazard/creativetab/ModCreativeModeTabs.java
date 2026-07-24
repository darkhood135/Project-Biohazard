package net.darkhood135.projectbiohazard.creativetab;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.datagen.ModRecipeProvider;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.VialItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomModelData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectBiohazard.MOD_ID);

    public static final Supplier<CreativeModeTab> PROJECT_BIOHAZARD_TAB =
            CREATIVE_MODE_TABS.register("project_biohazard_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.UMBRELLA_INSIGNIA.get()))
                    .title(Component.translatable("creativetab.projectbiohazard.project_biohazard"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.UMBRELLA_BADGE);
                        output.accept(ModItems.TRONA);
                        output.accept(ModBlocks.TRONA_ORE);
                        output.accept(ModBlocks.TRONA_BLOCK);
                        output.accept(ModItems.SODA_ASH);
                        output.accept(ModItems.BAUXITE);
                        output.accept(ModBlocks.BAUXITE_ORE);
                        output.accept(ModBlocks.DEEPSLATE_BAUXITE_ORE);
                        output.accept(ModItems.ALUMINUM_INGOT);
                        output.accept(ModItems.EMF_VISUALIZER);
                        output.accept(ModBlocks.FLESH_BLOCK);
                        output.accept(ModItems.GLASS_VIAL);
                        output.accept(ModItems.GREEN_HERB);
                        output.accept(ModItems.RED_HERB);
                        output.accept(ModItems.YELLOW_HERB);
                        output.accept(ModItems.BLUE_HERB);
                        output.accept(ModItems.BORON_SHARD);
                        output.accept(ModBlocks.BEECH_PLANKS);
                        output.accept(ModBlocks.BEECH_STAIRS);
                        output.accept(ModBlocks.BEECH_SLAB);
                        output.accept(ModBlocks.BEECH_PRESSURE_PLATE);
                        output.accept(ModBlocks.BEECH_BUTTON);
                        output.accept(ModBlocks.BEECH_FENCE);
                        output.accept(ModBlocks.BEECH_FENCE_GATE);
                        output.accept(ModBlocks.BEECH_DOOR);
                        output.accept(ModBlocks.BEECH_TRAPDOOR);
                        output.accept(ModBlocks.BOROSILICATE_GLASS);
                        output.accept(ModBlocks.BOROSILICATE_GLASS_PANE);

                        output.accept(ModItems.WOODEN_HATCHET);
                        output.accept(ModItems.STONE_HATCHET);
                        output.accept(ModItems.COPPER_HATCHET);
                        output.accept(ModItems.IRON_HATCHET);
                        output.accept(ModItems.GOLDEN_HATCHET);
                        output.accept(ModItems.DIAMOND_HATCHET);
                        output.accept(ModItems.NETHERITE_HATCHET);

                        output.accept(ModItems.ALUMINUM_SWORD);
                        output.accept(ModItems.ALUMINUM_PICKAXE);
                        output.accept(ModItems.ALUMINUM_AXE);
                        output.accept(ModItems.ALUMINUM_SHOVEL);
                        output.accept(ModItems.ALUMINUM_HOE);
                        output.accept(ModItems.ALUMINUM_HATCHET);
                        output.accept(ModItems.SECURE_MUSIC_DISC);
                        output.accept(ModItems.EMPTY_SYRINGE);
                        output.accept(ModBlocks.TYPEWRITER);
                        output.accept(ModItems.INK_RIBBON);
                    })
                    .build());

    public static final Supplier<CreativeModeTab> VIAL_MIXTURES_TAB =
            CREATIVE_MODE_TABS.register("vial_mixtures_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.WATER_VIAL.get()))
                    .title(Component.translatable("creativetab.projectbiohazard.vial_mixtures"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GREEN_HERB);
                        output.accept(ModItems.RED_HERB);
                        output.accept(ModItems.YELLOW_HERB);
                        output.accept(ModItems.BLUE_HERB);
                        output.accept(ModItems.GLASS_VIAL);
                        for (List<VialItem.Herb> mix : ModRecipeProvider.allHerbMixtures()) {
                            String key = VialItem.Herb.toKey(mix);
                            ItemStack v = new ItemStack(ModItems.GLASS_VIAL.get());
                            v.set(ModDataComponentTypes.HERB_VIAL_COMBINATION.get(), key);
                            v.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(List.of(), List.of(), List.of(key), List.of()));
                            output.accept(v);
                        }
                        // potion vials: one per registered potion
                        itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potion ->
                                output.accept(PotionContents.createItemStack(ModItems.WATER_VIAL.get(), potion)));
                        output.accept(ModItems.HONEY_VIAL);
                        output.accept(ModItems.EMPTY_SYRINGE);
                        itemDisplayParameters.holders().lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
                            ItemStack s = new ItemStack(ModItems.SYRINGE.get());
                            s.set(DataComponents.POTION_CONTENTS, new PotionContents(potion));
                            output.accept(s);
                        });
                    })
                    .build());

    public static final Supplier<CreativeModeTab> BIOHAZARD_BLOCKS_TAB =
            CREATIVE_MODE_TABS.register("biohazard_blocks_tab", () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModBlocks.TYPEWRITER.get()))
                    .title(Component.translatable("creativetab.projectbiohazard.biohazard_blocks"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModBlocks.TYPEWRITER);
                        output.accept(ModBlocks.BEECH_PLANKS);
                        output.accept(ModBlocks.BEECH_STAIRS);
                        output.accept(ModBlocks.BEECH_SLAB);
                        output.accept(ModBlocks.BEECH_FENCE);
                        output.accept(ModBlocks.BEECH_FENCE_GATE);
                        output.accept(ModBlocks.BEECH_DOOR);
                        output.accept(ModBlocks.BEECH_TRAPDOOR);
                        output.accept(ModBlocks.BEECH_PRESSURE_PLATE);
                        output.accept(ModBlocks.BEECH_BUTTON);
                        output.accept(ModBlocks.STONE_TILES);
                        output.accept(ModBlocks.STONE_TILE_STAIRS);
                        output.accept(ModBlocks.STONE_TILE_SLAB);
                        output.accept(ModBlocks.MOSSY_STONE_TILES);
                        output.accept(ModBlocks.MOSSY_STONE_TILE_STAIRS);
                        output.accept(ModBlocks.MOSSY_STONE_TILE_SLAB);
                        output.accept(ModBlocks.WEATHERED_BRICKS);
                        output.accept(ModBlocks.WEATHERED_BRICK_STAIRS);
                        output.accept(ModBlocks.WEATHERED_BRICK_SLAB);
                        output.accept(ModBlocks.WEATHERED_BRICK_WALL);
                        output.accept(ModBlocks.DIRTY_GLASS);
                        output.accept(ModBlocks.DIRTY_GLASS_PANE);
                    })
                    .build());



    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
