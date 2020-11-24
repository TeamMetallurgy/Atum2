package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class IdolOfLaborItem extends Item {

    public IdolOfLaborItem() {
        super(new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, @Nonnull PlayerEntity player, @Nonnull Hand hand) {
        if (world.isRemote) {
            player.sendStatusMessage(new TranslationTextComponent(this.getTranslationKey() + "." + MathHelper.nextInt(random, 1, 65)).mergeStyle(TextFormatting.YELLOW), true);
        }
        return super.onItemRightClick(world, player, hand);
    }
}