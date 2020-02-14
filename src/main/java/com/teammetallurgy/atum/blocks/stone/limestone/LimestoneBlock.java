package com.teammetallurgy.atum.blocks.stone.limestone;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LimestoneBlock extends Block {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("contains_scarab");

    public LimestoneBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.8F, 6.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(HAS_SCARAB, false));
    }

    @Override
    public void spawnAdditionalDrops(BlockState state, World world, BlockPos pos, @Nonnull ItemStack stack) {
        if (!world.isRemote && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && state.get(HAS_SCARAB) && RANDOM.nextDouble() <= 0.90D) {
            ScarabEntity scarab = AtumEntities.SCARAB.create(world);
            scarab.setLocationAndAngles((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(scarab);
            scarab.spawnExplosionParticle();
        }
        super.spawnAdditionalDrops(state, world, pos, stack);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(HAS_SCARAB);
    }

    /*@Override
    public Property[] getNonRenderingProperties() { //TODO
        return new Property[]{HAS_SCARAB};
    }*/
}