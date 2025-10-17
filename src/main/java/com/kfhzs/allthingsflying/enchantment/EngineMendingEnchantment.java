package com.kfhzs.allthingsflying.enchantment;

import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.level.Level;

public class EngineMendingEnchantment extends Enchantment {

    // 创建自定义的附魔类别，检查物品是否有引擎
    public static final EnchantmentCategory HAS_ENGINE = EnchantmentCategory.create("has_engine", item -> true);

    public EngineMendingEnchantment() {
        super(Rarity.RARE, HAS_ENGINE, new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND});
    }

    @Override
    public int getMinCost(int level) {
        return 1 + (level - 1) * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 5;
    }

    @Override
    public int getMinLevel() {
        return super.getMinLevel();
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return EngineHelper.hasEngine(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return canEnchant(stack);
    }


    /**
     * 检查是否应该修复引擎耐久
     */
    public static boolean shouldRepairEngine(Player player, ItemStack itemStack) {
        if (player == null || itemStack.isEmpty()) return false;

        // 检查是否有引擎修复附魔
        return itemStack.getEnchantmentLevel(ModEnchantments.ENGINE_MENDING.get()) > 0;
    }

    /**
     * 尝试修复引擎耐久
     * @return 是否成功修复
     */
    public static boolean tryRepairEngine(Player player, ItemStack itemStack, Level level) {
        if (!shouldRepairEngine(player, itemStack)) return false;

        if (!EngineHelper.hasEngine(itemStack)) return false;

        int currentDurability = EngineHelper.getEngineDurability(itemStack);
        int maxDurability = EngineHelper.getMaxEngineDurability(itemStack);

        // 如果耐久已经满了，不需要修复
        if (currentDurability >= maxDurability) return false;

        // 检查玩家是否有经验
        if (player.totalExperience <= 0) return false;

        // 计算需要修复的耐久量（每次修复最多5点，平衡性考虑）
        int repairAmount = Math.min(5, maxDurability - currentDurability);
        int experienceCost = Math.min(repairAmount, player.totalExperience);

        if (experienceCost <= 0) return false;

        // 修复耐久
        int newDurability = currentDurability + experienceCost;
        EngineHelper.setEngineDurability(itemStack, Math.min(newDurability, maxDurability));

        // 消耗经验
        player.giveExperiencePoints(-experienceCost);

        return true;
    }

}