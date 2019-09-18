package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.arrow.EntityArrowQuickdraw;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.BaseBowItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ShusBreathItem extends BaseBowItem {

    public ShusBreathItem() {
        super();
        this.setMaxDamage(650);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    protected ArrowEntity setArrow(@Nonnull ItemStack stack, World world, PlayerEntity player, float velocity) {
        return new EntityArrowQuickdraw(world, player);
    }

    @Override
    protected float getDrawbackSpeed(@Nonnull ItemStack stack, LivingEntity entity) {
        return (float) (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 10.0F;
    }

    @Override
    protected void onVelocity(World world, PlayerEntity player, float velocity) {
        if (velocity == 1.0F) {
            double x = MathHelper.nextDouble(random, 0.01D, 0.1D);
            double z = MathHelper.nextDouble(random, 0.01D, 0.1D);
            for (int l = 0; l < 12; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), x, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, 0.0D, z);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), -x, 0.0D, 0.0D);
                Atum.proxy.spawnParticle(AtumParticles.Types.SHU, player, player.posX + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), player.posY + 1.0D, player.posZ + (world.rand.nextDouble() - 0.5D) * (double) player.getWidth(), 0.0D, 0.0D, -z);
            }
        }
    }
}