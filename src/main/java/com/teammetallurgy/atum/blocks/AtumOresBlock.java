package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.util.Mth;

import javax.annotation.Nonnull;
import java.util.Random;

public class AtumOresBlock extends OreBlock {

    public AtumOresBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    protected int xpOnDrop(@Nonnull Random rand) {
        if (this == AtumBlocks.COAL_ORE) {
            return Mth.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.RELIC_ORE) {
            return Mth.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.BONE_ORE) {
            return Mth.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.DIAMOND_ORE) {
            return Mth.nextInt(rand, 3, 7);
        } else if (this == AtumBlocks.EMERALD_ORE) {
            return Mth.nextInt(rand, 3, 7);
        } else if (this == AtumBlocks.LAPIS_ORE) {
            return Mth.nextInt(rand, 2, 5);
        } else {
            return 0;
        }
    }
}