package com.kfhzs.allthingsflying.items;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FlightGiftPackage extends Item {

    public FlightGiftPackage() {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    }

    /**
     * 获取所有可用的飞行器物品（运行时动态生成，包含联动内容）
     */
    private List<Supplier<Item>> getAvailableAerocrafts() {
        List<Supplier<Item>> items = new ArrayList<>();
        items.add(ItemsRegister.DRONE);
        items.add(ItemsRegister.ROCKET_PC1);
        items.add(ItemsRegister.MAGIC_BROOM);
        items.add(ItemsRegister.FLYING_SWORD);

        // 添加联动物品（如果可用）
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            items.add(IntegrationItemsRegister.FLYING_CARPET);
        }

        return items;
    }

    /**
     * 获取物品名称映射（运行时动态生成）
     */
    private Map<Item, Component> getAerocraftNames() {
        Map<Item, Component> names = new HashMap<>();
        names.put(ItemsRegister.DRONE.get(), ItemsRegister.DRONE.get().getDefaultInstance().getDisplayName());
        names.put(ItemsRegister.ROCKET_PC1.get(), ItemsRegister.ROCKET_PC1.get().getDefaultInstance().getDisplayName());
        names.put(ItemsRegister.MAGIC_BROOM.get(), ItemsRegister.MAGIC_BROOM.get().getDefaultInstance().getDisplayName());
        names.put(ItemsRegister.FLYING_SWORD.get(), ItemsRegister.FLYING_SWORD.get().getDefaultInstance().getDisplayName());

        // 添加联动物品名称（如果可用）
        if (IntegrationItemsRegister.isChangShengJueLoaded()) {
            names.put(IntegrationItemsRegister.FLYING_CARPET.get(),
                     IntegrationItemsRegister.FLYING_CARPET.get().getDefaultInstance().getDisplayName());
        }

        return names;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            Item randomAerocraft = getRandomAerocraft(level.random);
            int amount = getRandomAmount(level.random);

            giveRandomFlightItems(player, randomAerocraft, amount);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);

            Map<Item, Component> aerocraftNames = getAerocraftNames();
            Component aerocraftName = aerocraftNames.getOrDefault(randomAerocraft,
                Component.translatable("message."+ AllThingsFlying.MODID + "." + this + ".engineName.message"));

            player.displayClientMessage(
                    Component.translatable("message."+ AllThingsFlying.MODID + "." + this + ".prompt.message", aerocraftName, amount),
                    true
            );

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private Item getRandomAerocraft(RandomSource random) {
        List<Supplier<Item>> availableAerocrafts = getAvailableAerocrafts();
        int index = random.nextInt(availableAerocrafts.size());
        return availableAerocrafts.get(index).get();
    }

    private int getRandomAmount(RandomSource random) {
        if (random.nextFloat() < 0.99f) {
            return 1;
        } else {
            return 1 + random.nextInt(2);
        }
    }

    private void giveRandomFlightItems(Player player, Item randomAerocraft, int amount) {
        giveItemToPlayer(player, new ItemStack(randomAerocraft, amount));
    }

    private void giveItemToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip." + AllThingsFlying.MODID + "." + this + ".tooltip"));
    }
}