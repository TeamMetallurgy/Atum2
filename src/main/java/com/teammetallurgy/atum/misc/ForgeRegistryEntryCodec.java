package com.teammetallurgy.atum.misc;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Map;

/**
 * Thanks to Gigaherz & Commoble, for creating this!
 */
public class ForgeRegistryEntryCodec {
    private static final Map<IForgeRegistry<?>, Codec<?>> CACHE = Maps.newConcurrentMap();

    @SuppressWarnings("unchecked")
    public static <T extends IForgeRegistryEntry<T>> Codec<T> getOrCreate(IForgeRegistry<T> registry) {
        return (Codec<T>) CACHE.computeIfAbsent(registry, ForgeRegistryEntryCodec::create);
    }

    private static <T extends IForgeRegistryEntry<T>> Codec<T> create(IForgeRegistry<T> registry) {
        // comapFlatMap converts a Codec<A> to a Codec<B>
        // when A may not be convertible to B, but B is always convertible to A.
        // The B will be serialized in the same format the A would be.
        return ResourceLocation.CODEC.comapFlatMap(
                id -> registry.containsKey(id) ? DataResult.success(registry.getValue(id)) : DataResult.error("Unknown registry id: " + id.toString()),
                IForgeRegistryEntry::getRegistryName);
    }
}