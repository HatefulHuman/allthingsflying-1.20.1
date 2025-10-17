package com.kfhzs.allthingsflying.items;

import com.kfhzs.allthingsflying.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

public class Engine extends Item {
    protected double power;
    protected float flightSpeed;
    protected float friction; // 默认阻力系数
    protected float stopThreshold;  // 停止阈值
    protected float acceleration;
    protected int engineLevel;
    protected int engineDurability;
    protected int engineSoundTime;
    protected SoundEvent engineSound;

    public Engine(Properties properties, float friction, float stopThreshold, double power, float flightSpeed, float acceleration, int engineLevel, int durability,
                  int engineSoundTime,SoundEvent engineSound) {
        super(properties);
        this.power = power;
        this.flightSpeed = flightSpeed;
        this.friction = friction;
        this.stopThreshold = stopThreshold;
        this.acceleration = acceleration;
        this.engineLevel = engineLevel;
        this.engineDurability = durability;
        this.engineSoundTime = engineSoundTime;
        this.engineSound = engineSound;
    }

    // Getter 方法
    public double getPower() {
        return power;
    }
    public float getFlightSpeed() {
        return flightSpeed;
    }

    public float getFriction() {
        return friction;
    }

    public float getStopThreshold() {
        return stopThreshold;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public int getEngineLevel() {
        return engineLevel;
    }

    public int getEngineDurability() {
        return engineDurability;
    }

    public int getEngineSoundTime() {
        return engineSoundTime;
    }

    public SoundEvent getEngineSound() {
        return engineSound;
    }

    public static Engine thermalEngine(Properties properties) {
        return new Engine(properties,0.93f,0.005f,50.0,1.0f,0.3f,1,
                2564,120, ModSounds.THERMAL_ENGINE_SOUND.get());
    }

    public static Engine aeroEngine(Properties properties) {
        return new Engine(properties,0.93f,0.005f,80.0,1.0f,0.3f,1,
                2564,20, ModSounds.AERO_ENGINE_SOUND.get());
    }

    public static Engine magicEngine(Properties properties) {
        return new Engine(properties,0.93f,0.005f,100.0,1.0f,0.3f,1,
                2564,28, ModSounds.MAGIC_ENGINE_SOUND.get());
    }

    public static Engine droneEngine(Properties properties) {
        return new Engine(properties,0.93f,0.005f,30.0,1.0f,0.3f,1,
                2564,36, ModSounds.DRONE_ENGINE_SOUND.get());
    }

    public static Engine cloudEngine(Properties properties) {
        return new Engine(properties,0.93f,0.005f,60.0,1.0f,0.3f,1,
                2564,36,ModSounds.CLOUD_ENGINE_SOUND.get());
    }

}
