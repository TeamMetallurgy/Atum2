package com.teammetallurgy.atum.entity.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowDoubleShot extends CustomArrow {

    public EntityArrowDoubleShot(World world) {
        super(world);
    }

    public EntityArrowDoubleShot(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_double.png");
    }
}