package com.teammetallurgy.atum.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemAtumSeeds extends ItemSeeds {
    private final Block crops;

    public ItemAtumSeeds(Block crops, Block soil) {
        super(crops, soil);
        this.crops = crops;
    }

    @Override
    @Nonnull
    //Same as vanilla, but added canPlaceBlockAt check
    public EnumActionResult onItemUse(EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        IBlockState cropState = this.crops.getDefaultState();
        if (!world.isRemote && facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, heldStack) && state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, this)
                && cropState.getBlock().canPlaceBlockAt(world, pos.up()) && world.isAirBlock(pos.up())) {
            world.setBlockState(pos.up(), cropState);
            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), heldStack);
            }
            heldStack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }


    @Override
    @Nonnull
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        return this.crops.getDefaultState();
    }
}