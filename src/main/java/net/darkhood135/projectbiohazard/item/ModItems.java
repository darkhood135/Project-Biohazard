package net.darkhood135.projectbiohazard.item;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.datagen.ModJukeboxSongs;
import net.darkhood135.projectbiohazard.food.ModFoodProperties;
import net.darkhood135.projectbiohazard.item.custom.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumables;
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
    public static final DeferredItem<Item> WATER_VIAL = ITEMS.registerItem("water_vial",
            properties -> new PotionVialItem(properties.stacksTo(1).food(ModFoodProperties.WATER_VIAL, ModFoodProperties.BASIC_VIAL_EFFECT)));
    public static final DeferredItem<Item> HONEY_VIAL = ITEMS.registerItem("honey_vial",
            properties -> new Item(properties
                    .craftRemainder(ModItems.GLASS_VIAL.get())
                    .food(Foods.HONEY_BOTTLE, Consumables.HONEY_BOTTLE)   // reuse vanilla hunger + poison-clear
                    .usingConvertsTo(ModItems.GLASS_VIAL.get())            // drink -> empty vial
                    .stacksTo(16)));

    public static final DeferredItem<Item> EMF_VISUALIZER = ITEMS.registerItem("emf_visualizer",
            properties -> new EMFItem(properties.durability(32)));

    // Hidden items (Visual only)
    public static final DeferredItem<Item> UMBRELLA_INSIGNIA = ITEMS.registerSimpleItem("umbrella_insignia",
            properties -> properties);

    // Music Discs
    public static final DeferredItem<Item> SECURE_MUSIC_DISC = ITEMS.registerItem("secure_music_disc",
            properties -> new Item(properties.jukeboxPlayable(ModJukeboxSongs.SECURE_KEY).rarity(Rarity.EPIC).stacksTo(1)));

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

    public static final DeferredItem<Item> ALUMINUM_SWORD = ITEMS.registerItem("aluminum_sword",
            properties -> new Item(properties.sword(ModToolMaterials.ALUMINUM, 3f, -2.4f)));
    public static final DeferredItem<Item> ALUMINUM_PICKAXE = ITEMS.registerItem("aluminum_pickaxe",
            properties -> new Item(properties.pickaxe(ModToolMaterials.ALUMINUM, 1f, -2.8f)));
    public static final DeferredItem<Item> ALUMINUM_SHOVEL = ITEMS.registerItem("aluminum_shovel",
            properties -> new ShovelItem(ModToolMaterials.ALUMINUM, 1.5f, -3f, properties));
    public static final DeferredItem<Item> ALUMINUM_AXE = ITEMS.registerItem("aluminum_axe",
            properties -> new AxeItem(ModToolMaterials.ALUMINUM, 6f, -3.2f, properties));
    public static final DeferredItem<Item> ALUMINUM_HOE = ITEMS.registerItem("aluminum_hoe",
            properties -> new HoeItem(ModToolMaterials.ALUMINUM, 0f, -3f, properties));

    // Hatchets
    public static final DeferredItem<Item> ALUMINUM_HATCHET = ITEMS.registerItem("aluminum_hatchet",
            properties -> new HatchetItem(ModToolMaterials.ALUMINUM, 1f, -1.5f, properties));
    public static final DeferredItem<Item> WOODEN_HATCHET = ITEMS.registerItem("wooden_hatchet",
            properties -> new HatchetItem(ToolMaterial.WOOD, 1f, -1.5f, properties));
    public static final DeferredItem<Item> STONE_HATCHET = ITEMS.registerItem("stone_hatchet",
            properties -> new HatchetItem(ToolMaterial.STONE, 1f, -1.5f, properties));
    public static final DeferredItem<Item> COPPER_HATCHET = ITEMS.registerItem("copper_hatchet",
            properties -> new HatchetItem(ToolMaterial.COPPER, 1f, -1.5f, properties));
    public static final DeferredItem<Item> IRON_HATCHET = ITEMS.registerItem("iron_hatchet",
            properties -> new HatchetItem(ToolMaterial.IRON, 1f, -1.5f, properties));
    public static final DeferredItem<Item> GOLDEN_HATCHET = ITEMS.registerItem("golden_hatchet",
            properties -> new HatchetItem(ToolMaterial.GOLD, 1f, -1.5f, properties));
    public static final DeferredItem<Item> DIAMOND_HATCHET = ITEMS.registerItem("diamond_hatchet",
            properties -> new HatchetItem(ToolMaterial.DIAMOND, 1f, -1.5f, properties));
    public static final DeferredItem<Item> NETHERITE_HATCHET = ITEMS.registerItem("netherite_hatchet",
            properties -> new HatchetItem(ToolMaterial.NETHERITE, 1f, -1.5f, properties));



    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
