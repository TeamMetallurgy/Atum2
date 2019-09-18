package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.blocks.base.BlockAtumSlab;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class AtumSlabItem extends ItemBlock { //TODO Remove 1.13
    private final BlockAtumSlab slab;

    public AtumSlabItem(Block block, BlockAtumSlab slab) {
        super(block);
        this.slab = slab;
        this.setMaxDamage(0);
    }

    @Override
    public int getMetadata(int damage) {
        return 0;
    }

    @Override
    @Nonnull
    public String getTranslationKey(ItemStack stack) {
        return this.slab.getTranslationKey();
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);

        if (!heldItem.isEmpty() && player.canPlayerEdit(pos.offset(facing), facing, heldItem)) {
            IBlockState state = world.getBlockState(pos);

            if (state.getBlock() == this.slab) {
                BlockAtumSlab.Type type = state.getValue(BlockAtumSlab.TYPE);

                if ((facing == EnumFacing.UP && type == BlockAtumSlab.Type.BOTTOM || facing == EnumFacing.DOWN && type == BlockAtumSlab.Type.TOP)) {
                    IBlockState doubleState = state.withProperty(BlockAtumSlab.TYPE, BlockAtumSlab.Type.DOUBLE);
                    AxisAlignedBB axisalignedbb = doubleState.getCollisionBoundingBox(world, pos);

                    if (axisalignedbb != null && axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(pos)) && world.setBlockState(pos, doubleState, 11)) {
                        SoundType soundType = this.slab.getSoundType(doubleState, world, pos, player);
                        world.playSound(player, pos, soundType.getPlaceSound(), SoundCategory.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
                        heldItem.shrink(1);

                        if (player instanceof EntityPlayerMP) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos, heldItem);
                        }
                    }
                    return EnumActionResult.SUCCESS;
                }
            }
            return this.tryPlace(player, heldItem, world, pos.offset(facing)) ? EnumActionResult.SUCCESS : super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World world, @Nonnull BlockPos pos, @Nonnull EnumFacing side, @Nonnull EntityPlayer player, ItemStack stack) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.slab) {
            boolean flag = state.getValue(BlockAtumSlab.TYPE) == BlockAtumSlab.Type.TOP;

            if ((side == EnumFacing.UP && !flag || side == EnumFacing.DOWN && flag)) {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState stateOffset = world.getBlockState(pos);
        return stateOffset.getBlock() == this.slab || super.canPlaceBlockOnSide(world, pos, side, player, stack);
    }

    private boolean tryPlace(EntityPlayer player, @Nonnull ItemStack stack, World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() == this.slab) {
            IBlockState doubleState = state.withProperty(BlockAtumSlab.TYPE, BlockAtumSlab.Type.DOUBLE);
            AxisAlignedBB axisalignedbb = doubleState.getCollisionBoundingBox(world, pos);

            if (axisalignedbb != Block.NULL_AABB && world.checkNoEntityCollision(axisalignedbb.offset(pos)) && world.setBlockState(pos, doubleState, 11)) {
                SoundType soundtype = this.slab.getSoundType(doubleState, world, pos, player);
                world.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }
}