package com.kfhzs.allthingsflying.event;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.items.IntegrationItemsRegister;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = AllThingsFlying.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class SimpleTooltipHandler {

    /**
     * 获取所有可骑乘的飞行器物品（运行时动态生成）
     */
    private static Set<Item> getRideableItems() {
        Set<Item> items = new HashSet<>();
        items.add(ItemsRegister.ROCKET_PC1.get());
        items.add(ItemsRegister.MAGIC_BROOM.get());
        items.add(ItemsRegister.DRONE.get());
        items.add(ItemsRegister.FLYING_SWORD.get());

        // 添加联动物品
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            items.add(IntegrationItemsRegister.FLYING_CARPET.get());
        }

        return items;
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        Set<Item> RIDEABLE_ITEMS = getRideableItems();

        if (EngineHelper.hasEngine(stack) && !RIDEABLE_ITEMS.contains(stack.getItem())) {
            int current = EngineHelper.getEngineDurability(stack);
            int max = EngineHelper.getMaxEngineDurability(stack);

            // 在工具提示中显示耐久信息
            if (current < max) {
                event.getToolTip().add(Component.translatable("tooltip."+ AllThingsFlying.MODID +".engineDurability.tooltip",current, max));
            }
        }
    }

}