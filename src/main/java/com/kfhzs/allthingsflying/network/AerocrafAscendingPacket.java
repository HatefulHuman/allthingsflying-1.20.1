package com.kfhzs.allthingsflying.network;

import com.kfhzs.allthingsflying.entity.AerocrafEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AerocrafAscendingPacket {
    private final int entityId;
    private final boolean ascending;

    public AerocrafAscendingPacket(int entityId, boolean ascending) {
        this.entityId = entityId;
        this.ascending = ascending;
    }

    public static void encode(AerocrafAscendingPacket msg, FriendlyByteBuf buffer) {
        buffer.writeInt(msg.entityId);
        buffer.writeBoolean(msg.ascending);
    }

    public static AerocrafAscendingPacket decode(FriendlyByteBuf buffer) {
        return new AerocrafAscendingPacket(buffer.readInt(), buffer.readBoolean());
    }

    public static void handle(AerocrafAscendingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                Entity entity = level.getEntity(msg.entityId);
                if (entity instanceof AerocrafEntity aerocraf) {
                    aerocraf.setAscending(msg.ascending);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
