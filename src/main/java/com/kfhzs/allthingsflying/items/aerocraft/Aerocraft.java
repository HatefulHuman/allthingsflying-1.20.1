package com.kfhzs.allthingsflying.items.aerocraft;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;


public class Aerocraft extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public Aerocraft(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        // 如果有引擎，就可以附魔
        return EngineHelper.hasEngine(stack) || super.isEnchantable(stack);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return EngineHelper.hasEngine(stack) && EngineHelper.getEngineDurability(stack) < EngineHelper.getMaxEngineDurability(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (EngineHelper.hasEngine(stack)) {
            return EngineHelper.getEngineDurability(stack);
        } else {
            return super.getMaxDamage(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (!EngineHelper.hasEngine(stack)) {
            tooltip.add(Component.translatable("tooltip."+ AllThingsFlying.MODID +"aerocraft.tooltip"));
        } else {
            if (EngineHelper.getEngineDurability(stack) < EngineHelper.getMaxEngineDurability(stack)) {
                tooltip.add(Component.translatable("tooltip."+ AllThingsFlying.MODID +".engineDurability.tooltip",
                        EngineHelper.getEngineDurability(stack), EngineHelper.getMaxEngineDurability(stack)));
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

