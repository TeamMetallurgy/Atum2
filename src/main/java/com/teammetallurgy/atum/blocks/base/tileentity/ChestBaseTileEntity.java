package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChestBaseTileEntity extends ChestTileEntity {
    public boolean canBeSingle;
    public boolean canBeDouble;
    private Block chestBlock;

    public ChestBaseTileEntity(boolean canBeSingle, boolean canBeDouble, Block chestBlock) {
        this.canBeSingle = canBeSingle;
        this.canBeDouble = canBeDouble;
        this.chestBlock = chestBlock;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull BlockState oldState, @Nonnull BlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    @Override
    protected ChestTileEntity getAdjacentChest(@Nonnull Direction side) {
        BlockPos pos = this.pos.offset(side);

        if (isChestAt(pos) && this.canBeDouble) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof ChestBaseTileEntity) {
                ChestBaseTileEntity chestBase = (ChestBaseTileEntity) tileEntity;
                chestBase.setNeighbor(this, side.getOpposite());
                return chestBase;
            }
        }
        return null;
    }

    private boolean isChestAt(BlockPos pos) {
        if (world == null) {
            return false;
        } else {
            Block block = world.getBlockState(pos).getBlock();
            TileEntity tileEntity = world.getTileEntity(pos);
            return block instanceof ChestBlock && ((ChestBlock) block).chestType == getChestType() && tileEntity instanceof ChestBaseTileEntity && block == this.chestBlock;
        }
    }

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX() - 1, pos.getY(), pos.getZ() - 1, pos.getX() + 2, pos.getY() + 2, pos.getZ() + 2);
    }

    @Override
    public void fillWithLoot(@Nullable PlayerEntity player) { //Added null check for LootTableManager to prevent issues with WIT
        if (this.lootTable != null && this.world != null && world.getServer() != null) {
            LootTableManager manager = this.world.getServer().getLootTableManager();
            if (manager != null) {
                super.fillWithLoot(player);
            }
        }
    }
}