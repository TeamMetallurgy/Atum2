package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.entity.animal.ScarabEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LogBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class DeadwoodBlock extends LogBlock {
    public static final BooleanProperty HAS_SCARAB = BooleanProperty.create("has_scarab");

    public DeadwoodBlock() {
        super(MaterialColor.OBSIDIAN, Block.Properties.create(Material.WOOD, MaterialColor.OBSIDIAN).hardnessAndResistance(1.0F).sound(SoundType.WOOD));
        this.setDefaultState(this.stateContainer.getBaseState().with(HAS_SCARAB, false));
    }

    /*@Override
    public boolean canSustainLeaves(BlockState state, IBlockReader world, BlockPos pos) { //TODO
        return false;
    }*/

    @Override
    public void spawnAdditionalDrops(BlockState state, World world, BlockPos pos, @Nonnull ItemStack stack) {
        super.spawnAdditionalDrops(state, world, pos, stack);
        if (!world.isRemote && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS) && state.get(HAS_SCARAB) && RANDOM.nextDouble() <= 0.40D) {
            ScarabEntity scarab = new ScarabEntity(AtumEntities.SCARAB, world);
            scarab.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.addEntity(scarab);
            scarab.spawnExplosionParticle();
        }
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(AXIS, HAS_SCARAB);
    }

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{HAS_SCARAB}; //TODO
    }
}