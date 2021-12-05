package com.teammetallurgy.atum.client.model.entity;

import com.teammetallurgy.atum.entity.undead.ForsakenEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ForsakenModel extends MonsterModel<ForsakenEntity> {

    public ForsakenModel() {
        this(0.0F);
    }

    public ForsakenModel(float modelSize) {
        super(modelSize, true);
        this.rightArm = new ModelPart(this, 40, 16);
        this.rightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.rightArm.setPos(-5.0F, 2.0F, 0.0F);
        this.leftArm = new ModelPart(this, 40, 16);
        this.leftArm.mirror = true;
        this.leftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelSize);
        this.leftArm.setPos(5.0F, 2.0F, 0.0F);
        this.rightLeg = new ModelPart(this, 0, 16);
        this.rightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.rightLeg.setPos(-2.0F, 12.0F, 0.0F);
        this.leftLeg = new ModelPart(this, 0, 16);
        this.leftLeg.mirror = true;
        this.leftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelSize);
        this.leftLeg.setPos(2.0F, 12.0F, 0.0F);
    }
}