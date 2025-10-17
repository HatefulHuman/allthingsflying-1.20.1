package com.kfhzs.allthingsflying.capability;

import com.kfhzs.allthingsflying.items.Engine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class EngineCapabilityImpl implements IEngineCapability {
    private ItemStack engineItem = ItemStack.EMPTY;
    private double power = 0.0;
    private float flightSpeed = 1.0f;
    private float friction = 0.93f; // 默认阻力系数
    private float stopThreshold = 0.005f;  // 停止阈值
    private float acceleration = 0.3f;
    private int engineLevel = 1;
    private int engineDurability = 2564;
    private int maxEngineDurability = 2564;
    private int engineSoundTime = 0;
    private SoundEvent engineSound;

    @Override
    public ItemStack getEngineItem() {
        return engineItem;
    }

    @Override
    public void setEngineItem(ItemStack engineStack) {
        this.engineItem = engineStack.copy();
        setPropertiesFromEngine(engineStack);
    }

    @Override
    public double getPower() {
        return power;
    }

    @Override
    public void setPower(double power) {
        this.power = power;
    }

    @Override
    public int getEngineDurability() {
        return engineDurability;
    }

    @Override
    public void setEngineDurability(int durability) {
        this.engineDurability = durability;
    }

    @Override
    public int getMaxEngineDurability() {
        return maxEngineDurability;
    }

    @Override
    public float getFlightSpeed() {
        float speedMultiplier;
        if (engineLevel <= 7) {
            speedMultiplier = 1.0f + (engineLevel - 1) * 0.2f;
        } else {
            int extraLevels = engineLevel - 7;
            speedMultiplier = (1.0f + 6 * 0.2f) + (extraLevels * 0.4f);
        }
        return flightSpeed * speedMultiplier;
    }

    @Override
    public void setFlightSpeed(float speed) {
        this.flightSpeed = speed;
    }

    @Override
    public float getFriction() {
        return friction;
    }

    @Override
    public void setFriction(float friction) {
        this.friction = friction;
    }

    @Override
    public float getStopThreshold() {
        return stopThreshold;
    }

    @Override
    public void setStopThreshold(float stopThreshold) {
        this.stopThreshold = stopThreshold;
    }

    @Override
    public float getAcceleration() {
        return acceleration;
    }

    @Override
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public int getEngineLevel() {
        return engineLevel;
    }

    @Override
    public void setEngineLevel(int level) {
        this.engineLevel = level;
        this.engineDurability = this.engineDurability + ((engineLevel - 1) * 577);
        this.maxEngineDurability = this.maxEngineDurability + ((engineLevel - 1) * 577);
    }

    @Override
    public boolean hasEngine() {
        return !engineItem.isEmpty() && power > 0;
    }

    @Override
    public int getEngineSoundTime() {
        return engineSoundTime;
    }

    @Override
    public void setEngineSoundTime(int engineSoundTime) {
        this.engineSoundTime = engineSoundTime;
    }

    @Override
    public SoundEvent getEngineSound() {
        return engineSound;
    }

    @Override
    public void setEngineSound(SoundEvent engineSound) {
        this.engineSound = engineSound;
    }

    @Override
    public void copyFrom(IEngineCapability other) {
        this.engineItem = other.getEngineItem().copy();
        this.power = other.getPower();
        this.flightSpeed = other.getFlightSpeed();
        this.friction = other.getFriction();
        this.stopThreshold = other.getStopThreshold();
        this.acceleration = other.getAcceleration();
        this.engineLevel = other.getEngineLevel();
        this.engineDurability = other.getEngineDurability();
        this.maxEngineDurability = other.getMaxEngineDurability();
        this.engineSoundTime = other.getEngineSoundTime();
        this.engineSound = other.getEngineSound();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        // 序列化 ItemStack
        if (!engineItem.isEmpty()) {
            CompoundTag engineTag = new CompoundTag();
            engineItem.save(engineTag);
            tag.put("EngineItem", engineTag);
        }
        tag.putDouble("Power", power);
        tag.putFloat("FlightSpeed", flightSpeed);
        tag.putFloat("Friction", friction);
        tag.putFloat("StopThreshold", stopThreshold);
        tag.putFloat("Scceleration", acceleration);
        tag.putInt("EngineLevel", engineLevel);
        tag.putInt("EngineDurability", engineDurability);
        tag.putInt("MaxEngineDurability", maxEngineDurability);
        tag.putInt("EngineSoundTime", this.engineSoundTime);
        // 序列化 SoundEvent
        if (this.engineSound != null) {
            tag.putString("EngineSound", this.engineSound.getLocation().toString());
        }
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (nbt.contains("EngineItem")) {
            this.engineItem = ItemStack.of(nbt.getCompound("EngineItem"));
            setPropertiesFromEngine(this.engineItem);
        }
        this.power = nbt.getDouble("Power");
        this.flightSpeed = nbt.getFloat("FlightSpeed");
        this.friction = nbt.getFloat("Friction");
        this.stopThreshold = nbt.getFloat("StopThreshold");
        this.acceleration = nbt.getFloat("Scceleration");
        this.engineLevel = nbt.getInt("EngineLevel");
        this.engineDurability = nbt.getInt("EngineDurability");
        this.maxEngineDurability = nbt.getInt("MaxEngineDurability");
        this.engineSoundTime = nbt.getInt("EngineSoundTime");
        if (nbt.contains("EngineSound")) {
            String soundLocation = nbt.getString("EngineSound");
            ResourceLocation soundRL = ResourceLocation.tryParse(soundLocation);
            if (soundRL != null) {
                this.engineSound = ForgeRegistries.SOUND_EVENTS.getValue(soundRL);
            }
        }
    }

    /**
     * 根据引擎物品设置属性
     */
    private void setPropertiesFromEngine(ItemStack engineStack) {
        if (engineStack.getItem() instanceof Engine engineItem) {
            this.power = engineItem.getPower();
            this.flightSpeed = engineItem.getFlightSpeed();
            this.friction = engineItem.getFriction();
            this.stopThreshold = engineItem.getStopThreshold();
            this.acceleration = engineItem.getAcceleration();
            this.engineLevel = engineItem.getEngineLevel();
            this.engineDurability = engineItem.getEngineDurability();
            this.engineSoundTime = engineItem.getEngineSoundTime();
            this.engineSound = engineItem.getEngineSound();
        } else {
            this.power = 30.0;
            this.flightSpeed = 1.0f;
            this.friction = 0.93f; // 默认阻力系数
            this.stopThreshold = 0.005f;  // 停止阈值
            this.acceleration = 0.3f;
            this.engineLevel = 1;
            this.engineDurability = 2564;
            this.maxEngineDurability = 2564;
            this.engineSoundTime = 0;
            this.engineSound = null;
        }
    }
}