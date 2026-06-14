package net.darkhood135.projectbiohazard.item;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.food.ModFoodProperties;
import net.darkhood135.projectbiohazard.item.custom.BlueHerbItem;
import net.darkhood135.projectbiohazard.item.custom.EMFItem;
import net.darkhood135.projectbiohazard.item.custom.GreenHerbItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectBiohazard.MOD_ID);

    public static final DeferredItem<Item> UMBRELLA_BADGE = ITEMS.registerSimpleItem("umbrella_badge",
            properties -> properties);
    public static final DeferredItem<Item> TRONA = ITEMS.registerSimpleItem("trona",
            properties -> properties);

    public static final DeferredItem<Item> EMF_VISUALIZER = ITEMS.registerItem("emf_visualizer",
            properties -> new EMFItem(properties.durability(32)));

    // Hidden items (Visual only)
    public static final DeferredItem<Item> UMBRELLA_INSIGNIA = ITEMS.registerSimpleItem("umbrella_insignia",
            properties -> properties);

    // Foods
    public static final DeferredItem<Item> GREEN_HERB = ITEMS.registerItem("green_herb",
            properties -> new GreenHerbItem(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)));
    public static final DeferredItem<Item> RED_HERB = ITEMS.registerItem("red_herb",
            properties -> new Item(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)));
    public static final DeferredItem<Item> BLUE_HERB = ITEMS.registerItem("blue_herb",
            properties -> new BlueHerbItem(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)));
    public static final DeferredItem<Item> YELLOW_HERB = ITEMS.registerItem("yellow_herb",
            properties -> new Item(properties.food(ModFoodProperties.HERB, ModFoodProperties.BASIC_HERB_EFFECT)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
