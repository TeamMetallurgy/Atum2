package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumPoiTypes;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.neoforged.neoforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class TeleporterAtum implements ITeleporter {
    public static final TeleporterAtum INSTANCE = new TeleporterAtum();

    @Override
    @Nonnull
    public Entity placeEntity(@Nonnull Entity entity, @Nonnull ServerLevel currentWorld, @Nonnull ServerLevel destWorld, float yaw, @Nonnull Function<Boolean, Entity> repositionEntity) {
        if (placeInPortal(destWorld, entity, yaw)) { //Generate portal
            return repositionEntity.apply(true);
        } else { //Don't generate portal
            return repositionEntity.apply(false);
        }
    }

    public boolean placeInPortal(ServerLevel level, Entity entity, float yaw) {
        return this.getPortalInfo(entity, level, null) != null;
    }

    @Override
    @Nullable
    public PortalInfo getPortalInfo(Entity entity, @Nonnull ServerLevel destWorld, @Nonnull Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        Optional<BlockUtil.FoundRectangle> result = teleporterResult(destWorld, entity.blockPosition());
        if (result.isPresent()) {
            BlockPos startPos = result.get().minCorner;
            return new PortalInfo(new Vec3(startPos.getX(), startPos.getY() + 5, startPos.getZ()), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot()); //+5 workaround for spawning in portal
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
            if (optional1.isEmpty()) {
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
            return poiType.is(AtumPoiTypes.PORTAL.getKey());
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

    public Optional<BlockUtil.FoundRectangle> makePortal(ServerLevel level, @Nonnull Entity entity) {
        return createPortal(level, new BlockPos(Mth.floor(entity.getX()), Mth.floor(entity.getY()), Mth.floor(entity.getZ())));
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(Level level, BlockPos pos) {
        BlockState portalState = AtumBlocks.PORTAL.get().defaultBlockState();
        BlockState sandState;

        while (pos.getY() > 1 && level.isEmptyBlock(pos)) {
            pos = pos.below();
        }

        while (!level.isEmptyBlock(pos.above()) && (level.getBlockState(pos).getBlock() != AtumBlocks.STRANGE_SAND.get() || level.getBlockState(pos).getBlock() != Blocks.GRASS_BLOCK)) {
            pos = pos.above();
        }

        if (level.dimension() == Level.OVERWORLD) {
            sandState = Blocks.SANDSTONE.defaultBlockState();
        } else {
            sandState = AtumBlocks.LIMESTONE_BRICK_LARGE.get().defaultBlockState();
        }


        //Bottom layers
        for (BlockPos basePos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-2, 0, -2), pos.offset(2, 1, 2))) {
            level.setBlock(basePos, sandState, 2);
        }

        //Pillars
        for (int y = 2; y < 4; y++) {
            level.setBlock(pos.offset(-2, y, -2), sandState, 2);
            level.setBlock(pos.offset(2, y, -2), sandState, 2);
            level.setBlock(pos.offset(-2, y, 2), sandState, 2);
            level.setBlock(pos.offset(2, y, 2), sandState, 2);
        }

        //Portal blocks
        for (BlockPos portalPos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-1, 1, -1), pos.offset(1, 1, 1))) {
            level.setBlock(portalPos, portalState, 2);
        }

        //Set air above portal blocks
        for (BlockPos airPos : BlockPos.MutableBlockPos.betweenClosed(pos.offset(-2, 2, -1), pos.offset(2, 3, 1))) {
            level.setBlock(airPos, Blocks.AIR.defaultBlockState(), 2);
        }
        return Optional.of(new BlockUtil.FoundRectangle(pos.immutable(), 3, 3));
    }
}