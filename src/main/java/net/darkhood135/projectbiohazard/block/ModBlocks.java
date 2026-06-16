package net.darkhood135.projectbiohazard.block;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.custom.FleshBlock;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ProjectBiohazard.MOD_ID);

    // Undead Wood
    //CHANGE THIS BELOW WHEN POSSIBLE
    public static final DeferredBlock<Block> UNDEAD_PLANKS = registerBlock("undead_planks",
            properties -> new Block(properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .ignitedByLava()
    ));
    public static final DeferredBlock<Block> UNDEAD_STAIRS = registerBlock("undead_stairs",
            properties -> new StairBlock(ModBlocks.UNDEAD_PLANKS.get().defaultBlockState(), properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_SLAB = registerBlock("undead_slab",
            properties -> new SlabBlock(properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_PRESSURE_PLATE = registerBlock("undead_pressure_plate",
            properties -> new PressurePlateBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .forceSolidOn()
                    .noCollision()
                    .pushReaction(PushReaction.DESTROY)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_BUTTON = registerBlock("undead_button",
            properties -> new ButtonBlock(BlockSetType.OAK, 20, properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .forceSolidOn()
                    .noCollision()
                    .pushReaction(PushReaction.DESTROY)
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_FENCE = registerBlock("undead_fence",
            properties -> new FenceBlock(properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_FENCE_GATE = registerBlock("undead_fence_gate",
            properties -> new FenceGateBlock(WoodType.OAK, properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_DOOR = registerBlock("undead_door",
            properties -> new DoorBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
                    .ignitedByLava()
                    .isValidSpawn(Blocks::never)
            ));
    public static final DeferredBlock<Block> UNDEAD_TRAPDOOR = registerBlock("undead_trapdoor",
            properties -> new TrapDoorBlock(BlockSetType.OAK, properties
                    .sound(SoundType.WOOD)
                    .strength(1.5f)
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
