package com.kfhzs.allthingsflying.entity.aerocraft.broom;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.item.model.AerocraftBlockItemAeroEngineModel;
import com.kfhzs.allthingsflying.entity.item.model.AerocraftItemDroneEngineMod;
import com.kfhzs.allthingsflying.entity.item.model.AerocraftItemMagicEngineMod;
import com.kfhzs.allthingsflying.entity.item.model.AerocraftItemThermalEngineMod;
import com.kfhzs.allthingsflying.entity.rendertype.CustomBlendRenderType;
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
public class MagicBroomEntityRender extends GeoEntityRenderer<MagicBroomEntity> {
    private final AerocraftBlockItemAeroEngineModel<MagicBroomEntity> aeroEngineModel;
    private final AerocraftItemThermalEngineMod<MagicBroomEntity> thermalEngineModel;
    private final AerocraftItemMagicEngineMod<MagicBroomEntity> magicEngineModel;
    private final AerocraftItemDroneEngineMod<MagicBroomEntity> droneEngineModel;
    // 纹理数组
    private static final ResourceLocation[] AERO_ENGINE_TEXTURES = createTextureArray("aero_engine", 16);
    private static final ResourceLocation[] THERMAL_ENGINE_TEXTURES = createTextureArray("thermal_engine", 20);
    private static final ResourceLocation[] MAGIC_ENGINE_TEXTURES = createTextureArray("magic_engine", 20);
    private static final ResourceLocation[] DRONE_ENGINE_TEXTURES = createTextureArray("drone_engine", 20);

    private static ResourceLocation[] createTextureArray(String engineName, int frameCount) {
        ResourceLocation[] textures = new ResourceLocation[frameCount];
        for (int i = 0; i < frameCount; i++) {
            textures[i] = ResourceLocation.fromNamespaceAndPath(AllThingsFlying.MODID, "textures/entity/" + engineName + "/" + engineName + "_" + (i + 1) + ".png");
        }
        return textures;
    }

    private static final float FRAME_DURATION = 0.05f;
    private static final boolean LOOP_ANIMATION = true;

    public MagicBroomEntityRender(EntityRendererProvider.Context context) {
        super(context, new DefaultedEntityGeoModel<>(ResourceLocation.tryBuild(AllThingsFlying.MODID, "magic_broom")));
        this.aeroEngineModel = new AerocraftBlockItemAeroEngineModel<>(context.bakeLayer(AerocraftBlockItemAeroEngineModel.LAYER_LOCATION));
        this.thermalEngineModel = new AerocraftItemThermalEngineMod<>(context.bakeLayer(AerocraftItemThermalEngineMod.LAYER_LOCATION));
        this.magicEngineModel = new AerocraftItemMagicEngineMod<>(context.bakeLayer(AerocraftItemMagicEngineMod.LAYER_LOCATION));
        this.droneEngineModel = new AerocraftItemDroneEngineMod<>(context.bakeLayer(AerocraftItemDroneEngineMod.LAYER_LOCATION));
    }

    @Override
    public void render(MagicBroomEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        float lerpYRot = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
        float lerpXRot = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
        poseStack.mulPose(Axis.YP.rotationDegrees(360 - lerpYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(lerpXRot));
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.AERO_ENGINE.get())) {
            this.renderAeroEngineModelEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.THERMAL_ENGINE.get())) {
            this.renderThermalEngineModelEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.MAGIC_ENGINE.get())) {
            this.renderMagicEngineEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.DRONE_ENGINE.get())) {
            this.renderDroneEngineEffect(entity, partialTick, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        }
        poseStack.popPose();
    }

    private void renderAeroEngineModelEffect(MagicBroomEntity entity, float partialTicks, PoseStack poseStack,
                                             MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F, 0.3F, -1.5F);
        poseStack.scale(0.125F, 0.125F, 0.5F);

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

    private void renderThermalEngineModelEffect(MagicBroomEntity entity, float partialTicks, PoseStack poseStack,
                                                MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F, 0.2F, 1.5F);
        poseStack.scale(0.5F, 0.5F, 0.5F);

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
                0.5f);
        poseStack.popPose();
    }

    private void renderMagicEngineEffect(MagicBroomEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));

        poseStack.translate(0.0F, -0.5F, 1.75F);
        poseStack.scale(0.5F, 0.5F, 0.5F);

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

    private void renderDroneEngineEffect(MagicBroomEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.XP.rotationDegrees(90));

        poseStack.translate(0.0F, 2.0F, -0.5F);
        poseStack.scale(0.15F, 0.15F, 0.15F);
        int currentFrame = getCurrentFrameIndex(entity, partialTicks, DRONE_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        this.droneEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        float alpha = calculateAlphaSmooth(entity);
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

    private float calculateAlphaSmooth(MagicBroomEntity animatable) {
        float currentSpeed = Math.abs(animatable.getPlayerSpeed());
        float maxSpeed = animatable.getMaxSpeed();
        float speedRatio = Mth.clamp(currentSpeed / maxSpeed, 0.0f, 0.7f);
        return speedRatio * speedRatio;
    }

    private int getCurrentFrameIndex(MagicBroomEntity animatable, float partialTick, int totalFrames) {
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
        if (EngineHelper.getEngineItem(stack).is(ItemsRegister.AERO_ENGINE.get())) {
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
        }
        return AERO_ENGINE_TEXTURES[0];
    }
}