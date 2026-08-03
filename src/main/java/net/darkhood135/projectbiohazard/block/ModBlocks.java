package net.darkhood135.projectbiohazard.block;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.custom.FleshBlock;
import net.darkhood135.projectbiohazard.block.custom.TypewriterBlock;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ProjectBiohazard.MOD_ID);

    // Stone Tiles
    public static final DeferredBlock<Block> STONE_PANELS = registerBlock("stone_panels",
            properties -> new Block(properties
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
            ));
    public static final DeferredBlock<Block> STONE_PANEL_STAIRS = registerBlock("stone_panel_stairs",
            properties -> new StairBlock(ModBlocks.STONE_PANELS.get().defaultBlockState(), properties
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> STONE_PANEL_SLAB = registerBlock("stone_panel_slab",
            properties -> new SlabBlock(properties
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> MOSSY_STONE_PANELS = registerBlock("mossy_stone_panels",
            properties -> new Block(properties
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
            ));
    public static final DeferredBlock<Block> MOSSY_STONE_PANEL_STAIRS = registerBlock("mossy_stone_panel_stairs",
            properties -> new StairBlock(ModBlocks.MOSSY_STONE_PANELS.get().defaultBlockState(), properties
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.5F, 6.0F)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> MOSSY_STONE_PANEL_SLAB = registerBlock("mossy_stone_panel_slab",
            properties -> new SlabBlock(properties
                    .sound(SoundType.STONE)
                    .strength(3f)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> DEEPSLATE_PANELS = registerBlock("deepslate_panels",
            properties -> new Block(properties
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE)
            ));
    public static final DeferredBlock<Block> DEEPSLATE_PANEL_STAIRS = registerBlock("deepslate_panel_stairs",
            properties -> new StairBlock(ModBlocks.STONE_PANELS.get().defaultBlockState(), properties
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> DEEPSLATE_PANEL_SLAB = registerBlock("deepslate_panel_slab",
            properties -> new SlabBlock(properties
                    .mapColor(MapColor.DEEPSLATE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.DEEPSLATE)
                    .isValidSpawn(Blocks::never)
            ));

    // Plaster
    public static final DeferredBlock<Block> PLASTER = registerBlock("plaster",
            properties -> new Block(properties
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> WEATHERED_PLASTER = registerBlock("weathered_plaster",
            properties -> new Block(properties
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> DRIPPING_PLASTER = registerBlock("dripping_plaster",
            properties -> new Block(properties
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> EXPOSED_PLASTER = registerBlock("exposed_plaster",
            properties -> new Block(properties
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> CRACKED_PLASTER = registerBlock("cracked_plaster",
            properties -> new Block(properties
                    .mapColor(MapColor.SAND)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));

    // Epoxy
    public static final DeferredBlock<Block> EPOXY_BLOCK = registerBlock("epoxy_block",
            properties -> new TransparentBlock(properties
                    .strength(1.8F, 1.8f)
                    .sound(SoundType.STONE)
                    .mapColor(MapColor.COLOR_CYAN)
                    .noOcclusion()
                    .isViewBlocking((state, level, pos) -> false)
            ));

    // Cleanroom Panels
    public static final DeferredBlock<Block> CLEANROOM_PANEL = registerBlock("cleanroom_panel",
            properties -> new Block(properties
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> BOUND_CLEANROOM_PANEL = registerBlock("bound_cleanroom_panel",
            properties -> new Block(properties
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> ACCENTED_CLEANROOM_PANEL = registerBlock("accented_cleanroom_panel",
            properties -> new Block(properties
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));


    // Weathered Bricks
    public static final DeferredBlock<Block> WEATHERED_BRICKS = registerBlock("weathered_bricks",
            properties -> new Block(properties
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
            ));
    public static final DeferredBlock<Block> WEATHERED_BRICK_STAIRS = registerBlock("weathered_brick_stairs",
            properties -> new StairBlock(ModBlocks.WEATHERED_BRICKS.get().defaultBlockState(), properties
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> WEATHERED_BRICK_SLAB = registerBlock("weathered_brick_slab",
            properties -> new SlabBlock(properties
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> WEATHERED_BRICK_WALL = registerBlock("weathered_brick_wall",
            properties -> new WallBlock(properties
                    .mapColor(MapColor.COLOR_RED)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .requiresCorrectToolForDrops()
                    .strength(2.0F, 6.0F)
                    .isValidSpawn(Blocks::never)
                    .forceSolidOn()
            ));

    // Beech Wood
    public static final DeferredBlock<Block> BEECH_PLANKS = registerBlock("beech_planks",
            properties -> new Block(properties
                    .sound(SoundType.WOOD)
                    .ignitedByLava()
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
    ));
    public static final DeferredBlock<Block> BEECH_STAIRS = registerBlock("beech_stairs",
            properties -> new StairBlock(ModBlocks.BEECH_PLANKS.get().defaultBlockState(), properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_SLAB = registerBlock("beech_slab",
            properties -> new SlabBlock(properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_PRESSURE_PLATE = registerBlock("beech_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .forceSolidOn()
                    .noCollision()
                    .pushReaction(PushReaction.DESTROY)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_BUTTON = registerBlock("beech_button",
            properties -> new ButtonBlock(BlockSetType.OAK, 20, properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .forceSolidOn()
                    .noCollision()
                    .pushReaction(PushReaction.DESTROY)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_FENCE = registerBlock("beech_fence",
            properties -> new FenceBlock(properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_FENCE_GATE = registerBlock("beech_fence_gate",
            properties -> new FenceGateBlock(WoodType.OAK, properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_DOOR = registerBlock("beech_door",
            properties -> new DoorBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> BEECH_TRAPDOOR = registerBlock("beech_trapdoor",
            properties -> new TrapDoorBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .mapColor(MapColor.WARPED_NYLIUM)
                    .instrument(NoteBlockInstrument.BASS)
                    .strength(2.0F, 3.0F)
                    .noOcclusion()
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));

    public static final DeferredBlock<Block> TRONA_ORE = registerBlock("trona_ore",
            properties -> new DropExperienceBlock(UniformInt.of(2,4), properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .strength(3f)
            ));
    public static final DeferredBlock<Block> SANDSTONE_TRONA_ORE = registerBlock("sandstone_trona_ore",
            properties -> new DropExperienceBlock(UniformInt.of(2,4), properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .strength(3f)
            ));
    public static final DeferredBlock<Block> RED_SANDSTONE_TRONA_ORE = registerBlock("red_sandstone_trona_ore",
            properties -> new DropExperienceBlock(UniformInt.of(2,4), properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .strength(3f)
            ));
    public static final DeferredBlock<Block> TRONA_BLOCK = registerBlock("trona_block",
            properties -> new Block(properties
                    .sound(SoundType.DECORATED_POT)
                    .strength(1f)
            ));

    public static final DeferredBlock<Block> BAUXITE_ORE = registerBlock("bauxite_ore",
            properties -> new Block(properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.STONE)
                    .strength(3f)
            ));

    public static final DeferredBlock<Block> DEEPSLATE_BAUXITE_ORE = registerBlock("deepslate_bauxite_ore",
            properties -> new Block(properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.DEEPSLATE)
                    .strength(4.5f)
            ));

    public static final DeferredBlock<Block> DEACTIVATED_REDSTONE_BLOCK = registerBlock("deactivated_redstone_block",
            properties -> new Block(properties
                    .requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .strength(5f)
            ));

    public static final DeferredBlock<Block> FLESH_BLOCK = registerBlock("flesh_block",
            properties -> new FleshBlock(properties
                    .sound(SoundType.MUD)
                    .strength(1f)
            ));

    public static final DeferredBlock<Block> DIRTY_GLASS = registerBlock("dirty_glass",
            properties -> new TransparentBlock(properties
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
            ));
    public static final DeferredBlock<Block> DIRTY_GLASS_PANE = registerBlock("dirty_glass_pane",
            properties -> new IronBarsBlock(properties
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(0.3f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
            ));

    public static final DeferredBlock<Block> BOROSILICATE_GLASS = registerBlock("borosilicate_glass",
            properties -> new TransparentBlock(properties
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(5F, 10f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
                    .isValidSpawn(Blocks::never)
                    .isRedstoneConductor((state, level, pos) -> false)
                    .isSuffocating((state, level, pos) -> false)
                    .isViewBlocking((state, level, pos) -> false)
            ));
    public static final DeferredBlock<Block> BOROSILICATE_GLASS_PANE = registerBlock("borosilicate_glass_pane",
            properties -> new IronBarsBlock(properties
                    .instrument(NoteBlockInstrument.HAT)
                    .strength(5F, 10f)
                    .sound(SoundType.GLASS)
                    .noOcclusion()
            ));

    // Typewriter
    public static final DeferredBlock<Block> TYPEWRITER = registerBlock("typewriter",
            properties -> new TypewriterBlock(properties
                    .sound(SoundType.METAL)
                    .strength(1.5f)
                    .pushReaction(PushReaction.DESTROY)
            ));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    public static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerItem(name, properties -> new BlockItem(block.get(), properties.useBlockDescriptionPrefix()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
