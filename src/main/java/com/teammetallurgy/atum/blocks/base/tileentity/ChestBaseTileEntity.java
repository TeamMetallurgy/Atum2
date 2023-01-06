package com.teammetallurgy.atum.blocks.base.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ChestBaseTileEntity extends ChestBlockEntity {
    public boolean canBeSingle;
    public boolean canBeDouble;
    private final Block chestBlock;

    public ChestBaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, boolean canBeSingle, boolean canBeDouble, Block chestBlock) {
        super(type, pos, state);
        this.canBeSingle = canBeSingle;
        this.canBeDouble = canBeDouble;
        this.chestBlock = chestBlock;
    }

    @Override
    @Nonnull
    protected Component getDefaultName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    private boolean isChestAt(BlockPos pos) {
        if (level == null) {
            return false;
        } else {
            Block block = level.getBlockState(pos).getBlock();
            BlockEntity tileEntity = level.getBlockEntity(pos);
            return tileEntity instanceof ChestBaseTileEntity && block == this.chestBlock;
        }
    }

    @Override
    @Nonnull
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition.getX() - 1, worldPosition.getY(), worldPosition.getZ() - 1, worldPosition.getX() + 2, worldPosition.getY() + 2, worldPosition.getZ() + 2);
    }

    @Override
    public void unpackLootTable(@Nullable Player player) { //Added null check for LootTableManager to prevent issues with WIT
        if (this.lootTable != null && this.level != null && level.getServer() != null) {
            LootTables manager = this.level.getServer().getLootTables();
            if (manager != null) {
                super.unpackLootTable(player);
            }
        }
    }
}