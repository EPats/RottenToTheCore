package io.github.epats.rottentothecore.common.message;

import io.github.epats.rottentothecore.common.capability.PlayerCapabilityProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundPacketSkyFlash {
    public ServerBoundPacketSkyFlash() {
    }

    public ServerBoundPacketSkyFlash(FriendlyByteBuf buf) {
    }

    public void toBytes(FriendlyByteBuf buf) {
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            // Here we are server side
            ServerPlayer player = ctx.getSender();
            player.getCapability(PlayerCapabilityProvider.playerCap).ifPresent(cap -> {
                if (this.sawLightning(player)) {
//                    cap.resetDarkness(player);
                }
            });
        });
        return true;
    }

    public boolean sawLightning(Player player) {
        int skyLight = player.level().getLightEngine().skyEngine.getLightValue(player.blockPosition());
        return skyLight > 0;
    }
}