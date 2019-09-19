package com.teammetallurgy.atum.blocks.beacon.tileentity;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.beacon.BlockRadiantBeacon;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class TileEntityRadiantBeacon extends BeaconTileEntity {
    private final List<BeamSegment> beamSegments = Lists.newArrayList();

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public void updateBeacon() {
        if (this.world != null) {
            this.updateSegmentColors();
        }
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        return false;
    }

    private void updateSegmentColors() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        this.beamSegments.clear();
        this.isComplete = true;
        float[] defaultColor = this.world.getBlockState(this.getPos()).getValue(BlockRadiantBeacon.COLOR).getColorComponentValues();
        TileEntityBeacon.BeamSegment beamSegment = new TileEntityBeacon.BeamSegment(defaultColor);
        this.beamSegments.add(beamSegment);
        boolean flag = true;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for (int height = y + 1; height < 256; ++height) {
            BlockState state = this.world.getBlockState(pos.setPos(x, height, z));
            float[] color;

            if (state.getBlock() == Blocks.STAINED_GLASS) {
                color = state.getValue(BlockStainedGlass.COLOR).getColorComponentValues();
            } else {
                if (state.getBlock() != Blocks.STAINED_GLASS_PANE || !(state.getBlock() instanceof BlockAir)) {
                    if (state.getLightOpacity(world, pos) >= 15) {
                        this.isComplete = false;
                        this.beamSegments.clear();
                        break;
                    }
                    float[] customColor = state.getBlock().getBeaconColorMultiplier(state, this.world, pos, this.getPos());
                    if (customColor != null) {
                        color = customColor;
                    } else {
                        beamSegment.incrementHeight();
                        continue;
                    }
                } else {
                    color = state.getValue(BlockStainedGlassPane.COLOR).getColorComponentValues();
                }
            }

            if (!flag) {
                color = new float[]{(beamSegment.getColors()[0] + color[0]) / 2.0F, (beamSegment.getColors()[1] + color[1]) / 2.0F, (beamSegment.getColors()[2] + color[2]) / 2.0F};
            }

            if (Arrays.equals(color, beamSegment.getColors())) {
                beamSegment.incrementHeight();
            } else {
                beamSegment = new TileEntityBeacon.BeamSegment(color);
                this.beamSegments.add(beamSegment);
            }
            flag = false;
        }

        if (!this.world.isRemote) {
            for (ServerPlayerEntity ServerPlayerEntity : this.world.getEntitiesWithinAABB(ServerPlayerEntity.class, (new AxisAlignedBB((double) x, (double) y, (double) z, (double) x, (double) (y - 4), (double) z)).grow(10.0D, 5.0D, 10.0D))) {
                CriteriaTriggers.CONSTRUCT_BEACON.trigger(ServerPlayerEntity, this);
            }
        }
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public List<TileEntityBeacon.BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }
}