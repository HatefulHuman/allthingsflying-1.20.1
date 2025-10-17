package com.kfhzs.allthingsflying.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CapabilityEventHandler {

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        event.addCapability(
                EngineCapabilityProvider.ENGINE_CAPABILITY_ID,
                new EngineCapabilityProvider()
        );
    }
}