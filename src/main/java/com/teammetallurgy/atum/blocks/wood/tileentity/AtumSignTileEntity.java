package com.teammetallurgy.atum.blocks.wood.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.tileentity.SignTileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

public class AtumSignTileEntity extends SignTileEntity {

    @Override
    @Nonnull
    public TileEntityType<?> getType() {
        return AtumTileEntities.SIGN;
    }
}