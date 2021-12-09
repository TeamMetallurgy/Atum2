package com.teammetallurgy.atum.client.model.shield;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class BrigandShieldModel extends AbstractShieldModel {
    private final ModelPart shieldCore;
    private final ModelPart handleCore;
    private final ModelPart shieldTop1;
    private final ModelPart shieldTop2;
    private final ModelPart shieldTop3;
    private final ModelPart shieldTop4;
    private final ModelPart shieldBottom1;
    private final ModelPart shieldBottom2;
    private final ModelPart shieldBottom3;
    private final ModelPart shieldBottom4;
    private final ModelPart handleSide1;
    private final ModelPart handleSide2;

    public BrigandShieldModel(ModelPart part) {
        this.shieldCore = part.getChild("shieldCore");
        this.handleCore = part.getChild("handleCore");
        this.shieldTop1 = part.getChild("shieldTop1");
        this.shieldTop2 = part.getChild("shieldTop2");
        this.shieldTop3 = part.getChild("shieldTop3");
        this.shieldTop4 = part.getChild("shieldTop4");
        this.shieldBottom1 = part.getChild("shieldBottom1");
        this.shieldBottom2 = part.getChild("shieldBottom2");
        this.shieldBottom3 = part.getChild("shieldBottom3");
        this.shieldBottom4 = part.getChild("shieldBottom4");
        this.handleSide1 = part.getChild("handleSide1");
        this.handleSide2 = part.getChild("handleSide2");

        /*this.texWidth = 32; //TODO Needs to be re-exported in Blockbench
        this.texHeight = 32;
        this.handleSide1 = new ModelPart(this, 25, 3);
        this.handleSide1.setPos(0.0F, 0.0F, 0.0F);
        this.handleSide1.addBox(-1.0F, 1.0F, 1.0F, 2, 1, 2, 0.0F);
        this.handleSide2 = new ModelPart(this, 25, 3);
        this.handleSide2.setPos(0.0F, 0.0F, 0.0F);
        this.handleSide2.addBox(-1.0F, -2.0F, 1.0F, 2, 1, 2, 0.0F);
        this.shieldTop2 = new ModelPart(this, 0, 6);
        this.shieldTop2.setPos(0.0F, 0.0F, 0.0F);
        this.shieldTop2.addBox(-5.0F, -4.0F, 0.0F, 10, 1, 1, 0.0F);
        this.shieldTop3 = new ModelPart(this, 0, 4);
        this.shieldTop3.setPos(0.0F, 0.0F, 0.0F);
        this.shieldTop3.addBox(-4.0F, -5.0F, 0.0F, 8, 1, 1, 0.0F);
        this.handleCore = new ModelPart(this, 20, 0);
        this.handleCore.setPos(0.0F, 0.0F, 0.0F);
        this.handleCore.addBox(-1.0F, -2.0F, 3.0F, 2, 4, 1, 0.0F);
        this.shieldBottom1 = new ModelPart(this, 0, 16);
        this.shieldBottom1.setPos(0.0F, 0.0F, 0.0F);
        this.shieldBottom1.addBox(-6.0F, 2.0F, 0.0F, 12, 1, 1, 0.0F);
        this.shieldBottom4 = new ModelPart(this, 0, 22);
        this.shieldBottom4.setPos(1.0F, 1.0F, 0.0F);
        this.shieldBottom4.addBox(-4.0F, 4.0F, 0.0F, 6, 1, 1, 0.0F);
        this.shieldBottom2 = new ModelPart(this, 0, 18);
        this.shieldBottom2.setPos(0.0F, 0.0F, 0.0F);
        this.shieldBottom2.addBox(-5.0F, 3.0F, 0.0F, 10, 1, 1, 0.0F);
        this.shieldCore = new ModelPart(this, 0, 10);
        this.shieldCore.setPos(0.0F, 0.0F, 0.0F);
        this.shieldCore.addBox(-6.0F, -2.0F, 0.0F, 12, 4, 1, 0.0F);
        this.shieldBottom3 = new ModelPart(this, 0, 20);
        this.shieldBottom3.setPos(0.0F, 0.0F, 0.0F);
        this.shieldBottom3.addBox(-4.0F, 4.0F, 0.0F, 8, 1, 1, 0.0F);
        this.shieldTop4 = new ModelPart(this, 0, 2);
        this.shieldTop4.setPos(0.0F, 0.0F, 0.0F);
        this.shieldTop4.addBox(-3.0F, -6.0F, 0.0F, 6, 1, 1, 0.0F);
        this.shieldTop1 = new ModelPart(this, 0, 8);
        this.shieldTop1.setPos(0.0F, 0.0F, 0.0F);
        this.shieldTop1.addBox(-6.0F, -3.0F, 0.0F, 12, 1, 1, 0.0F);
        this.handleCore.addChild(this.handleSide1);
        this.handleCore.addChild(this.handleSide2);
        this.shieldCore.addChild(this.shieldTop2);
        this.shieldCore.addChild(this.shieldTop3);
        this.shieldCore.addChild(this.shieldBottom1);
        this.shieldCore.addChild(this.shieldBottom4);
        this.shieldCore.addChild(this.shieldBottom2);
        this.shieldCore.addChild(this.shieldBottom3);
        this.shieldCore.addChild(this.shieldTop4);
        this.shieldCore.addChild(this.shieldTop1);*/
    }

    @Override
    public void renderToBuffer(@Nonnull PoseStack matrixStack, @Nonnull VertexConsumer vertexBuilder, int i, int i1, float v, float v1, float v2, float v3) {
        matrixStack.pushPose();
        matrixStack.scale(1.0F / 0.78F, -1.0F / 0.78F, -1.0F / 0.78F);
        matrixStack.translate(0.0F, 0.0F, -0.025F);
        this.handleCore.render(matrixStack, vertexBuilder, i, i1, v, v1, v2, v3);
        this.shieldCore.render(matrixStack, vertexBuilder, i, i1, v, v1, v2, v3);
        matrixStack.popPose();
    }
}