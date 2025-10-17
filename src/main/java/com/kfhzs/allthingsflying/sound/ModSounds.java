package com.kfhzs.allthingsflying.sound;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, AllThingsFlying.MODID);

    public static final RegistryObject<SoundEvent> AERO_ENGINE_SOUND = registerSoundEvent("aero_engine_sound");
    public static final RegistryObject<SoundEvent> CLOUD_ENGINE_SOUND = registerSoundEvent("cloud_engine_sound");
    public static final RegistryObject<SoundEvent> DRONE_ENGINE_SOUND = registerSoundEvent("drone_engine_sound");
    public static final RegistryObject<SoundEvent> MAGIC_ENGINE_SOUND = registerSoundEvent("magic_engine_sound");
    public static final RegistryObject<SoundEvent> THERMAL_ENGINE_SOUND = registerSoundEvent("thermal_engine_sound");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(AllThingsFlying.MODID, name)));
    }

    public static void register(IEventBus eventBus){
        SOUND_EVENTS.register(eventBus);
    }
}
