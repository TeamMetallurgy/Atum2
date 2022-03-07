package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowFireEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class RasFuryItem extends BaseBowItem implements IArtifact {

    public RasFuryItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650));
        this.setRepairItem(AtumItems.NEBU_INGOT.get());
    }

    @Override
    public God getGod() {
        return God.RA;
    }

    @Override
    protected CustomArrow setArrow(@Nonnull ItemStack stack, Level world, Player player, float velocity) {
        return new ArrowFireEntity(world, player);
    }
}