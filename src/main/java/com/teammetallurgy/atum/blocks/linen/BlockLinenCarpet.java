package com.teammetallurgy.atum.blocks.linen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockLinenCarpet extends BlockLinen {
    private static final Map<EnumDyeColor, Block> LINEN_CARPET = Maps.newEnumMap(EnumDyeColor.class);
    private static final AxisAlignedBB CARPET_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    private BlockLinenCarpet() {
        super(Material.CARPET);
        this.setHardness(0.1F);
    }

    public static void registerLinenCarpets() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockLinenCarpet linenCarpet = new BlockLinenCarpet();
            LINEN_CARPET.put(color, linenCarpet);
            AtumRegistry.registerBlock(linenCarpet, "linen_carpet_" + color.getName());
        }
    }

    public static Block getLinenBlock(EnumDyeColor color) {
        return LINEN_CARPET.get(color);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return CARPET_AABB;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World world, @Nonnull BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.checkForDrop(worldIn, pos, state);
    }

    private boolean checkForDrop(World world, BlockPos pos, BlockState state) {
        if (!this.canBlockStay(world, pos)) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
            return false;
        } else {
            return true;
        }
    }

    private boolean canBlockStay(World world, BlockPos pos) {
        return !world.isAirBlock(pos.down());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldSideBeRendered(BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, Direction facing) {
        if (facing == Direction.UP) {
            return true;
        } else {
            return world.getBlockState(pos.offset(facing)).getBlock() == this || super.shouldSideBeRendered(state, world, pos, facing);
        }
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, BlockState state, BlockPos pos, Direction face) {
        return face == Direction.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    public String getColorString() {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return this.getRegistryName().getPath().replace("linen_carpet_", "");
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "carpet");
    }
}