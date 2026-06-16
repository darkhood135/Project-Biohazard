package net.darkhood135.projectbiohazard.networking;

import net.darkhood135.projectbiohazard.networking.packet.TestPacketC2S;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// On the server!
public class ServerboundPackets {
    public static void handleTestPacket(TestPacketC2S testPacket, IPayloadContext context) {
        Player player = context.player();
        ServerLevel level = ((ServerLevel) player.level());
        EntityType.COW.spawn(level, player.getOnPos(), EntitySpawnReason.TRIGGERED);
        player.sendSystemMessage(Component.literal(testPacket.name() + " has just said " + testPacket.value()));
    }
}
