package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowFire;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.math.MathHelper;
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
        return new EntityArrowFire(world, player);
    }

    @Override
    protected void onVelocity(World world, PlayerEntity player, float velocity) {
        if (velocity == 1.0F) {
            for (int amount = 0; amount < 20; ++amount) {
                float timesRandom = world.rand.nextFloat() * 4.0F;
                float cosRandom = world.rand.nextFloat() * ((float) Math.PI * 2F);
                double x = (double) (MathHelper.cos(cosRandom) * timesRandom) * 0.1D;
                double y = 0.01D + world.rand.nextDouble() * 0.1D;
                double z = (double) (MathHelper.sin(cosRandom) * timesRandom) * 0.1D;
                player.world.addParticle(AtumParticles.RA_FIRE, player.posX, player.posY + 0.7D, player.posZ + z * 0.1D, x / 10, y, z / 10);
            }
        }
    }
}