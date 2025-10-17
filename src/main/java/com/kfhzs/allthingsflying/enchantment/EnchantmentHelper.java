package com.kfhzs.allthingsflying.enchantment;

import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentHelper {

    /**
     * 检查物品是否可以接受引擎相关的附魔
     */
    public static boolean canReceiveEngineEnchantments(ItemStack stack) {
        return EngineHelper.hasEngine(stack);
    }

    /**
     * 检查特定的附魔是否可以应用到有引擎的物品上
     */
    public static boolean isEngineRelatedEnchantment(Enchantment enchantment) {
        return enchantment instanceof EngineMendingEnchantment;
    }
}