package com.teammetallurgy.atum.blocks.beacon.tileentity;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.beacon.RadiantBeaconBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BedrockBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class RadiantBeaconTileEntity extends BeaconTileEntity {
    private List<BeamSegment> beamSegments = Lists.newArrayList();

    @Override
    @Nonnull
    public TileEntityType<?> getType() {
        return AtumTileEntities.RADIANT_BEACON;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void tick() {
        World world = this.world;
        if (world != null) {
            this.updateSegmentColors(world);
        }
    }

    private void updateSegmentColors(World world) {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        this.beamSegments.clear();
        float[] defaultColor = world.getBlockState(this.getPos()).get(RadiantBeaconBlock.COLOR).getColorComponentValues();
        BeaconTileEntity.BeamSegment beamSegment = new BeaconTileEntity.BeamSegment(defaultColor);
        this.beamSegments.add(beamSegment);
        boolean flag = true;
        BlockPos.Mutable pos = new BlockPos.Mutable();

        for (int height = y + 1; height < 256; ++height) {
            BlockState state = world.getBlockState(pos.setPos(x, height, z));
            float[] color;

            if (state.getBlock() instanceof IBeaconBeamColorProvider) {
                color = ((IBeaconBeamColorProvider) state.getBlock()).getColor().getColorComponentValues();
            } else {
                if (state.getBlock() instanceof AirBlock) {
                    break;
                } else {
                    if (state.getOpacity(world, pos) >= 15 && !(state.getBlock() instanceof BedrockBlock)) {
                        this.beamSegments.clear();
                        break;
                    }
                    float[] customColor = state.getBlock().getBeaconColorMultiplier(state, world, pos, this.getPos());
                    if (customColor != null) {
                        color = customColor;
                    } else {
                        beamSegment.incrementHeight();
                        continue;
                    }
                }
            }

            if (!flag) {
                color = new float[]{(beamSegment.getColors()[0] + color[0]) / 2.0F, (beamSegment.getColors()[1] + color[1]) / 2.0F, (beamSegment.getColors()[2] + color[2]) / 2.0F};
            }

            if (Arrays.equals(color, beamSegment.getColors())) {
                beamSegment.incrementHeight();
            } else {
                beamSegment = new BeaconTileEntity.BeamSegment(color);
                this.beamSegments.add(beamSegment);
            }
            flag = false;
        }

        if (!world.isRemote) {
            for (ServerPlayerEntity ServerPlayerEntity : world.getEntitiesWithinAABB(ServerPlayerEntity.class, (new AxisAlignedBB((double) x, y, z, x, y - 4,
                    z)).grow(10.0D, 5.0D, 10.0D))) {
                CriteriaTriggers.CONSTRUCT_BEACON.trigger(ServerPlayerEntity, this);
            }
        }
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public List<BeaconTileEntity.BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }
}