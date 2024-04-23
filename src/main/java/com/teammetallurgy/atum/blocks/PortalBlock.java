package com.teammetallurgy.atum.blocks;

import com.google.common.cache.LoadingCache;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumAPI;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.util.ITeleporter;

import javax.annotation.Nonnull;

public class PortalBlock extends HalfTransparentBlock {
    private static final VoxelShape PORTAL_AABB = Shapes.box(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public PortalBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_ORANGE).pushReaction(PushReaction.BLOCK).strength(-1.0F).sound(SoundType.GLASS).noOcclusion().lightLevel((state) -> 10).randomTicks());
    }

    @Override
    @Nonnull
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return PORTAL_AABB;
    }

    @Override
    @Nonnull
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter reader, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean canBeReplaced(@Nonnull BlockState state, @Nonnull Fluid fluid) {
        return false;
    }

    public boolean trySpawnPortal(Level level, BlockPos pos) {
        Size size = new Size(level, pos);

        if (size.isValid()) {
            size.placePortalBlocks();
            level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 0.7F, 1.0F);
            return true;
        } else {
            Size size1 = new Size(level, pos);

            if (size1.isValid()) {
                size1.placePortalBlocks();
                level.playSound(null, pos, SoundEvents.END_PORTAL_SPAWN, SoundSource.BLOCKS, 1.0F, 1.0F);
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void neighborChanged(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos, boolean isMoving) {
        Size size = new Size(level, pos);
        BlockPos posUp = pos.above();
        if (neighborBlock == this || !(neighborPos.getX() == posUp.getX() && neighborPos.getY() == posUp.getY() && neighborPos.getZ() == posUp.getZ())) {
            if (!size.isValid()) {
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    @Override
    public void entityInside(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
           changeDimension(serverLevel, entity, new TeleporterAtum());
        }
    }

    public static void changeDimension(ServerLevel serverLevel, Entity entity, ITeleporter teleporter) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canChangeDimensions() && entity instanceof ServerPlayer player) {
            ResourceKey<Level> key = serverLevel.dimension() == Atum.ATUM ? Level.OVERWORLD : Atum.ATUM;
            ServerLevel destWorld = serverLevel.getServer().getLevel(key);
            if (destWorld == null) {
                return;
            }
            if (player.portalCooldown <= 0) {
                serverLevel.getProfiler().push("portal");
                player.changeDimension(destWorld, teleporter);
                player.portalCooldown = 300; //Set portal cooldown
                serverLevel.getProfiler().pop();
            }
        }
    }

    @Override
    @Nonnull
    public ItemStack getCloneItemStack(@Nonnull BlockState state, @Nonnull HitResult target, @Nonnull LevelReader level, @Nonnull BlockPos pos, @Nonnull Player player) {
        return ItemStack.EMPTY;
    }

    public static BlockPattern.BlockPatternMatch createPatternHelper(LevelAccessor level, BlockPos pos) {
        Size size = new Size(level, pos);
        LoadingCache<BlockPos, BlockInWorld> cache = BlockPattern.createLevelCache(level, true);
        if (!size.isValid()) {
            size = new Size(level, pos);
        }

        if (!size.isValid()) {
            return new BlockPattern.BlockPatternMatch(pos, Direction.NORTH, Direction.SOUTH, cache, 1, 1, 1);
        } else {
            return new BlockPattern.BlockPatternMatch(pos, Direction.NORTH, Direction.EAST, cache, size.width, 4, size.length);
        }
    }

    public static class Size {
        private static final int MAX_SIZE = 9;
        private static final int MIN_SIZE = 3;

        private final LevelAccessor level;
        private boolean valid = false;
        private BlockPos nw;
        private BlockPos se;
        private int width;
        private int length;

        public Size(LevelAccessor level, BlockPos pos) {
            this.level = level;

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

            this.nw = nwCorner.offset(1, 0, 1);
            this.se = seCorner.offset(-1, 0, -1);
            int wallWidth = width + 2;
            int wallLength = length + 2;
            this.width = wallWidth;
            this.length = wallLength;

            for (int y = 0; y <= 1; y++) {
                for (int x = 0; x < wallWidth; x++) {
                    for (int z = 0; z < wallLength; z++) {
                        if (y == 0 || x == 0 || z == 0 || x == wallWidth - 1 || z == wallLength - 1) {
                            if (!isSandBlock(level.getBlockState(nwCorner.below().offset(x, y, z)))) {
                                return;
                            }
                        }
                    }
                }
            }

            for (int y = 0; y < 2; y++) {
                if (!isSandBlock(level.getBlockState(neCorner.offset(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(level.getBlockState(nwCorner.offset(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(level.getBlockState(seCorner.offset(0, y + 1, 0)))) {
                    return;
                }
                if (!isSandBlock(level.getBlockState(swCorner.offset(0, y + 1, 0)))) {
                    return;
                }
            }
            this.valid = true;
        }

        int getDistanceUntilEdge(BlockPos pos, Direction facing) {
            int i;

            for (i = 0; i < 9; ++i) {
                BlockPos blockpos = pos.relative(facing, i);

                if (!this.isEmptyBlock(this.level.getBlockState(blockpos)) || !isSandBlock(this.level.getBlockState(blockpos.below()))) {
                    break;
                }
            }

            BlockState state = this.level.getBlockState(pos.relative(facing, i));
            return isSandBlock(state) ? i : 0;
        }

        boolean isEmptyBlock(BlockState state) {
            return state.getFluidState().is(FluidTags.WATER);
        }

        boolean isSandBlock(BlockState state) {
            return state.is(Tags.Blocks.SANDSTONE) || state.is(AtumAPI.Tags.LIMESTONE_BRICKS);
        }

        boolean isValid() {
            return this.valid;
        }

        void placePortalBlocks() {
            for (BlockPos portalPos : BlockPos.MutableBlockPos.betweenClosed(nw, se)) {
                this.level.setBlock(portalPos, AtumBlocks.PORTAL.get().defaultBlockState(), 2);
            }
        }
    }
}