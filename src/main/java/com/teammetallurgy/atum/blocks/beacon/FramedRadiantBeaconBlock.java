package com.teammetallurgy.atum.blocks.beacon;

import net.minecraft.item.DyeColor;

public class FramedRadiantBeaconBlock extends RadiantBeaconBlock {

    public FramedRadiantBeaconBlock() {
        this.setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
    }
}