package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.entity.projectile.arrow.ArrowQuickdrawEntity;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ShusBreathItem extends BaseBowItem {

    public ShusBreathItem() {
        super(new Item.Properties().rarity(Rarity.RARE).maxDamage(650));
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    protected ArrowEntity setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new ArrowQuickdrawEntity(world, player);
    }

    @Override
    public float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return (float) (stack.getUseDuration() - entity.getItemInUseCount()) / 10.0F;
    }
}