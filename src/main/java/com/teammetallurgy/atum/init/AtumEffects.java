package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.effect.MarkedForDeathEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AtumEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECT_DEFERRED = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Atum.MOD_ID);
    public static final RegistryObject<MobEffect> MARKED_FOR_DEATH = register("marked_for_death", new MarkedForDeathEffect()); //TODO Fix, crashes

    public static RegistryObject<MobEffect> register(String name, MobEffect effect) {
        MinecraftForge.EVENT_BUS.register(effect);
        return MOB_EFFECT_DEFERRED.register(name, () -> effect);
    }
}