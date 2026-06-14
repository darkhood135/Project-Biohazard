package net.darkhood135.projectbiohazard.block;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.block.custom.FleshBlock;
import net.darkhood135.projectbiohazard.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ProjectBiohazard.MOD_ID);

    public static final DeferredBlock<Block> TRONA_ORE = registerBlock("trona_ore",
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
