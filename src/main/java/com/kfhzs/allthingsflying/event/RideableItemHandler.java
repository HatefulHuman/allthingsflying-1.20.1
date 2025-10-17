package com.kfhzs.allthingsflying.event;

import com.kfhzs.allthingsflying.AllThingsFlying;
import com.kfhzs.allthingsflying.entity.EntityRegister;
import com.kfhzs.allthingsflying.entity.item.AerocraftItemEntity;
import com.kfhzs.allthingsflying.items.ItemsRegister;
import com.kfhzs.allthingsflying.particle.ModParticles;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = AllThingsFlying.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RideableItemHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        Level level = event.getLevel();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        Set<Item> RIDEABLE_ITEMS = Set.of(
                ItemsRegister.ROCKET_PC1.get(),
                ItemsRegister.MAGIC_BROOM.get(),
                ItemsRegister.DRONE.get(),
                ItemsRegister.FLYING_SWORD.get()
        );
        if (!level.isClientSide && !RIDEABLE_ITEMS.contains(stack.getItem())) {
            if (EngineHelper.hasEngine(stack)) {
                AerocraftItemEntity entity;
                entity = new AerocraftItemEntity(level, player.getX(), player.getY(), player.getZ(), stack, EntityRegister.RIDEABLE_ITEM.get());
                Vec3 look = player.getLookAngle();
                Vec3 pos = player.getEyePosition()
                        .add(look.scale(2.0))
                        .subtract(0, 0.5, 0);
                entity.setPos(pos.x, pos.y, pos.z);
                entity.setYRot(player.getYRot());
                entity.setOwnerUUID(player.getUUID());
                if (!player.isCreative()) {
                    player.getItemInHand(event.getHand()).shrink(1);
                    level.addFreshEntity(entity);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);  // 取消事件，防止后续处理（如投掷物品）
                } else {
                    level.addFreshEntity(entity);
                }
            }
        }
    }

    private static final String GIFT_GIVEN_TAG = "allthingsflying_first_join_gift";

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CompoundTag persistentData = player.getPersistentData();
            CompoundTag data = persistentData.getCompound(Player.PERSISTED_NBT_TAG);

            if (!data.getBoolean(GIFT_GIVEN_TAG)) {
                ItemStack giftPackage = new ItemStack(ItemsRegister.FLIGHT_GIFT_PACKAGE.get(), 1);
                if (!player.getInventory().add(giftPackage)) {
                    player.drop(giftPackage, false);
                }

                data.putBoolean(GIFT_GIVEN_TAG, true);
                persistentData.put(Player.PERSISTED_NBT_TAG, data);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event) {
//        PlayerAnimationTrigger.onPlayerRender(event);
    }
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
//        PlayerAnimationTrigger.onPlayerTick(event);
    }
}
