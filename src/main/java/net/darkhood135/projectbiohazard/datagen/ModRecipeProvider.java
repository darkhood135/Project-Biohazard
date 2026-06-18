package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.component.ModDataComponentTypes;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.darkhood135.projectbiohazard.item.custom.VialItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
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

    private static List<List<VialItem.Herb>> allMixtures() {
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
        // Herbs
        for (List<VialItem.Herb> mix : allMixtures()) {
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

        for (List<VialItem.Herb> mix : allMixtures()) {
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

        shaped(RecipeCategory.BREWING, ModItems.GLASS_VIAL.get(), 4)
                .pattern("   ")
                .pattern("Z Z")
                .pattern(" Z ")
                .define('Z', ModBlocks.BOROSILICATE_GLASS)
                .unlockedBy(getHasName(ModItems.BORON_SHARD.get()), has(ModItems.BORON_SHARD.get()))
                .save(output);

        shapeless(RecipeCategory.MISC, ModItems.TRONA.get(), 9)
                .requires(ModBlocks.TRONA_BLOCK)
                .unlockedBy(getHasName(ModBlocks.TRONA_BLOCK.get()), has(ModBlocks.TRONA_BLOCK.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":" + "trona_from_block");

        stairBuilder(ModBlocks.UNDEAD_STAIRS.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);
        slab(RecipeCategory.BUILDING_BLOCKS, ModBlocks.UNDEAD_SLAB.get(), ModBlocks.UNDEAD_PLANKS.get());
        buttonBuilder(ModBlocks.UNDEAD_BUTTON.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS.get()))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);
        pressurePlate(ModBlocks.UNDEAD_PRESSURE_PLATE.get(), ModBlocks.UNDEAD_PLANKS.get());
        fenceBuilder(ModBlocks.UNDEAD_FENCE.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS.get()))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);
        fenceGateBuilder(ModBlocks.UNDEAD_FENCE_GATE.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS.get()))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);
        doorBuilder(ModBlocks.UNDEAD_DOOR.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS.get()))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);
        trapdoorBuilder(ModBlocks.UNDEAD_TRAPDOOR.get(), Ingredient.of(ModBlocks.UNDEAD_PLANKS.get()))
                .group("undead")
                .unlockedBy(getHasName(ModBlocks.UNDEAD_PLANKS.get()), has(ModBlocks.UNDEAD_PLANKS.get()))
                .save(output);

        wall(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BOROSILICATE_GLASS_PANE.get(), ModBlocks.BOROSILICATE_GLASS.get());

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
