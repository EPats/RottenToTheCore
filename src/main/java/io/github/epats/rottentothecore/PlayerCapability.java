package io.github.epats.rottentothecore;

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
    private DamageSource CHARLIE; // = new DamageSource(RottenToTheCore.MODID + ".charlie");

    public void saveNBTData(CompoundTag compound) {
        compound.putInt("darkness", darknessTicks);
    }

    public void loadNBTData(CompoundTag compound) {
        darknessTicks = compound.getInt("darkness");
    }
}
