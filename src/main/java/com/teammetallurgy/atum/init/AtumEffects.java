package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.effect.MarkedForDeathEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtumEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED = DeferredRegister.create(Registries.MOB_EFFECT, Atum.MOD_ID);
    public static final DeferredHolder<MobEffect, MobEffect> MARKED_FOR_DEATH = register("marked_for_death", new MarkedForDeathEffect());

    public static DeferredHolder<MobEffect, MobEffect> register(String name, MobEffect effect) {
        NeoForge.EVENT_BUS.register(effect);
        return MOB_EFFECT_DEFERRED.register(name, () -> effect);
    }
}