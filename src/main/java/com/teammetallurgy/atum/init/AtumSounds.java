package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerSound;

@GameRegistry.ObjectHolder(Constants.MOD_ID)
public class AtumSounds {
    public static final SoundEvent PHARAOH_SPAWN = registerSound("pharaohspawn");
}