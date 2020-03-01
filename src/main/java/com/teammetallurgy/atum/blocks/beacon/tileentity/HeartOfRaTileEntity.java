package com.teammetallurgy.atum.blocks.beacon.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class HeartOfRaTileEntity extends TileEntity {

    public HeartOfRaTileEntity() {
        super(AtumTileEntities.HEART_OF_RA);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public double getMaxRenderDistanceSquared() {
        return 65536.0D;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }
}