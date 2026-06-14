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
                        output.accept(ModItems.EMF_VISUALIZER);
                        output.accept(ModBlocks.FLESH_BLOCK);
                        output.accept(ModItems.GREEN_HERB);
                        output.accept(ModItems.RED_HERB);
                        output.accept(ModItems.YELLOW_HERB);
                        output.accept(ModItems.BLUE_HERB);

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
