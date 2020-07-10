package com.teammetallurgy.atum.misc.datagenerator;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.block.Block;
import net.minecraft.data.*;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static com.teammetallurgy.atum.misc.StackHelper.getBlockFromName;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getName();
            Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(colorName + "_dye"));
            if (dye != null) {
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("crystal_" + colorName + "_stained_glass"), 8).key('#', AtumBlocks.CRYSTAL_GLASS).key('X', dye).patternLine("###").patternLine("#X#").patternLine("###").setGroup("stained_glass").addCriterion("has_glass", this.hasItem(AtumBlocks.CRYSTAL_GLASS)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("framed_" + colorName + "_stained_glass"), 8).key('#', AtumBlocks.FRAMED_GLASS).key('X', dye).patternLine("###").patternLine("#X#").patternLine("###").setGroup("stained_glass").addCriterion("has_glass", this.hasItem(AtumBlocks.FRAMED_GLASS)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("framed_" + colorName + "_stained_glass")).key('#', getBlockFromName("crystal_" + colorName + "_stained_glass")).key('X', Tags.Items.RODS_WOODEN).patternLine(" X ").patternLine("#X#").patternLine(" X ").setGroup("stained_glass").addCriterion("has_glass", this.hasItem(AtumBlocks.CRYSTAL_GLASS)).build(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_framed_glass_from_crystal_stained_glass"));
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("thin_crystal_" + colorName + "_stained_glass"), 16).key('#', getBlockFromName("crystal_" + colorName + "_stained_glass")).patternLine("###").patternLine("###").setGroup("stained_glass_pane").addCriterion("has_glass", this.hasItem(AtumBlocks.CRYSTAL_GLASS)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("thin_crystal_" + colorName + "_stained_glass"), 8).key('#', AtumBlocks.THIN_CRYSTAL_GLASS).key('$', dye).patternLine("###").patternLine("#$#").patternLine("###").setGroup("stained_glass_pane").addCriterion("has_glass_pane", this.hasItem(AtumBlocks.CRYSTAL_GLASS)).build(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_thin_crystal_glass_from_thin_crystal_glass"));
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("thin_framed_" + colorName + "_stained_glass"), 16).key('#', getBlockFromName("framed_" + colorName + "_stained_glass")).patternLine("###").patternLine("###").setGroup("stained_glass_pane").addCriterion("has_glass", this.hasItem(AtumBlocks.FRAMED_GLASS)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("thin_framed_" + colorName + "_stained_glass"), 8).key('#', AtumBlocks.THIN_FRAMED_GLASS).key('$', dye).patternLine("###").patternLine("#$#").patternLine("###").setGroup("stained_glass_pane").addCriterion("has_glass_pane", this.hasItem(AtumBlocks.FRAMED_GLASS)).build(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_thin_framed_glass_from_thin_framed_glass"));

                Block ceramic = getBlockFromName("ceramic_" + colorName);
                if (color != DyeColor.WHITE) {
                    ShapelessRecipeBuilder.shapelessRecipe(getBlockFromName("linen_" + colorName)).addIngredient(Ingredient.fromItems(dye)).addIngredient(AtumBlocks.LINEN_WHITE).setGroup("wool").addCriterion("has_white_linen", this.hasItem(AtumBlocks.LINEN_WHITE)).build(consumer);
                    ShapedRecipeBuilder.shapedRecipe(getBlockFromName("linen_carpet_" + colorName), 8).key('#', AtumBlocks.LINEN_WHITE).key('$', dye).patternLine("###").patternLine("#$#").patternLine("###").setGroup("carpet").addCriterion("has_white_linen_carpet", this.hasItem(AtumBlocks.LINEN_CARPET_WHITE)).addCriterion("has_dye", this.hasItem(dye)).build(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_linen_carpet_from_white_linen_carpet"));
                    ShapelessRecipeBuilder.shapelessRecipe(ceramic).addIngredient(Ingredient.fromItems(dye)).addIngredient(AtumBlocks.CERAMIC_WHITE).addCriterion("has_white_ceramic", this.hasItem(AtumBlocks.CERAMIC_WHITE)).build(consumer);
                }
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("ceramic_tile_" + colorName), 3).key('#', ceramic).patternLine("##").addCriterion("has_white_ceramic", this.hasItem(AtumBlocks.CERAMIC_WHITE)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("ceramic_slab_" + colorName), 6).key('#', ceramic).patternLine("###").addCriterion("has_white_ceramic", this.hasItem(AtumBlocks.CERAMIC_WHITE)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("ceramic_stairs_" + colorName), 4).key('#', ceramic).patternLine("#  ").patternLine("## ").patternLine("###").addCriterion("has_white_ceramic", this.hasItem(AtumBlocks.CERAMIC_WHITE)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("ceramic_wall_" + colorName), 6).key('#', ceramic).patternLine("###").patternLine("###").addCriterion("has_white_ceramic", this.hasItem(AtumBlocks.CERAMIC_WHITE)).build(consumer);
                ShapedRecipeBuilder.shapedRecipe(getBlockFromName("linen_carpet_" + colorName), 3).key('#', getBlockFromName("linen_" + colorName)).patternLine("##").setGroup("carpet").addCriterion("has_white_linen", this.hasItem(AtumBlocks.LINEN_WHITE)).build(consumer);
                //Add tag versions of the following vanilla recipes
                ShapelessRecipeBuilder.shapelessRecipe(StackHelper.getBlockFromName(new ResourceLocation(colorName + "_concrete_powder")), 8).addIngredient(dye).addIngredient(Ingredient.fromTag(ItemTags.SAND), 4).addIngredient(Ingredient.fromTag(Tags.Items.GRAVEL), 4).setGroup("concrete_powder").addCriterion("has_sand", this.hasItem(Tags.Items.SAND)).addCriterion("has_gravel", this.hasItem(Tags.Items.GRAVEL)).build(consumer);
            }
        }
    }
}