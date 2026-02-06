package com.kfhzs.allthingsflying.entity;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.enchantment.EngineMendingEnchantment;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.network.AerocrafAscendingPacket;
import com.kfhzs.allthingsflying.network.AerocrafSprintingPacket;
import com.kfhzs.allthingsflying.network.NetworkHandler;
import com.kfhzs.allthingsflying.particle.ModParticles;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AerocrafEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final EntityDataAccessor<ItemStack> ITEM_STACK;
    private static final EntityDataAccessor<Integer> DATA_ID_HURT;
    private static final EntityDataAccessor<Integer> DATA_ID_HURTDIR;
    private static final EntityDataAccessor<Float> DATA_ID_DAMAGE;
    public static final EntityDataAccessor<String> DATA_ID_PLAYER_ACTION;
    public static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID;
    private static final EntityDataAccessor<Boolean> DATA_SPRINTING;
    public static final EntityDataAccessor<Float> DATA_MAX_SPEED;
    private static final EntityDataAccessor<Boolean> DATA_ASCENDING;
    public final AnimationState animationState = new AnimationState();

    private int collisionCooldown = 0; // 碰撞冷却时间
    protected int actionsTick;
    private float defaultXRot = 0.0f; // 默认水平俯仰
    private int lerpSteps;
    private double lerpX;
    private double lerpY;
    private double lerpZ;
    private double lerpYRot;
    private double lerpXRot;
    protected float playerSpeed = 0.0f;
    private SoundEvent currentEngineSound;
    private int soundTime;

    public AerocrafEntity(EntityType<?> aSuper, Level level) {
        super(aSuper, level);
        this.actionsTick = 10;
    }

    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ID_HURT, 0);
        this.entityData.define(DATA_ID_HURTDIR, 1);
        this.entityData.define(DATA_ID_DAMAGE, 0.0F);
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
        this.entityData.define(DATA_SPRINTING, false);
        this.entityData.define(DATA_ASCENDING, false);
    }

    @Override
    protected void checkFallDamage(double p_38307_, boolean p_38308_, BlockState blockState, BlockPos pos) {
        if (this.onGround()){
            this.resetFallDistance();
        }
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    public void tick() {
        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (this.isInWater() && !this.level().isClientSide) {
            ItemStack stack = this.getPickResult();
            if (stack != null && !stack.isEmpty()) {
                this.spawnAtLocation(stack);
                this.discard();
                this.gameEvent(GameEvent.ENTITY_DIE);
            }
            return;
        }

        super.tick();
        Level level = this.level();
        ItemStack stack = this.getItemStack();
        if (!level.isClientSide && this.collisionCooldown > 0) {
            this.collisionCooldown--;
        }

        SoundEvent engineSound = EngineHelper.getEngineSound(stack);
        int engineSoundTime = EngineHelper.getEngineSoundTime(stack);
        if (this.getFirstPassenger() instanceof Player player) {
            this.checkCollisionDamage();
            if (this.playerSpeed > 0.1) {
                if (level.isClientSide) {
                    boolean isSprinting = Minecraft.getInstance().options.keySprint.isDown();
                    if (isSprinting) {
                        NetworkHandler.sendToServer(new AerocrafSprintingPacket(this.getId(), isSprinting));
                    }
                }
                if (!EngineHelper.getEngineItem(stack).is(ItemsRegister.THERMAL_ENGINE.get())
                        && !EngineHelper.getEngineItem(stack).is(ItemsRegister.DRONE_ENGINE.get())){
                    if (engineSound != null && soundTime <= 0) {
                        soundTime = engineSoundTime;
                        level.playSound(null, this.getX(), this.getY(), this.getZ(),
                                engineSound, SoundSource.PLAYERS, 1.5f, 1.0f);
                        currentEngineSound = engineSound;
                    }
                    soundTime--;
                }
            } else {
                if (this.tickCount % 20 == 0) {
                    if (!stack.isEmpty()) {
                        EngineMendingEnchantment.tryRepairEngine(player, stack, this.level());
                    }
                }
                if (!EngineHelper.getEngineItem(stack).is(ItemsRegister.THERMAL_ENGINE.get())
                        && !EngineHelper.getEngineItem(stack).is(ItemsRegister.DRONE_ENGINE.get())) {
                    stopEngineSound();
                }
            }
            if (level.isClientSide) {
                boolean isJump = Minecraft.getInstance().options.keyJump.isDown();
                NetworkHandler.sendToServer(new AerocrafAscendingPacket(this.getId(), isJump));
            }
            if (this.isAscending()) {
                Vec3 currentMotion = this.getDeltaMovement();
                this.setDeltaMovement(currentMotion.x, 0.5, currentMotion.z);
            }
            if (this.isSprinting()){
                if (this.tickCount % 20 == 0) {
                    if (!level.isClientSide) {
                        ItemStack itemStack = this.getItemStack();
                        int engineDurability = EngineHelper.getEngineDurability(itemStack);
                        int engineLevel = EngineHelper.getEngineLevel(itemStack);
                        EngineHelper.setEngineDurability(itemStack,engineDurability - engineLevel * 2);
                    }
                }
            }
        }

        if (EngineHelper.getEngineItem(this.getItemStack()).is(ItemsRegister.THERMAL_ENGINE.get())
                || EngineHelper.getEngineItem(this.getItemStack()).is(ItemsRegister.DRONE_ENGINE.get())){
            if (engineSound != null && soundTime <= 0) {
                soundTime = engineSoundTime;
                level.playSound(null, this.getX(), this.getY(), this.getZ(),
                        engineSound, SoundSource.PLAYERS,
                        1.5f, 1.0f);
                currentEngineSound = engineSound;
            }
            soundTime--;
        }
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.checkInsideBlocks();
        List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.20000000298023224, -0.009999999776482582, 0.20000000298023224), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            boolean flag = !this.level().isClientSide && !(this.getControllingPassenger() instanceof Player);

            for(int j = 0; j < list.size(); ++j) {
                Entity entity = list.get(j);
                if (!entity.hasPassenger(this)) {
                    boolean b = this.hasEnoughSpaceFor(entity);
                    if (flag && this.getPassengers().size() < this.getMaxPassengers() && !entity.isPassenger() && b && entity instanceof LivingEntity && !(entity instanceof WaterAnimal) && !(entity instanceof Player)) {
                        entity.startRiding(this);
                    } else {
                        this.push(entity);
                    }
                }
            }
        }
    }

    /**
     * 停止当前播放的引擎声音
     */
    private void stopEngineSound() {
        if (currentEngineSound != null) {
            Minecraft.getInstance().getSoundManager().stop(currentEngineSound.getLocation(), SoundSource.PLAYERS);

            currentEngineSound = null;
        }
    }

    public void spawnThermalEngineParticles(AerocrafEntity entity, double speed, double lerpXZ, double lerpY) {
        if (entity.level().isClientSide) {
            Vec3 lookVec = entity.getLookAngle();

            double spawnX = entity.getX() - lookVec.x * lerpXZ;
            double spawnY = entity.getY() + lerpY * 1.5;
            double spawnZ = entity.getZ() - lookVec.z * lerpXZ;

            for (int i = 0; i < 3; i++) {
                double baseSpeed = speed + Math.random() * 0.3;

                double spreadX = (Math.random() - 0.5) * 0.1;
                double spreadY = (Math.random() - 0.5) * 0.1;
                double spreadZ = (Math.random() - 0.5) * 0.1;

                entity.level().addParticle(
                        ModParticles.THERMAL_ENGINE_SMOKE.get(),
                        spawnX + (Math.random() - 0.5) * 0.3,
                        spawnY + (Math.random() - 0.5) * 0.3,
                        spawnZ + (Math.random() - 0.5) * 0.3,
                        -lookVec.x * baseSpeed + spreadX,
                        -lookVec.y * baseSpeed * 0.3 + spreadY + 0.02,
                        -lookVec.z * baseSpeed + spreadZ
                );
            }
        }
    }

    public void spawnCloudEngineParticles(AerocrafEntity entity, double speed, double lerpXZ, double lerpY) {
        if (entity.level().isClientSide) {
            Vec3 lookVec = entity.getLookAngle();

            double spawnX = entity.getX() - lookVec.x * lerpXZ;
            double spawnY = entity.getY() + lerpY - lookVec.y * 1.5;
            double spawnZ = entity.getZ() - lookVec.z * lerpXZ;

            for (int i = 0; i < 5; i++) {
                double baseSpeed = speed * 0.3 + Math.random() * 0.3;

                double spreadX = (Math.random() - 0.5) * 0.1;
                double spreadY = (Math.random() - 0.5) * 0.1;
                double spreadZ = (Math.random() - 0.5) * 0.1;

                entity.level().addParticle(
                        ModParticles.CLOUD_ENGINE_SMOKE.get(),
                        spawnX + (Math.random() - 0.5) * 0.3,
                        spawnY + (Math.random() - 0.5) * 0.3,
                        spawnZ + (Math.random() - 0.5) * 0.3,
                        -lookVec.x * baseSpeed + spreadX,
                        -lookVec.y * baseSpeed * 0.3 + spreadY + 0.02,
                        -lookVec.z * baseSpeed + spreadZ
                );
            }
        }
    }

    /**
     * 生成白光粒子
     */
    public void spawnWhiteLightParticles(AerocrafEntity entity, double speed, double lerpXZ, double lerpY) {
        if (entity.level().isClientSide) {
            Vec3 lookVec = entity.getLookAngle();

            double centerX = entity.getX();
            double centerY = entity.getY() + 0.5 + lerpY;
            double centerZ = entity.getZ();

            int particleCount = 1;
            if (Math.random() < 0.2) {
                particleCount = 2;
            }

            for (int i = 0; i < particleCount; i++) {
                double halfWidth = 1.8;
                double halfHeight = 0.5;
                double halfDepth = 3.0;

                double particleX = centerX + (Math.random() - 0.5) * halfWidth * 2;
                double particleY = centerY + (Math.random() - 0.5) * halfHeight * 2;
                double particleZ = centerZ + (Math.random() - 0.5) * halfDepth * 2;

                // 粒子向后飞行（与飞行方向相反）
                double velocityX = -lookVec.x * speed;
                double velocityY = -lookVec.y * speed;
                double velocityZ = -lookVec.z * speed;

                entity.level().addParticle(
                        ModParticles.WHITE_LIGHT.get(),
                        particleX,
                        particleY,
                        particleZ,
                        velocityX,
                        velocityY,
                        velocityZ
                );
            }
        }
    }

    /**
     * 检测并处理碰撞伤害
     */
    protected void checkCollisionDamage() {
        if (this.collisionCooldown > 0) return;
        if (playerSpeed < 0.5) return;

        Vec3 lookVec = this.getLookAngle().scale(2.0); // 向前检测2格距离
        AABB collisionBox = this.getBoundingBox().expandTowards(lookVec).inflate(0.5);

        List<Entity> entities = this.level().getEntities(this, collisionBox,
                entity -> entity instanceof LivingEntity &&
                        entity != this.getFirstPassenger() &&
                        !this.hasPassenger(entity));

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                this.applyCollisionDamage(livingEntity, playerSpeed);
                this.collisionCooldown = 10; // 设置碰撞冷却
            }
        }
    }

    /**
     * 应用碰撞伤害
     */
    protected void applyCollisionDamage(LivingEntity target, float speed) {
        ItemStack itemStack = this.getItemStack();
        // 计算伤害 速度越快伤害越高
        if (this.getFirstPassenger() instanceof Player player) {
            if (!target.isDeadOrDying()) {
                target.hurt(this.damageSources().playerAttack(player),speed * 2.0f);
            }
            if (!this.level().isClientSide) {
                int engineDurability = EngineHelper.getEngineDurability(itemStack);
                int engineLevel = EngineHelper.getEngineLevel(itemStack);
                EngineHelper.setEngineDurability(itemStack,engineDurability - engineLevel * 4);
            }
            for (Entity entity : this.getPassengers()) {
                if (entity instanceof Player player1) {
                    if (player.getInventory().getArmor(3).isEmpty()) {
                        player1.hurt(this.damageSources().thorns(target),speed * 3.0f);
                    }else {
                        player1.hurt(this.damageSources().thorns(target),speed * 2.0f);
                    }
                }
            }
        }

        // 击退效果
        Vec3 knockback = this.getLookAngle().scale(speed * 0.5f);
        target.push(knockback.x, knockback.y * 0.5, knockback.z);

        this.level().playSound(null, this.blockPosition(), SoundEvents.PLAYER_ATTACK_STRONG, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        if (passenger instanceof Player rider) {
            if (this.level().isClientSide) {
                var animation = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData((AbstractClientPlayer) rider)
                        .get(Objects.requireNonNull(ResourceLocation.tryBuild(AllThingsFlying.MODID, "animation")));
                if (animation != null && animation.isActive()) {
                    animation.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                            null
                    );
                }
            }
            if (!EngineHelper.getEngineItem(this.getItemStack()).is(ItemsRegister.THERMAL_ENGINE.get())
                    && !EngineHelper.getEngineItem(this.getItemStack()).is(ItemsRegister.DRONE_ENGINE.get())) {
                stopEngineSound();
            }
        }
        if (this.getPassengers().isEmpty()) {
            this.setXRot(defaultXRot);
            this.xRotO = defaultXRot;
        }
    }

    @Override
    public void lerpTo(double pX, double pY, double pZ, float pYRot, float pXRot, int pLerpSteps, boolean pTeleport) {
        this.lerpX = pX;
        this.lerpY = pY;
        this.lerpZ = pZ;
        this.lerpYRot = pYRot;
        this.lerpXRot = pXRot;
        this.lerpSteps = 10;
    }

    public boolean hasEnoughSpaceFor(Entity entity) {
        return entity.getBbWidth() < this.getBbWidth();
    }

    protected boolean canAddPassenger(Entity p_38390_) {
        return this.getPassengers().size() < this.getMaxPassengers();
    }

    protected int getMaxPassengers() {
        return 2;
    }

    protected void clampRotation(Entity entity) {
        entity.setYBodyRot(this.getYRot());
        float f = Mth.wrapDegrees(entity.getYRot() - this.getYRot());
        float f1 = Mth.clamp(f, -105.0F, 105.0F);
        entity.yRotO += f1 - f;
        entity.setYRot(entity.getYRot() + f1 - f);
        entity.setYHeadRot(entity.getYRot());
        if (entity == this.getFirstPassenger() && this.getFirstPassenger() instanceof Player){
            this.setYRot(entity.getYRot());
            this.setXRot(entity.getXRot() * 0.5f);
            this.xRotO = this.getXRot();
        }
    }

    public void onPassengerTurned(Entity entity) {
        this.clampRotation(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource damageSource, float p_38320_) {
        if (this.isInvulnerableTo(damageSource)|| damageSource.is(DamageTypes.LAVA) || damageSource.is(DamageTypes.IN_FIRE) || damageSource.is(DamageTypes.ON_FIRE)) {
            this.clearFire();
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            if (this.getOwnerUUID() == null || damageSource.getEntity() == null || (damageSource.getEntity() != null && !this.getOwnerUUID().equals(damageSource.getEntity().getUUID()))) {
                if (this.getOwnerUUID() != null && damageSource.getEntity() != null) {
                    ServerPlayer player = this.level().getServer().getPlayerList().getPlayer(this.getOwnerUUID());
                    if (player != null) {
                        player.hurt(new DamageSource(this.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), damageSource.getEntity()), 10.0F);
                    }
                }
                this.setHurtDir(-this.getHurtDir());
                this.setHurtTime(10);
                this.setDamage(this.getDamage() + p_38320_ * 10.0F);
                this.markHurt();
                this.gameEvent(GameEvent.ENTITY_DAMAGE, damageSource.getEntity());
                boolean flag = damageSource.getEntity() instanceof Player && ((Player)damageSource.getEntity()).getAbilities().instabuild;
                if (flag || this.getDamage() > 40.0F) {
                    if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                        this.destroy(damageSource);
                    }
                    stopEngineSound();
                    this.discard();
                }
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec3, InteractionHand hand) {
        if (!this.level().isClientSide) {
            if (player.isShiftKeyDown() && this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID())) {
                this.retrieveToPlayerInventory(player);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void retrieveToPlayerInventory(Player player) {
        stopEngineSound();
        if (!this.level().isClientSide) {
            ItemStack stack = this.getPickResult();
            if (stack != null && !stack.isEmpty()) {
                if (player.getInventory().add(stack)) {
                    this.discard();
                    this.gameEvent(GameEvent.ENTITY_DIE);
                } else {
                    player.drop(stack, false);
                    this.discard();
                }
            }
        }
    }

    protected float getSinglePassengerXOffset() {
        return 0.0F;
    }

    protected void destroy(DamageSource damageSource) {
        this.spawnAtLocation(this.getDropItem());
    }

    public Item getDropItem() {
        return null;
    }

    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        LivingEntity livingentity1;
        if (entity instanceof LivingEntity livingentity) {
            livingentity1 = livingentity;
        } else {
            livingentity1 = null;
        }

        return livingentity1;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        UUID uuid;
        if (compoundTag.hasUUID("Owner")) {
            uuid = compoundTag.getUUID("Owner");
        } else {
            String s = compoundTag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(Objects.requireNonNull(this.getServer()), s);
        }
        this.entityData.set(ITEM_STACK, ItemStack.of(compoundTag.getCompound("Item")));
        if (uuid != null) {
            this.setOwnerUUID(uuid);
        }

        if (compoundTag.contains("Sprinting")) {
            this.setSprinting(compoundTag.getBoolean("Sprinting"));
        }

        if (compoundTag.contains("FlySpeed")) {
            this.setMaxSpeed(compoundTag.getFloat("FlySpeed"));
        }
        if (compoundTag.contains("Ascending")) {
            this.setAscending(compoundTag.getBoolean("Ascending"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
        pCompound.put("Item", this.getItemStack().save(new CompoundTag()));
        pCompound.putBoolean("Sprinting", this.isSprinting());
        pCompound.putFloat("FlySpeed", this.getMaxSpeed());
        pCompound.putBoolean("Ascending", this.isAscending());
    }

    public ItemStack getItemStack() {
        return this.entityData.get(ITEM_STACK);
    }

    public void setHurtDir(int p_38363_) {
        this.entityData.set(DATA_ID_HURTDIR, p_38363_);
    }

    public int getHurtDir() {
        return this.entityData.get(DATA_ID_HURTDIR);
    }

    public void setHurtTime(int p_38355_) {
        this.entityData.set(DATA_ID_HURT, p_38355_);
    }

    public int getHurtTime() {
        return this.entityData.get(DATA_ID_HURT);
    }

    public void setDamage(float damage) {
        this.entityData.set(DATA_ID_DAMAGE, damage);
    }

    public float getDamage() {
        return this.entityData.get(DATA_ID_DAMAGE);
    }

    public String getDataIdPlayerAction() {
        return this.entityData.get(DATA_ID_PLAYER_ACTION);
    }

    public void setDataIdPlayerAction(String dataIdPlayerAction){
        this.entityData.set(DATA_ID_PLAYER_ACTION, dataIdPlayerAction);
    }

    // 最大速度相关方法
    public void setMaxSpeed(float maxSpeed) {
        this.entityData.set(DATA_MAX_SPEED, maxSpeed);
    }

    public float getMaxSpeed() {
        return this.entityData.get(DATA_MAX_SPEED);
    }

    // 最大速度相关方法
    public void setAscending(boolean maxSpeed) {
        this.entityData.set(DATA_ASCENDING, maxSpeed);
    }

    public boolean isAscending() {
        return this.entityData.get(DATA_ASCENDING);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)((Optional)this.entityData.get(DATA_OWNERUUID_ID)).orElse(null);
    }

    public void setOwnerUUID(@Nullable UUID pUuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(pUuid));
    }

    public void setSprinting(boolean sprinting) {
        this.entityData.set(DATA_SPRINTING, sprinting);
    }

    public boolean isSprinting() {
        return this.entityData.get(DATA_SPRINTING) && EngineHelper.getEngineDurability(this.getItemStack()) > 0;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    static {
        ITEM_STACK = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.ITEM_STACK);
        DATA_ID_HURT = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.INT);
        DATA_ID_HURTDIR = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.INT);
        DATA_ID_DAMAGE = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.FLOAT);
        DATA_ID_PLAYER_ACTION = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.STRING);
        DATA_OWNERUUID_ID = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_SPRINTING = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.BOOLEAN);
        DATA_MAX_SPEED = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.FLOAT);
        DATA_ASCENDING = SynchedEntityData.defineId(AerocrafEntity.class, EntityDataSerializers.BOOLEAN);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(((new AnimationController<>(this, "idle", 0, (state) -> {
            if (this.getPassengers().isEmpty()) {
                state.setAndContinue(DefaultAnimations.IDLE);
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }))));
        controllerRegistrar.add(((new AnimationController<>(this, "fly", 0, (state) -> {
            if (!this.getPassengers().isEmpty()) {
                state.setAndContinue(DefaultAnimations.IDLE);
                return PlayState.CONTINUE;
            }
            return PlayState.STOP;
        }))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
