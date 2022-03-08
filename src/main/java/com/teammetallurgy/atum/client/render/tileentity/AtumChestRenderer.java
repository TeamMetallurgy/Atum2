package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, value = Dist.CLIENT)
public class AtumChestRenderer extends ChestRenderer<ChestBaseTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final Material LIMESTONE_CHEST = getChestMaterial("limestone_chest");
    private static final Material LIMESTONE_CHEST_LEFT = getChestMaterial("limestone_chest_left");
    private static final Material LIMESTONE_CHEST_RIGHT = getChestMaterial("limestone_chest_right");

    public AtumChestRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    @Nonnull
    protected Material getMaterial(@Nonnull ChestBaseTileEntity chest, @Nonnull ChestType chestType) {
        return getChestMaterial(chestType, LIMESTONE_CHEST, LIMESTONE_CHEST_LEFT, LIMESTONE_CHEST_RIGHT);
    }

    protected static Material getChestMaterial(ChestType chestType, Material single, @Nullable Material left, @Nullable Material right) { //Copied from Atlases
        switch (chestType) {
            case LEFT:
                return left;
            case RIGHT:
                return right;
            case SINGLE:
            default:
                return single;
        }
    }

    public static Material getChestMaterial(String fileName) { //Copied from Atlases
        ResourceLocation chestTexture = CACHE.get(fileName);
        if (chestTexture == null) {
            chestTexture = new ResourceLocation(Atum.MOD_ID, "entity/chest/" + fileName);
            CACHE.put(fileName, chestTexture);
        }

        ClientHandler.addToChestAtlas(chestTexture);
        return new Material(Sheets.CHEST_SHEET, chestTexture);
    }
}