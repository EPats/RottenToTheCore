package io.github.epats.rottentothecore.common.capability;

import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.RottenToTheCore;
import net.minecraft.nbt.CompoundTag;

public class PlayerCapability {
    private boolean alreadyInDarkness = false;
    public CapabilityIntegerWrapper darkness = new CapabilityIntegerWrapper("darkness", 0, 0, Config.ServerConfig.darknessMaxValue);
    public CapabilityIntegerWrapper sanity = new CapabilityIntegerWrapper("sanity",
            Config.ServerConfig.sanityStartingValue, Config.ServerConfig.sanityMinValue, Config.ServerConfig.sanityMaxValue);

    public void saveNBTData(CompoundTag compound) {
        this.darkness.saveNBTData(compound);
        this.sanity.saveNBTData(compound);
    }

    public void loadNBTData(CompoundTag compound) {
        this.darkness = new CapabilityIntegerWrapper("darkness", compound);
        this.sanity = new CapabilityIntegerWrapper("sanity", compound);
    }

    public void resetFromOld(PlayerCapability oldCap, String respawnType) {
        this.alreadyInDarkness = false;
        switch(respawnType) {
            case "respawnFromEnd":
                this.darkness = new CapabilityIntegerWrapper(oldCap.darkness);
                this.sanity = new CapabilityIntegerWrapper(oldCap.sanity);
            case "default":
                this.darkness = new CapabilityIntegerWrapper(oldCap.darkness.getValueId(), 0, 0, Config.ServerConfig.darknessMaxValue);
                this.sanity = new CapabilityIntegerWrapper(oldCap.sanity.getValueId(), Config.ServerConfig.sanityStartingValue,
                        Config.ServerConfig.sanityMinValue, Config.ServerConfig.sanityMaxValue);
                break;
            default:
                RottenToTheCore.LOGGER.debug("Unknown respawn type: " + respawnType);
        }
    }

    public void resetFromOld(PlayerCapability oldCap) {
        resetFromOld(oldCap, "default");
    }
    public boolean isAlreadyInDarkness() {
        return this.alreadyInDarkness;
    }


    public void increaseDarkness() {
        if(!this.alreadyInDarkness)
            this.alreadyInDarkness = true;
        this.darkness.increaseByOne();
    }


}
