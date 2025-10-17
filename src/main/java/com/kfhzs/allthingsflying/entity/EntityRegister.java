package com.kfhzs.allthingsflying.entity;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.aerocraft.broom.MagicBroomEntity;
import com.kfhzs.allthingsflying.entity.aerocraft.drone.DroneEntity;
import com.kfhzs.allthingsflying.entity.aerocraft.rocket.RocketEntity;
import com.kfhzs.allthingsflying.entity.aerocraft.sword.FlyingSwordEntity;
import com.kfhzs.allthingsflying.entity.item.AerocraftItemEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, AllThingsFlying.MODID);

    public static final RegistryObject<EntityType<AerocraftItemEntity>> RIDEABLE_ITEM =
            ENTITY_TYPES.register("rideable_item",
                    () -> EntityType.Builder.<AerocraftItemEntity>of(AerocraftItemEntity::new, MobCategory.MISC)
                            .clientTrackingRange(10)
                            .build("rideable_item"));

    public static final RegistryObject<EntityType<RocketEntity>> ROCKET_PC1 =
            ENTITY_TYPES.register("rocket_pc1",
                    () -> EntityType.Builder.<RocketEntity>of(RocketEntity::new, MobCategory.MISC)
                            .sized(3.0f,1.0f)
                            .clientTrackingRange(10)
                            .build("rocket_pc1"));

    public static final RegistryObject<EntityType<MagicBroomEntity>> MAGIC_BROOM =
            ENTITY_TYPES.register("magic_broom",
                    () -> EntityType.Builder.<MagicBroomEntity>of(MagicBroomEntity::new, MobCategory.MISC)
                            .sized(3.0f,0.5f)
                            .clientTrackingRange(10)
                            .build("magic_broom"));

    public static final RegistryObject<EntityType<DroneEntity>> DRONE =
            ENTITY_TYPES.register("drone",
                    () -> EntityType.Builder.<DroneEntity>of(DroneEntity::new, MobCategory.MISC)
                            .sized(3.0f,1.0f)
                            .clientTrackingRange(10)
                            .build("drone"));

    public static final RegistryObject<EntityType<FlyingSwordEntity>> FLYING_SWORD =
            ENTITY_TYPES.register("flying_sword",
                    () -> EntityType.Builder.<FlyingSwordEntity>of(FlyingSwordEntity::new, MobCategory.MISC)
                            .sized(3.0f,0.5f)
                            .clientTrackingRange(10)
                            .build("flying_sword"));


    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(name));
    }

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
