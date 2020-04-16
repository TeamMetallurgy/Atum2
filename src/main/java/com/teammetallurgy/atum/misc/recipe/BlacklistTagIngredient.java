package com.teammetallurgy.atum.misc.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.Tag;

import java.util.function.Predicate;
import java.util.stream.Stream;

public class BlacklistTagIngredient extends Ingredient {

    public BlacklistTagIngredient(Tag<Item> tag, Predicate<ItemStack> blacklist) {
        super(Stream.of(new Ingredient.TagList(tag)).filter(p -> !p.getStacks().stream().allMatch(blacklist)));
    }
}