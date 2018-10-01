package com.teammetallurgy.atum.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreIngredient;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class BlacklistOreIngredient extends OreIngredient {
    private final Predicate<ItemStack> blacklist;

    public BlacklistOreIngredient(String ore, Predicate<ItemStack> blacklist) {
        super(ore);
        this.blacklist = blacklist;
    }

    @Override
    public boolean apply(@Nullable ItemStack input) {
        return super.apply(input) && !this.blacklist.test(input);
    }
}
