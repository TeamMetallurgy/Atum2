package com.teammetallurgy.atum.items.artifacts.seth;

import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowPoison;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class SethsVenomItem extends BaseBowItem {

    public SethsVenomItem() {
        super();
        this.setMaxDamage(650);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    protected CustomArrow setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new EntityArrowPoison(world, player);
    }
}