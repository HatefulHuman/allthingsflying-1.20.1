package com.kfhzs.allthingsflying.items.aerocraft;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AerocraftRender extends GeoItemRenderer<Aerocraft> {
    public AerocraftRender(String name) {
        super(new DefaultedItemGeoModel<>(ResourceLocation.tryBuild(AllThingsFlying.MODID, name)));
    }
}

