package com.teammetallurgy.atum.world.teleporter;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

public class AtumStartTeleporter implements ITeleporter {
    private final BlockPos targetPos;

    public AtumStartTeleporter(BlockPos targetPos) {
        this.targetPos = targetPos;
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw) {
        BlockPos pos = this.targetPos;
        while (pos.getY() > 1 && world.isAirBlock(pos.down())) {
            pos = pos.down();
        }
        while (!world.isAirBlock(pos.up()) && (world.getBlockState(pos.down()).getBlock() != AtumBlocks.SAND || world.getBlockState(pos.down()).getBlock() != AtumBlocks.SAND_LAYERED)) {
            pos = pos.up();
        }

        System.out.println("Pos: " + pos + " dimension: " + entity.dimension);
        entity.moveToBlockPosAndAngles(pos, yaw, entity.rotationPitch);
    }
}