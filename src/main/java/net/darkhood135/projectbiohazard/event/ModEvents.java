package net.darkhood135.projectbiohazard.event;

import net.darkhood135.projectbiohazard.ProjectBiohazard;
import net.darkhood135.projectbiohazard.attachmenttype.ModAttachmentTypes;
import net.darkhood135.projectbiohazard.networking.ServerboundPackets;
import net.darkhood135.projectbiohazard.networking.packet.TestPacketC2S;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ProjectBiohazard.MOD_ID)
public class ModEvents {
    private static final Identifier yellowHerbHPID = Identifier.fromNamespaceAndPath(ProjectBiohazard.MOD_ID,"yellow_herb_hp");

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToServer(TestPacketC2S.TYPE, TestPacketC2S.STREAM_CODEC, ServerboundPackets::handleTestPacket);
    }

    // Yellow Herb HP
    public static void grantYellowHerbHP (Player player) {
        int boost = player.getData(ModAttachmentTypes.YELLOW_HERB_HP);
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        maxHealth.removeModifier(yellowHerbHPID);   // Clearing the old one first
        maxHealth.addTransientModifier(
                new AttributeModifier(yellowHerbHPID, boost, AttributeModifier.Operation.ADD_VALUE));
    }

    // TESTING ONLY!!!!
    /*
    @SubscribeEvent
    public static void setYellow(PlayerEvent.PlayerLoggedInEvent event) {
        Player newPlayer = event.getEntity();
        newPlayer.setData(ModAttachmentTypes.YELLOW_HERB_HP, 0);
    }
    */

    @SubscribeEvent
    public static void setYellowHerbProgressOnClone(PlayerEvent.Clone event) {
        Player newPlayer = event.getEntity();
        newPlayer.setData(ModAttachmentTypes.YELLOW_HERB_HP, event.getOriginal().getData(ModAttachmentTypes.YELLOW_HERB_HP));
        grantYellowHerbHP(newPlayer);
    }
    @SubscribeEvent
    public static void setYellowHerbProgressOnRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        player.setData(ModAttachmentTypes.YELLOW_HERB_HP, player.getData(ModAttachmentTypes.YELLOW_HERB_HP));
        grantYellowHerbHP(player);
        player.heal(1000f);
    }

}
