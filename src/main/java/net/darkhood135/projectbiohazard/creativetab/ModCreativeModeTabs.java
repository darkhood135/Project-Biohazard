package net.darkhood135.projectbiohazard.creativetab;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.awt.*;
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
                        output.accept(ModBlocks.UNDEAD_PLANKS);
                        output.accept(ModBlocks.UNDEAD_STAIRS);
                        output.accept(ModBlocks.UNDEAD_SLAB);
                        output.accept(ModBlocks.UNDEAD_PRESSURE_PLATE);
                        output.accept(ModBlocks.UNDEAD_BUTTON);
                        output.accept(ModBlocks.UNDEAD_FENCE);
                        output.accept(ModBlocks.UNDEAD_FENCE_GATE);
                        output.accept(ModBlocks.UNDEAD_DOOR);
                        output.accept(ModBlocks.UNDEAD_TRAPDOOR);
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
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
