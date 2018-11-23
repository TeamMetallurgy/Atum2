package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.entity.EntityHeartOfRa;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemHeartOfRa extends Item {

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);

        if (!state.isSideSolid(world, pos, EnumFacing.UP) || !player.capabilities.isCreativeMode) {
            return EnumActionResult.FAIL;
        } else {
            BlockPos posUp = pos.up();
            ItemStack heldStack = player.getHeldItem(hand);

            if (!player.canPlayerEdit(posUp, facing, heldStack)) {
                return EnumActionResult.FAIL;
            } else {
                BlockPos posUpCheck = posUp.up();
                boolean notEnoughSpace = !world.isAirBlock(posUp) && !world.getBlockState(posUp).getBlock().isReplaceable(world, posUp);
                notEnoughSpace = notEnoughSpace | (!world.isAirBlock(posUpCheck) && !world.getBlockState(posUpCheck).getBlock().isReplaceable(world, posUpCheck));

                if (notEnoughSpace) {
                    return EnumActionResult.FAIL;
                } else {
                    double x = (double) posUp.getX();
                    double y = (double) posUp.getY();
                    double z = (double) posUp.getZ();
                    List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(x, y, z, x + 1.0D, y + 2.0D, z + 1.0D));

                    if (!entities.isEmpty()) {
                        return EnumActionResult.FAIL;
                    } else {
                        if (!world.isRemote) {
                            EntityHeartOfRa heartOfRa = new EntityHeartOfRa(world, (double) ((float) pos.getX() + 0.5F), (double) (pos.getY() + 1), (double) ((float) pos.getZ() + 0.5F));
                            heartOfRa.setShowBottom(false);
                            world.spawnEntity(heartOfRa);
                        }
                        heldStack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }
        }
    }
}