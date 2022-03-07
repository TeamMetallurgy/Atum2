package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

public class AtumHelper {

    /**
     * Used to register a new registry
     *
     * @param registryName the unique string to register the registry as
     * @param type         the class that the registry is for
     * @return a new registry
     */
    public static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistryNoCreate(String registryName, Class<T> type) {
        return new RegistryBuilder<T>().setName(new ResourceLocation(Atum.MOD_ID, registryName)).setType(type).setMaxID(Integer.MAX_VALUE >> 5).allowModification();
    }

    public static <T extends IForgeRegistryEntry<T>> IForgeRegistry<T> makeRegistry(String registryName, Class<T> type) {
        return makeRegistryNoCreate(registryName, type).create();
    }
}
