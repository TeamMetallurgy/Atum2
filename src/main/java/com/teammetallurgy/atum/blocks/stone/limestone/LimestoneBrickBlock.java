package com.teammetallurgy.atum.blocks.stone.limestone;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.blocks.machines.BlockKiln;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.Property;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class LimestoneBrickBlock extends Block implements IRenderMapper {
    public static final PropertyBool UNBREAKABLE = PropertyBool.create("unbreakable");
    private static final Map<BrickType, BlockAtumDoor> DOORS = Maps.newEnumMap(BrickType.class);

    public LimestoneBrickBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(1.5F, 8.0F));
        this.setDefaultState(this.stateContainer.getBaseState().with(UNBREAKABLE, false));
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        super.onBlockPlacedBy(world, pos, state, placer, stack);

        if (state.getBlock() == LimestoneBrickBlock.getBrick(BrickType.SMALL)) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos checkPos = pos.add(dx, dy, dz);
                        BlockState kilnState = world.getBlockState(checkPos);
                        if (kilnState.getBlock() == AtumBlocks.KILN) {
                            BlockKiln kiln = (BlockKiln) kilnState.getBlock();
                            kiln.tryMakeMultiblock(world, checkPos, kilnState);
                        }
                    }
                }
            }
        }
    }

    @Override
    public float getBlockHardness(BlockState state, World world, BlockPos pos) {
        return state.get(UNBREAKABLE) ? -1.0F : super.getBlockHardness(state, world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
        return world.getBlockState(pos).getValue(UNBREAKABLE) ? 6000000.0F : super.getExplosionResistance(world, pos, exploder, explosion);
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(UNBREAKABLE, meta > 0);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(UNBREAKABLE) ? 1 : 0;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UNBREAKABLE);
    }

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{UNBREAKABLE};
    }

    public enum BrickType implements IStringSerializable {
        SMALL("small"),
        LARGE("large"),
        CRACKED("cracked_brick"),
        CHISELED("chiseled"),
        CARVED("carved");

        private final String name;

        BrickType(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

        @Override
        @Nonnull
        public String getName() {
            return this.name;
        }
    }
}