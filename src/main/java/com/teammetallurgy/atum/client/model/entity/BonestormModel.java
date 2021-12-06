package com.teammetallurgy.atum.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.teammetallurgy.atum.entity.undead.BonestormEntity;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Arrays;

@OnlyIn(Dist.CLIENT)
public class BonestormModel<T extends BonestormEntity> extends ListModel<T> {
    private ModelPart[] bonestormSticks;
    private ModelPart bonestormHead = new ModelPart(this, 0, 0);
    private final ImmutableList<ModelPart> parts;

    public BonestormModel() {
        this.bonestormHead.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8);
        this.bonestormSticks = new ModelPart[12];

        for (int i = 0; i < this.bonestormSticks.length; ++i) {
            this.bonestormSticks[i] = new ModelPart(this, 0, 16);
            this.bonestormSticks[i].addBox(0.0F, 0.0F, 0.0F, 2, 8, 2);
        }

        ImmutableList.Builder<ModelPart> partsList = ImmutableList.builder();
        partsList.add(this.bonestormHead);
        partsList.addAll(Arrays.asList(this.bonestormSticks));
        this.parts = partsList.build();
    }

    @Override
    @Nonnull
    public Iterable<ModelPart> parts() {
        return this.parts;
    }

    @Override
    public void setupAnim(@Nonnull T bonestorm, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float f = ageInTicks * (float) Math.PI * -0.1F;
        int i;

        for (i = 0; i < 4; ++i) {
            this.bonestormSticks[i].y = -2.0F + Mth.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.bonestormSticks[i].x = Mth.cos(f) * 9.0F;
            this.bonestormSticks[i].z = Mth.sin(f) * 9.0F;
            ++f;
        }

        f = ((float) Math.PI / 4F) + ageInTicks * (float) Math.PI * 0.03F;

        for (i = 4; i < 8; ++i) {
            this.bonestormSticks[i].y = 2.0F + Mth.cos(((float) (i * 2) + ageInTicks) * 0.25F);
            this.bonestormSticks[i].x = Mth.cos(f) * 7.0F;
            this.bonestormSticks[i].z = Mth.sin(f) * 7.0F;
            ++f;
        }

        f = 0.47123894F + ageInTicks * (float) Math.PI * -0.05F;

        for (i = 8; i < 12; ++i) {
            this.bonestormSticks[i].y = 11.0F + Mth.cos(((float) i * 1.5F + ageInTicks) * 0.5F);
            this.bonestormSticks[i].x = Mth.cos(f) * 5.0F;
            this.bonestormSticks[i].z = Mth.sin(f) * 5.0F;
            ++f;
        }

        this.bonestormHead.getYRot() = netHeadYaw / (180F / (float) Math.PI);
        this.bonestormHead.getXRot() = headPitch / (180F / (float) Math.PI);
    }
}