package com.teammetallurgy.atum.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelQuern extends ModelBase {
    private ModelRenderer quernstonecore;
    private ModelRenderer feedbase;
    private ModelRenderer quernstoneBase;
    private ModelRenderer quernstoneleft;
    private ModelRenderer quernstoneright;
    private ModelRenderer quernstoneback;
    private ModelRenderer quernstonefront;
    private ModelRenderer handle;
    private ModelRenderer feedleft;
    private ModelRenderer feedright;

    public ModelQuern() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.quernstoneBase = new ModelRenderer(this, 16, 34);
        this.quernstoneBase.setRotationPoint(0.0F, 23.0F, 0.0F);
        this.quernstoneBase.addBox(-6.0F, -1.0F, -6.0F, 12, 2, 12, 0.0F);
        this.quernstoneleft = new ModelRenderer(this, 0, 48);
        this.quernstoneleft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.quernstoneleft.addBox(-6.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.quernstonefront = new ModelRenderer(this, 0, 34);
        this.quernstonefront.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.quernstonefront.addBox(-4.0F, -2.0F, -6.0F, 8, 4, 1, 0.0F);
        this.quernstoneback = new ModelRenderer(this, 0, 34);
        this.quernstoneback.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.quernstoneback.addBox(-4.0F, -2.0F, 5.0F, 8, 4, 1, 0.0F);
        this.feedbase = new ModelRenderer(this, 28, 53);
        this.feedbase.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.feedbase.addBox(-1.5F, -1.0F, -8.0F, 3, 1, 2, 0.0F);
        this.quernstonecore = new ModelRenderer(this, 0, 18);
        this.quernstonecore.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.quernstonecore.addBox(-5.0F, -2.0F, -5.0F, 10, 4, 10, 0.0F);
        this.handle = new ModelRenderer(this, 22, 0);
        this.handle.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handle.addBox(-5.0F, -5.0F, -3.0F, 1, 3, 1, 0.0F);
        this.quernstoneright = new ModelRenderer(this, 0, 48);
        this.quernstoneright.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.quernstoneright.addBox(5.0F, -2.0F, -4.0F, 1, 4, 8, 0.0F);
        this.feedright = new ModelRenderer(this, 29, 56);
        this.feedright.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.feedright.addBox(0.5F, -2.0F, -8.0F, 1, 1, 2, 0.0F);
        this.feedleft = new ModelRenderer(this, 29, 56);
        this.feedleft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.feedleft.addBox(-1.5F, -2.0F, -8.0F, 1, 1, 2, 0.0F);
        this.quernstonecore.addChild(this.quernstoneleft);
        this.quernstonecore.addChild(this.quernstonefront);
        this.quernstonecore.addChild(this.quernstoneback);
        this.quernstonecore.addChild(this.handle);
        this.quernstonecore.addChild(this.quernstoneright);
        this.feedbase.addChild(this.feedright);
        this.feedbase.addChild(this.feedleft);
    }

    public void renderAll() {
        this.quernstoneBase.render(0.0625F);
        this.feedbase.render(0.0625F);
        this.quernstonecore.render(0.0625F);
    }
}