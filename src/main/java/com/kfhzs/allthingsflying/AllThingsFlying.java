package com.kfhzs.allthingsflying;

import com.kfhzs.allthingsflying.enchantment.ModEnchantments;
import com.kfhzs.allthingsflying.entity.EntityRegister;
import com.kfhzs.allthingsflying.entity.aerocraft.broom.MagicBroomEntityRender;
import com.kfhzs.allthingsflying.entity.aerocraft.carpet.FlyingCarpetEntityRender;
import com.kfhzs.allthingsflying.entity.aerocraft.drone.DroneEntityRender;
import com.kfhzs.allthingsflying.entity.aerocraft.rocket.RocketEntityRender;
import com.kfhzs.allthingsflying.entity.aerocraft.sword.FlyingSwordEntityRender;
import com.kfhzs.allthingsflying.entity.item.AerocraftItemRenderer;
import com.kfhzs.allthingsflying.entity.item.model.*;
import com.kfhzs.allthingsflying.items.IntegrationItemsRegister;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.loot_modifier.ModLootModifier;
import com.kfhzs.allthingsflying.network.NetworkHandler;
import com.kfhzs.allthingsflying.particle.ModParticles;
import com.kfhzs.allthingsflying.recipe.ModRecipeTypes;
import com.kfhzs.allthingsflying.sound.ModSounds;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

@Mod(AllThingsFlying.MODID)
public class AllThingsFlying {

    public static final String MODID = "allthingsflying";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> ALL_THINGS_FLYING_TAB = CREATIVE_MODE_TABS.register("allthingsflying_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MODID + ".allthingsflying_tab")).icon(() -> ItemsRegister.THERMAL_ENGINE.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ItemsRegister.DRONE.get());
                output.accept(ItemsRegister.ROCKET_PC1.get());
                output.accept(ItemsRegister.MAGIC_BROOM.get());
                output.accept(ItemsRegister.FLYING_SWORD.get());

                // 长生诀联动飞行器
                if (IntegrationItemsRegister.isChangShengJueLoaded()) {
                    output.accept(IntegrationItemsRegister.FLYING_CARPET.get());
                }

                output.accept(ItemsRegister.AERO_ENGINE.get());
                output.accept(ItemsRegister.THERMAL_ENGINE.get());
                output.accept(ItemsRegister.MAGIC_ENGINE.get());

                output.accept(ItemsRegister.DRONE_ENGINE.get());
                output.accept(ItemsRegister.CLOUD_ENGINE.get());

                // 长生诀联动引擎
                if (IntegrationItemsRegister.isChangShengJueLoaded()) {
                    output.accept(IntegrationItemsRegister.STREAMER_ENGINE.get());
                }

                output.accept(ItemsRegister.UPGRADE_CORE.get());

                output.accept(ItemsRegister.HEAVY_UPGRADE_CORE.get());

                output.accept(ItemsRegister.FLIGHT_GIFT_PACKAGE.get());
            }).build());

    public AllThingsFlying() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onClientSetup);
        modEventBus.addListener(this::registerLayerDefinitions);
        modEventBus.addListener(this::addCreative);

        CREATIVE_MODE_TABS.register(modEventBus);
        ItemsRegister.register(modEventBus);
        IntegrationItemsRegister.register(modEventBus); // 注册联动物品
        EntityRegister.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEnchantments.register(modEventBus);
        ModSounds.register(modEventBus);
        ModLootModifier.register(modEventBus);

        // 注册配方序列化器
        ModRecipeTypes.register(modEventBus);
        NetworkHandler.register();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    public void onClientSetup(final FMLClientSetupEvent event) {
        EntityRenderers.register(EntityRegister.RIDEABLE_ITEM.get(), AerocraftItemRenderer::new);
        EntityRenderers.register(EntityRegister.ROCKET_PC1.get(), RocketEntityRender::new);
        EntityRenderers.register(EntityRegister.MAGIC_BROOM.get(), MagicBroomEntityRender::new);
        EntityRenderers.register(EntityRegister.DRONE.get(), DroneEntityRender::new);
        EntityRenderers.register(EntityRegister.FLYING_SWORD.get(), FlyingSwordEntityRender::new);
        EntityRenderers.register(EntityRegister.FLYING_CARPET.get(), FlyingCarpetEntityRender::new);
    }

    public void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AerocraftItemAeroEngineModel.LAYER_LOCATION, AerocraftItemAeroEngineModel::createBodyLayer);
        event.registerLayerDefinition(AerocraftBlockItemAeroEngineModel.LAYER_LOCATION, AerocraftBlockItemAeroEngineModel::createBodyLayer);
        event.registerLayerDefinition(AerocraftItemThermalEngineModel.LAYER_LOCATION, AerocraftItemThermalEngineModel::createBodyLayer);
        event.registerLayerDefinition(AerocraftItemMagicEngineModel.LAYER_LOCATION, AerocraftItemMagicEngineModel::createBodyLayer);
        event.registerLayerDefinition(AerocraftItemDroneEngineModel.LAYER_LOCATION, AerocraftItemDroneEngineModel::createBodyLayer);
        event.registerLayerDefinition(AerocraftBedItemDroneEngineMod.LAYER_LOCATION, AerocraftBedItemDroneEngineMod::createBodyLayer);
        event.registerLayerDefinition(AerocraftItemStreamerEngineMod.LAYER_LOCATION, AerocraftItemStreamerEngineMod::createBodyLayer);
        event.registerLayerDefinition(AerocraftBlockItemStreamerEngineMod.LAYER_LOCATION, AerocraftBlockItemStreamerEngineMod::createBodyLayer);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

}
