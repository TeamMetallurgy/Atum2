package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.BlockPlanks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.SoundType;
import net.minecraft.state.Property;

public class BlockAtumFenceGate extends FenceGateBlock {

    public BlockAtumFenceGate() {
        super(BlockPlanks.EnumType.OAK); //TODO 1.13
        this.setHardness(2.0F).setResistance(5.0F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public Property[] getNonRenderingProperties() {
        return new Property[]{POWERED};
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "fenceGateWood");
    }
}