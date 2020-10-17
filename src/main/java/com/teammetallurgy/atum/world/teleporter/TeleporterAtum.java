package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumPointsOfInterest;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.TeleportationRepositioner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.village.PointOfInterest;
import net.minecraft.village.PointOfInterestManager;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

public class TeleporterAtum implements ITeleporter { //TODO
    public static final TeleporterAtum INSTANCE = new TeleporterAtum();

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        if (!placeInPortal(destWorld, entity, yaw)) {
            makePortal(destWorld, entity);
            placeInPortal(destWorld, entity, yaw);
            return repositionEntity.apply(false);
        } else {
            placeInPortal(destWorld, entity, yaw);
            return repositionEntity.apply(false);
        }
    }

    public boolean placeInPortal(ServerWorld world, Entity entity, float yaw) {
        PortalInfo portalInfo = getPortalInfo(entity, world, null);
        if (portalInfo == null) {
            return false;
        } else {
            Vector3d vec3d1 = portalInfo.pos;
            Vector3d vec3d2 = portalInfo.motion;
            entity.setMotion(vec3d2);
            entity.rotationYaw = yaw + portalInfo.rotationYaw;
            entity.moveForced(vec3d1.x, vec3d1.y, vec3d1.z);
            return true;
        }
    }

    protected Optional<TeleportationRepositioner.Result> func_241830_a(ServerWorld serverWorld, BlockPos pos) {
        Optional<TeleportationRepositioner.Result> optional = getExistingPortal(serverWorld, pos);
        if (optional.isPresent()) {
            return optional;
        } else {
            Optional<TeleportationRepositioner.Result> optional1 = createPortal(serverWorld, pos);
            if (!optional1.isPresent()) {
                Atum.LOG.error("Unable to create a portal, likely target out of worldborder");
            }
            return optional1;
        }
    }

    public Optional<TeleportationRepositioner.Result> getExistingPortal(ServerWorld serverWorld, BlockPos pos) {
        PointOfInterestManager posManager = serverWorld.getPointOfInterestManager();
        int i = 128;
        posManager.ensureLoadedAndValid(serverWorld, pos, i);
        Optional<PointOfInterest> optional = posManager.getInSquare((poiType) -> {
            return poiType == AtumPointsOfInterest.PORTAL;
        }, pos, i, PointOfInterestManager.Status.ANY).sorted(Comparator.<PointOfInterest>comparingDouble((poi) -> {
            return poi.getPos().distanceSq(pos);
        }).thenComparingInt((poi) -> {
            return poi.getPos().getY();
        })).findFirst();
        return optional.map((poi) -> {
            BlockPos posPos = poi.getPos();
            serverWorld.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(posPos), 3, posPos);
            BlockState blockstate = serverWorld.getBlockState(posPos);
            return TeleportationRepositioner.findLargestRectangle(posPos, Direction.Axis.X, 9, Direction.Axis.Y, 9, (posIn) -> serverWorld.getBlockState(posIn) == blockstate);
        });
    }

    public Optional<TeleportationRepositioner.Result> makePortal(ServerWorld world, @Nonnull Entity entity) {
        return createPortal(world, new BlockPos(MathHelper.floor(entity.getPosX()), MathHelper.floor(entity.getPosY()), MathHelper.floor(entity.getPosZ())));
    }

    public Optional<TeleportationRepositioner.Result> createPortal(World world, BlockPos pos) {
        BlockState portalState = AtumBlocks.PORTAL.getDefaultState();
        BlockState sandState;

        while (pos.getY() > 1 && world.isAirBlock(pos)) {
            pos = pos.down();
        }

        while (!world.isAirBlock(pos.up()) && (world.getBlockState(pos).getBlock() != AtumBlocks.SAND || world.getBlockState(pos).getBlock() != Blocks.GRASS)) {
            pos = pos.up();
        }

        if (world.getDimensionKey() == World.OVERWORLD) {
            sandState = Blocks.SANDSTONE.getDefaultState();
        } else {
            sandState = AtumBlocks.LIMESTONE_BRICK_LARGE.getDefaultState();
        }


        //Bottom layers
        for (BlockPos basePos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-2, 0, -2), pos.add(2, 1, 2))) {
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
        for (BlockPos portalPos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-1, 1, -1), pos.add(1, 1, 1))) {
            world.setBlockState(portalPos, portalState, 2);
        }

        //Set air above portal blocks
        for (BlockPos airPos : BlockPos.Mutable.getAllInBoxMutable(pos.add(-2, 2, -1), pos.add(2, 3, 1))) {
            world.setBlockState(airPos, Blocks.AIR.getDefaultState(), 2);
        }
        return Optional.of(new TeleportationRepositioner.Result(pos.toImmutable(), 3, 3));
    }
}