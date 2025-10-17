package com.kfhzs.allthingsflying.entity.item;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.item.model.*;
import com.kfhzs.allthingsflying.entity.rendertype.CustomBlendRenderType;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AerocraftItemRenderer extends EntityRenderer<AerocraftItemEntity> {
    private final ItemRenderer itemRenderer;
    private final AerocraftItemAeroEngineModel<AerocraftItemEntity> aeroEngineModel;
    private final AerocraftBlockItemAeroEngineModel<AerocraftItemEntity> blockAeroEngineModel;
    private final AerocraftItemThermalEngineMod<AerocraftItemEntity> thermalEngineModel;
    private final AerocraftItemMagicEngineMod<AerocraftItemEntity> magicEngineModel;
    private final AerocraftItemDroneEngineMod<AerocraftItemEntity> droneEngineModel;
    private final AerocraftBedItemDroneEngineMod<AerocraftItemEntity> droneEngineBedModel;

    // 纹理数组
    private static final ResourceLocation[] AERO_ENGINE_TEXTURES = createTextureArray("aero_engine", 16);
    private static final ResourceLocation[] THERMAL_ENGINE_TEXTURES = createTextureArray("thermal_engine", 20);
    private static final ResourceLocation[] MAGIC_ENGINE_TEXTURES = createTextureArray("magic_engine", 20);
    private static final ResourceLocation[] DRONE_ENGINE_TEXTURES = createTextureArray("drone_engine", 20);
    private static ResourceLocation[] createTextureArray(String engineName, int frameCount) {
        ResourceLocation[] textures = new ResourceLocation[frameCount];
        for (int i = 0; i < frameCount; i++) {
            textures[i] = ResourceLocation.fromNamespaceAndPath(AllThingsFlying.MODID,
                    "textures/entity/" + engineName + "/" + engineName + "_" + (i + 1) + ".png");
        }
        return textures;
    }


    private static final float FRAME_DURATION = 0.05f;
    private static final boolean LOOP_ANIMATION = true;

    public AerocraftItemRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
        this.aeroEngineModel = new AerocraftItemAeroEngineModel<>(context.bakeLayer(AerocraftItemAeroEngineModel.LAYER_LOCATION));
        this.blockAeroEngineModel = new AerocraftBlockItemAeroEngineModel<>(context.bakeLayer(AerocraftBlockItemAeroEngineModel.LAYER_LOCATION));
        this.thermalEngineModel = new AerocraftItemThermalEngineMod<>(context.bakeLayer(AerocraftItemThermalEngineMod.LAYER_LOCATION));
        this.magicEngineModel = new AerocraftItemMagicEngineMod<>(context.bakeLayer(AerocraftItemMagicEngineMod.LAYER_LOCATION));
        this.droneEngineModel = new AerocraftItemDroneEngineMod<>(context.bakeLayer(AerocraftItemDroneEngineMod.LAYER_LOCATION));
        this.droneEngineBedModel = new AerocraftBedItemDroneEngineMod<>(context.bakeLayer(AerocraftBedItemDroneEngineMod.LAYER_LOCATION));
    }

    @Override
    public void render(AerocraftItemEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // 渲染原始物品
        poseStack.pushPose();
        float lerpYRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float lerpXRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
        Item item = entity.getItemStack().getItem();
        poseStack.mulPose(Axis.YP.rotationDegrees(360 - lerpYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(item instanceof BlockItem blockItem
                && !(blockItem.getBlock() instanceof BushBlock)
                && !(blockItem.getBlock() instanceof SignBlock)
                && !(blockItem.getBlock() instanceof LadderBlock)
                && !(blockItem.getBlock() instanceof DoorBlock) ? 0 : 90 + lerpXRot));
        poseStack.scale(1.5f, 1.5f, 1.5f);
        poseStack.translate(0.0F, 0.0F, -0.1F);
        if (item instanceof TieredItem || item instanceof Vanishable) {
            if (item instanceof TridentItem) {
                poseStack.translate(0.5F, 1.5F, 0.5F);
            } else {
                poseStack.mulPose(Axis.ZP.rotationDegrees(item instanceof Equipable ? 0 : 45f));
            }
        } else if (item instanceof BlockItem blockItem
                && !(blockItem.getBlock() instanceof SignBlock)
                && !(blockItem.getBlock() instanceof DoorBlock)) {
            if (blockItem.getBlock() instanceof SkullBlock && !item.getDefaultInstance().is(Blocks.DRAGON_HEAD.asItem())) {
                poseStack.scale(2.0f, 2.0f, 2.0f);
                poseStack.translate(0.0F, 0.5F, 0.05F);
            } else if (item.getDefaultInstance().is(Blocks.DRAGON_HEAD.asItem())) {
                poseStack.scale(1.1f, 1.1f, 1.1f);
                poseStack.translate(0.0F, 0.6F, 0.05F);
            } else if (blockItem.getBlock() instanceof TrapDoorBlock) {
                poseStack.translate(0.0F, 1.32F, 0.1F);
            } else if (blockItem.getBlock() instanceof BedBlock) {
                poseStack.translate(0.0F, 0.5F, 0.6F);
            } else if (blockItem.getBlock() instanceof LadderBlock || blockItem.getBlock() instanceof BushBlock) {
                poseStack.translate(0.0F, 0.0F, 0.08F);
            } else {
                poseStack.translate(0.0F, 0.5F, 0.1F);
            }
        }

        this.itemRenderer.renderStatic(
                entity.getItemStack(),
                ItemDisplayContext.NONE,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId()
        );
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, buffer, packedLight);
        if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.AERO_ENGINE.get())) {
            if (isSpecialBlockItem(item)) {
                renderAeroEngineBlockItemModelEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            } else {
                renderAeroEngineModelEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            }
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.THERMAL_ENGINE.get())) {
            if (isSpecialBlockItem(item)) {
                renderThermalEngineBlockItemModelEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            } else {
                renderThermalEngineModelEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            }
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.MAGIC_ENGINE.get())) {
            if (isSpecialBlockItem(item)) {
                renderMagicEngineBlockItemModelEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            } else {
                renderMagicEngineEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
            }
        } else if (EngineHelper.getEngineItem(entity.getItemStack()).is(ItemsRegister.DRONE_ENGINE.get())) {
            renderDroneEngineEffect(entity, partialTicks, poseStack, buffer, packedLight, lerpYRot, lerpXRot);
        }

    }

    private void renderAeroEngineModelEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpXRot * 0.5f));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F, -0.8F, -1.3F);

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

    private void renderAeroEngineBlockItemModelEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                                  MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        Item item = entity.getItemStack().getItem();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.375F, 0.375F, 0.375F);
        if (item instanceof BedItem) {
            poseStack.translate(0.0F, 0.65F, -3.0F);
        }else {
            poseStack.translate(0.0F, 1.3F, -1.3F);
        }
        int currentFrame = getCurrentFrameIndex(entity, partialTicks, AERO_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        blockAeroEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
        float alpha = calculateAlphaSmooth(entity);
        blockAeroEngineModel.renderToBuffer(poseStack,
                buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                packedLight,
                OverlayTexture.NO_OVERLAY,
                0.67F,
                0.94F,
                0.93F,
                alpha);
        poseStack.popPose();
    }

    private void renderThermalEngineModelEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                                MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpXRot * 0.5f));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.5F, 0.5F, 0.5F);
        poseStack.translate(0.0F, -0.2F, 1.5F);

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

    private void renderThermalEngineBlockItemModelEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                                MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();
        Item item = entity.getItemStack().getItem();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.5F, 0.5F, 0.5F);
        if (item instanceof BedItem) {
            poseStack.translate(0.0F, 0.8F, 3.0F);
        }else {
            poseStack.translate(0.0F, 0.8F, 1.5F);
        }

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

    private void renderMagicEngineEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                                MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));
        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpXRot * 0.5f));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.7F, 0.7F, 0.7F);
        poseStack.translate(0.0F, -1.2F, 1.5F);
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

    private void renderMagicEngineBlockItemModelEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        Item item = entity.getItemStack().getItem();
        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.7F, 0.7F, 0.7F);
        if (item instanceof BedItem) {
            poseStack.translate(0.0F, -0.8F, 3.0F);
        }else {
            poseStack.translate(0.0F, -1.2F, 1.5F);
        }
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

    private void renderDroneEngineEffect(AerocraftItemEntity entity, float partialTicks, PoseStack poseStack,
                                         MultiBufferSource buffer, int packedLight, float lerpYRot, float lerpXRot) {
        poseStack.pushPose();

        poseStack.mulPose(Axis.YP.rotationDegrees(180 - lerpYRot));

        poseStack.translate(0.0F, 0.0F, 0.0F);

        poseStack.scale(0.6F, 0.6F, 0.6F);
        poseStack.translate(0.0F, -1.5F, 0.0F);
        int currentFrame = getCurrentFrameIndex(entity, partialTicks, MAGIC_ENGINE_TEXTURES.length);
        ResourceLocation currentTexture = getFrameTexture(currentFrame, entity.getItemStack());
        Item item = entity.getItemStack().getItem();
        if (item instanceof BedItem) {
            droneEngineBedModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
            droneEngineBedModel.renderToBuffer(poseStack,
                    buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    0.7F);
        }else {
            droneEngineModel.setupAnim(entity, 0, 0, entity.tickCount + partialTicks, lerpYRot, lerpXRot);
            droneEngineModel.renderToBuffer(poseStack,
                    buffer.getBuffer(CustomBlendRenderType.wind(currentTexture)),
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    1.0F,
                    1.0F,
                    1.0F,
                    0.7F);
        }

        poseStack.popPose();
    }

    private boolean isSpecialBlockItem(Item item) {
        return item instanceof BlockItem blockItem &&
                !(blockItem.getBlock() instanceof BushBlock) &&
                !(blockItem.getBlock() instanceof SignBlock) &&
                !(blockItem.getBlock() instanceof LadderBlock) &&
                !(blockItem.getBlock() instanceof DoorBlock) &&
                !(blockItem.getBlock() instanceof CarpetBlock) &&
                !(blockItem.getBlock() instanceof SnowLayerBlock);
    }

    private float calculateAlphaSmooth(AerocraftItemEntity animatable) {
        float currentSpeed = Math.abs(animatable.getPlayerSpeed());
        float maxSpeed = animatable.getMaxSpeed();
        float speedRatio = Mth.clamp(currentSpeed / maxSpeed, 0.0f, 0.7f);
        return speedRatio * speedRatio;
    }

    private int getCurrentFrameIndex(AerocraftItemEntity animatable, float partialTick, int totalFrames) {
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
        }
        return AERO_ENGINE_TEXTURES[0];
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(AerocraftItemEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}