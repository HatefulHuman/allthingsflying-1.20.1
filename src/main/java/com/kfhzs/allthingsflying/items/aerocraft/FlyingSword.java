package com.kfhzs.allthingsflying.items.aerocraft;

import com.kfhzs.allthingsflying.entity.aerocraft.drone.DroneEntity;
import com.kfhzs.allthingsflying.entity.aerocraft.sword.FlyingSwordEntity;
import com.kfhzs.allthingsflying.recipe.EngineHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class FlyingSword extends Aerocraft {
    public FlyingSword() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private AerocraftRender renderer = null;
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new AerocraftRender("flying_sword");
                }
                return renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) return super.use(level, player, hand);
        if (player.getMainHandItem().is(this)) {
            if (EngineHelper.hasEngine(player.getMainHandItem())) {
                FlyingSwordEntity boat = new FlyingSwordEntity(level, player.getX(), player.getY(), player.getZ(), player.getMainHandItem());
                Vec3 look = player.getLookAngle();
                Vec3 pos = player.getEyePosition()
                        .add(look.scale(2.0))
                        .subtract(0, 0.5, 0);
                boat.setPos(pos.x, pos.y, pos.z);
                boat.setYRot(player.getYRot());
                boat.setOwnerUUID(player.getUUID());
                if (!player.isCreative()) {
                    player.getItemInHand(hand).shrink(1);
                    level.addFreshEntity(boat);
                } else {
                    level.addFreshEntity(boat);
                }
            }
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}
