package com.teammetallurgy.atum.items.artifacts.montu;

import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowExplosive;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class MontusBlastItem extends BaseBowItem {

    public MontusBlastItem() {
        super(new Item.Properties().rarity(Rarity.RARE).maxDamage(650));
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    protected ArrowEntity setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new EntityArrowExplosive(world, player, velocity);
    }

    @Override
    protected float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return super.getDrawbackSpeed(stack, entity) / 2.0F;
    }
}