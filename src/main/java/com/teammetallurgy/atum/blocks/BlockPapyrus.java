package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockReed;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPapyrus extends BlockReed implements IRenderMapper {
    public static final PropertyBool TOP = PropertyBool.create("top");

    public BlockPapyrus() {
        super();
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE, TOP);
    }

    @Override
    @Nonnull
    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess world, BlockPos pos) {
        BlockPos upperPos = pos.add(0, 1, 0);
        return state.withProperty(TOP, world.isAirBlock(upperPos));
    }

    @Override
    public boolean canSustainPlant(@Nonnull IBlockState state, @Nonnull IBlockAccess world, BlockPos pos, @Nonnull EnumFacing direction, IPlantable plantable) {
        if (plantable.getPlant(world, pos.up()) == this) {
            return true;
        }
        return super.canSustainPlant(state, world, pos, direction, plantable);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return AtumItems.PAPYRUS_PLANT;
    }

    @Override
    @Nonnull
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(AtumItems.PAPYRUS_PLANT);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{AGE};
    }
}