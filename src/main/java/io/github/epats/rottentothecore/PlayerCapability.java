package io.github.epats.rottentothecore;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;

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
