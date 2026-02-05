package com.kfhzs.allthingsflying.items;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.items.aerocraft.Drone;
import com.kfhzs.allthingsflying.items.aerocraft.FlyingSword;
import com.kfhzs.allthingsflying.items.aerocraft.MagicBroom;
import com.kfhzs.allthingsflying.items.aerocraft.Rocket;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AllThingsFlying.MODID);

    public static final RegistryObject<Item> AERO_ENGINE = ITEMS.register("aero_engine", () -> Engine.aeroEngine(new Item.Properties()));
    public static final RegistryObject<Item> THERMAL_ENGINE = ITEMS.register("thermal_engine", () -> Engine.thermalEngine(new Item.Properties()));
    public static final RegistryObject<Item> MAGIC_ENGINE = ITEMS.register("magic_engine", () -> Engine.magicEngine(new Item.Properties()));
    public static final RegistryObject<Item> DRONE_ENGINE = ITEMS.register("drone_engine", () -> Engine.droneEngine(new Item.Properties()));
    public static final RegistryObject<Item> CLOUD_ENGINE = ITEMS.register("cloud_engine", () -> Engine.cloudEngine(new Item.Properties()));

    public static final RegistryObject<Item> UPGRADE_CORE = ITEMS.register("upgrade_core", ()-> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEAVY_UPGRADE_CORE = ITEMS.register("heavy_upgrade_core", ()-> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DRONE = ITEMS.register("drone", Drone::new);
    public static final RegistryObject<Item> ROCKET_PC1 = ITEMS.register("rocket_pc1", Rocket::new);
    public static final RegistryObject<Item> MAGIC_BROOM = ITEMS.register("magic_broom", MagicBroom::new);
    public static final RegistryObject<Item> FLYING_SWORD = ITEMS.register("flying_sword", FlyingSword::new);

    public static final RegistryObject<Item> FLIGHT_GIFT_PACKAGE = ITEMS.register("flight_gift_package", FlightGiftPackage::new);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
