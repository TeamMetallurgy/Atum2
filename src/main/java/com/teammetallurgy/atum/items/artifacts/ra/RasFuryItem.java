package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.entity.projectile.arrow.ArrowFireEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class RasFuryItem extends BaseBowItem {

    public RasFuryItem() {
        super(new Item.Properties().rarity(Rarity.RARE).maxDamage(650));
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    protected CustomArrow setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new ArrowFireEntity(world, player);
    }
}