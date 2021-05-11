package com.teammetallurgy.atum.misc.datagenerator;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGenerator extends BlockStateProvider {

    public BlockStatesGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Atum.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        generateTorchWithUnlit(AtumBlocks.PALM_TORCH);
        generateTorchWithUnlit(AtumBlocks.DEADWOOD_TORCH);
        generateTorchWithUnlit(AtumBlocks.LIMESTONE_TORCH);
        generateTorchWithUnlit(AtumBlocks.BONE_TORCH);
        generateTorchWithUnlit(AtumBlocks.NEBU_TORCH);
        generateTorch(AtumBlocks.TORCH_OF_ANPUT);
        generateTorch(AtumBlocks.TORCH_OF_ANUBIS);
        generateTorch(AtumBlocks.TORCH_OF_ATEM);
        generateTorch(AtumBlocks.TORCH_OF_GEB);
        generateTorch(AtumBlocks.TORCH_OF_HORUS);
        generateTorch(AtumBlocks.TORCH_OF_ISIS);
        generateTorch(AtumBlocks.TORCH_OF_MONTU);
        generateTorch(AtumBlocks.TORCH_OF_NEPTHYS);
        generateTorch(AtumBlocks.TORCH_OF_NUIT);
        generateTorch(AtumBlocks.TORCH_OF_OSIRIS);
        generateTorch(AtumBlocks.TORCH_OF_PTAH);
        generateTorch(AtumBlocks.TORCH_OF_RA);
        generateTorch(AtumBlocks.TORCH_OF_SETH);
        generateTorch(AtumBlocks.TORCH_OF_SHU);
        generateTorch(AtumBlocks.TORCH_OF_TEFNUT);
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

        simpleBlockWithItem(AtumBlocks.CRYSTAL_GLASS);
        simpleBlockWithItem(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS);
        simpleBlockWithItem(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS);
        paneBlockWithItem((PaneBlock) AtumBlocks.CRYSTAL_GLASS_PANE, new ResourceLocation(Atum.MOD_ID, "block/crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/crystal_glass_pane_top"));
        paneBlockWithItem((PaneBlock) AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS_PANE, new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass_pane_top"));
        paneBlockWithItem((PaneBlock) AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass_pane_top"));
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getString();
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_crystal_glass"));
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_palm_framed_crystal_glass"));
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass"));
            paneBlockWithItem((PaneBlock) StackHelper.getBlockFromName(colorName + "_stained_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_crystal_glass_pane_top"));
            paneBlockWithItem((PaneBlock) StackHelper.getBlockFromName(colorName + "_stained_palm_framed_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_palm_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass_pane_top"));
            paneBlockWithItem((PaneBlock) StackHelper.getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_deadwood_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass_pane_top"));
        }
    }

    private void generateTorch(Block torch) {
        String torchName = torch.getRegistryName().getPath();
        ResourceLocation wallTorchName = new ResourceLocation(Atum.MOD_ID, "wall_" + torchName);
        ResourceLocation torchLocation = new ResourceLocation(Atum.MOD_ID, "block/" + torchName);
        simpleBlock(torch, models().torch(torchName, torchLocation));
        horizontalBlock(StackHelper.getBlockFromName(wallTorchName), models().torchWall("wall_" + torchName, new ResourceLocation(Atum.MOD_ID, "block/" + torchName)), 90);
        super.itemModels().getBuilder(torch.getRegistryName().getPath()).parent(itemModels().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", torchLocation);
        super.itemModels().getBuilder(wallTorchName.getPath()).parent(itemModels().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", torchLocation);

    }

    private void generateTorchWithUnlit(Block torch) {
        String torchName = torch.getRegistryName().getPath();
        generateTorch(torch);
        ResourceLocation unlitTorchLocation = new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit");
        simpleBlock(AtumTorchUnlitBlock.UNLIT.get(torch), models().torch(torchName + "_unlit", unlitTorchLocation));
        horizontalBlock(StackHelper.getBlockFromName(new ResourceLocation(Atum.MOD_ID, "wall_" + torchName + "_unlit")), models().torchWall("wall_" + torchName + "_unlit", new ResourceLocation(Atum.MOD_ID, "block/" + torchName + "_unlit")), 90);
    }

    private void wallBlock(WallBlock wallBlock, Block textureBlock) {
        super.wallBlock(wallBlock, modLoc("block/" + textureBlock.getRegistryName().getPath()));
    }

    private void simpleBlockWithItem(Block block) {
        super.simpleBlock(block);
        super.simpleBlockItem(block, cubeAll(block));
    }

    private void paneBlockWithItem(PaneBlock block, ResourceLocation pane, ResourceLocation edge) {
        super.paneBlock(block, pane, edge);
        super.itemModels().getBuilder(block.getRegistryName().getPath()).parent(itemModels().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", pane);
    }
}