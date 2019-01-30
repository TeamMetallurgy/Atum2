package com.teammetallurgy.atum.blocks.linen;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.Map;

public class BlockLinen extends Block implements IOreDictEntry {
    private static final Map<EnumDyeColor, Block> LINEN = Maps.newEnumMap(EnumDyeColor.class);


    public BlockLinen(Material material) {
        super(material);
        this.setHardness(0.6F);
        this.setSoundType(SoundType.CLOTH);
    }

    public static void registerLinenBlocks() {
        for (EnumDyeColor color : EnumDyeColor.values()) {
            BlockLinen linen = new BlockLinen(Material.CLOTH);
            LINEN.put(color, linen);
            AtumRegistry.registerBlock(linen, "linen_" + color.getName());
        }
    }

    public static Block getLinen(EnumDyeColor color) {
        return LINEN.get(color);
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.getBlockColor(EnumDyeColor.valueOf(getColorString().toUpperCase()));
    }

    public String getColorString() {
        Preconditions.checkNotNull(this.getRegistryName(), "registryName");
        return this.getRegistryName().getPath().replace("linen_", "");
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "wool");
        OreDictHelper.add(this, "wool" + WordUtils.capitalize(EnumDyeColor.valueOf(this.getColorString().toUpperCase()).getTranslationKey()));
    }
}