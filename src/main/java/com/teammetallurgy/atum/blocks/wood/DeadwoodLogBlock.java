package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;

public class DeadwoodLogBlock extends RotatedPillarBlock {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("has_scarab");
    private boolean canBeStripped;

    public DeadwoodLogBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD, (state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MaterialColor.PODZOL : MaterialColor.COLOR_BROWN).strength(1.0F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(HAS_SCARAB, false));
    }

    public DeadwoodLogBlock setCanBeStripped() {
        this.canBeStripped = true;
        return this;
    }

    @Override
    public void spawnAfterBreak(@Nonnull BlockState state, @Nonnull ServerLevel world, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        super.spawnAfterBreak(state, world, pos, stack);
        if (!world.isClientSide && world.getDifficulty() != Difficulty.PEACEFUL && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (state.getValue(HAS_SCARAB) && RANDOM.nextDouble() <= 0.40D) {
                ScarabEntity scarab = new ScarabEntity(AtumEntities.SCARAB, world);
                scarab.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
                world.addFreshEntity(scarab);
                scarab.spawnAnim();
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AXIS, HAS_SCARAB);
    }
    @Override
    public BlockState getToolModifiedState(BlockState state, Level world, BlockPos pos, Player player, ItemStack stack, ToolAction toolAction) {
        if (this.canBeStripped) {
            return toolAction == ToolActions.AXE_STRIP ? AtumBlocks.STRIPPED_DEADWOOD_LOG.defaultBlockState() : super.getToolModifiedState(state, world, pos, player, stack, toolAction);
        } else {
            return null;
        }
    }
}