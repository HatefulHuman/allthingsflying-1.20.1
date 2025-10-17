package com.kfhzs.allthingsflying.loot_modifier;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLM_REGISTER =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, AllThingsFlying.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> GHAST_GIFT_PACKAGE =
            GLM_REGISTER.register("ghast_gift_package", () -> FlightGiftPackageModifier.CODEC);

    public static void register(IEventBus eventBus){
        GLM_REGISTER.register(eventBus);
    }
}
