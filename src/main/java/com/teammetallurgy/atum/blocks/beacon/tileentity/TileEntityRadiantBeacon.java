package com.teammetallurgy.atum.blocks.beacon.tileentity;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.beacon.BlockRadiantBeacon;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class TileEntityRadiantBeacon extends TileEntityBeacon {
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
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
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
            IBlockState state = this.world.getBlockState(pos.setPos(x, height, z));
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
            for (EntityPlayerMP entityplayermp : this.world.getEntitiesWithinAABB(EntityPlayerMP.class, (new AxisAlignedBB((double) x, (double) y, (double) z, (double) x, (double) (y - 4), (double) z)).grow(10.0D, 5.0D, 10.0D))) {
                CriteriaTriggers.CONSTRUCT_BEACON.trigger(entityplayermp, this);
            }
        }
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public List<TileEntityBeacon.BeamSegment> getBeamSegments() {
        return this.beamSegments;
    }
}