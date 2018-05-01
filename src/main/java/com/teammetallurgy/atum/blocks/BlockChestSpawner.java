package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.blocks.tileentity.chests.TileEntityChestSpawner;
import com.teammetallurgy.atum.items.AtumLoot;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockChestSpawner extends BlockChest {

    public BlockChestSpawner() {
        super(Type.BASIC);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityChestSpawner();
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Blocks.CHEST);
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (stack.hasDisplayName()) {
            ((TileEntityChestSpawner) world.getTileEntity(pos)).setCustomName(stack.getDisplayName());
        }

        AtumLoot.fillChest((TileEntityChestSpawner) world.getTileEntity(pos), 5, 0.5F);
    }
}