package com.teammetallurgy.atum.blocks.wood.tileentity.crate;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.CrateContainer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nonnull;

public class CrateTileEntity extends InventoryBaseTileEntity implements TickableBlockEntity {
    private int ticksSinceSync;
    private int numPlayersUsing;
    public float lidAngle;
    public float prevLidAngle;

    public CrateTileEntity() {
        super(AtumTileEntities.CRATE, 27);
    }

    @Override
    public void tick() {
        if (this.level != null) {
            int x = this.worldPosition.getX();
            int y = this.worldPosition.getY();
            int z = this.worldPosition.getZ();
            ++this.ticksSinceSync;
            this.numPlayersUsing = calculatePlayersUsingSync(this.level, this, this.ticksSinceSync, x, y, z, this.numPlayersUsing);
            this.prevLidAngle = this.lidAngle;

            if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
                this.level.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.WOODEN_TRAPDOOR_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
                float lidAngleCached = this.lidAngle;

                if (this.numPlayersUsing > 0) {
                    this.lidAngle += 0.1F;
                } else {
                    this.lidAngle -= 0.1F;
                }
                if (this.lidAngle > 1.0F) {
                    this.lidAngle = 1.0F;
                }

                if (this.lidAngle < 0.5F && lidAngleCached >= 0.5F) {
                    this.level.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.WOODEN_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
                }

                if (this.lidAngle < 0.0F) {
                    this.lidAngle = 0.0F;
                }
            }
        }
    }

    public static int calculatePlayersUsingSync(Level world, BaseContainerBlockEntity te, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
        if (!world.isClientSide && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing(world, te, x, y, z);
        }
        return numPlayersUsing;
    }

    public static int calculatePlayersUsing(Level world, BaseContainerBlockEntity te, int x, int y, int z) {
        int amount = 0;
        for (Player player : world.getEntitiesOfClass(Player.class, new AABB((float) x - 5.0F, (float) y - 5.0F, (float) z - 5.0F, (float) (x + 1) + 5.0F, ((float) (y + 1) + 5.0F), ((float) (z + 1) + 5.0F)))) {
            if (player.containerMenu instanceof CrateContainer) {
                Container inventory = ((CrateContainer) player.containerMenu).getCrateInventory();
                if (inventory == te) {
                    ++amount;
                }
            }
        }
        return amount;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 1) {
            this.numPlayersUsing = type;
            return true;
        } else {
            return super.triggerEvent(id, type);
        }
    }

    @Override
    public void startOpen(@Nonnull Player player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    @Override
    public void stopOpen(@Nonnull Player player) {
        if (!player.isSpectator()) {
            --this.numPlayersUsing;
            this.onOpenOrClose();
        }
    }

    protected void onOpenOrClose() {
        Block block = this.getBlockState().getBlock();
        if (this.level != null && block instanceof CrateBlock) {
            this.level.blockEvent(this.worldPosition, block, 1, this.numPlayersUsing);
            this.level.updateNeighborsAt(this.worldPosition, block);
        }
    }

    public float getLidAngle(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return ChestMenu.threeRows(windowID, playerInventory, this);
    }
}