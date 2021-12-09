package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.blocks.GodforgedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;

public class NebuHammerItem extends SimpleItem {

    @Override
    public boolean mineBlock(@Nonnull ItemStack stack, @Nonnull Level level, BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity livingEntity) {
        return state.getBlock() instanceof GodforgedBlock && super.mineBlock(stack, level, state, pos, livingEntity);
    }

    @Override
    public float getDestroySpeed(@Nonnull ItemStack stack, @Nonnull BlockState state) {
        return state.getBlock() instanceof GodforgedBlock ? 50.0F : super.getDestroySpeed(stack, state);
    }
}