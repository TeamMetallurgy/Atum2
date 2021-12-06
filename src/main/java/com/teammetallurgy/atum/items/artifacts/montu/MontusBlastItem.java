package com.teammetallurgy.atum.items.artifacts.montu;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowExplosiveEntity;
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

public class MontusBlastItem extends BaseBowItem implements IArtifact {

    public MontusBlastItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650));
        this.setRepairItem(AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.MONTU;
    }

    @Override
    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level world, Player player, float velocity) {
        return new ArrowExplosiveEntity(world, player, velocity);
    }

    @Override
    public int getArrowLoose(@Nonnull ItemStack stack, int timeLeft) {
        return super.getArrowLoose(stack, timeLeft) / 2;
    }

    @Override
    public float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return super.getDrawbackSpeed(stack, entity) / 2.0F;
    }
}