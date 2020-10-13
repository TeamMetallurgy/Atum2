package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class AtemsBountyItem extends FishingRodItem {

    public AtemsBountyItem() {
        super(new Item.Properties().maxDamage(100).rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, @Nonnull ITooltipFlag tooltipType) {
        int remaining = stack.getMaxDamage() - stack.getDamage();
        tooltip.add(new TranslationTextComponent("atum.tooltip.uses_remaining", remaining));
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }
}