package com.kfhzs.allthingsflying.mixin;

import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemEnchantmentMixin {

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void onIsEnchantable(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        // 如果物品有引擎，就让它可附魔
        if (EngineHelper.hasEngine(stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getEnchantmentValue", at = @At("HEAD"), cancellable = true)
    private void onGetEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(1);
    }
}