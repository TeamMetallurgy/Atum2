package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.KilnRecipe;
import com.teammetallurgy.atum.blocks.machines.KilnBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SpongeBlock;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.Tags.Items.*;

public class KilnTileEntity extends KilnBaseTileEntity implements ITickableTileEntity {
    public int burnTime;
    public int recipesUsed;
    public int cookTime;
    public int cookTimeTotal;
    public final IIntArray kilnData = new IIntArray() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return KilnTileEntity.this.burnTime;
                case 1:
                    return KilnTileEntity.this.recipesUsed;
                case 2:
                    return KilnTileEntity.this.cookTime;
                case 3:
                    return KilnTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    KilnTileEntity.this.burnTime = value;
                    break;
                case 1:
                    KilnTileEntity.this.recipesUsed = value;
                    break;
                case 2:
                    KilnTileEntity.this.cookTime = value;
                    break;
                case 3:
                    KilnTileEntity.this.cookTimeTotal = value;
            }

        }

        @Override
        public int size() {
            return 4;
        }
    };

    public KilnTileEntity() {
        super(AtumTileEntities.KILN);
    }

    @Override
    public void tick() {
        if (!isPrimary()) {
            return;
        }

        //System.out.println("COOK TIME: " + this.cookTime);
        //.println("COOK TIME TOTAL: " + cookTimeTotal);

        boolean isBurning = this.isBurning();
        boolean markDirty = false;

        if (this.isBurning()) {
            --this.burnTime;
        }

        if (this.world != null && !this.world.isRemote) {
            ItemStack fuelStack = this.inventory.get(4);

            if (this.isBurning() || !fuelStack.isEmpty() && !this.getInputs().isEmpty()) {
                boolean canSmeltAny = false;
                for (int i = 0; i <= 4; i++) {
                    canSmeltAny |= this.canSmelt(i, 5, 8) != -1;
                }

                if (!this.isBurning() && canSmeltAny) {
                    this.burnTime = ForgeHooks.getBurnTime(fuelStack);
                    this.recipesUsed = burnTime;

                    if (this.isBurning()) {
                        markDirty = true;
                        if (!fuelStack.isEmpty()) {
                            Item fuelItemCached = fuelStack.getItem();
                            fuelStack.shrink(1);

                            if (fuelStack.isEmpty()) {
                                ItemStack containerStack = fuelItemCached.getContainerItem(fuelStack);
                                this.inventory.set(4, containerStack);
                            }
                        }
                    }
                }

                if (this.isBurning() && canSmeltAny) {
                    ++this.cookTime;
                    if (this.cookTime == this.cookTimeTotal) {
                        this.cookTime = 0;
                        this.cookTimeTotal = 0;
                        if (!this.isInputEmpty()) {
                            this.cookTimeTotal = this.getCookTime();
                        }
                        for (int i = 0; i <= 4; i++) {
                            this.smeltItem(i, 5, 8);
                        }
                        markDirty = true;
                    }
                } else {
                    this.cookTime = 0;
                }
            } else if ((!this.isBurning() && this.cookTime > 0) || this.isInputEmpty()) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (isBurning != this.isBurning()) {
                markDirty = true;
                world.setBlockState(pos, world.getBlockState(pos).with(KilnBlock.LIT, this.isBurning()));
                BlockPos secondaryKilnPos = KilnBlock.getSecondaryKilnFromPrimary(world, pos);
                if (secondaryKilnPos != null) {
                    BlockState secondaryState = world.getBlockState(secondaryKilnPos);
                    if (secondaryState.getBlock() == AtumBlocks.KILN) {
                        world.setBlockState(secondaryKilnPos, secondaryState.with(KilnBlock.LIT, this.isBurning()));
                    }
                }
            }
        }

        if (markDirty) {
            this.markDirty();
        }
    }

    private boolean isInputEmpty() {
        for (int i = 0; i <= 4; i++) {
            if (!this.inventory.get(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                primary.setInventorySlotContents(index, stack);
            }
            return;
        }

        ItemStack slotStack = this.inventory.get(index);
        boolean isValid = !stack.isEmpty() && stack.isItemEqual(slotStack) && ItemStack.areItemStackTagsEqual(stack, slotStack);

        this.inventory.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index <= 3 && !isValid) {
            this.cookTimeTotal = this.getCookTime();
            this.markDirty();
        }
    }

    public boolean isBurning() {
        if (!isPrimary()) {
            KilnBaseTileEntity primary = getPrimary();
            if (primary != null) {
                return ((KilnTileEntity) primary).isBurning();
            }
        }
        return this.burnTime > 0;
    }

    private int canSmelt(int inputSlot, int outputSlotStart, int outputSlotEnd) {
        if (this.inventory.get(inputSlot).isEmpty()) {
            return -1;
        } else {
            ItemStack result = this.getSmeltingResult(this.inventory.get(inputSlot));

            if (result.isEmpty()) {
                return -1;
            } else {
                for (int outputSlot = outputSlotStart; outputSlot <= outputSlotEnd; outputSlot++) {
                    ItemStack output = this.inventory.get(outputSlot);

                    if (output.isEmpty()) {
                        return outputSlot;
                    } else if (!output.isItemEqual(result)) {
                        continue;
                    } else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit() && output.getCount() + result.getCount() <= output.getMaxStackSize()) {
                        return outputSlot;
                    } else {
                        if (output.getCount() + result.getCount() <= result.getMaxStackSize())
                            return outputSlot;
                    }
                }
            }
        }
        return -1;
    }

    private void smeltItem(int inputSlot, int outputSlotStart, int outputSlotEnd) {
        int outputSlot = this.canSmelt(inputSlot, outputSlotStart, outputSlotEnd);
        if (outputSlot != -1) {
            ItemStack input = this.inventory.get(inputSlot);
            ItemStack result = this.getSmeltingResult(input);
            ItemStack output = this.inventory.get(outputSlot);

            if (output.isEmpty()) {
                this.inventory.set(outputSlot, result);
            } else if (output.getItem() == result.getItem()) {
                output.grow(result.getCount());
            }
            input.shrink(1);
        }
    }

    @Nonnull
    private ItemStack getSmeltingResult(@Nonnull ItemStack input) {
        if (this.world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            RecipeManager recipeManager = serverWorld.getRecipeManager();
            List<KilnRecipe> recipes = new ArrayList<>(RecipeHelper.getRecipes(recipeManager, IAtumRecipeType.KILN));
            recipes.addAll(RecipeHelper.getKilnRecipesFromFurnace(recipeManager));
            for (KilnRecipe kilnRecipe : recipes) {
                for (Ingredient ingredient : kilnRecipe.getIngredients()) {
                    if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, input)) {
                        return kilnRecipe.getCraftingResult(this);
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private List<ItemStack> getInputs() {
        return Arrays.asList(this.inventory.get(0), this.inventory.get(1), this.inventory.get(2), this.inventory.get(3));
    }

    protected int getCookTime() {
        return this.world.getRecipeManager().getRecipe((IRecipeType<? extends KilnRecipe>) IAtumRecipeType.KILN, this, this.world).map(KilnRecipe::getCookTime).orElse(200);
    }

    @Override
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) {
        return new KilnContainer(windowID, playerInventory, this.pos);
    }

    public static boolean canKilnNotSmelt(Ingredient ingredient) {
        for (ItemStack stack : ingredient.getMatchingStacks()) {
            return canKilnNotSmelt(stack);
        }
        return true;
    }

    public static boolean canKilnNotSmelt(ItemStack stack) {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(stack.getItem());

        return IAtumRecipeType.kilnBlacklist.contains(item.getRegistryName()) || IAtumRecipeType.kilnBlacklist.contains(block.getRegistryName()) ||
                item.isFood() || block instanceof OreBlock || item.isIn(ItemTags.COALS) || item.isIn(ORES_COAL) || item.isIn(STORAGE_BLOCKS_COAL) ||
                item.isIn(ItemTags.PLANKS) || item.isIn(ItemTags.LOGS) || item.isIn(RODS_WOODEN) || item.isIn(ItemTags.SMALL_FLOWERS) ||
                item.isIn(ORES) || item.isIn(INGOTS) && !item.isIn(INGOTS_BRICK) || item.isIn(NUGGETS) || item.isIn(GEMS) || item.isIn(DUSTS) ||
                item.isIn(DYES) || item.isIn(SLIMEBALLS) || item.isIn(LEATHER) || block instanceof SpongeBlock;
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.recipesUsed = ForgeHooks.getBurnTime(this.inventory.get(4));
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        return compound;
    }
}