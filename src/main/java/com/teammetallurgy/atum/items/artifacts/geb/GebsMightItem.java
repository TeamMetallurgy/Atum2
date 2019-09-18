package com.teammetallurgy.atum.items.artifacts.geb;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.HammerItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class GebsMightItem extends HammerItem {

    public GebsMightItem() {
        super(ItemTier.DIAMOND, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    protected void onStun(LivingEntity target) {
        for (int amount = 0; amount < 50; ++amount) {
            double d0 = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            Atum.proxy.spawnParticle(AtumParticles.Types.GEB, target, target.posX, target.posY + target.getEyeHeight() - 0.1D, target.posZ, d0, d1, d2);
        }
        stun.put(target, 80);
    }
}