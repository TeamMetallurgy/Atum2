package com.teammetallurgy.atum.client.render;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.ChestSpawnerBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.LimestoneChestBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.SarcophagusBlock;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.client.model.shield.AbstractShieldModel;
import com.teammetallurgy.atum.client.model.shield.AtumsProtectionModel;
import com.teammetallurgy.atum.client.model.shield.BrigandShieldModel;
import com.teammetallurgy.atum.client.model.shield.StoneguardShieldModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Map;

public class ItemStackRenderer extends ItemStackTileEntityRenderer {
    private static final Map<String, ResourceLocation> SHIELD_CACHE = Maps.newHashMap();
    private static final Material ATUMS_PROTECTION_MATERIAL = getShieldMaterial("atums_protection");
    private static final Material BRIGAND_SHIELD_MATERIAL = getShieldMaterial("brigand_shield");
    private static final Material STONEGUARD_SHIELD_MATERIAL = getShieldMaterial("stoneguard_shield");
    private static final AtumsProtectionModel ATUMS_PROTECTION = new AtumsProtectionModel();
    private static final BrigandShieldModel BRIGAND_SHIELD = new BrigandShieldModel();
    private static final StoneguardShieldModel STONEGUARD_SHIELD = new StoneguardShieldModel();

    @Override
    public void render(@Nonnull ItemStack stack, @Nonnull MatrixStack matrixStack, @Nonnull IRenderTypeBuffer buffer, int i, int i1) {
        Item item = stack.getItem();

        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();

            if (block instanceof LimestoneChestBlock || block instanceof ChestSpawnerBlock) {
                TileEntityRendererDispatcher.instance.renderItem(AtumTileEntities.LIMESTONE_CHEST.create(), matrixStack, buffer, i, i1);
            } else if (block instanceof SarcophagusBlock) {
                TileEntityRendererDispatcher.instance.renderItem(AtumTileEntities.SARCOPHAGUS.create(), matrixStack, buffer, i, i1);
            }
        } else {
            if (item == AtumItems.BRIGAND_SHIELD) {
                renderShield(stack, BRIGAND_SHIELD, BRIGAND_SHIELD_MATERIAL, matrixStack, buffer, i, i1);
            } else if (item == AtumItems.STONEGUARD_SHIELD) {
                renderShield(stack, STONEGUARD_SHIELD, STONEGUARD_SHIELD_MATERIAL, matrixStack, buffer, i, i1);
            } else if (item == AtumItems.ATUMS_PROTECTION) {
                renderShield(stack, ATUMS_PROTECTION, ATUMS_PROTECTION_MATERIAL, matrixStack, buffer, i, i1);
            }
        }
    }

    private void renderShield(@Nonnull ItemStack stack, AbstractShieldModel shieldModel, Material material, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i, int i1) {
        IVertexBuilder builder = material.getSprite().wrapBuffer(ItemRenderer.getBuffer(buffer, shieldModel.getRenderType(material.getAtlasLocation()), false, stack.hasEffect()));
        shieldModel.render(matrixStack, builder, i, i1, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static Material getShieldMaterial(String fileName) {
        ResourceLocation shieldTexture = SHIELD_CACHE.get(fileName);
        if (shieldTexture == null) {
            shieldTexture = new ResourceLocation(Atum.MOD_ID, "shield/" + fileName);
            SHIELD_CACHE.put(fileName, shieldTexture);
        }

        ClientHandler.addToShieldAtlas(shieldTexture);
        return new Material(Atlases.SHIELD_ATLAS, shieldTexture);
    }
}