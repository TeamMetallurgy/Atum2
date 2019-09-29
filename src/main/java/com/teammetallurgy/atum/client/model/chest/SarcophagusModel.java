package com.teammetallurgy.atum.client.model.chest;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class SarcophagusModel extends Model {
    public RendererModel base;
    public RendererModel lid;
    public RendererModel liddeco1;
    public RendererModel liddeco2;
    public RendererModel liddeco3;
    public RendererModel gemchest;
    public RendererModel gemhead;

    public SarcophagusModel() {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.lid = new RendererModel(this, 0, 0);
        this.lid.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.lid.addBox(-16.0F, -2.0F, -16.0F, 30, 2, 14, 0.0F);
        this.gemchest = new RendererModel(this, 0, 45);
        this.gemchest.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.gemchest.addBox(0.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.base = new RendererModel(this, 0, 19);
        this.base.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.base.addBox(-16.0F, 0.0F, -16.0F, 30, 10, 14, 0.0F);
        this.liddeco3 = new RendererModel(this, 0, 45);
        this.liddeco3.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.liddeco3.addBox(-4.0F, -4.0F, -13.0F, 15, 1, 8, 0.0F);
        this.gemhead = new RendererModel(this, 0, 45);
        this.gemhead.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.gemhead.addBox(-12.0F, -4.5F, -10.0F, 2, 2, 2, 0.0F);
        this.liddeco1 = new RendererModel(this, 48, 51);
        this.liddeco1.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.liddeco1.addBox(-15.0F, -3.0F, -15.0F, 28, 1, 12, 0.0F);
        this.liddeco2 = new RendererModel(this, 90, 0);
        this.liddeco2.setRotationPoint(1.0F, 14.0F, 9.0F);
        this.liddeco2.addBox(-14.0F, -4.0F, -13.0F, 8, 1, 8, 0.0F);
    }

    public void renderAll() {
        this.lid.render(0.0625F);
        this.base.render(0.0625F);
        this.liddeco1.render(0.0625F);
        this.liddeco2.render(0.0625F);
        this.liddeco3.render(0.0625F);
        this.gemhead.render(0.0625F);
        this.gemchest.render(0.0625F);
    }
}
