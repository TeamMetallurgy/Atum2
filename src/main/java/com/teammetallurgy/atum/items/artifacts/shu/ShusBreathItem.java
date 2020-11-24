package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.entity.projectile.arrow.ArrowQuickdrawEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ShusBreathItem extends BaseBowItem {

    public ShusBreathItem() {
        super(new Item.Properties().rarity(Rarity.RARE).maxDamage(650));
        this.setRepairItem(AtumItems.NEBU_INGOT);
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