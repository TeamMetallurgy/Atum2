package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumPoiTypes;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class TeleporterAtum implements ITeleporter {
    public static final TeleporterAtum INSTANCE = new TeleporterAtum();

    @Override
    public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        Entity movedEntity = repositionEntity.apply(false);
        if (!placeInPortal(destWorld, entity, yaw)) {
            makePortal(destWorld, movedEntity);
            placeInPortal(destWorld, movedEntity, yaw);
            return movedEntity;
        } else {
            placeInPortal(destWorld, movedEntity, yaw);
            return movedEntity;
        }
    }

    public boolean placeInPortal(ServerLevel world, Entity entity, float yaw) {
        PortalInfo portalInfo = this.getPortalInfo(entity, world, null);
        if (portalInfo == null) {
            return false;
        } else {
            Vec3 vec3d1 = portalInfo.pos;
            Vec3 vec3d2 = portalInfo.speed;
            entity.setDeltaMovement(vec3d2);
            entity.setYRot(yaw + portalInfo.yRot);
            entity.moveTo(vec3d1.x, vec3d1.y + 1, vec3d1.z);
            return true;
        }
    }

    @Override
    @Nullable
    public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        Optional<BlockUtil.FoundRectangle> result = teleporterResult(destWorld, entity.blockPosition());
        if (result.isPresent()) {
            BlockPos startPos = result.get().minCorner;
            return new PortalInfo(new Vec3(startPos.getX(), startPos.getY(), startPos.getZ()), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot());
        } else {
            return new PortalInfo(entity.position(), Vec3.ZERO, entity.getYRot(), entity.getXRot());
        }
    }

    protected Optional<BlockUtil.FoundRectangle> teleporterResult(ServerLevel serverLevel, BlockPos pos) {
        Optional<BlockUtil.FoundRectangle> optional = getExistingPortal(serverLevel, pos);
        if (optional.isPresent()) {
            return optional;
        } else {
            Optional<BlockUtil.FoundRectangle> optional1 = createPortal(serverLevel, pos);
            if (!optional1.isPresent()) {
                Atum.LOG.error("Unable to create a portal, likely target out of worldborder");
            }
            return optional1;
        }
    }

    public Optional<BlockUtil.FoundRectangle> getExistingPortal(ServerLevel serverLevel, BlockPos pos) {
        PoiManager posManager = serverLevel.getPoiManager();
        int i = 128;
        posManager.ensureLoadedAndValid(serverLevel, pos, i);
        Optional<PoiRecord> optional = posManager.getInSquare((poiType) -> {
            return poiType.is(AtumPoiTypes.PORTAL);
        }, pos, i, PoiManager.Occupancy.ANY).sorted(Comparator.<PoiRecord>comparingDouble((poi) -> {
            return poi.getPos().distSqr(pos);
        }).thenComparingInt((poi) -> {
            return poi.getPos().getY();
        })).findFirst();
        return optional.map((poi) -> {
            BlockPos posPos = poi.getPos();
            serverLevel.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(posPos), 3, posPos);
            BlockState blockstate = serverLevel.getBlockState(posPos);
            return BlockUtil.getLargestRectangleAround(posPos, Direction.Axis.X, 9, Direction.Axis.Z, 9, (posIn) -> serverLevel.getBlockState(posIn) == blockstate);
        });
    }

    public Optional<BlockUtil.FoundRectangle> makePortal(ServerLevel world, @Nonnull Entity entity) {
        return createPortal(world, new BlockPos(Mth.floor(entity.getX()), Mth.floor(entity.getY()), Mth.floor(entity.getZ())));
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(Level world, BlockPos pos) {
        BlockState portalState = AtumBlocks.PORTAL.get().defaultBlockState();
        BlockState sandState;

        while (pos.getY() > 1 && world.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        while (!world.isEmptyBlock(pos.above()) && (world.getBlockState(pos).getBlock() != AtumBlocks.SAND.get() || world.getBlockState(pos).getBlock() != Blocks.GRASS)) {
            pos = pos.above();
        }

        if (world.dimension() == Level.OVERWORLD) {
            sandState = Blocks.SANDSTONE.defaultBlockState();
        } else {
            sandState = AtumBlocks.LIMESTONE_BRICK_LARGE.get().defaultBlockState();
        }


        //Bottom layers
        for (BlockPos basePos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 1, 2))) {
            world.setBlock(basePos, sandState, 2);
        }

        //Pillars
        for (int y = 2; y < 4; y++) {
            world.setBlock(pos.offset(-2, y, -2), sandState, 2);
            world.setBlock(pos.offset(2, y, -2), sandState, 2);
            world.setBlock(pos.offset(-2, y, 2), sandState, 2);
            world.setBlock(pos.offset(2, y, 2), sandState, 2);
        }

        //Portal blocks
        for (BlockPos portalPos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-1, 1, -1), pos.offset(1, 1, 1))) {
            world.setBlock(portalPos, portalState, 2);
        }

        //Set air above portal blocks
        for (BlockPos airPos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-2, 2, -1), pos.offset(2, 3, 1))) {
            world.setBlock(airPos, Blocks.AIR.defaultBlockState(), 2);
        }
        return Optional.of(new BlockUtil.FoundRectangle(pos.immutable(), 3, 3));
    }
}