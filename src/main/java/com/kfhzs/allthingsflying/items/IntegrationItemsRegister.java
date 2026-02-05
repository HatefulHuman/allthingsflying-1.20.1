package com.kfhzs.allthingsflying.items;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.items.aerocraft.FlyingCarpet;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 模组联动物品注册类
 * 用于注册需要其他模组才能使用的联动内容
 */
public class IntegrationItemsRegister {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AllThingsFlying.MODID);

    // ========== 长生诀联动物品 ==========
    // 流光引擎 - 需要长生诀模组 (chang_sheng_jue)
    public static final RegistryObject<Item> STREAMER_ENGINE = ITEMS.register("streamer_engine",
            () -> Engine.streamerEngine(new Item.Properties()));

    // 飞毯 - 需要长生诀模组 (chang_sheng_jue)
    public static final RegistryObject<Item> FLYING_CARPET = ITEMS.register("flying_carpet", FlyingCarpet::new);

    /**
     * 注册联动物品到事件总线
     *
     * @param eventBus Mod事件总线
     */
    public static void register(IEventBus eventBus) {
        // 检查长生诀模组是否加载
        if (ModList.get().isLoaded("chang_sheng_jue")) {
            ITEMS.register(eventBus);
        }
    }

    /**
     * 检查长生诀模组是否加载
     *
     * @return 如果长生诀模组已加载返回true
     */
    public static boolean isChangShengJueLoaded() {
        return ModList.get().isLoaded("chang_sheng_jue");
    }
}
