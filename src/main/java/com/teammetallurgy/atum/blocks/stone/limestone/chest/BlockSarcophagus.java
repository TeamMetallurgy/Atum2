package com.teammetallurgy.atum.blocks.stone.limestone.chest;

import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber
public class BlockSarcophagus extends BlockChestBase {

    public BlockSarcophagus() {
        this.setHardness(4.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySarcophagus();
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntitySarcophagus && !((TileEntitySarcophagus) tileEntity).isOpenable ? -1.0F : super.getBlockHardness(state, world, pos);
    }

    @Override
    public boolean onBlockActivated(World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        EnumFacing facing = state.getValue(FACING);

        //Right-Click left block, when right-clicking right block
        BlockPos posLeft = pos.offset(facing.rotateY());
        TileEntity tileLeft = world.getTileEntity(posLeft);
        if (world.getBlockState(posLeft).getBlock() == this && tileLeft instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileLeft;
            if (!sarcophagus.hasSpawned) {
                this.onBlockActivated(world, pos.offset(facing.rotateY()), state, player, hand, side, hitX, hitY, hitZ);
                return false;
            }
        }

        if (tileEntity instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            if (!sarcophagus.hasSpawned) {
                if (this.canSpawnPharaoh(world, pos, facing)) {
                    for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                        TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                        if (tileEntityOffset instanceof TileEntitySarcophagus) {
                            ((TileEntitySarcophagus) tileEntityOffset).hasSpawned = true;
                        }
                    }
                    sarcophagus.spawn(player, world.getDifficultyForLocation(pos));
                    sarcophagus.hasSpawned = true;
                    return false;
                } else if (!sarcophagus.isOpenable) {
                    player.sendStatusMessage(new TextComponentTranslation("chat.atum.cannotSpawnPharaoh").setStyle(new Style().setColor(TextFormatting.RED)), true);
                    world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_INFECT, SoundCategory.HOSTILE, 0.7F, 0.4F, false);
                    return false;
                }
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    private boolean canSpawnPharaoh(World world, BlockPos pos, EnumFacing facing) {
        boolean isTopLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomLeftCorner = world.getBlockState(pos.offset(facing.rotateY(), 2).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isTopRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing.getOpposite(), 1)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        boolean isBottomRightCorner = world.getBlockState(pos.offset(facing.rotateYCCW(), 3).offset(facing, 2)).getBlock() == AtumBlocks.PHARAOH_TORCH;
        return isTopLeftCorner && isBottomLeftCorner && isTopRightCorner && isBottomRightCorner;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            sarcophagus.setOpenable();
            sarcophagus.hasSpawned = true;

            for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                TileEntity tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                if (tileEntityOffset instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus) tileEntityOffset).hasSpawned = true;
                    ((TileEntitySarcophagus) tileEntityOffset).setOpenable();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlaced(BlockEvent.PlaceEvent event) { //Prevent placement, if right side of Sarcophagus would be next to another Sarcophagi block
        IBlockState placedState = event.getPlacedBlock();
        if (placedState.getBlock() instanceof BlockSarcophagus) {
            if (!canPlaceRightSac(event.getWorld(), event.getPos(), placedState.getValue(FACING))) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean canPlaceRightSac(World world, BlockPos pos, EnumFacing facing) {
        boolean right2 = world.getBlockState(pos.offset(facing.rotateYCCW(), 2)).getBlock() instanceof BlockSarcophagus;
        boolean up = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing)).getBlock() instanceof BlockSarcophagus;
        boolean down = world.getBlockState(pos.offset(facing.rotateYCCW()).offset(facing.getOpposite())).getBlock() instanceof BlockSarcophagus;
        return !right2 && !up && !down;
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumBlocks.SARCOPHAGUS);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        drops.add(new ItemStack(AtumBlocks.SARCOPHAGUS));
    }
}