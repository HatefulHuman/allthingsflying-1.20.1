package com.kfhzs.allthingsflying.network;

import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AerocrafSprintingPacket {
    private final int entityId;
    private final boolean sprinting;

    public AerocrafSprintingPacket(int entityId, boolean sprinting) {
        this.entityId = entityId;
        this.sprinting = sprinting;
    }

    public static void encode(AerocrafSprintingPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.sprinting);
    }

    public static AerocrafSprintingPacket decode(FriendlyByteBuf buffer) {
        return new AerocrafSprintingPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(AerocrafSprintingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                Entity entity = level.getEntity(msg.entityId);
                if (entity instanceof AerocrafEntity flyingSword) {
                    flyingSword.setSprinting(msg.sprinting);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
