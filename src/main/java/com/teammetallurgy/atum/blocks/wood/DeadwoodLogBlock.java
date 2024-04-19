package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;

import javax.annotation.Nonnull;

public class DeadwoodLogBlock extends RotatedPillarBlock {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("has_scarab");
    private boolean canBeStripped;

    public DeadwoodLogBlock() {
        super(BlockBehaviour.Properties.of().mapColor((state) -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.PODZOL : MapColor.COLOR_BROWN).ignitedByLava().instrument(NoteBlockInstrument.BASS).strength(1.0F).sound(SoundType.WOOD));
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.Y).setValue(HAS_SCARAB, false));
    }

    public DeadwoodLogBlock setCanBeStripped() {
        this.canBeStripped = true;
        return this;
    }

    @Override
    public void spawnAfterBreak(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos, @Nonnull ItemStack stack, boolean b) {
        super.spawnAfterBreak(state, level, pos, stack, b);
        if (!level.isClientSide && level.getDifficulty() != Difficulty.PEACEFUL && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            if (state.getValue(HAS_SCARAB) && level.random.nextDouble() <= 0.40D) {
                ScarabEntity scarab = new ScarabEntity(AtumEntities.SCARAB.get(), level);
                scarab.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
                level.addFreshEntity(scarab);
                scarab.spawnAnim();
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(AXIS, HAS_SCARAB);
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        if (this.canBeStripped) {
            return toolAction == ToolActions.AXE_STRIP ? AtumBlocks.STRIPPED_DEADWOOD_LOG.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : super.getToolModifiedState(state, context, toolAction, simulate);
        } else {
            return null;
        }
    }
}