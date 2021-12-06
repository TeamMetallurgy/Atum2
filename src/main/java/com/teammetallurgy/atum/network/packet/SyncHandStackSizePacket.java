package com.teammetallurgy.atum.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class SyncHandStackSizePacket {
    private final ItemStack stack;
    private final int hand;

    public SyncHandStackSizePacket(@Nonnull ItemStack stack, int hand) {
        this.stack = stack;
        this.hand = hand;
    }

    public static void encode(SyncHandStackSizePacket packet, FriendlyByteBuf buf) {
        buf.writeItem(packet.stack);
        buf.writeVarInt(packet.hand);
    }

    public static SyncHandStackSizePacket decode(FriendlyByteBuf buf) {
        return new SyncHandStackSizePacket(buf.readItem(), buf.readVarInt());
    }

    public static class Handler {
        public static void handle(SyncHandStackSizePacket message, Supplier<NetworkEvent.Context> ctx) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                InteractionHand hand = message.hand == 1 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                EquipmentSlot handType = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                ItemStack heldStack = player.getItemInHand(hand);
                if (heldStack.getCount() == 0) {
                    player.setItemSlot(handType, message.stack);
                } else if (heldStack.getItem() instanceof BlockItem) {
                    player.setItemSlot(handType, new ItemStack(heldStack.getItem(), heldStack.getCount() + 1));
                }
                ctx.get().setPacketHandled(true);
            }
        }
    }
}