package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.client.model.entity.CamelModel;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.LayeredTexture;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class CamelRender extends MobRenderer<CamelEntity, CamelModel<CamelEntity>> {
    private static final Map<String, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation GIRAFI = new ResourceLocation(Constants.MOD_ID, "textures/entity/camel_girafi.png");

    public CamelRender(EntityRendererManager renderManager) {
        super(renderManager, new CamelModel(0.0F), 0.7F);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull CamelEntity camel) {
        String textureName = camel.getTexture();

        ResourceLocation location = CACHE.get(textureName);
        if (location == null) {
            location = new ResourceLocation(textureName);
            String[] texturePath = new String[3];
            texturePath[0] = new ResourceLocation(Constants.MOD_ID, "textures/entity/camel_" + camel.getVariant()) + ".png";
            if (camel.hasCustomName() && camel.getCustomName() != null) {
                String customName = camel.getCustomName().getFormattedText();
                if (customName.equalsIgnoreCase("girafi")) {
                    texturePath[0] = GIRAFI.toString();
                }
            }

            ItemStack armor = camel.getArmor();
            if (!armor.isEmpty()) {
                CamelEntity.ArmorType armorType = CamelEntity.ArmorType.getByItemStack(armor);
                texturePath[1] = armorType.getTextureName();
            }

            DyeColor color = camel.getColor();
            if (color != null) {
                texturePath[2] = new ResourceLocation(Constants.MOD_ID, "textures/entity/camel_carpet/camel_carpet_" + color.getName()) + ".png";
            }
            Minecraft.getInstance().getTextureManager().loadTexture(location, new LayeredTexture(texturePath));
            CACHE.put(textureName, location);
        }

        return location;
    }
}