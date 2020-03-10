package com.teammetallurgy.atum.client.model.shield;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public abstract class AbstractShieldModel extends Model {

    public AbstractShieldModel() {
        super(RenderType::getEntitySolid);
    }

    public abstract ModelRenderer getPlate();

    public abstract ModelRenderer getHandle();

    public ModelRenderer getOptional() {
        return null;
    }
}