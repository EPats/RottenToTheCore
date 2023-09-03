package io.github.epats.rottentothecore;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundPacketThoughts {
    private MutableComponent message = Component.translatable("rottentothecore.thoughts.blank");

    public ClientBoundPacketThoughts(MutableComponent message) {
        this.message = message;
    }

    public ClientBoundPacketThoughts(FriendlyByteBuf buf) {
        this.message = (MutableComponent) buf.readComponent();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeComponent(this.message);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are client side.
            // Be very careful not to access client-only classes here! (like Minecraft)
            // because
            // this packet needs to be available server-side too
            ClientData.changeThought(this.message);
        });
        return true;
    }
}
