package com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.util.Direction;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ChestSpawnerTileEntity extends ChestBaseTileEntity {
    private final AbstractSpawner spawnerLogic = new AbstractSpawner() {
        @Override
        public void broadcastEvent(int id) {
            ChestSpawnerTileEntity.this.world.addBlockEvent(ChestSpawnerTileEntity.this.pos, Blocks.SPAWNER, id, 0);
        }

        @Override
        @SuppressWarnings("all")
        public World getWorld() {
            return ChestSpawnerTileEntity.this.world;
        }

        @Override
        @Nonnull
        public BlockPos getSpawnerPosition() {
            return ChestSpawnerTileEntity.this.pos;
        }

        @Override
        public void setNextSpawnData(@Nonnull WeightedSpawnerEntity spawnerEntity) {
            super.setNextSpawnData(spawnerEntity);

            if (this.getWorld() != null) {
                BlockState BlockState = this.getWorld().getBlockState(this.getSpawnerPosition());
                this.getWorld().notifyBlockUpdate(ChestSpawnerTileEntity.this.pos, BlockState, BlockState, 4);
            }
        }

        @Override
        public void tick() {
            if (world != null && isRuinChest) {
                if (!world.isDaytime()) {
                    setEntityType(getNightTime(spawnPool));
                } else {
                    setEntityType(getDayTime(spawnPool));
                }
            }
            super.tick();
        }
    };
    private int spawnPool;
    private boolean isRuinChest;

    public ChestSpawnerTileEntity() {
        super(AtumTileEntities.CHEST_SPAWNER, true, false, AtumBlocks.CHEST_SPAWNER);
        if (this.isRuinChest) {
            spawnPool = MathHelper.nextInt(new Random(), 0, 2);
        }
    }

    private static EntityType<?> getDayTime(int spawnPool) {
        switch (spawnPool) {
            case 1:
                return AtumEntities.BRIGAND;
            case 2:
                return AtumEntities.NOMAD;
            case 0:
            default:
                return AtumEntities.BARBARIAN;
        }
    }

    private static EntityType<?> getNightTime(int spawnPool) {
        switch (spawnPool) {
            case 1:
                return AtumEntities.FORSAKEN;
            case 2:
                return AtumEntities.BONESTORM;
            case 0:
            default:
                return AtumEntities.MUMMY;
        }
    }

    public void setRuinChest() {
        this.isRuinChest = true;
    }

    @Override
    public void tick() {
        this.spawnerLogic.tick();
        super.tick();
    }

    @Override
    public void read(@Nonnull CompoundNBT compound) {
        super.read(compound);
        this.spawnerLogic.read(compound);
        spawnPool = compound.getInt("spawnPool");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        super.write(compound);
        this.spawnerLogic.write(compound);
        compound.putInt("spawnPool", spawnPool);
        return compound;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull PlayerEntity player) {
        double d0 = 4.0D;
        double d1 = 3.0D;
        List<MonsterEntity> list = super.world.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB((double) super.pos.getX() - d0, (double) super.pos.getY() - d1, (double) super.pos.getZ() - d0, (double) super.pos.getX() + d0, (double) super.pos.getY() + d1, (double) super.pos.getZ() + d0));
        if (!list.isEmpty()) {
            if (!super.world.isRemote) {
                player.sendMessage(new TranslationTextComponent("chat.atum.enemies"));
            }
            return false;
        } else {
            return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    @Nullable
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.pos, 1, this.getUpdateTag());
    }

    @Override
    @Nonnull
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = this.write(new CompoundNBT());
        tag.remove("SpawnPotentials");
        return tag;
    }

    @Override
    public boolean receiveClientEvent(int id, int type) {
        if (id == 1) {
            return super.receiveClientEvent(id, type);
        }
        return this.spawnerLogic.setDelayToMin(id);
    }

    @Override
    public boolean onlyOpsCanSetNbt() {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int index, @Nonnull ItemStack stack) {
        return false;
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int index, int count) {
        return ItemStack.EMPTY;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nonnull Direction direction) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.empty();
        } else {
            return super.getCapability(capability, direction);
        }
    }
}