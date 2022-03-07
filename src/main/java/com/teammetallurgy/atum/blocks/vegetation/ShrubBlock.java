package com.teammetallurgy.atum.blocks.vegetation;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DeadBushBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ShrubBlock extends DeadBushBlock {

    public ShrubBlock() {
        super(Block.Properties.of(Material.REPLACEABLE_PLANT, MaterialColor.WOOD).noCollission().strength(0.0F).sound(SoundType.GRASS));
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return state.is(BlockTags.SAND);
    }

    @Override
    @Nonnull
    public List<ItemStack> onSheared(@Nullable Player player, @Nonnull ItemStack stack, Level world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }
}