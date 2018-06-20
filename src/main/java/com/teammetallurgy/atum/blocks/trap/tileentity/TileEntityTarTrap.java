package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.trap.BlockTrap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;

import java.util.List;

public class TileEntityTarTrap extends TileEntityTrap implements ITickable {

    @Override
    public void update() {
        EntityPlayer player = world.getClosestPlayer((double) getPos().getX(), (double) getPos().getY(), (double) getPos().getZ(), 4.0D, false);
        if (!this.isDisabled && player != null && !player.capabilities.isCreativeMode) {
            EnumFacing facing = world.getBlockState(pos).getValue(BlockTrap.FACING);
            List<EntityPlayer> players = world.getEntitiesWithinAABB(EntityPlayer.class, getFacingBoxWithRange(facing, 1));
            for (EntityPlayer p : players) {
                if (p != null && !player.isPotionActive(MobEffects.SLOWNESS)) {
                    player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 80, 3));
                    this.spawnTar(facing);
                }
            }
        }
    }

    private void spawnTar(EnumFacing facing) {
        world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        //TODO Particles
    }
}