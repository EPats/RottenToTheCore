package io.github.epats.rottentothecore.common.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ObjectHolder;

public class IntegerCapabilityStorage implements INBTSerializable<CompoundTag>, AA {

    private static final String NBT_KEY_VALUE = "value";
    private static final String NBT_KEY_MAX = "max_value";
    private static final String NBT_KEY_MIN = "min_value";
    int value = 0;
    int maxValue;
    int minValue;



    IntegerCapabilityStorage(int minValue, int maxValue, int startingValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = setValue(startingValue);
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public int getMaxValue() {
        return this.maxValue;
    }

    @Override
    public int getMinValue() {
        return this.minValue;
    }

    @Override
    public int changeValue(int changeAmount) {
        if((changeAmount > 0 && !this.canIncrease()) || (changeAmount < 0 && !this.canDecrease()))
            return this.value;
        else if (this.value + changeAmount >= this.maxValue) {
            this.value = this.maxValue;
            return this.value;
        } else if (this.value + changeAmount <= this.minValue) {
            this.value = this.minValue;
            return this.value;
        } else {
            this.value += changeAmount;
            return this.value;
        }
    }

    @Override
    public int increase() {
        return changeValue(1);
    }

    @Override
    public int decrease() {
        return changeValue(-1);
    }

    @Override
    public int setValue(int newValue) {
        if(newValue > this.maxValue)
            this.value = this.maxValue;
        else if (newValue < this.minValue)
            this.value = this.minValue;
        else
            this.value = newValue;
        return value;
    }


    @Override
    public boolean canIncrease() {
        return this.value < this.maxValue;
    }

    @Override
    public boolean canDecrease() {
        return this.value > this.minValue;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        tag.putInt(NBT_KEY_VALUE, this.value);
        tag.putInt(NBT_KEY_MAX, this.maxValue);
        tag.putInt(NBT_KEY_MIN, this.minValue);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.maxValue = nbt.getInt(NBT_KEY_MAX);
        this.minValue = nbt.getInt(NBT_KEY_MIN);
        this.setValue(nbt.getInt(NBT_KEY_VALUE));
    }
}
