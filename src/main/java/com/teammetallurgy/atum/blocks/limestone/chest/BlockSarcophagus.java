package com.teammetallurgy.atum.blocks.limestone.chest;

import com.teammetallurgy.atum.blocks.base.BlockChestBase;
import com.teammetallurgy.atum.blocks.limestone.chest.tileentity.TileEntitySarcophagus;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockSarcophagus extends BlockChestBase {

    public BlockSarcophagus() {
        this.setHardness(4.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntitySarcophagus();
    }

    @Override
    public boolean onBlockActivated(World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySarcophagus) {
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            if (!sarcophagus.hasSpawned()) {
                for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                    TileEntity  tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                    if (tileEntityOffset instanceof TileEntitySarcophagus) {
                        ((TileEntitySarcophagus)tileEntityOffset).hasSpawned = true;
                        ((TileEntitySarcophagus)tileEntityOffset).isOpenable = true;
                    }
                }
                sarcophagus.spawn(player);
            }
        }

        if (world.isRemote) {
            return true;
        } else {
            IInventory inventory = this.getLockableContainer(world, pos);
            if (inventory != null) {
                player.displayGUIChest(inventory);
            }
            return true;
        }
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntitySarcophagus) {
            System.out.println("John");
            TileEntitySarcophagus sarcophagus = (TileEntitySarcophagus) tileEntity;
            sarcophagus.isOpenable = true;
            sarcophagus.hasSpawned = true;

            for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                TileEntity  tileEntityOffset = world.getTileEntity(pos.offset(horizontal));
                if (tileEntityOffset instanceof TileEntitySarcophagus) {
                    ((TileEntitySarcophagus)tileEntityOffset).hasSpawned = true;
                    ((TileEntitySarcophagus)tileEntityOffset).isOpenable = true;
                }
            }
        }
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