package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class AtumDamageTypes {
    public static final ResourceKey<DamageType> ASSASSINATED = register("assassinated");
    public static final ResourceKey<DamageType> PHARAOH_ORB = register("pharaoh_orb");

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Atum.MOD_ID, name));
    }
}