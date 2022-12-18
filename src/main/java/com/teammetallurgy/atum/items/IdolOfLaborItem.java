package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class IdolOfLaborItem extends Item {

    public IdolOfLaborItem() {
        super(new Item.Properties().rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        if (level.isClientSide) {
            player.displayClientMessage(Component.translatable(this.getDescriptionId() + "." + Mth.nextInt(level.random, 1, 65)).withStyle(ChatFormatting.YELLOW), true);
        }
        return super.use(level, player, hand);
    }
}