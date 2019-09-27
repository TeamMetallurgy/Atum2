package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.state.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class LeavesAtumBlock extends LeavesBlock implements IRenderMapper {

    public LeavesAtumBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, @Nonnull World world, @Nonnull BlockPos pos, Random random) { //TODO Check if leaves stays when they should
        if (!state.get(PERSISTENT) && state.get(DISTANCE) >= 3) {
            spawnDrops(state, world, pos);
            world.removeBlock(pos, false);
        }
    }

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{DISTANCE, PERSISTENT};
    }
}