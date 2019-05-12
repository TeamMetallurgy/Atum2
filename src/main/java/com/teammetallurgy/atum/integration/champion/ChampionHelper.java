package com.teammetallurgy.atum.integration.champion;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class ChampionHelper {

    public static boolean isChampion(Entity entity) {
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBT(compound);
        if (compound.hasKey("ForgeCaps")) {
            NBTTagCompound forgeCaps = compound.getCompoundTag("ForgeCaps");
            return forgeCaps.hasKey("champions:championship");
        }
        return false;
    }

    public static int getTier(Entity entity) {
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBT(compound);
        if (compound.hasKey("ForgeCaps")) {
            NBTTagCompound forgeCaps = compound.getCompoundTag("ForgeCaps");
            if (forgeCaps.hasKey("champions:championship")) {
                NBTTagCompound map = forgeCaps.getCompoundTag("champions:championship");
                if (map.hasKey("tier")) {
                    return map.getInteger("tier");
                }
            }
        }
        return 0;
    }

    public static ResourceLocation getTexture(Entity entity, String entityName) {
        int tier = ChampionHelper.getTier(entity);
        if (tier > 0) {
            ResourceLocation textureResourceLocation = new ResourceLocation(Constants.MOD_ID, "textures/entity/" + entityName + "_variant_champion_" + tier + ".png");

            TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
            ITextureObject texture = textureManager.getTexture(textureResourceLocation);

            if (texture != TextureUtil.MISSING_TEXTURE) {
                return textureResourceLocation;
            }
        }

        return null;
    }

}
