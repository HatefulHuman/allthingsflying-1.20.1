package com.kfhzs.allthingsflying.recipe;

import com.kfhzs.allthingsflying.capability.IEngineCapability;
import com.kfhzs.allthingsflying.capability.ModCapabilities;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;

public class EngineHelper {
    /**
     * 检查物品是否已经有引擎
     */
    public static boolean hasEngine(ItemStack stack) {
        if (stack.isEmpty()) return false;
        // 优先检查Capability
        if (stack.getCapability(ModCapabilities.ENGINE_CAPABILITY).isPresent()) {
            return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                    .map(IEngineCapability::hasEngine)
                    .orElse(false);
        }
        return false;
    }

    /**
     * 获取引擎耐久
     */
    public static int getEngineDurability(ItemStack stack) {
        if (!hasEngine(stack)) return 2564;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getEngineDurability)
                .orElse(2564);
    }

    /**
     * 获取引擎耐久
     */
    public static int getMaxEngineDurability(ItemStack stack) {
        if (!hasEngine(stack)) return 2564;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getMaxEngineDurability)
                .orElse(2564);
    }

    public static void setEngineDurability(ItemStack stack, int amount) {
        stack.getCapability(ModCapabilities.ENGINE_CAPABILITY).ifPresent(cap -> cap.setEngineDurability(amount));
    }

    /**
     * 获取引擎功率
     */
    public static double getPower(ItemStack stack) {
        if (!hasEngine(stack)) return 0.0;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getPower)
                .orElse(0.0);
    }

    /**
     * 获取飞行速度
     */
    public static float getFlightSpeed(ItemStack stack) {
        if (!hasEngine(stack)) return 0.1f; // 默认速度

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getFlightSpeed)
                .orElse(0.1f);
    }

    /**
     * 获取阻力系数
     */
    public static float getFriction(ItemStack stack) {
        if (!hasEngine(stack)) return 0.1f; // 默认速度

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getFriction)
                .orElse(0.1f);
    }

    /**
     * 获取停止阈值
     */
    public static float getStopThreshold(ItemStack stack) {
        if (!hasEngine(stack)) return 0.1f; // 默认速度

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getStopThreshold)
                .orElse(0.1f);
    }

    /**
     * 获取飞行加速度
     */
    public static float getAcceleration(ItemStack stack) {
        if (!hasEngine(stack)) return 0.1f; // 默认速度

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getAcceleration)
                .orElse(0.1f);
    }

    /**
     * 获取飞行引擎
     */
    public static ItemStack getEngineItem(ItemStack stack) {
        if (!hasEngine(stack)) return ItemStack.EMPTY; // 默认速度

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getEngineItem)
                .orElse(null);
    }

    /**
     * 获取飞行等级
     */
    public static int getEngineLevel(ItemStack stack) {
        if (!hasEngine(stack)) return 1;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getEngineLevel)
                .orElse(1);
    }

    /**
     * 获取飞行音效播放时间
     */
    public static int getEngineSoundTime(ItemStack stack) {
        if (!hasEngine(stack)) return 0;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getEngineSoundTime)
                .orElse(0);
    }

    /**
     * 获取飞行音效
     */
    public static SoundEvent getEngineSound(ItemStack stack) {
        if (!hasEngine(stack)) return null;

        return stack.getCapability(ModCapabilities.ENGINE_CAPABILITY)
                .map(IEngineCapability::getEngineSound)
                .orElse(null);
    }

}