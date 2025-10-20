package com.kfhzs.allthingsflying.entity.aerocraft.drone;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import com.kfhzs.allthingsflying.entity.EntityRegister;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;

import java.util.Objects;

public class DroneEntity extends AerocrafEntity {
    private float friction;
    private float stopThreshold;
    private float acceleration;
    private static final String[] ACTIONS = {"zuoxia2", "tangxia", "zhanli", "zhanli2", "zhanli3"};

    public DroneEntity(EntityType<?> aSuper, Level level) {
        super(aSuper, level);
        // 骑乘时取消重力
        this.setNoGravity(true);
        this.blocksBuilding = true;
        this.setNoGravity(true);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    public DroneEntity(Level level, double p_38294_, double p_38295_, double p_38296_, ItemStack stack) {
        this(EntityRegister.DRONE.get(), level);
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
    public void tick() {
        super.tick();
        // 每次tick都从物品动态获取，但缓存以避免性能问题
        if (this.tickCount % 20 == 0) { // 每20tick更新一次
            updatePhysicsFromItem();
        }
        if (EngineHelper.getEngineItem(this.getItemStack()).is(ItemsRegister.CLOUD_ENGINE.get())) {
            if (this.playerSpeed > 0.2 && !this.isInWater()) {
                spawnCloudEngineParticles(this, 0.15,1.0,0.3);
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

            boolean left = Minecraft.getInstance().options.keyLeft.isDown();
            boolean right = Minecraft.getInstance().options.keyRight.isDown();
            if (right || left) {
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

    private void updatePhysicsFromItem() {
        ItemStack stack = this.getItemStack();
        if (!stack.isEmpty()) {
            this.setMaxSpeed(Math.max(EngineHelper.getFlightSpeed(stack), 0.1f));
            this.friction = Math.max(EngineHelper.getFriction(stack), 0.8f);
            this.stopThreshold = Math.max(EngineHelper.getStopThreshold(stack), 0.001f);
            this.acceleration = Math.max(EngineHelper.getAcceleration(stack), 0.1f);
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
        if (!(this.getFirstPassenger() instanceof Player)) {
            return 0.3f;
        }
        if (this.entityData.get(DATA_ID_PLAYER_ACTION).equals(ACTIONS[0])
                || this.entityData.get(DATA_ID_PLAYER_ACTION).equals(ACTIONS[1])) {
            return 0.4f;
        } else {
            return 1.0f;
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

    protected int getMaxPassengers() {
        return 1;
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

    public boolean isFlying() {
        return this.playerSpeed > 0 && !this.isInWater();
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

}
