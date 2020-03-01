package com.teammetallurgy.atum.blocks.wood.tileentity.crate;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.wood.CrateBlock;
import com.teammetallurgy.atum.init.AtumTileEntities;
import com.teammetallurgy.atum.inventory.container.block.CrateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class CrateTileEntity extends InventoryBaseTileEntity implements ITickableTileEntity, IChestLid {
    private int ticksSinceSync;
    private int numPlayersUsing;
    public float lidAngle;
    public float prevLidAngle;

    public CrateTileEntity() {
        super(AtumTileEntities.CRATE, 27);
    }

    @Override
    public void tick() {
        int x = this.pos.getX();
        int y = this.pos.getY();
        int z = this.pos.getZ();
        if (world != null) {
            ++this.ticksSinceSync;
            this.numPlayersUsing = calculatePlayersUsingSync(this.world, this, this.ticksSinceSync, x, y, z, this.numPlayersUsing);
            this.prevLidAngle = this.lidAngle;

            if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
                this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
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
                    this.world.playSound(null, (double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D, SoundEvents.BLOCK_WOODEN_TRAPDOOR_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
                }

                if (this.lidAngle < 0.0F) {
                    this.lidAngle = 0.0F;
                }
            }
        }
    }

    public static int calculatePlayersUsingSync(World world, LockableTileEntity te, int ticksSinceSync, int x, int y, int z, int numPlayersUsing) {
        if (!world.isRemote && numPlayersUsing != 0 && (ticksSinceSync + x + y + z) % 200 == 0) {
            numPlayersUsing = calculatePlayersUsing(world, te, x, y, z);
        }
        return numPlayersUsing;
    }

    public static int calculatePlayersUsing(World world, LockableTileEntity te, int x, int y, int z) {
        int amount = 0;
        for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB((float) x - 5.0F, (float) y - 5.0F, (float) z - 5.0F, (float) (x + 1) + 5.0F, ((float) (y + 1) + 5.0F), ((float) (z + 1) + 5.0F)))) {
            if (player.openContainer instanceof CrateContainer) {
                IInventory inventory = ((CrateContainer) player.openContainer).getCrateInventory();
                if (inventory == te) {
                    ++amount;
                }
            }
        }
        return amount;
    }

    @Override
    public void openInventory(@Nonnull PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }

            ++this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockState().getBlock(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockState().getBlock());
        }
    }

    @Override
    public void closeInventory(@Nonnull PlayerEntity player) {
        if (!player.isSpectator() && this.getBlockState().getBlock() instanceof CrateBlock) {
            --this.numPlayersUsing;
            this.world.addBlockEvent(this.pos, this.getBlockState().getBlock(), 1, this.numPlayersUsing);
            this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockState().getBlock());
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public float getLidAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevLidAngle, this.lidAngle);
    }

    @Override
    @Nonnull
    protected Container createMenu(int windowID, @Nonnull PlayerInventory playerInventory) {
        return ChestContainer.createGeneric9X3(windowID, playerInventory, this);
    }
}