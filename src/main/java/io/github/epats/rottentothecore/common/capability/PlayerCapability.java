package io.github.epats.rottentothecore.common.capability;

import io.github.epats.rottentothecore.RottenToTheCore;
import io.github.epats.rottentothecore.common.message.ClientBoundPacketThoughts;
import io.github.epats.rottentothecore.common.message.MessageRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;

public class PlayerCapability {
    private boolean alreadyDark = false;
    private int darknessTicks = 0;

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("darkness", darknessTicks);
    }

    public void loadNBTData(CompoundTag compound) {
        darknessTicks = compound.getInt("darkness");
    }

    public int getDarknessTicks() {
        return this.darknessTicks;
    }

    public void resetFromOld(PlayerCapability oldCap) {
        alreadyDark = false;
        darknessTicks = 0;
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

    public int increaseDarkness() {
        if(!this.alreadyDark)
            this.alreadyDark = true;
        return ++this.darknessTicks;
    }

    public boolean isAlreadyDark() {
        return this.alreadyDark;
    }
}
