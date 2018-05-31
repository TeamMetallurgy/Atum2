package com.teammetallurgy.atum.integration;

import com.teammetallurgy.atum.utils.AtumRegistry;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIIntegration implements IModPlugin {

    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        for (ItemStack stack : AtumRegistry.HIDE_LIST) {
            blacklist.addIngredientToBlacklist(stack);
        }
    }
}