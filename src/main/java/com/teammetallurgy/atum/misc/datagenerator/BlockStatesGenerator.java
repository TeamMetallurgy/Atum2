package com.teammetallurgy.atum.misc.datagenerator;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        wallBlock(AtumBlocks.LIMESTONE_WALL, AtumBlocks.LIMESTONE);
        wallBlock(AtumBlocks.LIMESTONE_CRACKED_WALL, AtumBlocks.LIMESTONE_CRACKED);
        wallBlock(AtumBlocks.SMALL_WALL, AtumBlocks.LIMESTONE_BRICK_SMALL);
        wallBlock(AtumBlocks.LARGE_WALL, AtumBlocks.LIMESTONE_BRICK_LARGE);
        wallBlock(AtumBlocks.CRACKED_BRICK_WALL, modLoc("block/limestone_brick_cracked"));
        wallBlock(AtumBlocks.CHISELED_WALL, AtumBlocks.LIMESTONE_BRICK_CHISELED);
        wallBlock(AtumBlocks.CARVED_WALL, AtumBlocks.LIMESTONE_BRICK_CARVED);
        wallBlock(AtumBlocks.ALABASTER_BRICK_SMOOTH_WALL, AtumBlocks.ALABASTER_BRICK_SMOOTH);
        wallBlock(AtumBlocks.ALABASTER_BRICK_POLISHED_WALL, AtumBlocks.ALABASTER_BRICK_POLISHED);
        wallBlock(AtumBlocks.ALABASTER_BRICK_CARVED_WALL, AtumBlocks.ALABASTER_BRICK_CARVED);
        wallBlock(AtumBlocks.ALABASTER_BRICK_TILED_WALL, AtumBlocks.ALABASTER_BRICK_TILED);
        wallBlock(AtumBlocks.ALABASTER_BRICK_PILLAR_WALL, AtumBlocks.ALABASTER_BRICK_PILLAR);
        wallBlock(AtumBlocks.PORPHYRY_BRICK_SMOOTH_WALL, AtumBlocks.PORPHYRY_BRICK_SMOOTH);
        wallBlock(AtumBlocks.PORPHYRY_BRICK_POLISHED_WALL, AtumBlocks.PORPHYRY_BRICK_POLISHED);
        wallBlock(AtumBlocks.PORPHYRY_BRICK_CARVED_WALL, AtumBlocks.PORPHYRY_BRICK_CARVED);
        wallBlock(AtumBlocks.PORPHYRY_BRICK_TILED_WALL, AtumBlocks.PORPHYRY_BRICK_TILED);
        wallBlock(AtumBlocks.PORPHYRY_BRICK_PILLAR_WALL, AtumBlocks.PORPHYRY_BRICK_PILLAR);
        wallBlock(AtumBlocks.CERAMIC_WHITE_WALL, AtumBlocks.CERAMIC_WHITE);
        wallBlock(AtumBlocks.CERAMIC_ORANGE_WALL, AtumBlocks.CERAMIC_ORANGE);
        wallBlock(AtumBlocks.CERAMIC_MAGENTA_WALL, AtumBlocks.CERAMIC_MAGENTA);
        wallBlock(AtumBlocks.CERAMIC_LIGHT_BLUE_WALL, AtumBlocks.CERAMIC_LIGHT_BLUE);
        wallBlock(AtumBlocks.CERAMIC_YELLOW_WALL, AtumBlocks.CERAMIC_YELLOW);
        wallBlock(AtumBlocks.CERAMIC_LIME_WALL, AtumBlocks.CERAMIC_LIME);
        wallBlock(AtumBlocks.CERAMIC_PINK_WALL, AtumBlocks.CERAMIC_PINK);
        wallBlock(AtumBlocks.CERAMIC_GRAY_WALL, AtumBlocks.CERAMIC_GRAY);
        wallBlock(AtumBlocks.CERAMIC_LIGHT_GRAY_WALL, AtumBlocks.CERAMIC_LIGHT_GRAY);
        wallBlock(AtumBlocks.CERAMIC_CYAN_WALL, AtumBlocks.CERAMIC_CYAN);
        wallBlock(AtumBlocks.CERAMIC_PURPLE_WALL, AtumBlocks.CERAMIC_PURPLE);
        wallBlock(AtumBlocks.CERAMIC_BLUE_WALL, AtumBlocks.CERAMIC_BLUE);
        wallBlock(AtumBlocks.CERAMIC_BROWN_WALL, AtumBlocks.CERAMIC_BROWN);
        wallBlock(AtumBlocks.CERAMIC_GREEN_WALL, AtumBlocks.CERAMIC_GREEN);
        wallBlock(AtumBlocks.CERAMIC_RED_WALL, AtumBlocks.CERAMIC_RED);
        wallBlock(AtumBlocks.CERAMIC_BLACK_WALL, AtumBlocks.CERAMIC_BLACK);

        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getString();
            cubeAll(StackHelper.getBlockFromName(colorName + "_stained_crystal_glass"));
        }
    }

    private void generateTorch(Block torch) {
        String torchName = torch.getRegistryName().getPath();
        simpleBlock(torch, models().torch(torchName, new ResourceLocation(Atum.MOD_ID, "block/" + torchName)));
        simpleBlock(AtumTorchUnlitBlock.UNLIT.get(torch), models().torch(torchName + "_unlit", new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit")));
        horizontalBlock(StackHelper.getBlockFromName(new ResourceLocation(Atum.MOD_ID, "wall_" + torchName)), models().torchWall("wall_" + torchName, new ResourceLocation(Atum.MOD_ID, "block/" + torchName)), 90);
        horizontalBlock(StackHelper.getBlockFromName(new ResourceLocation(Atum.MOD_ID, "wall_" + torchName + "_unlit")), models().torchWall("wall_" + torchName + "_unlit", new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit")), 90);
    }

    private void wallBlock(WallBlock wallBlock, Block textureBlock) {
        super.wallBlock(wallBlock, modLoc("block/" + textureBlock.getRegistryName().getPath()));
    }
}