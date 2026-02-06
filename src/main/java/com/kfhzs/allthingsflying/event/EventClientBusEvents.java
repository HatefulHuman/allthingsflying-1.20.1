package com.kfhzs.allthingsflying.event;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.particle.ModParticles;
import com.kfhzs.allthingsflying.particle.EngineParticle;
import com.kfhzs.allthingsflying.particle.WhiteLightParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = AllThingsFlying.MODID,bus = Mod.EventBusSubscriber.Bus.MOD ,value = Dist.CLIENT)
public class EventClientBusEvents {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet( ModParticles.THERMAL_ENGINE_SMOKE.get(), EngineParticle.ThermalEngineProvider::new);
        event.registerSpriteSet( ModParticles.CLOUD_ENGINE_SMOKE.get(), EngineParticle.CloudEngineProvider::new);
        event.registerSpriteSet( ModParticles.WHITE_LIGHT.get(), WhiteLightParticle.Provider::new);
    }
}

