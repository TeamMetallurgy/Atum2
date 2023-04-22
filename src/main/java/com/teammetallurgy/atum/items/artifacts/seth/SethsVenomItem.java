package com.teammetallurgy.atum.items.artifacts.seth;

import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.entity.projectile.arrow.ArrowPoisonEntity;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class SethsVenomItem extends BaseBowItem implements IArtifact {

    public SethsVenomItem() {
        super(new Item.Properties().rarity(Rarity.RARE).durability(650), AtumItems.NEBU_INGOT);
    }

    @Override
    public God getGod() {
        return God.SETH;
    }

    @Override
    protected AbstractArrow setArrow(@Nonnull ItemStack stack, Level level, Player player, float velocity) {
        return new ArrowPoisonEntity(level, player);
    }
}