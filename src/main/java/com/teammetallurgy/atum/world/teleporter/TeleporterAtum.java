package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class TeleporterAtum implements ITeleporter {

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

    public static boolean placeInPortal(ServerWorld world, Entity entity, float yaw) {
        return false;
        /*Vector3d lastPortalVec = entity.getLastPortalVec(); //TODO
        Direction direction = entity.getTeleportDirection();
        BlockPattern.PortalInfo portalInfo = placeInExistingPortal(world, new BlockPos(entity), entity.getMotion(), direction, lastPortalVec.x, lastPortalVec.y, entity instanceof PlayerEntity);
        if (portalInfo == null) {
            return false;
        } else {
            Vector3d vec3d1 = portalInfo.pos;
            Vector3d vec3d2 = portalInfo.motion;
            entity.setMotion(vec3d2);
            entity.rotationYaw = yaw + (float) portalInfo.rotation;
            entity.moveForced(vec3d1.x, vec3d1.y, vec3d1.z);
            return true;
        }*/
    }

    /*@Nullable
    public static BlockPattern.PortalInfo placeInExistingPortal(ServerWorld world, @Nonnull BlockPos pos, @Nonnull Vector3d portalPos, @Nonnull Direction direction, double d, double d1, boolean b) {
        PointOfInterestManager poiManager = world.getPointOfInterestManager();
        poiManager.ensureLoadedAndValid(world, pos, 128);
        List<PointOfInterest> list = poiManager.getInSquare((poi) -> poi == AtumPointsOfInterest.PORTAL, pos, 128, PointOfInterestManager.Status.ANY).collect(Collectors.toList());
        Optional<PointOfInterest> optional = list.stream().min(Comparator.<PointOfInterest>comparingDouble((poi) -> poi.getPos().distanceSq(pos)).thenComparingInt((poi) -> poi.getPos().getY()));
        return optional.map((poi) -> {
            BlockPos posPos = poi.getPos();
            world.getChunkProvider().registerTicket(TicketType.PORTAL, new ChunkPos(posPos), 3, posPos);
            BlockPattern.PatternHelper patternHelper = PortalBlock.createPatternHelper(world, posPos);
            return patternHelper.getPortalInfo(direction, posPos, d1, portalPos, d);
        }).orElse(null);
    }*/

    public static void makePortal(ServerWorld world, @Nonnull Entity entity) {
        createPortal(world, new BlockPos(MathHelper.floor(entity.getPosX()), MathHelper.floor(entity.getPosY()), MathHelper.floor(entity.getPosZ())), entity);
    }

    public static void createPortal(World world, BlockPos pos, @Nullable Entity entity) {
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
    }
}