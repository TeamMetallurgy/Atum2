package com.teammetallurgy.atum.inventory.container.slot;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class KilnOutputSlot extends Slot {
    private final PlayerEntity player;
    private int removeCount;

    public KilnOutputSlot(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    @Nonnull
    public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(player, stack);
        return stack;
    }

    @Override
    protected void onCrafting(@Nonnull ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(@Nonnull ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        World world = player.world;

        if (!world.isRemote() && world instanceof ServerWorld) {
            this.spawnAllOrbs((ServerWorld) world, stack, this.removeCount);
        }
        this.removeCount = 0;
    }

    private void spawnAllOrbs(ServerWorld serverWorld, @Nonnull ItemStack stack, int removeCount) {
        List<KilnRecipe> recipes = new ArrayList<>(RecipeHelper.getRecipes(serverWorld.getRecipeManager(), IAtumRecipeType.KILN));
        recipes.addAll(RecipeHelper.getKilnRecipesFromFurnace(serverWorld.getRecipeManager()));
        for (KilnRecipe kilnRecipe : recipes) {
            if (StackHelper.areStacksEqualIgnoreSize(stack, kilnRecipe.getRecipeOutput())) {
                this.spawnExpOrbs(player, removeCount, kilnRecipe.getExperience());
            }
        }
    }

    private void spawnExpOrbs(PlayerEntity player, int count, float experience) {
        if (experience == 0.0F) {
            count = 0;
        } else if (experience < 1.0F) {
            int expCount = MathHelper.floor((float) count * experience);
            if (expCount < MathHelper.ceil((float) count * experience) && Math.random() < (double) ((float) count * experience - (float) expCount)) {
                ++expCount;
            }

            count = expCount;
        }
        while (count > 0) {
            int xpSplit = ExperienceOrbEntity.getXPSplit(count);
            count -= xpSplit;
            player.world.addEntity(new ExperienceOrbEntity(player.world, player.getPosX(), player.getPosY() + 0.5D, player.getPosZ() + 0.5D, xpSplit));
        }
    }
}