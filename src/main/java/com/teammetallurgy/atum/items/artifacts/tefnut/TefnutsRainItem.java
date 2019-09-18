package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowRain;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class TefnutsRainItem extends BaseBowItem {

    public TefnutsRainItem() {
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
        return new EntityArrowRain(world, player, velocity);
    }

    @Override
    protected void onShoot(ArrowEntity arrow, PlayerEntity player, float velocity) {
        arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F, velocity * 2.5F, 0.0F);
    }
}