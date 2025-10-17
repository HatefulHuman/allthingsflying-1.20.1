package com.kfhzs.allthingsflying.datagen;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.datagen.language.CSJCNLanguageProvider;
import com.kfhzs.allthingsflying.datagen.language.CSJUSLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = AllThingsFlying.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new ModRecipesProvider(packOutput));
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(packOutput,existingFileHelper));
        generator.addProvider(event.includeClient(), new ModItemModelProvider(packOutput,existingFileHelper));
        generator.addProvider(event.includeServer(), new ModGlobalLootModifiersProvider(packOutput));

        event.getGenerator().addProvider(
                event.includeClient(),
                new CSJUSLanguageProvider(packOutput, AllThingsFlying.MODID, "en_us"));
        event.getGenerator().addProvider(
                event.includeClient(),
                new CSJCNLanguageProvider(packOutput, AllThingsFlying.MODID, "zh_cn"));

    }
}
