package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class DeadwoodLogBlock extends RotatedPillarBlock {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("has_scarab");

    public DeadwoodLogBlock() {
        super(AbstractBlock.Properties.create(Material.WOOD, (state) -> state.get(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.OBSIDIAN : MaterialColor.BROWN).hardnessAndResistance(1.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.stateContainer.getBaseState().with(AXIS, Direction.Axis.Y).with(HAS_SCARAB, false));
    }

    @Override
    public void spawnAdditionalDrops(@Nonnull BlockState state, @Nonnull ServerWorld world, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        super.spawnAdditionalDrops(state, world, pos, stack);
        if (!world.isRemote && world.getDifficulty() != Difficulty.PEACEFUL && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            if (state.get(HAS_SCARAB) && RANDOM.nextDouble() <= 0.40D) {
                ScarabEntity scarab = new ScarabEntity(AtumEntities.SCARAB, world);
                scarab.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addEntity(scarab);
                scarab.spawnExplosionParticle();
            }
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(AXIS, HAS_SCARAB);
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack stack, ToolType toolType) {
        return toolType == ToolType.AXE ? AtumBlocks.STRIPPED_DEADWOOD_LOG.getDefaultState() : super.getToolModifiedState(state, world, pos, player, stack, toolType);
    }
}