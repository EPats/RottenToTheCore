package io.github.epats.rottentothecore.common.capability;

import net.minecraft.nbt.CompoundTag;

public class CapabilityIntegerWrapper {
    private final String valueId;
    private int value;
    private int maxValue;
    private int minValue;

    public CapabilityIntegerWrapper(String id, int startValue, int minValue, int maxValue) {
        this.valueId = id;
        this.value = startValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public CapabilityIntegerWrapper(String id, CompoundTag compound) {
        this.valueId = id;
        if(compound.contains(id) && compound.getBoolean(id)) {
            this.value = compound.getInt(id + "_value");
            this.minValue = compound.getInt(id + "_min");
            this.maxValue = compound.getInt(id + "_max");
        } else {
            this.value = -1;
            this.minValue = -1;
            this.maxValue = -1;
        }
    }

    public CapabilityIntegerWrapper(CapabilityIntegerWrapper otherWrapper) {
        this.valueId = otherWrapper.getValueId();
        this.value = otherWrapper.getValue();
        this.minValue = otherWrapper.getMinValue();
        this.maxValue = otherWrapper.getMaxValue();
    }

    public void saveNBTData(CompoundTag compound) {
        compound.putBoolean(this.valueId, true);
        compound.putInt(this.valueId + "_value", this.value);
        compound.putInt(this.valueId + "_max", this.maxValue);
        compound.putInt(this.valueId + "_min", this.minValue);
    }

    public String getValueId() { return this.valueId; }
    public int getValue() { return this.value; }
    public int getMaxValue() { return this.maxValue; }
    public int getMinValue() { return this.minValue; }

    public void setMaxValue(int newMaxValue, boolean capCurrentValue) {
        this.maxValue = newMaxValue;
        if(capCurrentValue && this.value > this.maxValue) {
            this.value = this.maxValue;
        }
    }
    public void setMinValue(int newMinValue, boolean capCurrentValue) {
        this.minValue = newMinValue;
        if(capCurrentValue && this.value < this.minValue) {
            this.value = this.minValue;
        }
    }

    public void setValue(int newValue) {
        this.value = Math.min(Math.max(newValue, this.minValue), this.maxValue);
    }
    public void setValueToMin() { this.value = this.minValue; }
    public void setValueToMax() { this.value = this.maxValue; }

    public void addToValue(int valueToAdd) {
        this.setValue(this.value + valueToAdd);
    }

    public boolean increaseByOne() {
        boolean canIncrease = this.canIncrease();
        this.addToValue(1);
        return canIncrease;
    }

    public boolean decreaseByOne() {
        boolean canDecrease = this.canDecrease();
        this.addToValue(-1);
        return canDecrease;
    }

    public boolean canIncrease() {
        return this.value < this.maxValue;
    }

    public boolean canDecrease() {
        return this.value > this.minValue;
    }

}
