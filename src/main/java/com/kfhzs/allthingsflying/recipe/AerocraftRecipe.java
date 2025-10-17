package com.kfhzs.allthingsflying.recipe;

import com.kfhzs.allthingsflying.capability.ModCapabilities;
import com.kfhzs.allthingsflying.items.Engine;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.items.aerocraft.Aerocraft;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class AerocraftRecipe extends CustomRecipe {

    public AerocraftRecipe(ResourceLocation resourceLocation, CraftingBookCategory craftingBookCategory) {
        super(resourceLocation, craftingBookCategory);
    }

    @Override
    public boolean matches(CraftingContainer container, @NotNull Level level) {
        int itemCount = 0;
        boolean hasEngine = false;
        boolean hasOtherItem = false;
        boolean hasUpgradeCore = false;
        boolean hasAerocraftItem = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                itemCount++;
                if (isEngine(stack)) {
                    hasEngine = true;
                } else if (isUpgradeCore(stack)) {
                    hasUpgradeCore = true;
                } else if (hasEngine(stack)) {
                    hasAerocraftItem = true;
                } else {
                    hasOtherItem = true;
                }
            }
        }

        boolean makeAerocraft = itemCount == 2 && hasEngine && hasOtherItem && !hasAerocraftItem && !hasUpgradeCore;
        boolean upgradeEngine = itemCount == 2 && hasUpgradeCore && hasAerocraftItem && !hasEngine && !hasOtherItem;
        return makeAerocraft || upgradeEngine;
    }

    @Override
    public @NotNull ItemStack assemble(CraftingContainer container, @NotNull RegistryAccess registryAccess) {
        if (isUpgradeRecipe(container)) {
            return assembleUpgrade(container);
        } else {
            return assembleAerocraft(container);
        }
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.UNIVERSAL_FLYING_SERIALIZER.get();
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
        NonNullList<ItemStack> remaining = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);

        for (int i = 0; i < remaining.size(); ++i) {
            ItemStack item = container.getItem(i);
            if (item.hasCraftingRemainingItem()) {
                remaining.set(i, item.getCraftingRemainingItem());
            } else if (!item.isEmpty()) {
                remaining.set(i, ItemStack.EMPTY);
            }
        }

        return remaining;
    }

    private ItemStack assembleAerocraft(CraftingContainer container) {
        ItemStack engine = ItemStack.EMPTY;
        ItemStack material = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (isEngine(stack)) {
                    engine = stack;
                } else {
                    material = stack;
                }
            }
        }

        if (!engine.isEmpty() && !material.isEmpty() && !hasEngine(material)) {
            // 创建新的飞行器物品，而不是修改原物品
            ItemStack flyingItem = new ItemStack(material.getItem(), 1);

            // 复制原物品的NBT
            if (material.hasTag()) {
                if (material.getTag() != null) {
                    flyingItem.setTag(material.getTag().copy());
                }
            }

            // 设置引擎能力
            ItemStack finalEngine = engine;

            int initialLevel = EngineHelper.getEngineLevel(finalEngine);
            flyingItem.getCapability(ModCapabilities.ENGINE_CAPABILITY).ifPresent(engineCap -> {
                engineCap.setEngineItem(finalEngine);
            });

            // 设置名称，包含初始等级
            String baseName = material.getHoverName().getString();
            String displayName = baseName + " §6" + finalEngine.getDisplayName().getString() + " §eLv." + initialLevel;
            flyingItem.setHoverName(Component.literal(displayName));

            return flyingItem;
        }

        return ItemStack.EMPTY;
    }

    private ItemStack assembleUpgrade(CraftingContainer container) {
        ItemStack aerocraftItem = ItemStack.EMPTY;
        ItemStack upgradeCore = ItemStack.EMPTY;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (hasEngine(stack)) {
                    aerocraftItem = stack;
                } else if (isUpgradeCore(stack)) {
                    upgradeCore = stack;
                }
            }
        }

        if (!aerocraftItem.isEmpty() && !upgradeCore.isEmpty()) {
            ItemStack upgradedItem = aerocraftItem.copy();

            ItemStack currentEngine = EngineHelper.getEngineItem(aerocraftItem);
            if (currentEngine.isEmpty()) {
                return ItemStack.EMPTY;
            }

            int currentLevel = EngineHelper.getEngineLevel(aerocraftItem);

            if (!isValidUpgradeCore(upgradeCore, currentLevel)) {
                return ItemStack.EMPTY;
            }

            int newLevel = currentLevel + 1;

            upgradedItem.getCapability(ModCapabilities.ENGINE_CAPABILITY).ifPresent(engineCap -> engineCap.setEngineLevel(newLevel));

            // 更新名称中的等级数字
            Component originalName = aerocraftItem.getHoverName();
            String nameString = originalName.getString();

            // 替换等级数字
            String newName = nameString.replaceAll("Lv\\.\\d+", "Lv." + newLevel);
            upgradedItem.setHoverName(Component.literal(newName));

            return upgradedItem;
        }

        return ItemStack.EMPTY;
    }

    private boolean isUpgradeRecipe(CraftingContainer container) {
        boolean hasUpgradeCore = false;
        boolean hasAerocraftItem = false;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (!stack.isEmpty()) {
                if (isUpgradeCore(stack)) {
                    hasUpgradeCore = true;
                } else if (hasEngine(stack)) {
                    hasAerocraftItem = true;
                }
            }
        }

        return hasUpgradeCore && hasAerocraftItem;
    }

    private boolean isEngine(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof Engine;
    }

    private boolean isValidUpgradeCore(ItemStack upgradeCore, int currentLevel) {
        if (currentLevel >= 1 && currentLevel < 7) {
            return upgradeCore.is(ItemsRegister.UPGRADE_CORE.get());
        } else if (currentLevel >= 7) {
            return upgradeCore.is(ItemsRegister.HEAVY_UPGRADE_CORE.get());
        }
        return false;
    }

    private boolean isUpgradeCore(ItemStack stack) {
        return stack.is(ItemsRegister.UPGRADE_CORE.get()) || stack.is(ItemsRegister.HEAVY_UPGRADE_CORE.get());
    }

    private boolean hasEngine(ItemStack stack) {
        return EngineHelper.hasEngine(stack);
    }

}