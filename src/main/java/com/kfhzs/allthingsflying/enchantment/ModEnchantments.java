package com.kfhzs.allthingsflying.enchantment;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AllThingsFlying.MODID);

    public static final RegistryObject<Enchantment> ENGINE_MENDING =
            ENCHANTMENTS.register("engine_mending", EngineMendingEnchantment::new);

    public static void register(IEventBus eventBus){
        ENCHANTMENTS.register(eventBus);
    }
}