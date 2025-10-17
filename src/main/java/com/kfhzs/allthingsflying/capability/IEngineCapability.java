package com.kfhzs.allthingsflying.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;

public interface IEngineCapability extends INBTSerializable<CompoundTag> {
    ItemStack getEngineItem();
    void setEngineItem(ItemStack engineStack);

    double getPower();
    void setPower(double power);

    int getEngineDurability();
    void setEngineDurability(int durability);

    int getMaxEngineDurability();

    float getFlightSpeed();
    void setFlightSpeed(float speed);

    float getFriction();
    void setFriction(float friction);

    float getStopThreshold();
    void setStopThreshold(float stopThreshold);

    float getAcceleration();
    void setAcceleration(float acceleration);

    int getEngineLevel();
    void setEngineLevel(int level);

    boolean hasEngine();

    int getEngineSoundTime();
    void setEngineSoundTime(int engineSoundTime);

    SoundEvent getEngineSound();
    void setEngineSound(SoundEvent engineSound);

    void copyFrom(IEngineCapability other);
}