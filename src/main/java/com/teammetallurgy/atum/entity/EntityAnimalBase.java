package com.teammetallurgy.atum.entity;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityAnimalBase extends EntityTameable {


    public EntityAnimalBase(World world) {
        super(world);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(@Nonnull EntityAgeable ageable) {
        return null;
    }
}
