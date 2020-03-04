package com.teammetallurgy.atum.integration.champion;

import com.teammetallurgy.atum.integration.IModIntegration;
import com.teammetallurgy.atum.integration.IntegrationHandler;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ChampionsHelper implements IModIntegration {
    public static final String CHAMPION_ID = "champions";

    @OnlyIn(Dist.CLIENT)
    public static boolean isChampion(Entity entity) {
        if (IntegrationHandler.getConfigValue(CHAMPION_ID)) {
            CompoundNBT compound = new CompoundNBT();
            entity.deserializeNBT(compound);
            if (compound.contains("ForgeCaps")) {
                CompoundNBT forgeCaps = compound.getCompound("ForgeCaps");
                return forgeCaps.contains("champions:championship");
            }
        }
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getTexture(Entity entity, String entityName) {
        int tier = ChampionsHelper.getTier(entity);
        if (tier > 0) {
            ResourceLocation textureResourceLocation = new ResourceLocation(Constants.MOD_ID, "textures/entity/variants/" + entityName + "_champion_" + tier + ".png");
            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            Texture texture = textureManager.getTexture(textureResourceLocation);

            if (texture == null) {
                textureManager.loadTexture(textureResourceLocation, new SimpleTexture(textureResourceLocation));
                texture = textureManager.getTexture(textureResourceLocation);
            }

            if (texture != MissingTextureSprite.getDynamicTexture()) {
                return textureResourceLocation;
            }
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    private static int getTier(Entity entity) {
        CompoundNBT compound = new CompoundNBT();
        entity.deserializeNBT(compound);
        if (compound.contains("ForgeCaps")) {
            CompoundNBT forgeCaps = compound.getCompound("ForgeCaps");
            if (forgeCaps.contains("champions:championship")) {
                CompoundNBT map = forgeCaps.getCompound("champions:championship");
                if (map.contains("tier")) {
                    return map.getInt("tier");
                }
            }
        }
        return 0;
    }
}