package net.darkhood135.projectbiohazard.attachmenttype;

import com.mojang.serialization.Codec;
import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ProjectBiohazard.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> YELLOW_HERB_HP = ATTACHMENT_TYPES.register("yellow_herb_hp",
            () -> AttachmentType.<Integer>builder(() -> 0).sync(ByteBufCodecs.INT).serialize(Codec.INT.fieldOf("yellow_herb_hp")).copyOnDeath().build());


    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
