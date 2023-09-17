package io.github.epats.rottentothecore.common.capability;

import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.common.message.ClientBoundPacketThoughts;
import io.github.epats.rottentothecore.common.message.MessageRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;

public class PlayerCapability {
    private boolean alreadyInDarkness = false;
    private int darknessTicks = 0;
    private int playerSanity = 0;

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("darkness", darknessTicks);
        compound.putInt("sanity", playerSanity);
    }

    public void loadNBTData(CompoundTag compound) {
        darknessTicks = compound.getInt("darkness");
        playerSanity = compound.getInt("sanity");
    }

    public void resetFromOld(PlayerCapability oldCap, String respawnType) {
        alreadyInDarkness = false;
        darknessTicks = 0;
        switch(respawnType) {
            default:
                playerSanity = 0;
                break;
        }
    }

    public void resetFromOld(PlayerCapability oldCap) {
        resetFromOld(oldCap, "default");
    }

    public int getDarknessTicks() {
        return this.darknessTicks;
    }

    public void resetDarkness(Player player) {
        if (this.darknessTicks != 0) {
            this.darknessTicks = 0;
            MessageRegistry.sendToPlayer(new ClientBoundPacketThoughts(Component
                    .translatable(RottenToTheCore.MOD_ID + ".darkness.gone.message" + (1 + player.level().random.nextInt(10)))
                    .withStyle(ChatFormatting.GOLD)), (ServerPlayer) player);
            alreadyInDarkness = false;
        }
    }

    public int increaseDarkness() {
        if(!this.alreadyInDarkness)
            this.alreadyInDarkness = true;
        return ++this.darknessTicks;
    }

    public boolean isAlreadyInDarkness() {
        return this.alreadyInDarkness;
    }
}
