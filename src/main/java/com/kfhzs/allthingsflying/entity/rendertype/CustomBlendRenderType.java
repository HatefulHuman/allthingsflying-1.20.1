package com.kfhzs.allthingsflying.entity.rendertype;


import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class CustomBlendRenderType extends RenderType {

    private static final Function<ResourceLocation, RenderType> BREAKING_THE_WIND = Util.memoize((resourceLocation) -> {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(resourceLocation, true, false);
        return RenderType.create("breaking_the_wind",
                DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setTextureState(renderstateshard$texturestateshard)
                        .setLayeringState(VIEW_OFFSET_Z_LAYERING)
                        .setTransparencyState(LIGHTNING_TRANSPARENCY)
                        .setDepthTestState(LEQUAL_DEPTH_TEST)
                        .setCullState(RenderStateShard.NO_CULL)
                        .setLightmapState(NO_LIGHTMAP)
                        .setWriteMaskState(COLOR_WRITE)
                        .setShaderState(RENDERTYPE_ENTITY_SHADOW_SHADER)
                        .createCompositeState(false));
    });

    public static RenderType wind(ResourceLocation texture) {
        return BREAKING_THE_WIND.apply(texture);
    }

    protected CustomBlendRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }
}
