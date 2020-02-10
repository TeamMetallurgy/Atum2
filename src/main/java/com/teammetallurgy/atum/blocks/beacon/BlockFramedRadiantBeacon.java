package com.teammetallurgy.atum.blocks.beacon;

import net.minecraft.item.DyeColor;

public class BlockFramedRadiantBeacon extends BlockRadiantBeacon {

    public BlockFramedRadiantBeacon() {
        this.setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
    }
}