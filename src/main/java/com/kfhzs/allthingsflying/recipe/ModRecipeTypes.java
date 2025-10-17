package com.kfhzs.allthingsflying.recipe;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeTypes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, AllThingsFlying.MODID);
    // 使用正确的序列化器类
    public static final RegistryObject<RecipeSerializer<AerocraftRecipe>> UNIVERSAL_FLYING_SERIALIZER =
            SERIALIZERS.register("universal_flying", () -> new SimpleCraftingRecipeSerializer<>(AerocraftRecipe::new));
    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
    }
}