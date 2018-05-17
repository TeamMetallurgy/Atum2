package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.AtumTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.BlockSandStone;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
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

public class BlockPortal extends BlockBreakable { //TODO Redo for 1.9. Switch over to having a sub-class with the size
    protected static final AxisAlignedBB PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

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

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);

        if (world.provider.isSurfaceWorld() && world.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < world.getDifficulty().getDifficultyId()) {
            int i = pos.getY();
            BlockPos blockpos;

            for (blockpos = pos; !world.getBlockState(blockpos).isTopSolid() && blockpos.getY() > 0; blockpos = blockpos.down()) {
            }

            if (i > 0 && !world.getBlockState(blockpos.up()).isNormalCube()) {
                Entity entity = ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityPigZombie.class), (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 1.1D, (double) blockpos.getZ() + 0.5D);

                if (entity != null) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }

    public boolean trySpawnPortal(World worldIn, BlockPos pos) {
        BlockPortal.Size size = new BlockPortal.Size(worldIn, pos);

        if (size.isValid()) {
            size.placePortalBlocks();
            return true;
        } else {
            BlockPortal.Size size1 = new BlockPortal.Size(worldIn, pos);

            if (size1.isValid()) {
                size1.placePortalBlocks();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) { //TODO
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = -1; y < 1; y++) {
                    IBlockState blockState = world.getBlockState(pos.add(x, y, z));
                    if (blockState != Blocks.SANDSTONE.getDefaultState() && blockState != this.getDefaultState() && blockState != BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).setBlockUnbreakable().getDefaultState()) {
                        world.setBlockToAir(pos);
                    }
                }
            }
        }
    }

    private static boolean isSandstone(IBlockState state) {
        Block block = state.getBlock();
        return block instanceof BlockSandStone || block == BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!entity.isSneaking()) {
            return;
        }
        if (!world.isRemote && !entity.isRiding() && !entity.isBeingRidden() && entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            final int dimension = player.dimension == AtumConfig.DIMENSION_ID ? DimensionType.OVERWORLD.getId() : AtumConfig.DIMENSION_ID;

            player.setSneaking(false);
            player.mcServer.getPlayerList().transferPlayerToDimension(player, dimension, new AtumTeleporter(player.mcServer.getWorld(dimension)));
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
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    public static class Size {
        private static final int MAX_SIZE = 9;
        private static final int MIN_SIZE = 3;

        private final World world;
        private boolean valid = false;
        private BlockPos bottomLeft;

        public Size(World world, BlockPos pos) {
            this.world = world;
            this.bottomLeft = pos;

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

            int wallWidth = width + 2;
            int wallLength = length + 2;

            for (int y = 0; y <= 1; y++) {
                for (int x = 0; x < wallWidth; x++) {
                    for (int z = 0; z < wallLength; z++) {
                        if (y == 0 || x == 0 || z == 0 || x == wallWidth - 1 || z == wallLength - 1) {
                            if (world.getBlockState(nwCorner.down().add(x, y, z)).getBlock() != Blocks.SANDSTONE) {
                                return;
                            }
                        }
                    }
                }
            }

            for (int y = 0; y < 2; y++) {
                if (world.getBlockState(neCorner.add(0, y + 1, 0)).getBlock() == Blocks.SANDSTONE) {
                    return;
                }

                if (world.getBlockState(nwCorner.add(0, y + 1, 0)).getBlock() == Blocks.SANDSTONE) {
                    return;
                }

                if (world.getBlockState(seCorner.add(0, y + 1, 0)).getBlock() == Blocks.SANDSTONE) {
                    return;
                }

                if (world.getBlockState(swCorner.add(0, y + 1, 0)).getBlock() == Blocks.SANDSTONE) {
                    return;
                }
            }

            this.valid = true;
        }

        int getDistanceUntilEdge(BlockPos pos, EnumFacing facing) {
            int i;

            for (i = 0; i < 9; ++i) {
                BlockPos blockpos = pos.offset(facing, i);

                if (!this.isEmptyBlock(this.world.getBlockState(blockpos)) || this.world.getBlockState(blockpos.down()).getBlock() != Blocks.SANDSTONE) {
                    break;
                }
            }

            Block block = this.world.getBlockState(pos.offset(facing, i)).getBlock();
            return block == Blocks.SANDSTONE ? i : 0;
        }

        boolean isEmptyBlock(IBlockState state) {
            return state.getMaterial() == Material.WATER;
        }

        boolean isValid() {
            return this.valid;
        }

        void placePortalBlocks() {
            BlockPos pos = this.bottomLeft;
            for (BlockPos.MutableBlockPos portalPos : BlockPos.MutableBlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1))) {
                this.world.setBlockState(portalPos, AtumBlocks.PORTAL.getDefaultState(), 2);
            }
        }
    }
}