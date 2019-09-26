package com.teammetallurgy.atum.blocks.linen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockLinen extends Block {
    private static final Map<DyeColor, Block> LINEN = Maps.newEnumMap(DyeColor.class);


    BlockLinen(Material material) {
        super(material);
        this.setHardness(0.6F);
        this.setSoundType(SoundType.CLOTH);
    }

    public static void registerLinenBlocks() {
        for (DyeColor color : DyeColor.values()) {
            BlockLinen linen = new BlockLinen(Material.CLOTH);
            LINEN.put(color, linen);
            AtumRegistry.registerBlock(linen, "linen_" + color.getName());
        }
    }

    public static Block getLinen(DyeColor color) {
        return LINEN.get(color);
    }

    @Override
    @Nonnull
    public MaterialColor getMapColor(BlockState state, IBlockReader world, BlockPos pos) {
        return MaterialColor.getBlockColor(DyeColor.valueOf(getColorString().toUpperCase()));
    }

    String getColorString() {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return this.getRegistryName().getPath().replace("linen_", "");
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "wool");
        OreDictHelper.add(this, "wool" + WordUtils.capitalize(DyeColor.valueOf(this.getColorString().toUpperCase()).getTranslationKey()));
    }
}