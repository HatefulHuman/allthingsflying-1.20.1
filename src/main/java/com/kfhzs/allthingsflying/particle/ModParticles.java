package com.kfhzs.allthingsflying.particle;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, AllThingsFlying.MODID);
    // 粒子类型注册
    public static final RegistryObject<SimpleParticleType> THERMAL_ENGINE_SMOKE =
            PARTICLE_TYPES.register("thermal_engine_smoke",
                    () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> CLOUD_ENGINE_SMOKE =
            PARTICLE_TYPES.register("cloud_engine_smoke",
                    () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> WHITE_LIGHT =
            PARTICLE_TYPES.register("white_light",
                    () -> new SimpleParticleType(true));

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}