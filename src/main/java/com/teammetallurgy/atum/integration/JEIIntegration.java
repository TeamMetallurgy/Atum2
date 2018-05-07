package com.teammetallurgy.atum.integration;

import com.teammetallurgy.atum.init.AtumBlocks;
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
        blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.DATE_BLOCK));
        blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.FLAX));
        blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.PAPYRUS));
        //blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.PORTAL));
        blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.LIMESTONE_FURNACE_LIT));
        blacklist.addIngredientToBlacklist(new ItemStack(AtumBlocks.LIT_REDSTONE_ORE));
    }
}