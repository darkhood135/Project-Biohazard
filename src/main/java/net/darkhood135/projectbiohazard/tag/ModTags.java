package net.darkhood135.projectbiohazard.tag;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static class Items {

        public static final TagKey<Item> EMF_TOOLS = createTag("emf_tools");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, name));
        }
    }
}
