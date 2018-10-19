package com.teammetallurgy.atum.client.model.shield;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAtumsProtection extends ModelBase {
    public ModelRenderer shieldCore;
    public ModelRenderer handleCore;
    public ModelRenderer gemStone;
    public ModelRenderer shieldTop1;
    public ModelRenderer shieldTop2;
    public ModelRenderer shieldTop3;
    public ModelRenderer shieldTop4;
    public ModelRenderer shieldBottom1;
    public ModelRenderer shieldBottom2;
    public ModelRenderer shieldBottom3;
    public ModelRenderer shieldBottom4;
    public ModelRenderer handleSide1;
    public ModelRenderer handleSide2;

    public ModelAtumsProtection() {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.handleCore = new ModelRenderer(this, 20, 0);
        this.handleCore.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handleCore.addBox(-1.0F, -2.0F, 3.0F, 2, 4, 1, 0.0F);
        this.shieldBottom4 = new ModelRenderer(this, 0, 22);
        this.shieldBottom4.setRotationPoint(1.0F, 1.0F, 0.0F);
        this.shieldBottom4.addBox(-4.0F, 4.0F, 0.0F, 6, 1, 1, 0.0F);
        this.shieldCore = new ModelRenderer(this, 0, 10);
        this.shieldCore.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldCore.addBox(-5.0F, -2.0F, 0.0F, 10, 4, 1, 0.0F);
        this.handleSide1 = new ModelRenderer(this, 25, 3);
        this.handleSide1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handleSide1.addBox(-1.0F, 1.0F, 1.0F, 2, 1, 2, 0.0F);
        this.shieldBottom3 = new ModelRenderer(this, 0, 20);
        this.shieldBottom3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldBottom3.addBox(-4.0F, 4.0F, 0.0F, 8, 1, 1, 0.0F);
        this.shieldBottom1 = new ModelRenderer(this, 0, 16);
        this.shieldBottom1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldBottom1.addBox(-6.0F, 2.0F, 0.0F, 12, 1, 1, 0.0F);
        this.shieldTop2 = new ModelRenderer(this, 0, 6);
        this.shieldTop2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldTop2.addBox(-5.0F, -4.0F, 0.0F, 10, 1, 1, 0.0F);
        this.gemStone = new ModelRenderer(this, 0, 24);
        this.gemStone.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.gemStone.addBox(-2.0F, -2.0F, -1.0F, 4, 4, 1, 0.0F);
        this.handleSide2 = new ModelRenderer(this, 25, 3);
        this.handleSide2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.handleSide2.addBox(-1.0F, -2.0F, 1.0F, 2, 1, 2, 0.0F);
        this.shieldBottom2 = new ModelRenderer(this, 0, 18);
        this.shieldBottom2.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldBottom2.addBox(-5.0F, 3.0F, 0.0F, 10, 1, 1, 0.0F);
        this.shieldTop4 = new ModelRenderer(this, 0, 2);
        this.shieldTop4.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldTop4.addBox(-3.0F, -6.0F, 0.0F, 6, 1, 1, 0.0F);
        this.shieldTop3 = new ModelRenderer(this, 0, 4);
        this.shieldTop3.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldTop3.addBox(-4.0F, -5.0F, 0.0F, 8, 1, 1, 0.0F);
        this.shieldTop1 = new ModelRenderer(this, 0, 8);
        this.shieldTop1.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.shieldTop1.addBox(-6.0F, -3.0F, 0.0F, 12, 1, 1, 0.0F);
        this.shieldCore.addChild(this.shieldBottom4);
        this.handleCore.addChild(this.handleSide1);
        this.shieldCore.addChild(this.shieldBottom3);
        this.shieldCore.addChild(this.shieldBottom1);
        this.shieldCore.addChild(this.shieldTop2);
        this.handleCore.addChild(this.handleSide2);
        this.shieldCore.addChild(this.shieldBottom2);
        this.shieldCore.addChild(this.shieldTop4);
        this.shieldCore.addChild(this.shieldTop3);
        this.shieldCore.addChild(this.shieldTop1);
    }

    public void render() {
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0D / 0.75D, -1.0D / 0.75D, -1.0D / 0.75D);
        this.handleCore.render(0.0625F);
        this.shieldCore.render(0.0625F);
        this.gemStone.render(0.0625F);
        GlStateManager.popMatrix();
    }
}