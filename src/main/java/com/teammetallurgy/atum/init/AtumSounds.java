package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.misc.AtumRegistry.registerSound;

@ObjectHolder(Atum.MOD_ID)
public class AtumSounds {
    public static final SoundEvent PHARAOH_SPAWN = registerSound("pharaohspawn");
}