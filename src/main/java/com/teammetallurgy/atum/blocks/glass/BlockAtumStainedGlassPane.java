package com.teammetallurgy.atum.blocks.glass;

import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockAtumStainedGlassPane extends BlockPane {

    private BlockAtumStainedGlassPane() {
        super(Material.GLASS, false);
        this.setHardness(0.3F);
        this.setSoundType(SoundType.GLASS);
    }

    public static void registerStainedGlassPane(Block glassBlock) {
        for (DyeColor color : DyeColor.values()) {
            Preconditions.checkNotNull(glassBlock.getRegistryName(), "registryName");
            AtumRegistry.registerBlock(new BlockAtumStainedGlassPane(), glassBlock.getRegistryName().getPath().replace("_glass", "") + "_" + color.getName() + "_stained_glass");
        }
    }

    public static Block getGlass(Block baseGlassBlack, DyeColor color) {
        Preconditions.checkNotNull(baseGlassBlack.getRegistryName(), "registryName");
        return REGISTRY.getObject(new ResourceLocation(Constants.MOD_ID, "thin_" + baseGlassBlack.getRegistryName().getPath().replace("_glass", "") + "_" + color.getName() + "_stained_glass"));
    }

    @Override
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    @Nullable
    public float[] getBeaconColorMultiplier(BlockState state, World world, BlockPos pos, BlockPos beaconPos) {
        return DyeColor.valueOf(WordUtils.swapCase(getColorString())).getColorComponentValues();
    }

    private String getColorString() {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return this.getRegistryName().getPath().replace("thin_", "").replace("framed_", "").replace("crystal_", "").replace("_stained", "").replace("_glass", "");
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, pos);
        }
    }

    @Override
    public void breakBlock(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (!world.isRemote) {
            BlockBeacon.updateColorAsync(world, pos);
        }
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "paneGlass", getColorString().replace("silver", "light_gray"));
        OreDictHelper.add(this, "paneGlass");
    }
}