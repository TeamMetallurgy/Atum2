package com.teammetallurgy.atum.api.recipe;

import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IQuernRecipe extends IForgeRegistryEntry<IQuernRecipe> {

    /**
     * @return The amount of rotations the quern have to rotate, to quern something.
     */
    int getRotations();
}