package net.darkhood135.projectbiohazard.attachmenttype;

import com.mojang.serialization.Codec;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.item.custom.SyringeItem;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ProjectBiohazard.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> YELLOW_HERB_HP = ATTACHMENT_TYPES.register("yellow_herb_hp",
            () -> AttachmentType.<Integer>builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("yellow_herb_hp")).copyOnDeath().build());

    public static final Supplier<AttachmentType<SyringeItem.SyringeInjection>> SYRINGE_INJECTION =
            ATTACHMENT_TYPES.register("syringe_injection", () -> AttachmentType.<SyringeItem.SyringeInjection>builder(() -> null).build());

    public static final Supplier<AttachmentType<List<ItemStack>>> SAVE_SNAPSHOT =
            ATTACHMENT_TYPES.register("save_snapshot",
                    () -> AttachmentType.<List<ItemStack>>builder(() -> List.of())
                            .serialize(ItemStack.CODEC.listOf().fieldOf("items"))   // match your yellow_herb serialize form
                            .build());                                              // NO copyOnDeath -> consumed on death

    public static final Supplier<AttachmentType<List<ItemStack>>> RESTORE_STASH =
            ATTACHMENT_TYPES.register("restore_stash",
                    () -> AttachmentType.<List<ItemStack>>builder(() -> List.of()).build());  // transient

    public static final Supplier<AttachmentType<GlobalPos>> SAVE_TYPEWRITER_POS =
            ATTACHMENT_TYPES.register("save_typewriter_pos",
                    () -> AttachmentType.<GlobalPos>builder(() -> null)
                            .serialize(GlobalPos.CODEC.fieldOf("pos"))   // match your serialize form
                            .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
