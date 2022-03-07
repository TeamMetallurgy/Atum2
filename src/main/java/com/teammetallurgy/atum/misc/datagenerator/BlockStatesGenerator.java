package com.teammetallurgy.atum.misc.datagenerator;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStatesGenerator extends BlockStateProvider {

    public BlockStatesGenerator(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Atum.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        generateTorchWithUnlit(AtumBlocks.PALM_TORCH.get());
        generateTorchWithUnlit(AtumBlocks.DEADWOOD_TORCH.get());
        generateTorchWithUnlit(AtumBlocks.LIMESTONE_TORCH.get());
        generateTorchWithUnlit(AtumBlocks.BONE_TORCH.get());
        generateTorchWithUnlit(AtumBlocks.NEBU_TORCH.get());
        generateTorch(AtumBlocks.TORCH_OF_ANPUT.get());
        generateTorch(AtumBlocks.TORCH_OF_ANUBIS.get());
        generateTorch(AtumBlocks.TORCH_OF_ATEM.get());
        generateTorch(AtumBlocks.TORCH_OF_GEB.get());
        generateTorch(AtumBlocks.TORCH_OF_HORUS.get());
        generateTorch(AtumBlocks.TORCH_OF_ISIS.get());
        generateTorch(AtumBlocks.TORCH_OF_MONTU.get());
        generateTorch(AtumBlocks.TORCH_OF_NEPTHYS.get());
        generateTorch(AtumBlocks.TORCH_OF_NUIT.get());
        generateTorch(AtumBlocks.TORCH_OF_OSIRIS.get());
        generateTorch(AtumBlocks.TORCH_OF_PTAH.get());
        generateTorch(AtumBlocks.TORCH_OF_RA.get());
        generateTorch(AtumBlocks.TORCH_OF_SETH.get());
        generateTorch(AtumBlocks.TORCH_OF_SHU.get());
        generateTorch(AtumBlocks.TORCH_OF_TEFNUT.get());
        wallBlock((WallBlock) AtumBlocks.LIMESTONE_WALL.get(), AtumBlocks.LIMESTONE.get());
        wallBlock((WallBlock) AtumBlocks.LIMESTONE_CRACKED_WALL.get(), AtumBlocks.LIMESTONE_CRACKED.get());
        wallBlock((WallBlock) AtumBlocks.SMALL_WALL.get(), AtumBlocks.LIMESTONE_BRICK_SMALL.get());
        wallBlock((WallBlock) AtumBlocks.LARGE_WALL.get(), AtumBlocks.LIMESTONE_BRICK_LARGE.get());
        wallBlock((WallBlock) AtumBlocks.CRACKED_BRICK_WALL.get(), modLoc("block/limestone_brick_cracked"));
        wallBlock((WallBlock) AtumBlocks.CHISELED_WALL.get(), AtumBlocks.LIMESTONE_BRICK_CHISELED.get());
        wallBlock((WallBlock) AtumBlocks.CARVED_WALL.get(), AtumBlocks.LIMESTONE_BRICK_CARVED.get());
        wallBlock((WallBlock) AtumBlocks.ALABASTER_BRICK_SMOOTH_WALL.get(), AtumBlocks.ALABASTER_BRICK_SMOOTH.get());
        wallBlock((WallBlock) AtumBlocks.ALABASTER_BRICK_POLISHED_WALL.get(), AtumBlocks.ALABASTER_BRICK_POLISHED.get());
        wallBlock((WallBlock) AtumBlocks.ALABASTER_BRICK_CARVED_WALL.get(), AtumBlocks.ALABASTER_BRICK_CARVED.get());
        wallBlock((WallBlock) AtumBlocks.ALABASTER_BRICK_TILED_WALL.get(), AtumBlocks.ALABASTER_BRICK_TILED.get());
        wallBlock((WallBlock) AtumBlocks.ALABASTER_BRICK_PILLAR_WALL.get(), AtumBlocks.ALABASTER_BRICK_PILLAR.get());
        wallBlock((WallBlock) AtumBlocks.PORPHYRY_BRICK_SMOOTH_WALL.get(), AtumBlocks.PORPHYRY_BRICK_SMOOTH.get());
        wallBlock((WallBlock) AtumBlocks.PORPHYRY_BRICK_POLISHED_WALL.get(), AtumBlocks.PORPHYRY_BRICK_POLISHED.get());
        wallBlock((WallBlock) AtumBlocks.PORPHYRY_BRICK_CARVED_WALL.get(), AtumBlocks.PORPHYRY_BRICK_CARVED.get());
        wallBlock((WallBlock) AtumBlocks.PORPHYRY_BRICK_TILED_WALL.get(), AtumBlocks.PORPHYRY_BRICK_TILED.get());
        wallBlock((WallBlock) AtumBlocks.PORPHYRY_BRICK_PILLAR_WALL.get(), AtumBlocks.PORPHYRY_BRICK_PILLAR.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_WHITE_WALL.get(), AtumBlocks.CERAMIC_WHITE.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_ORANGE_WALL.get(), AtumBlocks.CERAMIC_ORANGE.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_MAGENTA_WALL.get(), AtumBlocks.CERAMIC_MAGENTA.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_LIGHT_BLUE_WALL.get(), AtumBlocks.CERAMIC_LIGHT_BLUE.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_YELLOW_WALL.get(), AtumBlocks.CERAMIC_YELLOW.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_LIME_WALL.get(), AtumBlocks.CERAMIC_LIME.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_PINK_WALL.get(), AtumBlocks.CERAMIC_PINK.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_GRAY_WALL.get(), AtumBlocks.CERAMIC_GRAY.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_LIGHT_GRAY_WALL.get(), AtumBlocks.CERAMIC_LIGHT_GRAY.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_CYAN_WALL.get(), AtumBlocks.CERAMIC_CYAN.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_PURPLE_WALL.get(), AtumBlocks.CERAMIC_PURPLE.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_BLUE_WALL.get(), AtumBlocks.CERAMIC_BLUE.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_BROWN_WALL.get(), AtumBlocks.CERAMIC_BROWN.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_GREEN_WALL.get(), AtumBlocks.CERAMIC_GREEN.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_RED_WALL.get(), AtumBlocks.CERAMIC_RED.get());
        wallBlock((WallBlock) AtumBlocks.CERAMIC_BLACK_WALL.get(), AtumBlocks.CERAMIC_BLACK.get());

        simpleBlockWithItem(AtumBlocks.CRYSTAL_GLASS.get());
        simpleBlockWithItem(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS.get());
        simpleBlockWithItem(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS.get());
        paneBlockWithItem((IronBarsBlock) AtumBlocks.CRYSTAL_GLASS_PANE.get(), new ResourceLocation(Atum.MOD_ID, "block/crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/crystal_glass_pane_top"));
        paneBlockWithItem((IronBarsBlock) AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass_pane_top"));
        paneBlockWithItem((IronBarsBlock) AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass_pane_top"));
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getSerializedName();
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_crystal_glass"));
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_palm_framed_crystal_glass"));
            simpleBlockWithItem(StackHelper.getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass"));
            paneBlockWithItem((IronBarsBlock) StackHelper.getBlockFromName(colorName + "_stained_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_crystal_glass_pane_top"));
            paneBlockWithItem((IronBarsBlock) StackHelper.getBlockFromName(colorName + "_stained_palm_framed_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_palm_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/palm_framed_crystal_glass_pane_top"));
            paneBlockWithItem((IronBarsBlock) StackHelper.getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass_pane"), new ResourceLocation(Atum.MOD_ID, "block/" + colorName + "_stained_deadwood_framed_crystal_glass"), new ResourceLocation(Atum.MOD_ID, "block/deadwood_framed_crystal_glass_pane_top"));
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

    private void paneBlockWithItem(IronBarsBlock block, ResourceLocation pane, ResourceLocation edge) {
        super.paneBlock(block, pane, edge);
        super.itemModels().getBuilder(block.getRegistryName().getPath()).parent(itemModels().getExistingFile(new ResourceLocation("item/generated"))).texture("layer0", pane);
    }
}