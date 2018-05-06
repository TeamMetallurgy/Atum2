package com.teammetallurgy.atum.blocks;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class BlockAtumStainedGlass extends BlockBreakable {

    public BlockAtumStainedGlass() {
        super(Material.GLASS, false);
        this.setHardness(0.3F);
        this.setSoundType(SoundType.GLASS);
    }

    public static void registerStainedGlass(Block glassBlock) {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            Preconditions.checkNotNull(glassBlock.getRegistryName(), "registryName");
            AtumRegistry.registerBlock(new BlockAtumStainedGlass(), glassBlock.getRegistryName().getResourcePath().replace("_glass", "") + "_" + color.getName() + "_stained_glass");
        }
    }

    public static Block getGlass(Block baseGlassBlack, EnumDyeColor color) {
        Preconditions.checkNotNull(baseGlassBlack.getRegistryName(), "registryName");
        return REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID, baseGlassBlack.getRegistryName().getResourcePath().replace("_glass", "") + "_" + color.getName() + "_stained_glass"));
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Nullable
    public float[] getBeaconColorMultiplier(IBlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return EnumDyeColor.valueOf(WordUtils.swapCase(this.getRegistryName().getResourcePath().replace("framed_", "").replace("crystal_", "").replace("_stained", "").replace("_glass", ""))).getColorComponentValues();
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, pos);
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, pos);
        }
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}