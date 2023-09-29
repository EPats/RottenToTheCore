package io.github.epats.rottentothecore.common.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides the capability for storing and managing player-related data, such as darkness and sanity levels.
 * This capability can be attached to player entities to store mod-specific data.
 */
public class PlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final Capability<PlayerCapability> playerCap = CapabilityManager.get(new CapabilityToken<>() {
    });

    private PlayerCapability playerCapability = null;
    private final LazyOptional<PlayerCapability> opt = LazyOptional.of(this::getPlayerCapability);

    @Nonnull
    private PlayerCapability getPlayerCapability() {
        if (playerCapability == null) {
            playerCapability = new PlayerCapability();
        }
        return playerCapability;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == playerCap) {
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        getPlayerCapability().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getPlayerCapability().loadNBTData(nbt);
    }
}