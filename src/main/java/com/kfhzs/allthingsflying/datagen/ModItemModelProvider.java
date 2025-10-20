package com.kfhzs.allthingsflying.datagen;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, AllThingsFlying.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem((ItemsRegister.AERO_ENGINE));
        simpleItem((ItemsRegister.THERMAL_ENGINE));
        simpleItem((ItemsRegister.MAGIC_ENGINE));
        simpleItem((ItemsRegister.DRONE_ENGINE));
        simpleItem((ItemsRegister.CLOUD_ENGINE));
        simpleItem((ItemsRegister.UPGRADE_CORE));
        simpleItem((ItemsRegister.HEAVY_UPGRADE_CORE));
        simpleItem((ItemsRegister.FLIGHT_GIFT_PACKAGE));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item){
        return withExistingParent(item.getId().getPath(),
                ResourceLocation.parse("item/generated")).texture("layer0",
                ResourceLocation.tryBuild(AllThingsFlying.MODID,"item/"+item.getId().getPath()));
    }

}
