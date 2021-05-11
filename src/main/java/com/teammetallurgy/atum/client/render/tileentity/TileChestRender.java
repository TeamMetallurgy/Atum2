package com.teammetallurgy.atum.client.render.tileentity;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.client.ClientHandler;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.state.properties.ChestType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class TileChestRender extends ChestTileEntityRenderer<ChestBaseTileEntity> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final RenderMaterial LIMESTONE_CHEST = getChestMaterial("limestone_chest");
    private static final RenderMaterial LIMESTONE_CHEST_LEFT = getChestMaterial("limestone_chest_left");
    private static final RenderMaterial LIMESTONE_CHEST_RIGHT = getChestMaterial("limestone_chest_right");

    public TileChestRender(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    @Nonnull
    protected RenderMaterial getMaterial(@Nonnull ChestBaseTileEntity chest, @Nonnull ChestType chestType) {
        return getChestMaterial(chestType, LIMESTONE_CHEST, LIMESTONE_CHEST_LEFT, LIMESTONE_CHEST_RIGHT);
    }

    protected static RenderMaterial getChestMaterial(ChestType chestType, RenderMaterial single, @Nullable RenderMaterial left, @Nullable RenderMaterial right) { //Copied from Atlases
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

    public static RenderMaterial getChestMaterial(String fileName) { //Copied from Atlases
        ResourceLocation chestTexture = CACHE.get(fileName);
        if (chestTexture == null) {
            chestTexture = new ResourceLocation(Atum.MOD_ID, "entity/chest/" + fileName);
            CACHE.put(fileName, chestTexture);
        }

        ClientHandler.addToChestAtlas(chestTexture);
        return new RenderMaterial(Atlases.CHEST_ATLAS, chestTexture);
    }
}