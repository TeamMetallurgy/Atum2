package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowDoubleShotWhite extends CustomArrow {

    public EntityArrowDoubleShotWhite(World world) {
        super(world);
    }

    public EntityArrowDoubleShotWhite(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_double_white.png");
    }
}