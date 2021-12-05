package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class LimestoneBlock extends Block {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("contains_scarab");

    public LimestoneBlock() {
        super(Block.Properties.of(Material.STONE, MaterialColor.SAND).strength(1.8F, 6.0F).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0));
        this.registerDefaultState(this.stateDefinition.any().setValue(HAS_SCARAB, false));
    }

    @Override
    public void spawnAfterBreak(@Nonnull BlockState state, ServerLevel world, @Nonnull BlockPos pos, @Nonnull ItemStack stack) {
        if (!world.isClientSide && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && state.getValue(HAS_SCARAB) && RANDOM.nextDouble() <= 0.90D) {
            ScarabEntity scarab = AtumEntities.SCARAB.create(world);
            scarab.moveTo((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addFreshEntity(scarab);
            scarab.spawnAnim();
        }
        super.spawnAfterBreak(state, world, pos, stack);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> container) {
        container.add(HAS_SCARAB);
    }
}