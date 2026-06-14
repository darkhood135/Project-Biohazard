package net.darkhood135.projectbiohazard.datagen;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {

    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        List<ItemLike> TRONA_SMELTABLES = List.of(
                ModBlocks.TRONA_ORE
        );
        oreSmelting(TRONA_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.TRONA.get(), 0.25f, 200, "trona");
        oreBlasting(TRONA_SMELTABLES, RecipeCategory.MISC, CookingBookCategory.BLOCKS, ModItems.TRONA.get(), 0.25f, 100, "trona");

        shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.TRONA_BLOCK.get())
                .pattern("ZZZ")
                .pattern("ZZZ")
                .pattern("ZZZ")
                .define('Z', ModItems.TRONA.get())
                .unlockedBy(getHasName(ModItems.TRONA.get()), has(ModItems.TRONA.get()))
                .save(output);
        ;

        shapeless(RecipeCategory.MISC, ModItems.TRONA.get(), 9)
                .requires(ModBlocks.TRONA_BLOCK)
                .unlockedBy(getHasName(ModBlocks.TRONA_BLOCK.get()), has(ModBlocks.TRONA_BLOCK.get()))
                .save(output, ProjectBiohazard.MOD_ID + ":" + "trona_from_block");
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
