package net.darkhood135.projectbiohazard.item;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectBiohazard.MOD_ID);

    public static final DeferredItem<Item> UMBRELLA_BADGE = ITEMS.registerSimpleItem("umbrella_badge",
            properties -> properties);
    public static final DeferredItem<Item> TRONA = ITEMS.registerSimpleItem("trona",
            properties -> properties);

    // Hidden items (Visual only)
    public static final DeferredItem<Item> UMBRELLA_INSIGNIA = ITEMS.registerSimpleItem("umbrella_insignia",
            properties -> properties);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
