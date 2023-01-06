package com.teammetallurgy.atum.blocks.base;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.DoorBlock;

public class DoorAtumBlock extends DoorBlock { //Needed for easier TheOneProbe support

    public DoorAtumBlock(Properties properties, SoundEvent openSound, SoundEvent closeSound) {
        super(properties, openSound, closeSound);
    }
}