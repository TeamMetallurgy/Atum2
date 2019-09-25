package com.teammetallurgy.atum.entity.projectile.arrow;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ArrowQuickdrawEntity extends CustomArrow {

    public ArrowQuickdrawEntity(EntityType<? extends ArrowQuickdrawEntity> entityType, World world) {
        super(entityType, world);
    }

    public ArrowQuickdrawEntity(World world, LivingEntity shooter) {
        super(world, shooter);
    }

    @Override
    public ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/arrow/arrow_quickdraw.png");
    }
}