package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumConfig;
import com.teammetallurgy.atum.world.AtumTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPortal extends BlockBreakable {
    private static final AxisAlignedBB PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockPortal() {
        super(Material.PORTAL, false);
        this.setTickRandomly(true);
        this.setHardness(-1.0F);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(0.75F);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return PORTAL_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    public boolean trySpawnPortal(World world, BlockPos pos) {
        BlockPortal.Size size = new BlockPortal.Size(world, pos);

        if (size.isValid()) {
            size.placePortalBlocks();
            world.playSound(null, pos, SoundEvents.BLOCK_END_PORTAL_SPAWN, SoundCategory.BLOCKS, 0.7F, 1.0F);
            return true;
        } else {
            BlockPortal.Size size1 = new BlockPortal.Size(world, pos);

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
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) {
        Size size = new Size(world, pos);

        if (!size.isValid()) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && !entity.isRiding() && !entity.isBeingRidden() && entity instanceof EntityPlayerMP && entity.timeUntilPortal <= 0) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            final int dimension = player.dimension == AtumConfig.DIMENSION_ID ? DimensionType.OVERWORLD.getId() : AtumConfig.DIMENSION_ID;
            player.timeUntilPortal = 300;

            player.changeDimension(dimension, new AtumTeleporter(player.server.getWorld(dimension)));
            if (player.dimension != AtumConfig.DIMENSION_ID) {
                player.setSpawnChunk(new BlockPos(player), true, AtumConfig.DIMENSION_ID);
            }
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
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

            int east = getDistanceUntilEdge(pos, EnumFacing.EAST);
            int west = getDistanceUntilEdge(pos, EnumFacing.WEST);
            int north = getDistanceUntilEdge(pos, EnumFacing.NORTH);
            int south = getDistanceUntilEdge(pos, EnumFacing.SOUTH);

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

        int getDistanceUntilEdge(BlockPos pos, EnumFacing facing) {
            int i;

            for (i = 0; i < 9; ++i) {
                BlockPos blockpos = pos.offset(facing, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos)) || !isSandBlock(this.world.getBlockState(blockpos.down()))) {
                    break;
                }
            }

            IBlockState state = this.world.getBlockState(pos.offset(facing, i));
            return isSandBlock(state) ? i : 0;
        }

        boolean isEmptyBlock(IBlockState state) {
            return state.getMaterial() == Material.WATER;
        }

        boolean isSandBlock(IBlockState state) {
            return state.getBlock() instanceof BlockSandStone || state.getBlock() instanceof BlockLimestoneBricks;
        }

        boolean isValid() {
            return this.valid;
        }

        void placePortalBlocks() {
            for (BlockPos.MutableBlockPos portalPos : BlockPos.MutableBlockPos.getAllInBoxMutable(nw, se)) {
                this.world.setBlockState(portalPos, AtumBlocks.PORTAL.getDefaultState(), 2);
            }
        }
    }
}