package com.teammetallurgy.atum.api.event;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.MinecraftForge;

public class AtumEvents {

    public static boolean onPharaohBeaten(PharaohEntity pharaoh, DamageSource damageSource) {
        return MinecraftForge.EVENT_BUS.post(new PharaohBeatenEvent(pharaoh, damageSource));
    }
}