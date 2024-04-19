package com.teammetallurgy.atum.inventory.container.slot;

import com.teammetallurgy.atum.api.recipe.AtumRecipeTypes;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class KilnOutputSlot extends Slot {
    private final Player player;
    private int removeCount;

    public KilnOutputSlot(Player player, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(amount, this.getItem().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(player, stack);
    }

    @Override
    protected void onQuickCraft(@Nonnull ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(@Nonnull ItemStack stack) {
        stack.onCraftedBy(this.player.level(), this.player, this.removeCount);
        Level level = player.level();

        if (!level.isClientSide() && level instanceof ServerLevel) {
            this.spawnAllOrbs((ServerLevel) level, stack, this.removeCount);
        }
        this.removeCount = 0;
    }

    private void spawnAllOrbs(ServerLevel serverLevel, @Nonnull ItemStack stack, int removeCount) {
        List<RecipeHolder<KilnRecipe>> recipeHolders = RecipeHelper.getRecipes(serverLevel.getRecipeManager(), AtumRecipeTypes.KILN.get());
        List<KilnRecipe> recipes = new ArrayList<>();
        recipeHolders.forEach(r -> recipes.add(r.value()));
        recipes.addAll(RecipeHelper.getKilnRecipesFromFurnace(serverLevel.getRecipeManager(), serverLevel));
        for (KilnRecipe kilnRecipe : recipes) {
            if (StackHelper.areStacksEqualIgnoreSize(stack, kilnRecipe.getResultItem(serverLevel.registryAccess()))) {
                this.spawnExpOrbs(player, removeCount, kilnRecipe.getExperience());
            }
        }
    }

    private void spawnExpOrbs(Player player, int count, float experience) {
        if (experience == 0.0F) {
            count = 0;
        } else if (experience < 1.0F) {
            int expCount = Mth.floor((float) count * experience);
            if (expCount < Mth.ceil((float) count * experience) && Math.random() < (double) ((float) count * experience - (float) expCount)) {
                ++expCount;
            }

            count = expCount;
        }
        while (count > 0) {
            int xpSplit = ExperienceOrb.getExperienceValue(count);
            count -= xpSplit;
            player.level().addFreshEntity(new ExperienceOrb(player.level(), player.getX(), player.getY() + 0.5D, player.getZ() + 0.5D, xpSplit));
        }
    }
}