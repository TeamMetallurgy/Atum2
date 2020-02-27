package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

public class PortalBlock extends BreakableBlock {
    private static final VoxelShape PORTAL_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public PortalBlock() {
        super(Properties.create(Material.PORTAL, MaterialColor.ORANGE_TERRACOTTA).hardnessAndResistance(-1.0F).sound(SoundType.GLASS).lightValue(10).tickRandomly());
    }

    @Override
    @Nonnull
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos pos, ISelectionContext context) {
        return PORTAL_AABB;
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    public boolean trySpawnPortal(World world, BlockPos pos) {
        PortalBlock.Size size = new PortalBlock.Size(world, pos);

        if (size.isValid()) {
            size.placePortalBlocks();
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 0.7F, 1.0F);
            return true;
        } else {
            PortalBlock.Size size1 = new PortalBlock.Size(world, pos);

            if (size1.isValid()) {
                size1.placePortalBlocks();
                world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean isMoving) {
        Size size = new Size(world, pos);

        if (neighborBlock == this || size.isSandBlock(neighborBlock.getDefaultState())) {
            if (!size.isValid()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!entity.isOnePlayerRiding() && !entity.isBeingRidden() && entity instanceof ServerPlayerEntity && entity.timeUntilPortal <= 0) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            /*final DimensionType dimension = player.dimension == AtumDimensionRegistration.ATUM ? DimensionType.OVERWORLD : AtumDimensionRegistration.ATUM; //TODO
            changeDimension(world, (ServerPlayerEntity) entity, dimension, new AtumTeleporter(player.server.getWorld(dimension)));*/
        }
    }

    public static void changeDimension(World world, ServerPlayerEntity player, DimensionType dimension) {
        if (!world.isRemote) {
            player.changeDimension(dimension);
            player.timeUntilPortal = 300;
            /*if (player.dimension == AtumDimensionRegistration.ATUM) { //TODO
                BlockPos playerPos = new BlockPos(player);
                if (world.isAirBlock(playerPos) && world.getBlockState(playerPos).isSideSolid(world, playerPos, Direction.UP)) {
                    player.setSpawnChunk(playerPos, true, AtumDimensionRegistration.ATUM);
                }
            }*/
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return ItemStack.EMPTY;
    }

    @Override
    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public static class Size {
        private static final int MAX_SIZE = 9;
        private static final int MIN_SIZE = 3;

        private final World world;
        private boolean valid = false;
        private BlockPos nw;
        private BlockPos se;

        public Size(World world, BlockPos pos) {
            this.world = world;

            int east = getDistanceUntilEdge(pos, Direction.EAST);
            int west = getDistanceUntilEdge(pos, Direction.WEST);
            int north = getDistanceUntilEdge(pos, Direction.NORTH);
            int south = getDistanceUntilEdge(pos, Direction.SOUTH);

            int width = east + west - 1;
            int length = north + south - 1;

            if (width > Size.MAX_SIZE || length > Size.MAX_SIZE) {
                return;
            }
            if (width < Size.MIN_SIZE || length < Size.MIN_SIZE) {
                return;
            }

            BlockPos neCorner = pos.east(east).north(north);
            BlockPos nwCorner = pos.west(west).north(north);
            BlockPos seCorner = pos.east(east).south(south);
            BlockPos swCorner = pos.west(west).south(south);

            this.nw = nwCorner.add(1, 0, 1);
            this.se = seCorner.add(-1, 0, -1);
            int wallWidth = width + 2;
            int wallLength = length + 2;

            for (int y = 0; y <= 1; y++) {
                for (int x = 0; x < wallWidth; x++) {
                    for (int z = 0; z < wallLength; z++) {
                        if (y == 0 || x == 0 || z == 0 || x == wallWidth - 1 || z == wallLength - 1) {
                            if (!isSandBlock(world.getBlockState(nwCorner.down().add(x, y, z)))) {
                                return;
                            }
                        }
                    }
                }
            }

            for (int y = 0; y < 2; y++) {
                if (!isSandBlock(world.getBlockState(neCorner.add(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(world.getBlockState(nwCorner.add(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(world.getBlockState(seCorner.add(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(world.getBlockState(swCorner.add(0, y + 1, 0)))) {
                    return;
                }
            }
            this.valid = true;
        }

        int getDistanceUntilEdge(BlockPos pos, Direction facing) {
            int i;

            for (i = 0; i < 9; ++i) {
                BlockPos blockpos = pos.offset(facing, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos)) || !isSandBlock(this.world.getBlockState(blockpos.down()))) {
                    break;
                }
            }

            BlockState state = this.world.getBlockState(pos.offset(facing, i));
            return isSandBlock(state) ? i : 0;
        }

        boolean isEmptyBlock(BlockState state) {
            return state.getMaterial() == Material.WATER;
        }

        boolean isSandBlock(BlockState state) {
            return state.getBlock().isIn(Tags.Blocks.SANDSTONE) || state.getBlock() instanceof LimestoneBrickBlock;
        }

        boolean isValid() {
            return this.valid;
        }

        void placePortalBlocks() {
            for (BlockPos portalPos : BlockPos.MutableBlockPos.getAllInBoxMutable(nw, se)) {
                this.world.setBlockState(portalPos, AtumBlocks.PORTAL.getDefaultState(), 2);
            }
        }
    }
}