package com.teammetallurgy.atum.inventory.slot;

import com.teammetallurgy.atum.api.recipe.RecipeHandlers;
import com.teammetallurgy.atum.api.recipe.kiln.IKilnRecipe;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nonnull;

public class SlotKilnOutput extends Slot {
    private final PlayerEntity player;
    private int removeCount;

    public SlotKilnOutput(PlayerEntity player, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
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
    public ItemStack onTake(PlayerEntity thePlayer, @Nonnull ItemStack stack) {
        this.onCrafting(stack);
        super.onTake(thePlayer, stack);
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

        if (!this.player.world.isRemote) {
            int count = this.removeCount;
            float exp = this.getExperience(stack);

            if (exp == 0.0F) {
                count = 0;
            } else if (exp < 1.0F) {
                int expCount = MathHelper.floor((float) count * exp);
                if (expCount < MathHelper.ceil((float) count * exp) && Math.random() < (double) ((float) count * exp - (float) expCount)) {
                    ++expCount;
                }
                count = expCount;
            }
            while (count > 0) {
                int k = EntityXPOrb.getXPSplit(count);
                count -= k;
                this.player.world.addEntity(new EntityXPOrb(this.player.world, this.player.posX, this.player.posY + 0.5D, this.player.posZ + 0.5D, k));
            }
        }
        this.removeCount = 0;
    }

    private float getExperience(@Nonnull ItemStack stack) { //TODO Needs testing
        for (IKilnRecipe kilnRecipe : RecipeHandlers.kilnRecipes) {
            if (StackHelper.areStacksEqualIgnoreSize(stack, kilnRecipe.getOutput())) {
                return kilnRecipe.getExperience();
            }
        }
        return 0.0F;
    }
}