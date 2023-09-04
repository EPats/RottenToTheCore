package io.github.epats.rottentothecore.common.capability;

import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.common.message.ClientBoundPacketThoughts;
import io.github.epats.rottentothecore.common.message.MessageRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;

public class PlayerCapability {
    private boolean alreadyDark = false;
    private int darknessTicks = 0;
    private DamageSource CHARLIE; // = new DamageSource(RottenToTheCore.MODID + ".charlie");

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("darkness", darknessTicks);
    }

    public void loadNBTData(CompoundTag compound) {
        darknessTicks = compound.getInt("darkness");
    }

    public void resetDarkness(Player player) {
        if (this.darknessTicks != 0) {
            this.darknessTicks = 0;
            MessageRegistry.sendToPlayer(new ClientBoundPacketThoughts(Component
                    .translatable(RottenToTheCore.MOD_ID + ".darkness.gone.message" + (1 + player.level().random.nextInt(10)))
                    .withStyle(ChatFormatting.GOLD)), (ServerPlayer) player);
            alreadyDark = false;
        }
    }
}
