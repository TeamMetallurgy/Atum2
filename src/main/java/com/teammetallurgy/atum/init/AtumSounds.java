package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerSound;

@ObjectHolder(Constants.MOD_ID)
public class AtumSounds {
    public static final SoundEvent PHARAOH_SPAWN = registerSound("pharaohspawn");
}