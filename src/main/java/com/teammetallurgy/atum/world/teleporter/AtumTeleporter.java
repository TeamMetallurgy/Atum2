/*
package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumTeleporter extends Teleporter {

    public AtumTeleporter(WorldServer worldServer) {
        super(worldServer);
    }

    @Override
    public void placeInPortal(@Nonnull Entity entity, float rotationYaw) {
        if (!this.placeInExistingPortal(entity, rotationYaw)) {
            this.makePortal(entity);
            this.placeInExistingPortal(entity, rotationYaw);
        }
    }

    @Override
    public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
        final long chunkId = ChunkPos.asLong(MathHelper.floor(entity.posX), MathHelper.floor(entity.posZ));

        double distance = -1.0D;
        boolean doesPortalExist = true;
        BlockPos location = BlockPos.ORIGIN;

        if (this.destinationCoordinateCache.containsKey(chunkId)) {
            final PortalPosition portalPosition = this.destinationCoordinateCache.get(chunkId);
            distance = 0.0D;
            location = portalPosition;
            portalPosition.lastUpdateTime = this.world.getGameTime();
            doesPortalExist = false;
        } else {
            final BlockPos entityPos = new BlockPos(entity);
            for (int offsetX = -128; offsetX <= 128; ++offsetX) {
                BlockPos positionCache;

                for (int offsetZ = -128; offsetZ <= 128; ++offsetZ) {

                    for (BlockPos currentPos = entityPos.add(offsetX, this.world.getActualHeight() - 1 - entityPos.getY(), offsetZ); currentPos.getY() >= 0; currentPos = positionCache) {
                        positionCache = currentPos.down();
                        if (this.world.getBlockState(currentPos).getBlock() == AtumBlocks.PORTAL) {
                            while (this.world.getBlockState(positionCache = currentPos.down()).getBlock() == AtumBlocks.PORTAL) {
                                currentPos = positionCache;
                            }
                            final double distanceToEntity = currentPos.distanceSq(entityPos);

                            if (distance < 0.0D || distanceToEntity < distance) {
                                distance = distanceToEntity;
                                location = currentPos;
                            }
                        }
                    }
                }
            }
        }

        if (distance >= 0.0D) {
            if (doesPortalExist) {
                this.destinationCoordinateCache.put(chunkId, new PortalPosition(location, this.world.getGameTime()));
            }

            double tpX = location.getX() + 0.5D;
            double tpY = location.getY() + 0.5D;
            double tpZ = location.getZ() + 0.5D;
            Direction direction = null;

            if (this.world.getBlockState(location.west()).getBlock() == AtumBlocks.PORTAL) {
                direction = Direction.NORTH;
            }

            if (this.world.getBlockState(location.east()).getBlock() == AtumBlocks.PORTAL) {
                direction = Direction.SOUTH;
            }

            if (this.world.getBlockState(location.north()).getBlock() == AtumBlocks.PORTAL) {
                direction = Direction.EAST;
            }

            if (this.world.getBlockState(location.south()).getBlock() == AtumBlocks.PORTAL) {
                direction = Direction.WEST;
            }

            final Direction Direction1 = Direction.byHorizontalIndex(MathHelper.floor(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3);

            if (direction != null) {
                Direction Direction2 = direction.rotateYCCW();
                final BlockPos blockpos2 = location.offset(direction);
                boolean flag2 = this.isInsideBlock(blockpos2);
                boolean flag3 = this.isInsideBlock(blockpos2.offset(Direction2));

                if (flag3 && flag2) {
                    location = location.offset(Direction2);
                    direction = direction.getOpposite();
                    Direction2 = Direction2.getOpposite();
                    final BlockPos blockpos3 = location.offset(direction);
                    flag2 = this.isInsideBlock(blockpos3);
                    flag3 = this.isInsideBlock(blockpos3.offset(Direction2));
                }

                float f6 = 0.5F;
                float f1 = 0.5F;

                if (!flag3 && flag2) {
                    f6 = 1.0F;
                } else if (flag3 && !flag2) {
                    f6 = 0.0F;
                } else if (flag3) {
                    f1 = 0.0F;
                }

                tpX = location.getX() + 0.5D;
                tpY = location.getY() + 0.5D;
                tpZ = location.getZ() + 0.5D;
                tpX += Direction2.getXOffset() * f6 + direction.getXOffset() * f1;
                tpZ += Direction2.getYOffset() * f6 + direction.getYOffset() * f1;
                float f2 = 0.0F;
                float f3 = 0.0F;
                float f4 = 0.0F;
                float f5 = 0.0F;

                if (direction == Direction1) {
                    f2 = 1.0F;
                    f3 = 1.0F;
                } else if (direction == Direction1.getOpposite()) {
                    f2 = -1.0F;
                    f3 = -1.0F;
                } else if (direction == Direction1.rotateY()) {
                    f4 = 1.0F;
                    f5 = -1.0F;
                } else {
                    f4 = -1.0F;
                    f5 = 1.0F;
                }

                final double d2 = entity.motionX;
                final double d3 = entity.motionZ;
                entity.motionX = d2 * f2 + d3 * f5;
                entity.motionZ = d2 * f4 + d3 * f3;
                entity.rotationYaw = rotationYaw - Direction1.getHorizontalIndex() * 90 + direction.getHorizontalIndex() * 90;
            } else {
                entity.motionX = entity.motionY = entity.motionZ = 0.0D;
            }
            entity.setLocationAndAngles(tpX, tpY, tpZ, entity.rotationYaw, entity.rotationPitch);
            return true;
        } else {
            return false;
        }
    }

    private boolean isInsideBlock(BlockPos position) {
        return !this.world.isAirBlock(position) || !this.world.isAirBlock(position.up());
    }

    @Override
    public boolean makePortal(Entity entity) {
        return createPortal(this.world, new BlockPos(MathHelper.floor(entity.posX), MathHelper.floor(entity.posY), MathHelper.floor(entity.posZ)), entity);
    }

    public static boolean createPortal(World world, BlockPos pos, @Nullable Entity entity) {
        BlockState portalState = AtumBlocks.PORTAL.getDefaultState();
        BlockState sandState;

        while (pos.getY() > 1 && world.isAirBlock(pos)) {
            pos = pos.down();
        }

        while (!world.isAirBlock(pos.up()) && (world.getBlockState(pos).getBlock() != AtumBlocks.SAND || world.getBlockState(pos).getBlock() != Blocks.GRASS)) {
            pos = pos.up();
        }

        if (entity != null && entity.dimension == 0) {
            sandState = Blocks.SANDSTONE.getDefaultState();
        } else {
            sandState = LimestoneBrickBlock.getBrick(LimestoneBrickBlock.BrickType.LARGE).getDefaultState();
        }


        //Bottom layers
        for (BlockPos.Mutable basePos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-2, 0, -2), pos.add(2, 1, 2))) {
            world.setBlockState(basePos, sandState, 2);
        }

        //Pillars
        for (int y = 2; y < 4; y++) {
            world.setBlockState(pos.add(-2, y, -2), sandState, 2);
            world.setBlockState(pos.add(2, y, -2), sandState, 2);
            world.setBlockState(pos.add(-2, y, 2), sandState, 2);
            world.setBlockState(pos.add(2, y, 2), sandState, 2);
        }

        //Portal blocks
        for (BlockPos.Mutable portalPos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-1, 1, -1), pos.add(1, 1, 1))) {
            world.setBlockState(portalPos, portalState, 2);
        }

        //Set air above portal blocks
        for (BlockPos.Mutable airPos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-2, 2, -1), pos.add(2, 3, 1))) {
            world.setBlockState(airPos, Blocks.AIR.getDefaultState(), 2);
        }
        return true;
    }
}*/
