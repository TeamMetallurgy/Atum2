package com.teammetallurgy.atum.blocks.wood.tileentity.crate;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityInventoryBase;
import com.teammetallurgy.atum.blocks.wood.BlockCrate;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nonnull;

public class TileEntityCrate extends TileEntityInventoryBase implements ITickable {
    private int ticksSinceSync;
    public int numPlayersUsing;
    private float lidAngle;

    public TileEntityCrate() {
        super(27);
    }

    @Override
    public void update() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        ++this.ticksSinceSync;

        if (!this.world.isRemote && this.numPlayersUsing != 0 && (this.ticksSinceSync + i + j + k) % 200 == 0) {
            this.numPlayersUsing = 0;

            for (EntityPlayer entityplayer : this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB((double) ((float) i - 5.0F), (double) ((float) j - 5.0F), (double) ((float) k - 5.0F), (double) ((float) (i + 1) + 5.0F), (double) ((float) (j + 1) + 5.0F), (double) ((float) (k + 1) + 5.0F)))) {
                if (entityplayer.openContainer instanceof ContainerCrate) {
                    IInventory iinventory = ((ContainerCrate) entityplayer.openContainer).getCrateInventory();

                    if (iinventory == this) {
                        ++this.numPlayersUsing;
                    }
                }
            }
        }
        float prevLidAngle = this.lidAngle;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d1 = (double) i + 0.5D;
            double d2 = (double) k + 0.5D;

            this.world.playSound(null, d1, (double) j + 0.5D, d2, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            } else {
                this.lidAngle -= 0.1F;
            }
            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            if (this.lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double) i + 0.5D;
                double d0 = (double) k + 0.5D;

                this.world.playSound(null, d3, (double) j + 0.5D, d0, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockCrate) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockType(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), false);
        }
    }

    @Override
    @Nonnull
    public Container createContainer(@Nonnull InventoryPlayer playerInventory, @Nonnull EntityPlayer player) {
        this.fillWithLoot(player);
        return new ContainerCrate(playerInventory, this, player);
    }

    @Override
    @Nonnull
    public String getGuiID() {
        return String.valueOf(new ResourceLocation(Constants.MOD_ID, "crate"));
    }
}