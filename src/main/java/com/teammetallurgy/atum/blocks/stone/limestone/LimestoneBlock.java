package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import javax.annotation.Nonnull;

public class LimestoneBlock extends Block {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("contains_scarab");

    public LimestoneBlock() {
        super(Block.Properties.of(Material.STONE, MapColor.SAND).strength(1.8F, 6.0F).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_SCARAB, false));
    }

    @Override
    public void spawnAfterBreak(@Nonnull BlockState state, ServerLevel level, @Nonnull BlockPos pos, @Nonnull ItemStack stack, boolean b) {
        if (!level.isClientSide && level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && state.getValue(HAS_SCARAB) && level.random.nextDouble() <= 0.90D) {
            ScarabEntity scarab = AtumEntities.SCARAB.get().create(level);
            scarab.moveTo((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            level.addFreshEntity(scarab);
            scarab.spawnAnim();
        }
        super.spawnAfterBreak(state, level, pos, stack, b);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(HAS_SCARAB);
    }
}