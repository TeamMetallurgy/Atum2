package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

import java.util.List;

public class TileEntityPoisonTrap extends TileEntityTrap {

    @Override
    public void update() {
        super.update();
        EntityPlayer player = world.getClosestPlayer((double) getPos().getX(), (double) getPos().getY(), (double) getPos().getZ(), 2.0D, false);
        if (!this.isDisabled && player != null && !player.capabilities.isCreativeMode) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getFacingBoxWithRange(facing, 1));
            for (EntityPlayer p : players) {
                if (p != null) {
                    this.spawnPoison(facing, player);
                }
            }
        }
    }

    private void spawnPoison(EnumFacing facing, EntityLivingBase entity) {
        double randomPos = world.rand.nextDouble() * 0.6D - 0.3D;
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + world.rand.nextDouble() * 0.7D;
        double z = (double) pos.getZ() + 0.5D;

        if (!entity.isPotionActive(MobEffects.POISON)) {
            entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 3, false, false));
            world.playSound(x, (double) pos.getY(), z, SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
        switch (facing) {
            case DOWN:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x - randomPos, (double) pos.getY() - 0.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case UP:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x - randomPos, (double) pos.getY() + 1.1D, z - 0.2 + (world.rand.nextDouble() * 0.4D), 0.0D, 0.0D, 0.0D);
                break;
            case WEST:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x - 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case EAST:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x + 0.52D, y, z + randomPos, 0.0D, 0.0D, 0.0D);
                break;
            case NORTH:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x + randomPos, y, z - 0.52D, 0.0D, 0.0D, 0.0D);
                break;
            case SOUTH:
                Atum.proxy.spawnParticle(AtumParticles.Types.GAS, entity, x + randomPos, y, z + 0.52D, 0.0D, 0.0D, 0.0D);
                break;
        }
    }
}