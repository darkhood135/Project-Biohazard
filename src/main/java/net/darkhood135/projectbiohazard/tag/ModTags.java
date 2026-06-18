package net.darkhood135.projectbiohazard.tag;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {

        public static final TagKey<Block> NEEDS_ALUMINUM_TOOL = createTag("needs_aluminum_tool");
        public static final TagKey<Block> INCORRECT_FOR_ALUMINUM_TOOL = createTag("incorrect_for_aluminum_tool");

        public static final TagKey<Block> HATCHET_MINEABLE = createTag("mineable/hatchet");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name));
        }
    }

    public static class Items {

        public static final TagKey<Item> HATCHETS = createTag("hatchets");
        public static final TagKey<Item> EMF_TOOLS = createTag("emf_tools");
        public static final TagKey<Item> ALUMINUM_REPAIRABLES = createTag("aluminum_repairables");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name));
        }
    }
}
