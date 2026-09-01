package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.VialItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.crafting.DataComponentIngredient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    // Herb Crafting Generation

    private static boolean isValidHerb(List<VialItem.Herb> mix) {
        for(VialItem.Herb herb : mix) {
            if (herb.isUnique() && Collections.frequency(mix, herb) > 1) {
                return false;
            }
        }
        return true;
    }

    public static List<List<VialItem.Herb>> allHerbMixtures() {
        List<List<VialItem.Herb>> result = new ArrayList<>();
        VialItem.Herb[] herbs = VialItem.Herb.values();
        // 2 Herbs
        for (int a = 0; a < herbs.length; a++) {
            for (int b = a; b < herbs.length; b++) {
                List<VialItem.Herb> mix = List.of(herbs[a], herbs[b]);
                if (isValidHerb(mix)) result.add(mix);
            }
        }
        // 3 Herbs
        for (int a = 0; a < herbs.length; a++) {
            for (int b = a; b < herbs.length; b++) {
                for (int c = b; c < herbs.length; c++) {
                    List<VialItem.Herb> mix = List.of(herbs[a], herbs[b], herbs[c]);
                    if (isValidHerb(mix)) result.add(mix);
                }
            }
        }
        return result;
    }

    private static ItemStackTemplate vialTemplate(String key) {
        DataComponentPatch patch = DataComponentPatch.builder()
                .set(ModDataComponentTypes.HERB_VIAL_COMBINATION.get(), key)
                .set(DataComponents.CUSTOM_MODEL_DATA,
                        new CustomModelData(List.of(), List.of(), List.of(key), List.of()))
                .build();
        return new ItemStackTemplate(ModItems.GLASS_VIAL.get(), patch);
    }

    private static ItemLike herbItem(VialItem.Herb herb) {
        return switch (herb) {
            case GREEN  -> ModItems.GREEN_HERB.get();
            case RED    -> ModItems.RED_HERB.get();
            case YELLOW -> ModItems.YELLOW_HERB.get();
            case BLUE   -> ModItems.BLUE_HERB.get();
        };
    }

    @Override
    protected void buildRecipes() {
        // Syringes
        this.registries.lookupOrThrow(Registries.POTION).listElements().forEach(potion -> {
            PotionContents contents = new PotionContents(potion);   // single-potion contents (verify ctor)

            Ingredient vialIn = DataComponentIngredient.of(false,
                    DataComponents.POTION_CONTENTS, contents, ModItems.WATER_VIAL.get());

            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(DataComponents.POTION_CONTENTS, contents)
                    .build();
            ItemStackTemplate result = new ItemStackTemplate(ModItems.SYRINGE.get(), patch);

            shapeless(RecipeCategory.MISC, result)
                    .requires(vialIn)
                    .requires(ModItems.EMPTY_SYRINGE.get())
                    .unlockedBy("has_syringe", has(ModItems.EMPTY_SYRINGE.get()))
                    .save(output, ProjectBiohazard.MOD_ID + ":syringe_" + potion.getRegisteredName().replace(':', '_'));
        });

        // Herbs
        for (List<VialItem.Herb> mix : allHerbMixtures()) {
            String key = VialItem.Herb.toKey(mix);
            DataComponentPatch patch = DataComponentPatch.builder()
                    .set(ModDataComponentTypes.HERB_VIAL_COMBINATION.get(), key)
                    .set(DataComponents.CUSTOM_MODEL_DATA,
                            new CustomModelData(List.of(), List.of(), List.of(key), List.of()))
                    .build();
            ItemStackTemplate result = new ItemStackTemplate(ModItems.GLASS_VIAL.get(), patch);

            ShapelessRecipeBuilder builder = shapeless(RecipeCategory.MISC, result)
                    .requires(ModItems.GLASS_VIAL.get())
                    .unlockedBy("has_glass_vial", has(ModItems.GLASS_VIAL.get()));
            for (VialItem.Herb herb : mix) {
                builder.requires(herbItem(herb));
            }
            builder.save(output, ProjectBiohazard.MOD_ID + ":vial_" + key);
        }
        for (List<VialItem.Herb> mix : allHerbMixtures()) {
            if (mix.size() != 3) continue;                 // incremental only makes 3-herb from 2-herb
            String finalKey = VialItem.Herb.toKey(mix);

            for (VialItem.Herb added : new LinkedHashSet<>(mix)) {   // distinct herbs only
                List<VialItem.Herb> pred = new ArrayList<>(mix);
                pred.remove(added);                          // removes ONE occurrence -> 2-herb base
                String predKey = VialItem.Herb.toKey(pred);

                Ingredient vialIn = DataComponentIngredient.of(false,
                        ModDataComponentTypes.HERB_VIAL_COMBINATION.get(), predKey,
                        ModItems.GLASS_VIAL.get());

                shapeless(RecipeCategory.MISC, vialTemplate(finalKey))
                        .requires(vialIn)
                        .requires(herbItem(added))
                        .unlockedBy("has_glass_vial", has(ModItems.GLASS_VIAL.get()))
                        .save(output, ProjectBiohazard.MOD_ID + ":vial_" + finalKey + "_from_" + predKey);
            }
        }

        // Ores
        List<ItemLike> TRONA_SMELTABLES = List.of(
                ModBlocks.TRONA_ORE,
                ModBlocks.SANDSTONE_TRONA_ORE,
                ModBlocks.RED_SANDSTONE_TRONA_ORE
        );
        List<ItemLike> BAUXITE_SMELTABLES = List.of(
                ModBlocks.BAUXITE_ORE,
                ModBlocks.DEEPSLATE_BAUXITE_ORE
        );

        oreSmelting(TRONA_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.TRONA.get(), 0.25f, 200, "trona");
        oreBlasting(TRONA_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.TRONA.get(), 0.25f, 100, "trona");

        oreSmelting(BAUXITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.BAUXITE.get(), 0.25f, 200, "bauxite");
        oreBlasting(BAUXITE_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.BAUXITE.get(), 0.25f, 100, "bauxite");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.TRONA), RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.SODA_ASH.get(), 0.25f, 200)
                .unlockedBy(getHasName(ModItems.TRONA.get()), has(ModItems.TRONA.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":soda_ash_from_cooking");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.TRONA), RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.SODA_ASH.get(), 0.25f, 100)
                .unlockedBy(getHasName(ModItems.TRONA.get()), has(ModItems.TRONA.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":soda_ash_from_blasting");

        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.BAUXITE), RecipeCategory.MISC, CookingBookCategory.MISC, ModItems.ALUMINUM_INGOT.get(), 0.25f, 200)
                .unlockedBy(getHasName(ModItems.BAUXITE.get()), has(ModItems.BAUXITE.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":aluminum_ingot_from_cooking");
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ModItems.BAUXITE), RecipeCategory.MISC, CookingBookCategory.MISC, ModItems.ALUMINUM_INGOT.get(), 0.25f, 100)
                .unlockedBy(getHasName(ModItems.BAUXITE.get()), has(ModItems.BAUXITE.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":aluminum_ingot_from_blasting");

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TRONA_BLOCK.get())
                .pattern("ZZZ")
                .pattern("ZZZ")
                .pattern("ZZZ")
                .define('Z', ModItems.TRONA.get())
                .unlockedBy(getHasName(ModItems.TRONA.get()), has(ModItems.TRONA.get()))
                .save(output);

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BOROSILICATE_GLASS.get(), 4)
                .pattern("XZX")
                .pattern("FYJ")
                .pattern("XZX")
                .define('X', Items.GLASS)
                .define('Y', ModItems.ALUMINUM_INGOT)
                .define('Z', Items.QUARTZ)
                .define('J', ModItems.BORON_SHARD)
                .define('F', ModItems.SODA_ASH)
                .unlockedBy(getHasName(ModItems.BORON_SHARD.get()), has(ModItems.BORON_SHARD.get()))
                .save(output);

        // First Aid Spray
        shaped(RecipeCategory.TOOLS, ModItems.FIRST_AID_SPRAY.get(), 1)
                .pattern("XZX")
                .pattern("ZYZ")
                .pattern("XXX")
                .define('X', ModItems.ALUMINUM_INGOT)
                .define('Y', Items.REDSTONE)
                .define('Z', ModItems.GREEN_HERB)
                .unlockedBy(getHasName(ModItems.GREEN_HERB.get()), has(ModItems.GREEN_HERB.get()))
                .save(output);

        // Stone Tiles
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANELS, Blocks.STONE);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANEL_SLAB, Blocks.STONE, 2);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANEL_STAIRS, Blocks.STONE);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANEL_SLAB, ModBlocks.STONE_PANELS, 2);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANEL_STAIRS, ModBlocks.STONE_PANELS);
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_STONE_PANELS)
                .requires(ModBlocks.STONE_PANELS)
                .requires(Blocks.VINE)
                .unlockedBy(getHasName(Blocks.VINE), has(Blocks.VINE))
                .save(output);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_STONE_PANEL_SLAB, ModBlocks.MOSSY_STONE_PANELS, 2);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_STONE_PANEL_STAIRS, ModBlocks.MOSSY_STONE_PANELS);
        stairBuilder(ModBlocks.STONE_PANEL_STAIRS.get(), Ingredient.of(ModBlocks.STONE_PANELS))
                .group("stone_tiles")
                .unlockedBy(getHasName(ModBlocks.STONE_PANELS.get()), has(ModBlocks.STONE_PANELS.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_PANEL_SLAB.get(), ModBlocks.STONE_PANELS.get());
        stairBuilder(ModBlocks.MOSSY_STONE_PANEL_STAIRS.get(), Ingredient.of(ModBlocks.MOSSY_STONE_PANELS))
                .group("mossy_stone_tiles")
                .unlockedBy(getHasName(ModBlocks.MOSSY_STONE_PANELS.get()), has(ModBlocks.MOSSY_STONE_PANELS.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_STONE_PANEL_SLAB.get(), ModBlocks.MOSSY_STONE_PANELS.get());

        // Weathered Bricks
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_BRICKS)
                .requires(Blocks.BRICKS)
                .requires(Blocks.GRAVEL)
                .unlockedBy(getHasName(Blocks.BRICKS), has(Blocks.BRICKS))
                .save(output);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_BRICK_SLAB, ModBlocks.WEATHERED_BRICKS);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_BRICK_STAIRS, ModBlocks.WEATHERED_BRICKS);
        stonecutterResultFromBase(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_BRICK_WALL, ModBlocks.WEATHERED_BRICKS);
        stairBuilder(ModBlocks.WEATHERED_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.WEATHERED_BRICKS))
                .group("weathered_bricks")
                .unlockedBy(getHasName(ModBlocks.WEATHERED_BRICKS.get()), has(ModBlocks.WEATHERED_BRICKS.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WEATHERED_BRICK_SLAB.get(), ModBlocks.WEATHERED_BRICKS.get());

        shapeless(RecipeCategory.MISC, ModItems.TRONA.get(), 9)
                .requires(ModBlocks.TRONA_BLOCK)
                .unlockedBy(getHasName(ModBlocks.TRONA_BLOCK.get()), has(ModBlocks.TRONA_BLOCK.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":" + "trona_from_block");

        // Beech Wood
        stairBuilder(ModBlocks.BEECH_STAIRS.get(), Ingredient.of(ModBlocks.BEECH_PLANKS))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BEECH_SLAB.get(), ModBlocks.BEECH_PLANKS.get());
        buttonBuilder(ModBlocks.BEECH_BUTTON.get(), Ingredient.of(ModBlocks.BEECH_PLANKS.get()))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);
        pressurePlate(ModBlocks.BEECH_PRESSURE_PLATE.get(), ModBlocks.BEECH_PLANKS.get());
        fenceBuilder(ModBlocks.BEECH_FENCE.get(), Ingredient.of(ModBlocks.BEECH_PLANKS.get()))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);
        fenceGateBuilder(ModBlocks.BEECH_FENCE_GATE.get(), Ingredient.of(ModBlocks.BEECH_PLANKS.get()))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);
        doorBuilder(ModBlocks.BEECH_DOOR.get(), Ingredient.of(ModBlocks.BEECH_PLANKS.get()))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);
        trapdoorBuilder(ModBlocks.BEECH_TRAPDOOR.get(), Ingredient.of(ModBlocks.BEECH_PLANKS.get()))
                .group("beech")
                .unlockedBy(getHasName(ModBlocks.BEECH_PLANKS.get()), has(ModBlocks.BEECH_PLANKS.get()))
                .save(output);

        // Dirty Glass
        shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIRTY_GLASS)
                .requires(Blocks.GLASS)
                .requires(Blocks.GRAVEL)
                .unlockedBy(getHasName(Blocks.GLASS), has(Blocks.GLASS))
                .save(output);
        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIRTY_GLASS_PANE.get(), ModBlocks.DIRTY_GLASS.get());

        // Borosilicate Glass Recipes
        shaped(RecipeCategory.BREWING, ModItems.GLASS_VIAL.get(), 4)
                .pattern("   ")
                .pattern("Z Z")
                .pattern(" Z ")
                .define('Z', ModBlocks.BOROSILICATE_GLASS)
                .unlockedBy(getHasName(ModItems.BORON_SHARD.get()), has(ModItems.BORON_SHARD.get()))
                .save(output);
        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BOROSILICATE_GLASS_PANE.get(), ModBlocks.BOROSILICATE_GLASS.get());
        shaped(RecipeCategory.BREWING, ModItems.EMPTY_SYRINGE.get(), 1)
                .pattern("  Z")
                .pattern(" X ")
                .pattern("Y  ")
                .define('Y', ModItems.ALUMINUM_INGOT)
                .define('Z', Items.IRON_INGOT)
                .define('X', ModItems.GLASS_VIAL)
                .unlockedBy(getHasName(ModItems.GLASS_VIAL.get()), has(ModItems.GLASS_VIAL.get()))
                .save(output);

        // Aluminum Tools
        shaped(RecipeCategory.COMBAT, ModItems.ALUMINUM_SWORD.get())
                .pattern("Z")
                .pattern("Z")
                .pattern("S")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.ALUMINUM_PICKAXE.get())
                .pattern("ZZZ")
                .pattern(" S ")
                .pattern(" S ")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.ALUMINUM_SHOVEL.get())
                .pattern("Z")
                .pattern("S")
                .pattern("S")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.ALUMINUM_AXE.get())
                .pattern("ZZ")
                .pattern("ZS")
                .pattern(" S")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);
        shaped(RecipeCategory.TOOLS, ModItems.ALUMINUM_HOE.get())
                .pattern("ZZ")
                .pattern(" S")
                .pattern(" S")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);

        // Hatchets
        shaped(RecipeCategory.COMBAT, ModItems.ALUMINUM_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.WOODEN_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .group("wooden")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.STONE_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', Blocks.COBBLESTONE)
                .define('S', Items.STICK)
                .group("stone")
                .unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.IRON_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .group("iron")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .group("gold")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .group("copper")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_HATCHET.get())
                .pattern("ZZZ")
                .pattern("ZS ")
                .pattern(" S ")
                .define('Z', Items.DIAMOND)
                .define('S', Items.STICK)
                .group("diamond")
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output);
        shaped(RecipeCategory.COMBAT, ModItems.ALUMINUM_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', ModItems.ALUMINUM_INGOT.get())
                .define('S', Items.STICK)
                .group("aluminum")
                .unlockedBy(getHasName(ModItems.ALUMINUM_INGOT.get()), has(ModItems.ALUMINUM_INGOT.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":aluminum_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.WOODEN_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .group("wooden")
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output, ProjectBiohazard.MOD_ID + ":wooden_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.STONE_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', Blocks.COBBLESTONE)
                .define('S', Items.STICK)
                .group("stone")
                .unlockedBy(getHasName(Blocks.COBBLESTONE), has(Blocks.COBBLESTONE))
                .save(output, ProjectBiohazard.MOD_ID + ":stone_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.IRON_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', Items.IRON_INGOT)
                .define('S', Items.STICK)
                .group("iron")
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output, ProjectBiohazard.MOD_ID + ":iron_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.GOLDEN_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', Items.GOLD_INGOT)
                .define('S', Items.STICK)
                .group("gold")
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(output, ProjectBiohazard.MOD_ID + ":golden_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.COPPER_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', Items.COPPER_INGOT)
                .define('S', Items.STICK)
                .group("copper")
                .unlockedBy(getHasName(Items.COPPER_INGOT), has(Items.COPPER_INGOT))
                .save(output, ProjectBiohazard.MOD_ID + ":copper_hatchet_right");
        shaped(RecipeCategory.COMBAT, ModItems.DIAMOND_HATCHET.get())
                .pattern("ZZZ")
                .pattern(" SZ")
                .pattern(" S ")
                .define('Z', Items.DIAMOND)
                .define('S', Items.STICK)
                .group("diamond")
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(output, ProjectBiohazard.MOD_ID + ":diamond_hatchet_right");
        netheriteSmithing(ModItems.DIAMOND_HATCHET.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_HATCHET.get());

        // Typewriter + Ink Ribbon
        shapeless(RecipeCategory.MISC, ModItems.INK_RIBBON.get())
                .requires(Items.STRING)
                .requires(Items.INK_SAC)
                .requires(ModItems.ALUMINUM_INGOT)
                .requires(Items.GREEN_DYE)
                .unlockedBy(getHasName(Items.INK_SAC), has(Items.INK_SAC))
                .save(output);
        shaped(RecipeCategory.DECORATIONS, ModBlocks.TYPEWRITER.get())
                .pattern("APA")
                .pattern("ETE")
                .pattern("IRI")
                .define('A', ModItems.ALUMINUM_INGOT)
                .define('T', Items.TOTEM_OF_UNDYING)
                .define('E', Items.ECHO_SHARD)
                .define('P', Items.PAPER)
                .define('I', Items.IRON_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.TOTEM_OF_UNDYING), has(Items.TOTEM_OF_UNDYING))
                .save(output);
    }

    // Must overwrite all default methods to ensure that it still belongs to Project Biohazards' mod ID
    @Override
    protected <T extends AbstractCookingRecipe> void oreCooking(AbstractCookingRecipe.Factory<T> factory, List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group, String fromDesc) {
        for(ItemLike item : smeltables) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(item), craftingCategory, cookingCategory, result, experience, cookingTime, factory).group(group).unlockedBy(getHasName(item), this.has(item)).save(this.output, ProjectBiohazard.MOD_ID + ":" + getItemName(result) + fromDesc + "_" + getItemName(item));
        }
    }

    @Override
    protected void oreBlasting(List<ItemLike> smeltables, RecipeCategory craftingCategory, CookingBookCategory cookingCategory, ItemLike result, float experience, int cookingTime, String group) {
        this.oreCooking(BlastingRecipe::new, smeltables, craftingCategory, cookingCategory, result, experience, cookingTime, group, "_from_blasting");
    }

    public static class Runner extends RecipeProvider.Runner {

        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "Project Biohazard Recipes";
        }
    }
}
