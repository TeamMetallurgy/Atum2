package com.teammetallurgy.atum.items.artifacts.horus;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowStraightEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class HorusSoaringItem extends BaseBowItem implements IArtifact {

    public HorusSoaringItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650));
        this.setRepairItem(AtumItems.NEBU_INGOT.get());
    }

    @Override
    public God getGod() {
        return God.HORUS;
    }

    @Override
    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level world, Player player, float velocity) {
        return new ArrowStraightEntity(world, player, velocity);
    }

    @Override
    protected void onShoot(AbstractArrow arrow, Player player, float velocity) {
        arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 2.0F, 0.0F);
    }
}