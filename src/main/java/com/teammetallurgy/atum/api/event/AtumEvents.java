package com.teammetallurgy.atum.api.event;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.common.NeoForge;

public class AtumEvents {

    public static PharaohBeatenEvent onPharaohBeaten(PharaohEntity pharaoh, DamageSource damageSource) {
        return NeoForge.EVENT_BUS.post(new PharaohBeatenEvent(pharaoh, damageSource));
    }
}