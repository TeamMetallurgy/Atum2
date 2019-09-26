package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockAtumOres extends OreBlock {

    public BlockAtumOres(Block.Properties properties) {
        super(properties);
    }

    @Override
    protected int func_220281_a(@Nonnull Random rand) {
        if (this == AtumBlocks.COAL_ORE) {
            return MathHelper.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.RELIC_ORE) {
            return MathHelper.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.BONE_ORE) {
            return MathHelper.nextInt(rand, 0, 2);
        } else if (this == AtumBlocks.DIAMOND_ORE) {
            return MathHelper.nextInt(rand, 3, 7);
        } else if (this == AtumBlocks.EMERALD_ORE) {
            return MathHelper.nextInt(rand, 3, 7);
        } else if (this == AtumBlocks.LAPIS_ORE) {
            return MathHelper.nextInt(rand, 2, 5);
        } else {
            return 0;
        }
    }
}