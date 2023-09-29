package io.github.epats.rottentothecore.common.message;

import io.github.epats.rottentothecore.client.ClientData;
import io.github.epats.rottentothecore.client.render.Thought;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundPacketThoughts {
    private final Thought thought;

    public ClientBoundPacketThoughts(Thought thought) {
        this.thought = thought;
    }

     public ClientBoundPacketThoughts(FriendlyByteBuf buf) {
        this.thought = new Thought(buf);
    }

    public void toBytes(FriendlyByteBuf buf) {
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
