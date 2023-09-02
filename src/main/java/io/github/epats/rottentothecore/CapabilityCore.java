package io.github.epats.rottentothecore;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityCore {
    public static ResourceLocation PLAYER_CAPABILITY = new ResourceLocation(RottenToTheCore.MODID, "player_capability");
    public static ResourceLocation WORLD_CAPABILITY = new ResourceLocation(RottenToTheCore.MODID, "world_capability");
}
