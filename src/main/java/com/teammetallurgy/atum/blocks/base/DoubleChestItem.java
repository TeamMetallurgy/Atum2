package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DoubleChestItem extends BlockItem {

    public DoubleChestItem(Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull PlayerEntity player, World world, @Nonnull BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, @Nonnull BlockState newState) {
        Direction horizontal = Direction.byHorizontalIndex(MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3);
        BlockPos posRight = pos.offset(horizontal.rotateY());

        if (!world.getBlockState(posRight).getBlock().isReplaceable(world, posRight) || !world.isAirBlock(posRight)) {
            return false;
        } else {
            return super.placeBlockAt(stack, player, world, pos, facing, hitX, hitY, hitZ, newState);
        }
    }
}