package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.blocks.GodforgedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class NebuHammerItem extends SimpleItem {

    @Override
    public boolean canHarvestBlock(@Nonnull ItemStack stack, BlockState state) {
        return state.getBlock() instanceof GodforgedBlock;
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return state.getBlock() instanceof GodforgedBlock ? 50.0F : super.getDestroySpeed(stack, state);
    }
}