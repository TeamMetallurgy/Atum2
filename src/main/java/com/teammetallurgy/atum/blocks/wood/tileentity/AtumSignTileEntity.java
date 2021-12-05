package com.teammetallurgy.atum.blocks.wood.tileentity;

import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nonnull;

public class AtumSignTileEntity extends SignBlockEntity {

    @Override
    @Nonnull
    public BlockEntityType<?> getType() {
        return AtumTileEntities.SIGN;
    }
}