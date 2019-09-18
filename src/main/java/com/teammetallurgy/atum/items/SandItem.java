package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.blocks.BlockSandLayers;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class SandItem extends ItemBlock {

    public SandItem(Block block) {
        super(block);
        this.setMaxDamage(0);
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(PlayerEntity player, World world, @Nonnull BlockPos pos, @Nonnull Hand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack)) {
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            BlockPos blockpos = pos;

            if ((facing != EnumFacing.UP || block != this.block) && !block.isReplaceable(world, pos)) {
                blockpos = pos.offset(facing);
                iblockstate = world.getBlockState(blockpos);
                block = iblockstate.getBlock();
            }

            if (block == this.block) {
                int i = iblockstate.getValue(BlockSandLayers.LAYERS);

                if (i < 8) {
                    IBlockState iblockstate1 = iblockstate.withProperty(BlockSandLayers.LAYERS, i + 1);
                    if (i == 7) {
                        iblockstate1 = AtumBlocks.SAND.getDefaultState();
                    }
                    AxisAlignedBB axisalignedbb = iblockstate1.getCollisionBoundingBox(world, blockpos);

                    if (axisalignedbb != null && axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(blockpos)) && world.setBlockState(blockpos, iblockstate1, 10)) {
                        SoundType soundtype = this.block.getSoundType(iblockstate1, world, pos, player);
                        world.playSound(player, blockpos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

                        if (player instanceof ServerPlayerEntity) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) player, pos, itemstack);
                        }

                        itemstack.shrink(1);
                        return EnumActionResult.SUCCESS;
                    }
                }
            }

            return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public boolean canPlaceBlockOnSide(World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull PlayerEntity player, @Nonnull ItemStack stack) {
        IBlockState state = world.getBlockState(pos);
        return (state.getBlock() == AtumBlocks.SAND_LAYERED && state.getValue(BlockSandLayers.LAYERS) <= 7) || super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }
}