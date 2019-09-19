package com.teammetallurgy.atum.blocks.glass;

import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class BlockAtumPane extends BlockPane {

    public BlockAtumPane() {
        super(Material.GLASS, false);
        this.setHardness(0.3F);
        this.setSoundType(SoundType.GLASS);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(new ItemStack(this), "paneGlass", "paneGlassColorless");
    }
}