package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowQuickdrawEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class ShusBreathItem extends BaseBowItem implements IArtifact {

    public ShusBreathItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650), AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.SHU;
    }

    @Override
    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level world, Player player, float velocity) {
        return new ArrowQuickdrawEntity(world, player);
    }

    @Override
    public int getArrowLoose(@Nonnull ItemStack stack, int timeLeft) {
        return super.getArrowLoose(stack, timeLeft) * 2;
    }

    @Override
    public float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return super.getDrawbackSpeed(stack, entity) * 2.0F;
    }
}