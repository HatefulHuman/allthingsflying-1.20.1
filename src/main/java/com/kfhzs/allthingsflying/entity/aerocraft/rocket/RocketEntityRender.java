package com.kfhzs.allthingsflying.entity.aerocraft.rocket;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.aerocraft.broom.MagicBroomEntity;
import com.kfhzs.allthingsflying.entity.item.model.*;
import com.kfhzs.allthingsflying.entity.rendertype.CustomBlendRenderType;
import com.kfhzs.allthingsflying.items.IntegrationItemsRegister;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class RocketEntityRender extends GeoEntityRenderer<RocketEntity> {
    private final AerocraftBlockItemAeroEngineModel<RocketEntity> aeroEngineModel;
    private final AerocraftItemThermalEngineModel<RocketEntity> thermalEngineModel;
    private final AerocraftItemMagicEngineModel<RocketEntity> magicEngineModel;
    private final AerocraftItemDroneEngineModel<RocketEntity> droneEngineModel;
    private final AerocraftBlockItemStreamerEngineMod<MagicBroomEntity> streamerBlockEngineModel;
    // 纹理数组
    private static final ResourceLocation[] AERO_ENGINE_TEXTURES = createTextureArray("aero_engine", 16);
    private static final ResourceLocation[] THERMAL_ENGINE_TEXTURES = createTextureArray("thermal_engine", 20);
    private static final ResourceLocation[] MAGIC_ENGINE_TEXTURES = createTextureArray("magic_engine", 20);
    private static final ResourceLocation[] DRONE_ENGINE_TEXTURES = createTextureArray("drone_engine", 20);
    private static final ResourceLocation[] STREAMER_ENGINE_TEXTURES = createTextureArray("streamer_engine", 20);
    private static ResourceLocation[] createTextureArray(String engineName, int frameCount) {
        ResourceLocation[] textures = new ResourceLocation[frameCount];
        for (int i = 0; i < frameCount; i++) {
            textures[i] = ResourceLocation.tryBuild(AllThingsFlying.MODID,"textures/entity/" + engineName + "/" + engineName + "_" + (i + 1) + ".png");
        }
        return textures;
    }

    private static final float FRAME_DURATION = 0.05f;
    private static final boolean LOOP_ANIMATION = true;

    public RocketEntityRender(EntityRendererProvider.Context context) {
        super(context, new DefaultedEntityGeoModel<>(ResourceLocation.tryBuild(AllThingsFlying.MODID, "rocket_pc1")));
        this.aeroEngineModel = new AerocraftBlockItemAeroEngineModel<>(context.bakeLayer(AerocraftBlockItemAeroEngineModel.LAYER_LOCATION));
        this.thermalEngineModel = new AerocraftItemThermalEngineModel<>(context.bakeLayer(AerocraftItemThermalEngineModel.LAYER_LOCATION));
        this.magicEngineModel = new AerocraftItemMagicEngineModel<>(context.bakeLayer(AerocraftItemMagicEngineModel.LAYER_LOCATION));
        this.droneEngineModel = new AerocraftItemDroneEngineModel<>(context.bakeLayer(AerocraftItemDroneEngineModel.LAYER_LOCATION));
        this.streamerBlockEngineModel = new AerocraftBlockItemStreamerEngineMod<>(context.bakeLayer(AerocraftBlockItemStreamerEngineMod.LAYER_LOCATION));
    }

    @Override
    public void render(RocketEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float lerpYRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float lerpXRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(360 - lerpYRot));
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.AERO_ENGINE.get())) {
            this.renderAeroEngineModelEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.THERMAL_ENGINE.get())) {
            this.renderThermalEngineModelEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.MAGIC_ENGINE.get())) {
            this.renderMagicEngineEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.DRONE_ENGINE.get())) {
            this.renderDroneEngineEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (IntegrationItemsRegister.isChangShengJueLoaded() && EngineHelper.getEngineItem(entity.getItemStack()).is(IntegrationItemsRegister.STREAMER_ENGINE.get())) {
            this.renderStreamerEngineBlockItemModelEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        }
        poseStack.popPose();
    }

    private void renderAeroEngineModelEffect(RocketEntity entity, float partialTicks, PoseStack poseStack,
                                             MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F, 0.3F, -1.5F);
        poseStack.scale(0.25F, 0.25F, 1.0F);

        int currentFrame = getCurrentFrameIndex(entity, partialTicks, AERO_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());

        aeroEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        float alpha = calculateAlphaSmooth(entity);

        aeroEngineModel.renderToBuffer(poseStack,
                buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0.67F,
                0.94F,
                0.93F,
                alpha);

        poseStack.popPose();
    }

    private void renderThermalEngineModelEffect(RocketEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F,0.2F,3.2F);
        poseStack.scale(0.5F,0.5F,0.5F);

        int currentFrame = getCurrentFrameIndex(entity, partialTicks, THERMAL_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        thermalEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        thermalEngineModel.renderToBuffer(poseStack,
                buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                0.5F);
        poseStack.popPose();
    }


    private void renderMagicEngineEffect(RocketEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F, -1.6F, 4.0F);
        poseStack.scale(1.5F, 1.5F, 1.5F);

        int currentFrame = getCurrentFrameIndex(entity, partialTicks, MAGIC_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        magicEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        float alpha = calculateAlphaSmooth(entity);
        magicEngineModel.renderToBuffer(poseStack,
                buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                alpha);
        poseStack.popPose();
    }

    private void renderDroneEngineEffect(RocketEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.translate(0.0F,3.0F,-0.5F);
        poseStack.scale(0.25F,0.25F,0.25F);
        int currentFrame = getCurrentFrameIndex(entity, partialTicks, DRONE_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        this.droneEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        this.droneEngineModel.renderToBuffer(poseStack,
                    buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                0.7F);

        poseStack.popPose();
    }

    private void renderStreamerEngineBlockItemModelEffect(RocketEntity entity, float partialTicks, PoseStack poseStack,
                                                          MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(1.0F, 1.0F, 1.0F);
        poseStack.translate(0.0F, -1.5F, -2.5F);
        int currentFrame = getCurrentFrameIndex(entity, partialTicks, STREAMER_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        streamerBlockEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        float alpha = calculateAlphaSmooth(entity);

        streamerBlockEngineModel.renderToBuffer(poseStack,
                buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                1.0F,
                1.0F,
                1.0F,
                alpha);
        poseStack.popPose();
    }


    private float calculateAlphaSmooth(RocketEntity animatable) {
        float currentSpeed = Math.abs(animatable.getPlayerSpeed());
        float maxSpeed = animatable.getMaxSpeed();
        float speedRatio = Mth.clamp(currentSpeed / maxSpeed, 0.0f, 0.7f);
        return speedRatio * speedRatio;
    }

    private int getCurrentFrameIndex(RocketEntity animatable, float partialTick, int totalFrames) {
        float totalTime = (animatable.tickCount + partialTick) * 0.05f;
        if (LOOP_ANIMATION) {
            float animationTime = totalTime % (totalFrames * FRAME_DURATION);
            return Mth.floor(animationTime / FRAME_DURATION) % totalFrames;
        } else {
            int frame = Mth.floor(totalTime / FRAME_DURATION);
            return Math.min(frame, totalFrames - 1);
        }
    }

    private ResourceLocation getFrameTexture(int frameIndex, ItemStack stack) {
        if (EngineHelper.getEngineItem(stack).is(ItemsRegister.AERO_ENGINE.get())){
            if (frameIndex >= 0 && frameIndex < AERO_ENGINE_TEXTURES.length) {
                return AERO_ENGINE_TEXTURES[frameIndex];
            }
        } else if (EngineHelper.getEngineItem(stack).is(ItemsRegister.THERMAL_ENGINE.get())) {
            if (frameIndex >= 0 && frameIndex < THERMAL_ENGINE_TEXTURES.length) {
                return THERMAL_ENGINE_TEXTURES[frameIndex];
            }
        } else if (EngineHelper.getEngineItem(stack).is(ItemsRegister.MAGIC_ENGINE.get())) {
            if (frameIndex >= 0 && frameIndex < MAGIC_ENGINE_TEXTURES.length) {
                return MAGIC_ENGINE_TEXTURES[frameIndex];
            }
        } else if (EngineHelper.getEngineItem(stack).is(ItemsRegister.DRONE_ENGINE.get())) {
            if (frameIndex >= 0 && frameIndex < DRONE_ENGINE_TEXTURES.length) {
                return DRONE_ENGINE_TEXTURES[frameIndex];
            }
        } else if (IntegrationItemsRegister.isChangShengJueLoaded() && EngineHelper.getEngineItem(stack).is(IntegrationItemsRegister.STREAMER_ENGINE.get())) {
            if (frameIndex >= 0 && frameIndex < STREAMER_ENGINE_TEXTURES.length) {
                return STREAMER_ENGINE_TEXTURES[frameIndex];
            }
        }
        return AERO_ENGINE_TEXTURES[0];
    }
}