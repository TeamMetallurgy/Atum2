package com.teammetallurgy.atum.blocks.machines.tileentity;

import com.teammetallurgy.atum.api.recipe.IAtumRecipeType;
import com.teammetallurgy.atum.api.recipe.recipes.QuernRecipe;
import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.QuernBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.misc.recipe.RecipeHelper;
import com.teammetallurgy.atum.network.NetworkHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;

public class QuernTileEntity extends InventoryBaseTileEntity implements TickableBlockEntity, WorldlyContainer {
    private int currentRotation;
    private int quernRotations;

    public QuernTileEntity() {
        super(AtumTileEntities.QUERN, 1);
    }

    @Override
    public void tick() {
        if (this.level != null && !this.level.isClientSide) {
            if (this.currentRotation >= 360) {
                this.currentRotation = 0;
                this.quernRotations += 1;
            }

            if (this.getItem(0).isEmpty()) {
                this.quernRotations = 0;
            }

            if (this.quernRotations > 0) {
                if (this.level instanceof ServerLevel) {
                    ServerLevel serverWorld = (ServerLevel) level;
                    Collection<QuernRecipe> recipes = RecipeHelper.getRecipes(serverWorld.getRecipeManager(), IAtumRecipeType.QUERN);
                    for (QuernRecipe quernRecipe : recipes) {
                        for (Ingredient ingredient : quernRecipe.getIngredients()) {
                            if (StackHelper.areIngredientsEqualIgnoreSize(ingredient, this.getItem(0)) && quernRecipe.getRotations() == this.quernRotations) {
                                this.removeItem(0, 1);
                                this.outputItems(quernRecipe.assemble(this), this.level, this.getBlockPos());
                                this.quernRotations = 0;
                                this.setChanged();
                            }
                        }
                    }
                }
            }
        }
    }

    private void outputItems(@Nonnull ItemStack stack, Level world, BlockPos pos) {
        Direction facing = world.getBlockState(pos).getValue(QuernBlock.FACING).getOpposite();
        BlockEntity tileEntity = world.getBlockEntity(pos.relative(facing));
        if (tileEntity instanceof WorldlyContainer && ((WorldlyContainer) tileEntity).getSlotsForFace(facing).length > 0 || tileEntity instanceof Container && ((Container) tileEntity).getContainerSize() > 0) {
            Container inventory = ((Container) tileEntity);
            stack = HopperBlockEntity.addItem(this, inventory, stack, facing);
        } else if (tileEntity != null) {
            LazyOptional<IItemHandler> capability = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
            if (capability.isPresent()) {
                IItemHandler itemHandler = capability.orElse(null);
                if (itemHandler != null) {
                    stack = ItemHandlerHelper.insertItem(itemHandler, stack, false);
                }
            }
        }

        if (!stack.isEmpty()) {
            StackHelper.spawnItemStack(world, (double) facing.getStepX() + pos.getX() + 0.5D, (double) pos.getY() + 0.15D, (double) facing.getStepZ() + pos.getZ() + 0.5, stack);
            if (world.isClientSide) {
                world.playLocalSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.CHICKEN_EGG, SoundSource.BLOCKS, 1.0F, 0.4F, false);
            }
        }
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        return RecipeHelper.isItemValidForSlot(this.level, stack, IAtumRecipeType.QUERN);
    }

    public int getRotations() {
        return this.currentRotation;
    }

    public void setRotations(int rotations) {
        this.currentRotation = rotations;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.currentRotation = compound.getInt("currentRotation");
        this.quernRotations = compound.getInt("quernRotations");
    }

    @Nonnull
    @Override
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        compound.putInt("currentRotation", this.currentRotation);
        compound.putInt("quernRotations", this.quernRotations);
        return compound;
    }

    @Override
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return null;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, 0, this.getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        this.load(this.getBlockState(), packet.getTag());
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level instanceof ServerLevel) {
            final Packet<?> packet = this.getUpdatePacket();
            NetworkHandler.sendToTracking((ServerLevel) this.level, this.worldPosition, packet, false);
        }
    }

    @Override
    @Nonnull
    public int[] getSlotsForFace(@Nonnull Direction side) {
        return new int[0];
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @Nonnull ItemStack stack, Direction facing) {
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @Nonnull ItemStack stack, @Nonnull Direction facing) {
        return false;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.empty();
        } else {
            return super.getCapability(capability, direction);
        }
    }
}