package com.teammetallurgy.atum.entity;

import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.world.World;

public class EntitySluime extends EntitySlime {
    public EntitySluime(World worldIn) {
        super(worldIn);
    }

    @Override
    public void setSlimeSize(int size, boolean resetHealth) {
        super.setSlimeSize(size, resetHealth);
    }
}