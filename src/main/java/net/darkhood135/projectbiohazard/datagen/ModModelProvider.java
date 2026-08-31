package net.darkhood135.projectbiohazard.datagen;

import com.mojang.math.Quadrant;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.ModBlocks;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ProjectBiohazard.MOD_ID);
    }

    @Override
    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
        return super.getKnownItems()
                .filter(holder -> holder.value() != ModItems.GLASS_VIAL.get())
                .filter(holder -> holder.value() != ModItems.WATER_VIAL.get())
                .filter(holder -> holder.value() != ModItems.SYRINGE.get())
                .filter(holder -> holder.value() != ModItems.EMF_VISUALIZER.get())
                .filter(holder -> holder.value() != ModBlocks.TYPEWRITER.asItem())
                .filter(holder -> holder.value() != ModItems.FIRST_AID_SPRAY.get());

    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        // Items
        itemModels.generateFlatItem(ModItems.TRONA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_BADGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.UMBRELLA_INSIGNIA.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.GREEN_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.RED_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BLUE_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.YELLOW_HERB.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BORON_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SODA_ASH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.BAUXITE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_INGOT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HONEY_VIAL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.SECURE_MUSIC_DISC.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.EMPTY_SYRINGE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.SPADE_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.CLUB_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_KEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ModItems.HEART_KEY.get(), ModelTemplates.FLAT_ITEM);


        // Materials
        itemModels.generateFlatItem(ModItems.INK_RIBBON.get(), ModelTemplates.FLAT_ITEM);

        // Aluminum Tools
        itemModels.generateFlatItem(ModItems.ALUMINUM_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.ALUMINUM_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Hatchets
        itemModels.generateFlatItem(ModItems.ALUMINUM_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.WOODEN_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.STONE_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.IRON_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.COPPER_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.GOLDEN_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.DIAMOND_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(ModItems.NETHERITE_HATCHET.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Spawn Eggs
        itemModels.generateFlatItem(ModItems.T_VIRUS_ZOMBIE_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);

        // Blocks
        blockModels.createTrivialCube(ModBlocks.TRONA_ORE.get());
        blockModels.createTrivialCube(ModBlocks.BAUXITE_ORE.get());
        blockModels.createTrivialCube(ModBlocks.DEEPSLATE_BAUXITE_ORE.get());
        blockModels.createTrivialBlock(
                ModBlocks.SANDSTONE_TRONA_ORE.get(),
                TexturedModel.createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP)
        );
        blockModels.createTrivialBlock(
                ModBlocks.RED_SANDSTONE_TRONA_ORE.get(),
                TexturedModel.createDefault(TextureMapping::cubeBottomTop, ModelTemplates.CUBE_BOTTOM_TOP)
        );
        blockModels.createTrivialCube(ModBlocks.TRONA_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.DEACTIVATED_REDSTONE_BLOCK.get());
        blockModels.createTrivialCube(ModBlocks.FLESH_BLOCK.get());

        blockModels.createGlassBlocks(ModBlocks.BOROSILICATE_GLASS.get(), ModBlocks.BOROSILICATE_GLASS_PANE.get());
        blockModels.createGlassBlocks(ModBlocks.DIRTY_GLASS.get(), ModBlocks.DIRTY_GLASS_PANE.get());

        blockModels.createTrivialCube(ModBlocks.EPOXY_BLOCK.get());

        blockModels.family(ModBlocks.STONE_PANELS.get())
                .stairs(ModBlocks.STONE_PANEL_STAIRS.get())
                .slab(ModBlocks.STONE_PANEL_SLAB.get());

        blockModels.family(ModBlocks.WEATHERED_BRICKS.get())
                .stairs(ModBlocks.WEATHERED_BRICK_STAIRS.get())
                .slab(ModBlocks.WEATHERED_BRICK_SLAB.get())
                .wall(ModBlocks.WEATHERED_BRICK_WALL.get());

        blockModels.family(ModBlocks.MOSSY_STONE_PANELS.get())
                .stairs(ModBlocks.MOSSY_STONE_PANEL_STAIRS.get())
                .slab(ModBlocks.MOSSY_STONE_PANEL_SLAB.get());

        blockModels.family(ModBlocks.DEEPSLATE_PANELS.get())
                .stairs(ModBlocks.DEEPSLATE_PANEL_STAIRS.get())
                .slab(ModBlocks.DEEPSLATE_PANEL_SLAB.get());

        // Plaster
        List<Variant> plasterVariants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Identifier modelLoc = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/plaster" + i);
            Material tex = TextureMapping.getBlockTexture(ModBlocks.PLASTER.get(), String.valueOf(i)); // block/plaster<i>
            ModelTemplates.CUBE_ALL.create(modelLoc, TextureMapping.cube(tex), blockModels.modelOutput);
            Variant base = BlockModelGenerators.plainModel(modelLoc);
            for (Quadrant q : Quadrant.values()) {           // R0, R90, R180, R270
                plasterVariants.add(base.withYRot(q));
            }
        }
        blockModels.registerSimpleItemModel(ModBlocks.PLASTER.get(),
                Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/plaster0"));
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.PLASTER.get(),
                        BlockModelGenerators.variants(plasterVariants.toArray(new Variant[0]))));

        // Weathered Plaster
        List<Variant> weatheredPlasterVariants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Identifier modelLoc = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/weathered_plaster" + i);
            TextureMapping tex = new TextureMapping()
                    .put(TextureSlot.SIDE,   TextureMapping.getBlockTexture(ModBlocks.WEATHERED_PLASTER.get(), String.valueOf(i))) // weathered_plaster<i>
                    .put(TextureSlot.TOP,    TextureMapping.getBlockTexture(ModBlocks.PLASTER.get(), "0"))                         // plaster0 on top
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.WEATHERED_PLASTER.get(), "_bottom"));        // weathered_plaster_bottom
            ModelTemplates.CUBE_BOTTOM_TOP.create(modelLoc, tex, blockModels.modelOutput);
            weatheredPlasterVariants.add(BlockModelGenerators.plainModel(modelLoc));
            Variant base = BlockModelGenerators.plainModel(modelLoc);
            weatheredPlasterVariants.add(base);
        }
        blockModels.registerSimpleItemModel(ModBlocks.WEATHERED_PLASTER.get(),
                Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/weathered_plaster0"));
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.WEATHERED_PLASTER.get(),
                        BlockModelGenerators.variants(weatheredPlasterVariants.toArray(new Variant[0]))));

        // Dripping Plaster
        List<Variant> drippingPlasterVariants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Identifier modelLoc = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/dripping_plaster" + i);
            TextureMapping tex = new TextureMapping()
                    .put(TextureSlot.SIDE,   TextureMapping.getBlockTexture(ModBlocks.DRIPPING_PLASTER.get(), String.valueOf(i))) // dripping_plaster<i>
                    .put(TextureSlot.TOP,    TextureMapping.getBlockTexture(ModBlocks.WEATHERED_PLASTER.get(), "_bottom"))                         // weathered_plaster_bottom but on top
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.PLASTER.get(), "0"));        // plaster0 on bottom
            ModelTemplates.CUBE_BOTTOM_TOP.create(modelLoc, tex, blockModels.modelOutput);
            drippingPlasterVariants.add(BlockModelGenerators.plainModel(modelLoc));
            Variant base = BlockModelGenerators.plainModel(modelLoc);
            drippingPlasterVariants.add(base);
        }
        blockModels.registerSimpleItemModel(ModBlocks.DRIPPING_PLASTER.get(),
                Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/dripping_plaster0"));
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.DRIPPING_PLASTER.get(),
                        BlockModelGenerators.variants(drippingPlasterVariants.toArray(new Variant[0]))));

        // Exposed Plaster
        List<Variant> exposedPlasterVariants = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Identifier modelLoc = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/exposed_plaster" + i);
            TextureMapping tex = new TextureMapping()
                    .put(TextureSlot.SIDE,   TextureMapping.getBlockTexture(ModBlocks.EXPOSED_PLASTER.get(), String.valueOf(i))) // exposed_plaster<i>
                    .put(TextureSlot.TOP,    TextureMapping.getBlockTexture(ModBlocks.PLASTER.get(), "0"))                         // plaster0 on top
                    .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.PLASTER.get(), "0"));        // plaster0 on bottom
            ModelTemplates.CUBE_BOTTOM_TOP.create(modelLoc, tex, blockModels.modelOutput);
            exposedPlasterVariants.add(BlockModelGenerators.plainModel(modelLoc));
            Variant base = BlockModelGenerators.plainModel(modelLoc);
            exposedPlasterVariants.add(base);
        }
        blockModels.registerSimpleItemModel(ModBlocks.EXPOSED_PLASTER.get(),
                Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/exposed_plaster0"));
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.EXPOSED_PLASTER.get(),
                        BlockModelGenerators.variants(exposedPlasterVariants.toArray(new Variant[0]))));

        // Cracked Plaster
        List<Variant> crackedPlasterVariants = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Identifier modelLoc = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/cracked_plaster" + i);
            Material tex = TextureMapping.getBlockTexture(ModBlocks.CRACKED_PLASTER.get(), String.valueOf(i)); // block/plaster<i>
            ModelTemplates.CUBE_ALL.create(modelLoc, TextureMapping.cube(tex), blockModels.modelOutput);
            Variant base = BlockModelGenerators.plainModel(modelLoc);
            for (Quadrant q : Quadrant.values()) {           // R0, R90, R180, R270
                crackedPlasterVariants.add(base.withYRot(q));
            }
        }
        blockModels.registerSimpleItemModel(ModBlocks.CRACKED_PLASTER.get(),
                Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/cracked_plaster0"));
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.CRACKED_PLASTER.get(),
                        BlockModelGenerators.variants(crackedPlasterVariants.toArray(new Variant[0]))));

        // Cleanroom Panel
        blockModels.createTrivialCube(ModBlocks.CLEANROOM_PANEL.get());

        // Accented Cleanroom Panel
        ModelTemplates.CUBE_COLUMN.create(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/accented_cleanroom_panel"),
                TextureMapping.column(
                        TextureMapping.getBlockTexture(ModBlocks.ACCENTED_CLEANROOM_PANEL.get()),
                        TextureMapping.getBlockTexture(ModBlocks.CLEANROOM_PANEL.get())), // reused base: top + bottom
                blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.ACCENTED_CLEANROOM_PANEL.get(),
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/accented_cleanroom_panel"))));
        blockModels.registerSimpleItemModel(ModBlocks.ACCENTED_CLEANROOM_PANEL.get(), Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/accented_cleanroom_panel"));

        // Bound Cleanroom Panel
        ModelTemplates.CUBE_COLUMN.create(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/bound_cleanroom_panel"),
                TextureMapping.column(
                        TextureMapping.getBlockTexture(ModBlocks.BOUND_CLEANROOM_PANEL.get()),
                        TextureMapping.getBlockTexture(ModBlocks.CLEANROOM_PANEL.get())), // reused base: top + bottom
                blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.BOUND_CLEANROOM_PANEL.get(),
                        BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/bound_cleanroom_panel"))));
        blockModels.registerSimpleItemModel(ModBlocks.BOUND_CLEANROOM_PANEL.get(), Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/bound_cleanroom_panel"));

        // Loot Barrel
        Identifier lootBarrelModel = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/loot_barrel");
        TextureMapping lootBarrelTex = new TextureMapping()
                .put(TextureSlot.SIDE,   TextureMapping.getBlockTexture(ModBlocks.LOOT_BARREL.get(), "_side"))   // block/loot_barrel_side
                .put(TextureSlot.TOP,    TextureMapping.getBlockTexture(ModBlocks.LOOT_BARREL.get(), "_top"))    // block/loot_barrel_top
                .put(TextureSlot.BOTTOM, new Material(Identifier.fromNamespaceAndPath("minecraft", "block/barrel_bottom"))); // vanilla barrel bottom
        ModelTemplates.CUBE_BOTTOM_TOP.create(lootBarrelModel, lootBarrelTex, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                BlockModelGenerators.createSimpleBlock(ModBlocks.LOOT_BARREL.get(),
                        BlockModelGenerators.plainVariant(lootBarrelModel)));
        blockModels.registerSimpleItemModel(ModBlocks.LOOT_BARREL.get(), lootBarrelModel);

        blockModels.family(ModBlocks.BEECH_PLANKS.get())
                .stairs(ModBlocks.BEECH_STAIRS.get())
                .slab(ModBlocks.BEECH_SLAB.get())
                .button(ModBlocks.BEECH_BUTTON.get())
                .pressurePlate(ModBlocks.BEECH_PRESSURE_PLATE.get())
                .fence(ModBlocks.BEECH_FENCE.get())
                .fenceGate(ModBlocks.BEECH_FENCE_GATE.get())
                .door(ModBlocks.BEECH_DOOR.get())
                .trapdoor(ModBlocks.BEECH_TRAPDOOR.get());

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(ModBlocks.TYPEWRITER.get(),
                BlockModelGenerators.plainVariant(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, "block/typewriter")))
                .with(BlockModelGenerators.ROTATION_HORIZONTAL_FACING));
    }
}
