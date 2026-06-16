package net.darkhood135.projectbiohazard.item;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.food.ModFoodProperties;
import net.darkhood135.projectbiohazard.item.custom.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Consumer;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectBiohazard.MOD_ID);

    public static final DeferredItem<Item> UMBRELLA_BADGE = ITEMS.registerSimpleItem("umbrella_badge",
            properties -> properties);
    public static final DeferredItem<Item> TRONA = ITEMS.registerSimpleItem("trona",
            properties -> properties);
    public static final DeferredItem<Item> SODA_ASH = ITEMS.registerSimpleItem("soda_ash",
            properties -> properties);
    public static final DeferredItem<Item> BAUXITE = ITEMS.registerSimpleItem("bauxite",
            properties -> properties);
    public static final DeferredItem<Item> ALUMINUM_INGOT = ITEMS.registerSimpleItem("aluminum_ingot",
            properties -> properties);

    public static final DeferredItem<Item> GLASS_VIAL = ITEMS.registerItem("glass_vial",
            properties -> new VialItem(properties.food(ModFoodProperties.HERB_VIAL, ModFoodProperties.BASIC_VIAL_EFFECT).component(ModDataComponentTypes.HERB_VIAL_COMBINATION.get(), "")));

    public static final DeferredItem<Item> EMF_VISUALIZER = ITEMS.registerItem("emf_visualizer",
            properties -> new EMFItem(properties.durability(32)));

    // Hidden items (Visual only)
    public static final DeferredItem<Item> UMBRELLA_INSIGNIA = ITEMS.registerSimpleItem("umbrella_insignia",
            properties -> properties);

    // Foods
    public static final DeferredItem<Item> GREEN_HERB = ITEMS.registerItem("green_herb",
            properties -> new HerbItem(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)) {
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable("tooltip.projectbiohazard.green_herb"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> RED_HERB = ITEMS.registerItem("red_herb",
            properties -> new Item(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT))
            {
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable("tooltip.projectbiohazard.red_herb"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> BLUE_HERB = ITEMS.registerItem("blue_herb",
            properties -> new HerbItem(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)){
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable("tooltip.projectbiohazard.blue_herb"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> YELLOW_HERB = ITEMS.registerItem("yellow_herb",
            properties -> new HerbItem(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT))
            {
                @Override
                public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
                    builder.accept(Component.translatable("tooltip.projectbiohazard.yellow_herb"));
                    super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
                }
            });
    public static final DeferredItem<Item> BORON_SHARD = ITEMS.registerItem("boron_shard", Item::new);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
