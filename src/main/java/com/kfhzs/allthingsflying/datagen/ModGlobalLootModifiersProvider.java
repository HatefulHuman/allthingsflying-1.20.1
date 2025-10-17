package com.kfhzs.allthingsflying.datagen;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.loot_modifier.FlightGiftPackageModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ModGlobalLootModifiersProvider  extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, AllThingsFlying.MODID);
    }

    @Override
    protected void start() {
        add("ghast_gift_package", new FlightGiftPackageModifier(
                new LootItemCondition[] {LootTableIdCondition.builder(ResourceLocation.parse("entities/ghast")).build()}));
    }
}