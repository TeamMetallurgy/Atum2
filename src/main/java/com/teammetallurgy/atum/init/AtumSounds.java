package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AtumSounds {
    public static final DeferredRegister<SoundEvent> BLOCK_ENTITY_DEFERRED = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Atum.MOD_ID);

    public static final RegistryObject<SoundEvent> PHARAOH_SPAWN = registerSound("pharaohspawn");

    /**
     * Registers a sound
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static RegistryObject<SoundEvent> registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Atum.MOD_ID, name);
        SoundEvent sound = new SoundEvent(resourceLocation);
        return BLOCK_ENTITY_DEFERRED.register(name, () -> sound);
    }
}