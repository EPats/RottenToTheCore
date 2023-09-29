package io.github.epats.rottentothecore.common.message;

import io.github.epats.rottentothecore.client.ClientData;
import io.github.epats.rottentothecore.client.render.Thought;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Represents a packet intended to be sent from the server to the client.
 * This packet carries information about a thought that should be displayed on the client side.
 */
public class ClientBoundPacketThoughts {
    private final Thought thought;

    /**
     * Constructs a new packet with the provided thought.
     *
     * @param thought The thought to be sent to the client.
     */
    public ClientBoundPacketThoughts(Thought thought) {
        this.thought = thought;
    }

    /**
     * Creates a new packet by deserializing a thought from a buffer.
     *
     * @param buf The buffer.
     */
     public ClientBoundPacketThoughts(FriendlyByteBuf buf) {
        this.thought = new Thought(buf);
    }

    /**
     * Serializes the thought from this packet to a buffer.
     *
     * @param buf The buffer.
     */
    public void serializeToBuffer(FriendlyByteBuf buf) {
        this.thought.serializeToBuffer(buf);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft)
            // because
            // this packet needs to be available server-side too
            ClientData.getInstance().processNewThought(this.thought);
        });
        return true;
    }
}
