package com.teammetallurgy.atum.api.event;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class PharaohBeatenEvent extends LivingDeathEvent {
    private final PharaohEntity pharaoh;

    public PharaohBeatenEvent(PharaohEntity pharaoh, DamageSource damageSource) {
        super(pharaoh, damageSource);
        this.pharaoh = pharaoh;
    }

    public PharaohEntity getPharaoh() {
        return this.pharaoh;
    }
}