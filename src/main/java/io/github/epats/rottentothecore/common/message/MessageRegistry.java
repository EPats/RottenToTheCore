package io.github.epats.rottentothecore.common.message;

import io.github.epats.rottentothecore.RottenFamilyTree;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Handles the registration and sending of custom network packets for the RottenToTheCore mod.
 * This class sets up a communication channel and provides utility methods to send packets
 * to various destinations (server, specific player, all players).
 */
public class MessageRegistry {

    public static final ResourceLocation MESSAGES = new ResourceLocation(RottenFamilyTree.ROTTEN_TO_THE_CORE, "messages");
    private static SimpleChannel INSTANCE;

    // Every packet needs a unique ID (unique for this channel)
    private static int packetId = 0;

    public static int id() {
        return packetId++;
    }

    public static void register() {
        // Make the channel. If needed you can do version checking here
        SimpleChannel net = NetworkRegistry.ChannelBuilder.named(MESSAGES)
                .networkProtocolVersion(() -> "1.0").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        // Register all our packets. We only have one right now. The new message has a
        // unique ID, an indication
        // of how it is going to be used (from client to server) and ways to encode and
        // decode it. Finally, 'handle'
        // will actually execute when the packet is received

        net.messageBuilder(ClientBoundPacketThoughts.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ClientBoundPacketThoughts::new).encoder(ClientBoundPacketThoughts::serializeToBuffer)
                .consumerMainThread(ClientBoundPacketThoughts::handle).add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static <MSG> void sendToAllPlayers(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }

    public static SimpleChannel getInstanceForAdditionalRegister() {
        return INSTANCE;
    }
}
