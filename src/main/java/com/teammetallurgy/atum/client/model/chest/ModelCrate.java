package com.teammetallurgy.atum.client.model.chest;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelCrate extends ModelBase {
    private ModelRenderer crateCore;
    public ModelRenderer crateLid;

    public ModelCrate() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.crateCore = new ModelRenderer(this, 0, 0);
        this.crateCore.setRotationPoint(8.0F, 9.0F, 0.0F);
        this.crateCore.addBox(-16.0F, 0.0F, -8.0F, 16, 15, 16, 0.0F);
        this.crateLid = new ModelRenderer(this, 0, 32);
        this.crateLid.setRotationPoint(8.0F, 8.0F, 0.0F);
        this.crateLid.addBox(-16.0F, 0.0F, -8.0F, 16, 1, 16, 0.0F);
    }

    public void renderAll() {
        this.crateCore.render(0.0625F);
        this.crateLid.render(0.0625F);
    }
}