package com.teammetallurgy.atum.misc.datagenerator;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

import static com.teammetallurgy.atum.misc.StackHelper.getBlockFromName;
import static com.teammetallurgy.atum.misc.StackHelper.getItemFromName;

public class RecipeGenerator extends RecipeProvider {

    public RecipeGenerator(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildShapelessRecipes(@Nonnull Consumer<FinishedRecipe> consumer) {
        for (DyeColor color : DyeColor.values()) {
            String colorName = color.getSerializedName();
            Item dye = ForgeRegistries.ITEMS.getValue(new ResourceLocation(colorName + "_dye"));
            if (dye != null) {
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_crystal_glass"), 8).define('#', AtumBlocks.CRYSTAL_GLASS).define('X', dye).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", this.has(AtumBlocks.CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_crystal_glass_pane"), 16).define('#', getBlockFromName(colorName + "_stained_crystal_glass")).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", this.has(AtumBlocks.CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_crystal_glass_pane"), 8).define('#', AtumBlocks.CRYSTAL_GLASS_PANE).define('$', dye).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", this.has(AtumBlocks.CRYSTAL_GLASS)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_crystal_glass_pane_from_crystal_glass_pane"));
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_palm_framed_crystal_glass"), 8).define('#', AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS).define('X', dye).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", this.has(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_palm_framed_crystal_glass")).define('#', getBlockFromName(colorName + "_stained_crystal_glass")).define('X', AtumItems.PALM_STICK).pattern(" X ").pattern("#X#").pattern(" X ").group("stained_glass").unlockedBy("has_glass", this.has(AtumBlocks.CRYSTAL_GLASS)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_palm_framed_crystal_glass_from_stained_crystal_glass"));
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_palm_framed_crystal_glass_pane"), 16).define('#', getBlockFromName(colorName + "_stained_palm_framed_crystal_glass")).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", this.has(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_palm_framed_crystal_glass_pane"), 8).define('#', AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS).define('$', dye).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", this.has(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_palm_framed_crystal_glass_pane_from_palm_framed_crystal_glass_pane"));
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass"), 8).define('#', AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS).define('X', dye).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", this.has(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass")).define('#', getBlockFromName(colorName + "_stained_crystal_glass")).define('X', AtumItems.DEADWOOD_STICK).pattern(" X ").pattern("#X#").pattern(" X ").group("stained_glass").unlockedBy("has_glass", this.has(AtumBlocks.CRYSTAL_GLASS)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_deadwood_framed_crystal_glass_from_stained_crystal_glass"));
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass_pane"), 16).define('#', getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass")).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", this.has(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName(colorName + "_stained_deadwood_framed_crystal_glass_pane"), 8).define('#', AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS).define('$', dye).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", this.has(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_stained_deadwood_framed_crystal_glass_pane_from_deadwood_framed_crystal_glass_pane"));

                Block ceramic = getBlockFromName("ceramic_" + colorName);
                if (color != DyeColor.WHITE) {
                    ShapelessRecipeBuilder.shapeless(getBlockFromName("linen_" + colorName)).requires(Ingredient.of(dye)).requires(AtumBlocks.LINEN_WHITE).group("wool").unlockedBy("has_white_linen", this.has(AtumBlocks.LINEN_WHITE)).save(consumer);
                    ShapedRecipeBuilder.shaped(getBlockFromName("linen_carpet_" + colorName), 8).define('#', AtumBlocks.LINEN_WHITE).define('$', dye).pattern("###").pattern("#$#").pattern("###").group("carpet").unlockedBy("has_white_linen_carpet", this.has(AtumBlocks.LINEN_CARPET_WHITE)).unlockedBy("has_dye", this.has(dye)).save(consumer, new ResourceLocation(Atum.MOD_ID, colorName + "_linen_carpet_from_white_linen_carpet"));
                    ShapelessRecipeBuilder.shapeless(ceramic).requires(Ingredient.of(dye)).requires(AtumBlocks.CERAMIC_WHITE).unlockedBy("has_white_ceramic", this.has(AtumBlocks.CERAMIC_WHITE)).save(consumer);
                }
                ShapedRecipeBuilder.shaped(getBlockFromName("ceramic_tile_" + colorName), 3).define('#', ceramic).pattern("##").unlockedBy("has_white_ceramic", this.has(AtumBlocks.CERAMIC_WHITE)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName("ceramic_slab_" + colorName), 6).define('#', ceramic).pattern("###").unlockedBy("has_white_ceramic", this.has(AtumBlocks.CERAMIC_WHITE)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName("ceramic_stairs_" + colorName), 4).define('#', ceramic).pattern("#  ").pattern("## ").pattern("###").unlockedBy("has_white_ceramic", this.has(AtumBlocks.CERAMIC_WHITE)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName("ceramic_wall_" + colorName), 6).define('#', ceramic).pattern("###").pattern("###").unlockedBy("has_white_ceramic", this.has(AtumBlocks.CERAMIC_WHITE)).save(consumer);
                ShapedRecipeBuilder.shaped(getBlockFromName("linen_carpet_" + colorName), 3).define('#', getBlockFromName("linen_" + colorName)).pattern("##").group("carpet").unlockedBy("has_white_linen", this.has(AtumBlocks.LINEN_WHITE)).save(consumer);
                //Add tag versions of the following vanilla recipes
                ShapelessRecipeBuilder.shapeless(StackHelper.getBlockFromName(new ResourceLocation(colorName + "_concrete_powder")), 8).requires(dye).requires(Ingredient.of(ItemTags.SAND), 4).requires(Ingredient.of(Tags.Items.GRAVEL), 4).group("concrete_powder").unlockedBy("has_sand", this.has(Tags.Items.SAND)).unlockedBy("has_gravel", this.has(Tags.Items.GRAVEL)).save(consumer);
            }
        }
        for (God god : God.values()) {
            String godName = god.getName();
            ShapedRecipeBuilder.shaped(getBlockFromName("torch_of_" + godName), 4).define('S', getItemFromName(godName + "_godshard")).define('T', AtumBlocks.NEBU_TORCH).pattern(" T ").pattern("TST").pattern(" T ").unlockedBy("has_nebu_torch", this.has(AtumBlocks.NEBU_TORCH)).save(consumer);
            ShapelessRecipeBuilder.shapeless(getBlockFromName("lantern_of_" + godName)).requires(AtumBlocks.NEBU_LANTERN).requires(getItemFromName(godName + "_godshard")).unlockedBy("has_nebu_lantern", this.has(AtumBlocks.NEBU_LANTERN)).save(consumer);
        }
    }
}