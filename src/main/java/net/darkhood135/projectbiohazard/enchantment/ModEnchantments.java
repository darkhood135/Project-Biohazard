package net.darkhood135.projectbiohazard.enchantment;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.tag.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> MASTER_KEY = registerKey("master_key");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        var enchantment = context.lookup(Registries.ENCHANTMENT);
        var items = context.lookup(Registries.ITEM);

        register(context, MASTER_KEY, Enchantment.enchantment(Enchantment.definition(items.getOrThrow(ModTags.Items.DOOR_KEYS),
                items.getOrThrow(ModTags.Items.DOOR_KEYS), 2, 1,
                Enchantment.dynamicCost(25, 25), Enchantment.dynamicCost(75, 25), 4,  EquipmentSlotGroup.MAINHAND)));

    }


    private static ResourceKey<Enchantment> registerKey(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID, id));
    }

    private static void register(BootstrapContext<Enchantment> context, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        context.register(key, builder.build(key.identifier()));
    }

}
