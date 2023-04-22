package com.teammetallurgy.atum.blocks.base;

import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class DoorAtumBlock extends DoorBlock { //Needed for easier TheOneProbe support

    public DoorAtumBlock(Properties properties, BlockSetType blockSetType) {
        super(properties, blockSetType);
    }
}