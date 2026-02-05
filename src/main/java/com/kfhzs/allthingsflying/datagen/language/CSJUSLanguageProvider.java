package com.kfhzs.allthingsflying.datagen.language;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.enchantment.ModEnchantments;
import com.kfhzs.allthingsflying.entity.EntityRegister;
import com.kfhzs.allthingsflying.items.IntegrationItemsRegister;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class CSJUSLanguageProvider extends LanguageProvider {
    public CSJUSLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        this.add(ItemsRegister.AERO_ENGINE.get(), "破空引擎");
        this.add(ItemsRegister.THERMAL_ENGINE.get(), "热能引擎");
        this.add(ItemsRegister.MAGIC_ENGINE.get(), "魔力引擎");
        this.add(ItemsRegister.DRONE_ENGINE.get(), "无人机引擎");
        this.add(ItemsRegister.CLOUD_ENGINE.get(), "腾云引擎");

        // 联动物品翻译
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            this.add(IntegrationItemsRegister.STREAMER_ENGINE.get(), "流光引擎");
        }

        this.add(ItemsRegister.UPGRADE_CORE.get(), "升级核心");
        this.add(ItemsRegister.HEAVY_UPGRADE_CORE.get(), "重型升级核心");

        this.add(ItemsRegister.FLIGHT_GIFT_PACKAGE.get(), "飞行大礼包");

        this.add(ItemsRegister.DRONE.get(), "破空引擎");
        this.add(ItemsRegister.ROCKET_PC1.get(), "火箭PC1");
        this.add(ItemsRegister.MAGIC_BROOM.get(), "神奇扫帚");
        this.add(ItemsRegister.FLYING_SWORD.get(), "飞剑");

        // 联动物品翻译
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            this.add(IntegrationItemsRegister.FLYING_CARPET.get(), "飞毯");
        }

        this.add("tooltip." + AllThingsFlying.MODID + "aerocraft.tooltip", "§6需要引擎来合成飞行器");
        this.add("tooltip." + AllThingsFlying.MODID + ".engineDurability.tooltip", "飞行器耐久: %s / %s");

        this.add("message." + AllThingsFlying.MODID + "." + ItemsRegister.FLIGHT_GIFT_PACKAGE.get() + ".engineName.message", "§6右键随机获得一种飞行器模具");
        this.add("message." + AllThingsFlying.MODID + "." + ItemsRegister.FLIGHT_GIFT_PACKAGE.get() + ".prompt.message", "§6开启飞行大礼包！§a随机飞行模具：§e%s * %s");
        this.add("tooltip." + AllThingsFlying.MODID + "." + ItemsRegister.FLIGHT_GIFT_PACKAGE.get() + ".tooltip", "§6右键随机获得一种飞行器模具");

        this.add("itemGroup." + AllThingsFlying.MODID + ".allthingsflying_tab", "万物皆可飞行");

        this.add(EntityRegister.RIDEABLE_ITEM.get(), "%s");

        this.add(EntityRegister.ROCKET_PC1.get(), "火箭PC1");
        this.add(EntityRegister.MAGIC_BROOM.get(), "神奇扫帚");
        this.add(EntityRegister.DRONE.get(), "无人机");
        this.add(EntityRegister.FLYING_SWORD.get(), "飞剑");

        // 联动实体翻译
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            this.add(EntityRegister.FLYING_CARPET.get(), "飞毯");
        }

        this.add(ModEnchantments.ENGINE_MENDING.get(), "引擎修复");

        this.add("sounds." + AllThingsFlying.MODID + ".aero_engine_sound", "飞行: 破空");
        this.add("sounds." + AllThingsFlying.MODID + ".cloud_engine_sound", "飞行: 腾云");
        this.add("sounds." + AllThingsFlying.MODID + ".drone_engine_sound", "飞行: 无人机");
        this.add("sounds." + AllThingsFlying.MODID + ".magic_engine_sound", "飞行: 魔力");
        this.add("sounds." + AllThingsFlying.MODID + ".thermal_engine_sound", "飞行: 热能");
        this.add("sounds." + AllThingsFlying.MODID + ".streamer_engine_sound", "飞行: 流光");

        this.add("tooltip.actions.handoff.tooltip", "右键释放%s,可骑乘飞行\\n飞行时使用鼠标控制方向,按下左右方向的移动键来切换飞行动作!");
    }
}
