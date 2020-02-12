package com.teammetallurgy.atum.blocks.beacon;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityHeartOfRa;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockHeartOfRa extends ContainerBlock {
    private static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);

    public BlockHeartOfRa() {
        super(Properties.create(AtumMats.HEART_OF_RA, MaterialColor.GOLD));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new TileEntityHeartOfRa();
    }

    @Override
    public boolean canEntitySpawn(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, EntityType<?> entityType) {
        return entityType == AtumEntities.HEART_OF_RA;
    }

    @Override
    public boolean canCreatureSpawn(BlockState state, IBlockReader world, BlockPos pos, EntitySpawnPlacementRegistry.PlacementType type, @Nullable EntityType<?> entityType) {
        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onBlockAdded(state, world, pos, oldState, isMoving);

        HeartOfRaEntity heartOfRa = new HeartOfRaEntity(world, (float) pos.getX() + 0.5F, pos.getY(), (float) pos.getZ() + 0.5F);
        world.addEntity(heartOfRa);
    }

    @Override
    @Nonnull
    public VoxelShape getRenderShape(BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos) {
        return SHAPE;
    }

    @Override
    @Nonnull
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }
}