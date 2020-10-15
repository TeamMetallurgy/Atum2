package com.teammetallurgy.atum.blocks;

import com.google.common.cache.LoadingCache;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtum;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtumStart;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;

public class PortalBlock extends BreakableBlock {
    private static final VoxelShape PORTAL_AABB = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public PortalBlock() {
        super(Properties.create(Material.PORTAL, MaterialColor.ORANGE_TERRACOTTA).hardnessAndResistance(-1.0F).sound(SoundType.GLASS).setLightLevel((state) -> 10).tickRandomly());
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return PORTAL_AABB;
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader reader, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isReplaceable(@Nonnull BlockState state, @Nonnull Fluid fluid) {
        return false;
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
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        Size size = new Size(world, pos);
        if (neighborBlock == this || size.isSandBlock(neighborBlock.getDefaultState())) {
            if (!size.isValid()) {
                world.setBlockState(pos, Blocks.AIR.getDefaultState());
            }
        }
    }

    @Override
    public void onEntityCollision(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (world instanceof ServerWorld) {
           changeDimension((ServerWorld) world, entity, new TeleporterAtum());
        }
    }

    public static void changeDimension(ServerWorld serverWorld, Entity entity, ITeleporter teleporter) {
        if (!entity.isPassenger() && !entity.isBeingRidden() && entity.isNonBoss() && entity instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) entity;
            RegistryKey<World> key = serverWorld.getDimensionKey() == Atum.ATUM ? World.OVERWORLD : Atum.ATUM;
            ServerWorld destWorld = serverWorld.getServer().getWorld(key);
            if (destWorld == null) {
                return;
            }
            //player.timeUntilPortal = 300; //TODO???
            player.changeDimension(destWorld, teleporter);
        }
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        return ItemStack.EMPTY;
    }

    public static BlockPattern.PatternHelper createPatternHelper(IWorld world, BlockPos pos) {
        Size size = new Size(world, pos);
        LoadingCache<BlockPos, CachedBlockInfo> cache = BlockPattern.createLoadingCache(world, true);
        if (!size.isValid()) {
            size = new Size(world, pos);
        }

        if (!size.isValid()) {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.SOUTH, cache, 1, 1, 1);
        } else {
            return new BlockPattern.PatternHelper(pos, Direction.NORTH, Direction.EAST, cache, size.width, 4, size.length);
        }
    }

    public static class Size {
        private static final int MAX_SIZE = 9;
        private static final int MIN_SIZE = 3;

        private final IWorld world;
        private boolean valid = false;
        private BlockPos nw;
        private BlockPos se;
        private int width;
        private int length;

        public Size(IWorld world, BlockPos pos) {
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
            this.width = wallWidth;
            this.length = wallLength;

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
            return state.getBlock().isIn(Tags.Blocks.SANDSTONE) || state.getBlock().isIn(AtumAPI.Tags.LIMESTONE_BRICKS);
        }

        boolean isValid() {
            return this.valid;
        }

        void placePortalBlocks() {
            for (BlockPos portalPos : BlockPos.Mutable.getAllInBoxMutable(nw, se)) {
                this.world.setBlockState(portalPos, AtumBlocks.PORTAL.getDefaultState(), 2);
            }
        }
    }
}