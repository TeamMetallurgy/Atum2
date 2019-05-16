package com.teammetallurgy.atum.client.render.entity.mobs;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.entity.stone.EntityStoneguard;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class RenderStoneguard extends RenderBiped<EntityStoneguard> {
    private static final Map<Integer, ResourceLocation> CACHE = Maps.newHashMap();
    private static final ResourceLocation STONEGUARD_IRON_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/stoneguard_derp.png");

    public RenderStoneguard(RenderManager renderManager) {
        super(renderManager, new ModelPlayer(0.0F, false), 0.5F);
        LayerBipedArmor armor = new LayerBipedArmor(this) {
            @Override
            protected void initArmor() {
                this.modelLeggings = new ModelBiped(0.5F);
                this.modelArmor = new ModelBiped(1.0F);
            }
        };
        this.addLayer(armor);
    }

    @Override
    @Nullable
    protected ResourceLocation getEntityTexture(@Nonnull EntityStoneguard stoneguard) {
        if (stoneguard.hasCustomName()) {
            if (stoneguard.getCustomNameTag().equalsIgnoreCase("iron") || stoneguard.getCustomNameTag().equalsIgnoreCase("nutz")) {
                return STONEGUARD_IRON_TEXTURE;
            }
        }
        ResourceLocation location = CACHE.get(stoneguard.getVariant());

        if (location == null) {
            location = new ResourceLocation(Constants.MOD_ID, "textures/entity/stoneguard_" + stoneguard.getVariant() + ".png");
            CACHE.put(stoneguard.getVariant(), location);
        }
        return location;
    }
}