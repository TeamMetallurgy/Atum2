package com.teammetallurgy.atum.network.packet;

import com.teammetallurgy.atum.Atum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import javax.annotation.Nonnull;

public class SyncHandStackSizePacket implements CustomPacketPayload {
    public static final ResourceLocation ID = new ResourceLocation(Atum.MOD_ID, "sync_hand_stack_size");
    private final ItemStack stack;
    private final int hand;

    public SyncHandStackSizePacket(@Nonnull ItemStack stack, int hand) {
        this.stack = stack;
        this.hand = hand;
    }

    public SyncHandStackSizePacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.hand = buf.readInt();
    }

    @Override
    @Nonnull
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(@Nonnull FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeVarInt(this.hand);
    }

    public void handle(PlayPayloadContext context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            InteractionHand hand = this.hand == 1 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
            EquipmentSlot handType = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            ItemStack heldStack = player.getItemInHand(hand);
            if (heldStack.getCount() == 0) {
                player.setItemSlot(handType, this.stack);
            } else if (heldStack.getItem() instanceof BlockItem) {
                player.setItemSlot(handType, new ItemStack(heldStack.getItem(), heldStack.getCount() + 1));
            }
        }
    }
}