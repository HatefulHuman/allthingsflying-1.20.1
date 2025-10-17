package com.kfhzs.allthingsflying.entity.item;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import com.kfhzs.allthingsflying.entity.aerocraft.rocket.RocketEntity;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.particle.ModParticles;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Objects;

public class AerocraftItemEntity extends AerocrafEntity {

    private float friction; // 默认阻力系数
    private float stopThreshold;  // 停止阈值
    private float acceleration;     // 加速度
    private static final String[] ACTIONS = {"zuoxia", "zuoxia2", "tangxia", "zhanli", "zhanli2", "zhanli3"};

    public AerocraftItemEntity(EntityType<?> type, Level level) {
        super(type, level);
        // 骑乘时取消重力
        this.setNoGravity(true);
        this.blocksBuilding = true;
        this.setNoGravity(true);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public AerocraftItemEntity(Level level, double p_38294_, double p_38295_, double p_38296_,
                               ItemStack stack, EntityType<AerocraftItemEntity> rideableItemEntityEntityType) {
        this(rideableItemEntityEntityType, level);
        this.entityData.set(ITEM_STACK, stack.copy().split(1));
        this.xo = p_38294_;
        this.yo = p_38295_;
        this.zo = p_38296_;
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ITEM_STACK, ItemStack.EMPTY);
        this.entityData.define(DATA_ID_PLAYER_ACTION, ACTIONS[RandomSource.create().nextInt(ACTIONS.length)]);
        this.entityData.define(DATA_MAX_SPEED, 0.0F);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (ITEM_STACK.equals(key)) {
            this.refreshDimensions();
        }
    }

    @Override
    public EntityDimensions getDimensions(Pose pose) {
        ItemStack itemStack = this.getItemStack();
        if (itemStack.isEmpty()) {
            return EntityDimensions.scalable(0.8f, 0.8f);
        }

        Item item = itemStack.getItem();

        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof SlabBlock) {
                return EntityDimensions.scalable(1.5f, 0.75f); // 半砖
            } else if (block instanceof CarpetBlock || block instanceof LadderBlock || block instanceof BushBlock) {
                return EntityDimensions.scalable(1.5f, 0.075f);
            } else if (block instanceof SnowLayerBlock) {
                return EntityDimensions.scalable(1.5f, 0.1875f); // 雪层
            } else if (block instanceof DoorBlock || block instanceof SignBlock) {
                return EntityDimensions.scalable(1.5f, 0.45f); // 门
            } else if (block instanceof BedBlock) {
                return EntityDimensions.scalable(3.0f, 0.85f); // 床
            } else if (block instanceof CakeBlock) {
                return EntityDimensions.scalable(1.5f, 0.225f); // 蛋糕
            } else {
                return EntityDimensions.scalable(1.5f, 1.5f);
            }
        } else {
            return EntityDimensions.scalable(1.5f, 0.5f);
        }
    }

    @Override
    public AABB makeBoundingBox() {
        EntityDimensions dimensions = this.getDimensions(this.getPose());
        return dimensions.makeBoundingBox(this.position());
    }

    private void updatePhysicsFromItem() {
        ItemStack stack = this.getItemStack();
        if (!stack.isEmpty()) {
            this.setMaxSpeed(Math.max(EngineHelper.getFlightSpeed(stack), 0.1f));
            this.friction = Math.max(EngineHelper.getFriction(stack), 0.8f);
            this.stopThreshold = Math.max(EngineHelper.getStopThreshold(stack), 0.001f);
            this.acceleration = Math.max(EngineHelper.getAcceleration(stack), 0.1f);
        }
    }

    @Override
    public void tick() {
        super.tick();
        // 每次tick都从物品动态获取，但缓存以避免性能问题
        if (this.tickCount % 20 == 0) { // 每20tick更新一次
            updatePhysicsFromItem();
        }
        ItemStack stack = this.getItemStack();
        if (EngineHelper.getEngineItem(stack).is(ItemsRegister.THERMAL_ENGINE.get())) {
            if (this.playerSpeed > 0.2 && !this.isInWater()) {
                spawnThermalEngineParticles(this,0.25,1.0, stack.getItem() instanceof BlockItem ? 0.75 : 0.2);
            } else {
                spawnThermalEngineParticles(this,0.15,1.0, stack.getItem() instanceof BlockItem ? 0.75 : 0.2);
            }
        }else if (EngineHelper.getEngineItem(stack).is(ItemsRegister.CLOUD_ENGINE.get())) {
            if (this.playerSpeed > 0.2 && !this.isInWater()) {
                spawnCloudEngineParticles(this, 0.05,1.0, stack.getItem() instanceof BlockItem ? 0.75 : 0.2);
            }
        }
        this.animationState.animateWhen(true, this.tickCount);
        if (this.getFirstPassenger() instanceof Player rider) {
            ItemStack mainHandItem = rider.getMainHandItem();
            ItemStack offhandItem = rider.getOffhandItem();
            if (this.level().isClientSide) {
                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) rider).get(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "animation")));
                if (animation != null) {
                    String actionName = this.getDataIdPlayerAction();
                    var keyframeAnimation = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, actionName)));
                    var keyframeAnimation1 = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, actionName + "_swinging")));
                    var keyframeAnimation2 = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, actionName + "_main_hand")));
                    var keyframeAnimation3 = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, actionName + "_off_hand")));
                    var keyframeAnimation4 = PlayerAnimationRegistry.getAnimation(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, actionName + "_main_and_off_hand")));
                    if (keyframeAnimation != null) {
                        if (animation.getAnimation() instanceof KeyframeAnimationPlayer currentPlayer) {
                            if (rider.swinging) {
                                if (keyframeAnimation1 != null) {
                                    if (!currentPlayer.getData().equals(keyframeAnimation1)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation1));
                                    }
                                }
                            } else if (!mainHandItem.isEmpty() && !offhandItem.isEmpty()) {
                                if (keyframeAnimation4 != null) {
                                    if (!currentPlayer.getData().equals(keyframeAnimation4)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation4));
                                    }
                                } else {
                                    if (!currentPlayer.getData().equals(keyframeAnimation)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation));
                                    }
                                }
                            } else if (!mainHandItem.isEmpty()) {
                                if (keyframeAnimation2 != null) {
                                    if (!currentPlayer.getData().equals(keyframeAnimation2)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation2));
                                    }
                                } else {
                                    if (!currentPlayer.getData().equals(keyframeAnimation)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation));
                                    }
                                }
                            } else if (!offhandItem.isEmpty()) {
                                if (keyframeAnimation3 != null) {
                                    if (!currentPlayer.getData().equals(keyframeAnimation3)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation3));
                                    }
                                } else {
                                    if (!currentPlayer.getData().equals(keyframeAnimation)) {
                                        animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation));
                                    }
                                }
                            } else {
                                if (!currentPlayer.getData().equals(keyframeAnimation)) {
                                    animation.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), new KeyframeAnimationPlayer(keyframeAnimation));
                                }
                            }
                        } else {
                            animation.setAnimation(new KeyframeAnimationPlayer(keyframeAnimation));
                        }
                    }
                }
            }

            this.setYRot(rider.getYRot());
            this.setXRot(rider.getXRot() * 0.5f);
            this.xRotO = this.getXRot();

            float currentMaxSpeed = this.getMaxSpeed();
            float currentMaxReverseSpeed = currentMaxSpeed * 0.5f; // 最大倒退速度为最大速度的一半

            if (!rider.isCreative()) {
                if (rider.zza > 0) {
                    float baseSpeed = this.playerSpeed + acceleration;
                    float targetSpeed = this.isSprinting() ? baseSpeed * 1.5f : baseSpeed;
                    float maxSpeed = this.isSprinting() ? currentMaxSpeed * 1.5f : currentMaxSpeed;
                    this.playerSpeed = Mth.clamp(targetSpeed, 0.0f, maxSpeed);
                } else if (rider.zza < 0) {
                    this.playerSpeed = Mth.clamp(this.playerSpeed - acceleration, -currentMaxReverseSpeed, 0.0f);
                    this.setSprinting(false);
                } else {
                    this.setSprinting(false);
                    this.playerSpeed *= friction;
                    if (Math.abs(this.playerSpeed) < stopThreshold) {
                        this.playerSpeed = 0.0f;
                    }
                }
            } else {
                if (rider.zza > 0) {
                    float baseSpeed = this.playerSpeed + acceleration;
                    float targetSpeed = this.isSprinting() ? baseSpeed * 1.5f : baseSpeed;
                    float maxSpeed = this.isSprinting() ? currentMaxSpeed * 1.5f : currentMaxSpeed;
                    this.playerSpeed = Mth.clamp(targetSpeed, 0.0f, maxSpeed);
                } else if (rider.zza < 0) {
                    this.setSprinting(false);
                    this.playerSpeed = Mth.clamp(this.playerSpeed - acceleration, -currentMaxReverseSpeed, 0.0f);
                } else {
                    this.setSprinting(false);
                    this.playerSpeed *= friction;
                    if (Math.abs(this.playerSpeed) < stopThreshold) {
                        this.playerSpeed = 0.0f;
                    }
                }
            }

            if (!this.level().isClientSide) {
                if (actionsTick > 0) {
                    actionsTick--;
                }
                if (rider.xxa != 0 && actionsTick == 0) {
                    String newAction = getString(rider);

                    this.setDataIdPlayerAction(newAction);
                    actionsTick = 10;
                }
            }

            float yRotRad = -this.getYRot() * Mth.DEG_TO_RAD;
            float xRotRad = this.getXRot() * Mth.DEG_TO_RAD;

            Vec3 movement = new Vec3(
                    Mth.sin(yRotRad) * this.playerSpeed,
                    -Mth.sin(xRotRad) * this.playerSpeed,
                    Mth.cos(yRotRad) * this.playerSpeed
            );

            this.setDeltaMovement(movement);
        } else {
            this.playerSpeed = 0.0f;
            this.setSprinting(false);
            this.setDeltaMovement(Vec3.ZERO);
        }
    }

    private @NotNull String getString(Player rider) {
        String currentAction = this.getDataIdPlayerAction();
        String newAction;

        if (rider.xxa > 0) {
            newAction = currentAction.equals(ACTIONS[0]) ? ACTIONS[ACTIONS.length - 1] : ACTIONS[getActionIndex(currentAction) - 1];
        } else {
            newAction = currentAction.equals(ACTIONS[ACTIONS.length - 1]) ? ACTIONS[0] : ACTIONS[getActionIndex(currentAction) + 1];
        }
        return newAction;
    }

    private int getActionIndex(String action) {
        for (int i = 0; i < ACTIONS.length; i++) {
            if (ACTIONS[i].equals(action)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public double getPassengersRidingOffset() {
        if (this.entityData.get(DATA_ID_PLAYER_ACTION).equals(ACTIONS[0]) || this.entityData.get(DATA_ID_PLAYER_ACTION).equals(ACTIONS[1]) || this.entityData.get(DATA_ID_PLAYER_ACTION).equals(ACTIONS[2])) {
            if (this.getItemStack().getItem() instanceof BlockItem blockItem){
                if (blockItem.getBlock() instanceof CarpetBlock || blockItem.getBlock() instanceof LadderBlock || blockItem.getBlock() instanceof BushBlock) {
                    return -0.15F;
                } else if (blockItem.getBlock() instanceof SlabBlock) {
                    return 0.525F;
                } else if (blockItem.getBlock() instanceof SnowLayerBlock) {
                    return 0.0F;
                } else if (blockItem.getBlock() instanceof DoorBlock || blockItem.getBlock() instanceof SignBlock) {
                    return 0.0F;
                } else if (blockItem.getBlock() instanceof BedBlock) {
                    return 0.6F;
                } else {
                    return 1.35F;
                }
            } else {
                return 0.1F;
            }
        } else {
            if (this.getItemStack().getItem() instanceof BlockItem blockItem) {
                if (blockItem.getBlock() instanceof CarpetBlock || blockItem.getBlock() instanceof LadderBlock || blockItem.getBlock() instanceof BushBlock) {
                    return 0.375F;
                } else if (blockItem.getBlock() instanceof SlabBlock) {
                    return 1.05F;
                } else if (blockItem.getBlock() instanceof SnowLayerBlock) {
                    return 0.45F;
                } else if (blockItem.getBlock() instanceof DoorBlock || blockItem.getBlock() instanceof SignBlock) {
                    return 0.525F;
                } else if (blockItem.getBlock() instanceof BedBlock) {
                    return 1.125F;
                } else {
                    return 1.9F;
                }
            } else {
                return 0.7F;
            }
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        } else {
            if (!this.level().isClientSide) {
                if (player.startRiding(this)) {
                    String action = ACTIONS[RandomSource.create().nextInt(ACTIONS.length)];
                    this.setDataIdPlayerAction(action);
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }

    protected void destroy(DamageSource damageSource) {
        super.destroy(damageSource);
    }

    public ItemStack getPickResult() {
        return this.getItemStack();
    }

    public Item getDropItem() {
        return this.getItemStack().copy().getItem();
    }

    protected int getMaxPassengers() {
        return 1;
    }

    public boolean isFlying() {
        return this.playerSpeed > 0 && !this.isInWater();
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    @Override
    public Component getName() {
        ItemStack itemStack = this.getItemStack();
        if (!itemStack.isEmpty()) {
            return Component.translatable("entity."+ AllThingsFlying.MODID +".rideable_item", itemStack.getHoverName());
        }
        return super.getName();
    }
}