package com.teammetallurgy.atum.utils;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockStatesGenerator extends BlockStateProvider {

    public BlockStatesGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Atum.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        generateTorch(AtumBlocks.PALM_TORCH);
        generateTorch(AtumBlocks.DEADWOOD_TORCH);
        generateTorch(AtumBlocks.LIMESTONE_TORCH);
        generateTorch(AtumBlocks.BONE_TORCH);
        generateTorch(AtumBlocks.PHARAOH_TORCH);

    }

    private void generateTorch(Block torch) {
        String torchName = torch.getRegistryName().getPath();
        simpleBlock(torch, models().torch(torchName, new ResourceLocation(Atum.MOD_ID, "block/" + torchName)));
        simpleBlock(AtumTorchUnlitBlock.UNLIT.get(torch), models().torch(torchName + "_unlit", new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit")));
        horizontalBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Atum.MOD_ID, "wall_" + torchName)), models().torchWall("wall_" + torchName, new ResourceLocation(Atum.MOD_ID, "block/" + torchName)), 90);
        horizontalBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Atum.MOD_ID, "wall_" + torchName + "_unlit")), models().torchWall("wall_" + torchName + "_unlit", new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit")), 90);
    }
}