package io.github.epats.rottentothecore.common.capability;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface AA {
    int getValue();

    int setValue(int newValue);

    int changeValue(int changeAmount);

    int increase();
    int decrease();

    boolean canIncrease();
    boolean canDecrease();

    default boolean isMax() {
        return !canIncrease();
    }

    default boolean isMin() {
        return !canDecrease();
    }

    int getMaxValue();
    int getMinValue();
}