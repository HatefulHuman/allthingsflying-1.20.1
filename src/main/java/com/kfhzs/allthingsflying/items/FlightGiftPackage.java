package com.kfhzs.allthingsflying.items;

import com.kfhzs.allthingsflying.AllThingsFlying;
import net.minecraft.ChatFormatting;
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
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FlightGiftPackage extends Item {
    private static final List<Supplier<Item>> ENGINE_ITEMS = List.of(
            ItemsRegister.DRONE,
            ItemsRegister.ROCKET_PC1,
            ItemsRegister.MAGIC_BROOM,
            ItemsRegister.FLYING_SWORD
    );

    private static final Map<Item, Component> ENGINE_NAMES = Map.of(
            ItemsRegister.DRONE.get(), ItemsRegister.DRONE.get().getDefaultInstance().getDisplayName(),
            ItemsRegister.ROCKET_PC1.get(), ItemsRegister.ROCKET_PC1.get().getDefaultInstance().getDisplayName(),
            ItemsRegister.MAGIC_BROOM.get(), ItemsRegister.MAGIC_BROOM.get().getDefaultInstance().getDisplayName(),
            ItemsRegister.FLYING_SWORD.get(), ItemsRegister.FLYING_SWORD.get().getDefaultInstance().getDisplayName()
    );

    public FlightGiftPackage() {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.EPIC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide) {
            Item randomEngine = getRandomEngine(level.random);
            int amount = getRandomAmount(level.random);

            giveRandomFlightItems(player, randomEngine, amount);

            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.0F);

            Component engineName = ENGINE_NAMES.getOrDefault(randomEngine, Component.translatable("message."+ AllThingsFlying.MODID + "." + this + ".engineName.message"));
            player.displayClientMessage(
                    Component.translatable("message."+ AllThingsFlying.MODID + "." + this + ".prompt.message",engineName, amount),
                    true
            );

            if (!player.isCreative()) {
                stack.shrink(1);
            }
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    private Item getRandomEngine(RandomSource random) {
        int index = random.nextInt(ENGINE_ITEMS.size());
        return ENGINE_ITEMS.get(index).get();
    }

    private int getRandomAmount(RandomSource random) {
        if (random.nextFloat() < 0.99f) {
            return 1;
        } else {
            return 1 + random.nextInt(2);
        }
    }

    private void giveRandomFlightItems(Player player, Item randomEngine, int engineAmount) {
        giveItemToPlayer(player, new ItemStack(randomEngine, engineAmount));
    }

    private void giveItemToPlayer(Player player, ItemStack stack) {
        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip." + AllThingsFlying.MODID + "." + this + ".tooltip"));
//        tooltip.add(Component.literal("§7可能包含："));
//        tooltip.add(Component.literal("§7- §e随机飞行引擎§7 x2-5"));
//        tooltip.add(Component.literal("§7- §b升级核心§7 x2-4"));
//        tooltip.add(Component.literal("§7- §6重型升级核心§7 (30%几率)"));
//        tooltip.add(Component.literal("§7- §a原版飞行道具§7 x1-2种"));
//        tooltip.add(Component.literal("§7- §d鞘翅§7 (2%稀有几率)"));
//        tooltip.add(Component.literal(""));
//        tooltip.add(Component.literal("§e§o惊喜不断，每次开启都有新体验！"));
    }

//    public static class WeightedEngine {
//        public final Supplier<Item> engine;
//        public final int weight;
//
//        public WeightedEngine(Supplier<Item> engine, int weight) {
//            this.engine = engine;
//            this.weight = weight;
//        }
//    }

//    private Item getWeightedRandomEngine(RandomSource random) {
//        List<WeightedEngine> weightedEngines = List.of(
//                new WeightedEngine(ItemsRegister.THERMAL_ENGINE, 30),  // 30% 热能引擎
//                new WeightedEngine(ItemsRegister.AERO_ENGINE, 25),     // 25% 破空引擎
//                new WeightedEngine(ItemsRegister.MAGIC_ENGINE, 20),    // 20% 魔力引擎
//                new WeightedEngine(ItemsRegister.DRONE_ENGINE, 15),    // 15% 无人机引擎
//                new WeightedEngine(ItemsRegister.CLOUD_ENGINE, 10)     // 10% 腾云引擎（最稀有）
//        );
//
//        int totalWeight = weightedEngines.stream().mapToInt(e -> e.weight).sum();
//        int randomValue = random.nextInt(totalWeight);
//
//        int currentWeight = 0;
//        for (WeightedEngine weightedEngine : weightedEngines) {
//            currentWeight += weightedEngine.weight;
//            if (randomValue < currentWeight) {
//                return weightedEngine.engine.get();
//            }
//        }
//
//        return ItemsRegister.THERMAL_ENGINE.get();
//    }
}