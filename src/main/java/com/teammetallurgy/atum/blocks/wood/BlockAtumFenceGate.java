package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;

public class BlockAtumFenceGate extends BlockFenceGate implements IRenderMapper, IOreDictEntry {

    public BlockAtumFenceGate() {
        super(BlockPlanks.EnumType.OAK); //TODO 1.13
        this.setHardness(2.0F).setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{POWERED};
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "fenceGateWood");
    }
}