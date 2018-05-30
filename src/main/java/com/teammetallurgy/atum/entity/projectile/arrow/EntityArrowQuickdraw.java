package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class EntityArrowQuickdraw extends CustomArrow {

    public EntityArrowQuickdraw(World world) {
        super(world);
    }

    public EntityArrowQuickdraw(World world, EntityLivingBase shooter) {
        super(world, shooter);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_quickdraw.png");
    }
}