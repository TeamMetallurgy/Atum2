package com.teammetallurgy.atum.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SyncHandStackSizePacket {
    private final ItemStack stack;
    private final int hand;

    public SyncHandStackSizePacket(@Nonnull ItemStack stack, int hand) {
        this.stack = stack;
        this.hand = hand;
    }

    public static void encode(SyncHandStackSizePacket packet, PacketBuffer buf) {
        buf.writeItemStack(packet.stack);
        buf.writeVarInt(packet.hand);
    }

    public static SyncHandStackSizePacket decode(PacketBuffer buf) {
        return new SyncHandStackSizePacket(buf.readItemStack(), buf.readVarInt());
    }

    public static class Handler {
        public static void handle(SyncHandStackSizePacket message, Supplier<NetworkEvent.Context> ctx) {
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                Hand hand = message.hand == 1 ? Hand.MAIN_HAND : Hand.OFF_HAND;
                EquipmentSlotType handType = hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
                ItemStack heldStack = player.getHeldItem(hand);
                if (heldStack.getCount() == 0) {
                    player.setItemStackToSlot(handType, message.stack);
                } else {
                    player.setItemStackToSlot(handType, new ItemStack(heldStack.getItem(), heldStack.getCount() + 1));
                }
                ctx.get().setPacketHandled(true);
            }
        }
    }
}