package io.github.epats.rottentothecore.common.capability;

import io.github.epats.rottentothecore.Config;
import io.github.epats.rottentothecore.RottenToTheCore;
import net.minecraft.nbt.CompoundTag;

/**
 * Manages the player's capabilities related to darkness and sanity.
 * Each capability is represented using a CapabilityIntegerWrapper to maintain
 * its value within defined bounds.
 */
public class PlayerCapability {
    public CapabilityIntegerWrapper darkness = new CapabilityIntegerWrapper("darkness", 0, 0, Config.ServerConfig.darknessMaxValue);
    public CapabilityIntegerWrapper sanity = new CapabilityIntegerWrapper("sanity",
            Config.ServerConfig.sanityStartingValue, Config.ServerConfig.sanityMinValue, Config.ServerConfig.sanityMaxValue);

    /**
     * Serializes the current state of the player's capabilities into the provided NBT compound.
     *
     * @param compound The NBT compound where the data should be stored.
     */
    public void saveNBTData(CompoundTag compound) {
        this.darkness.saveNBTData(compound);
        this.sanity.saveNBTData(compound);
    }

    /**
     * Loads and sets the player's capabilities from the provided NBT compound.
     *
     * @param compound The NBT compound containing the serialized capability data.
     */
    public void loadNBTData(CompoundTag compound) {
        this.darkness = new CapabilityIntegerWrapper("darkness", compound);
        this.sanity = new CapabilityIntegerWrapper("sanity", compound);
    }

    /**
     * Resets the player's capabilities based on another PlayerCapability instance and a specified respawn type.
     *
     * @param oldCap The previous PlayerCapability instance.
     * @param respawnType The type of respawn (e.g., "respawnFromEnd", "default").
     */
    public void resetFromOld(PlayerCapability oldCap, String respawnType) {
        switch (respawnType) {
            case "respawnFromEnd" -> {
                this.darkness = new CapabilityIntegerWrapper(oldCap.darkness);
                this.sanity = new CapabilityIntegerWrapper(oldCap.sanity);
            }
            case "default" -> {
                this.darkness = new CapabilityIntegerWrapper(oldCap.darkness.getValueId(), 0, 0, Config.ServerConfig.darknessMaxValue);
                this.sanity = new CapabilityIntegerWrapper(oldCap.sanity.getValueId(), Config.ServerConfig.sanityStartingValue,
                        Config.ServerConfig.sanityMinValue, Config.ServerConfig.sanityMaxValue);
            }
            default -> RottenToTheCore.LOGGER.debug("Unknown respawn type: " + respawnType);
        }
    }

    public void resetFromOld(PlayerCapability oldCap) {
        resetFromOld(oldCap, "default");
    }

    /**
     * Checks if the player's current darkness level exceeds the minimum value.
     *
     * @return True if the player's darkness level is above the minimum, false otherwise.
     */
    public boolean isAlreadyInDarkness() {
        return this.darkness.getValue() > this.darkness.getMinValue();
    }

}
