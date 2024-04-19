package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtumSounds {
    public static final DeferredRegister<SoundEvent> SOUND_DEFERRED = DeferredRegister.create(Registries.SOUND_EVENT, Atum.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> PHARAOH_SPAWN = registerSound("pharaohspawn");

    /**
     * Registers a sound
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Atum.MOD_ID, name);
        SoundEvent sound = SoundEvent.createVariableRangeEvent(resourceLocation);
        return SOUND_DEFERRED.register(name, () -> sound);
    }
}