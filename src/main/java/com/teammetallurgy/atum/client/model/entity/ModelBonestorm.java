package com.teammetallurgy.atum.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ModelBonestorm extends ModelBase {
    private ModelRenderer[] bonestormSticks = new ModelRenderer[12];
    private ModelRenderer bonestormHead;

    public ModelBonestorm() {
        for (int i = 0; i < this.bonestormSticks.length; ++i) {
            this.bonestormSticks[i] = new ModelRenderer(this, 0, 16);
            this.bonestormSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }

        this.bonestormHead = new ModelRenderer(this, 0, 0);
        this.bonestormHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);
        this.bonestormHead.render(scale);

        for (ModelRenderer bonestormStick : this.bonestormSticks) {
            bonestormStick.render(scale);
        }
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
        float f = ageInTicks * (float) Math.PI * -0.1F;
        int i;

        for (i = 0; i < 4; ++i) {
            this.bonestormSticks[i].rotationPointY = -2.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.bonestormSticks[i].rotationPointX = MathHelper.cos(f) * 9.0F;
            this.bonestormSticks[i].rotationPointZ = MathHelper.sin(f) * 9.0F;
            ++f;
        }

        f = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;

        for (i = 4; i < 8; ++i) {
            this.bonestormSticks[i].rotationPointY = 2.0F + MathHelper.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.bonestormSticks[i].rotationPointX = MathHelper.cos(f) * 7.0F;
            this.bonestormSticks[i].rotationPointZ = MathHelper.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;

        for (i = 8; i < 12; ++i) {
            this.bonestormSticks[i].rotationPointY = 11.0F + MathHelper.cos(((float) i * 1.5F + ageInTicks) * 0.5F);
            this.bonestormSticks[i].rotationPointX = MathHelper.cos(f) * 5.0F;
            this.bonestormSticks[i].rotationPointZ = MathHelper.sin(f) * 5.0F;
            ++f;
        }

        this.bonestormHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
        this.bonestormHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
    }
}