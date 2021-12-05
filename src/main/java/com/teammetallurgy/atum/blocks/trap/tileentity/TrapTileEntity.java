package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrapTileEntity extends InventoryBaseTileEntity implements TickableBlockEntity {
    protected int burnTime;
    protected int currentItemBurnTime;
    protected boolean isDisabled = false;
    public boolean isInsidePyramid = true;
    public final ContainerData trapData = new ContainerData() {
        @Override
        public int get(int index) {
            switch (index) {
                case 0:
                    return TrapTileEntity.this.burnTime;
                case 1:
                    return TrapTileEntity.this.currentItemBurnTime;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    TrapTileEntity.this.burnTime = value;
                    break;
                case 1:
                    TrapTileEntity.this.currentItemBurnTime = value;
                    break;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public TrapTileEntity(BlockEntityType<?> tileEntityType) {
        super(tileEntityType, 1);
    }

    public void setDisabledStatus(boolean isDisabled) {
        this.isDisabled = isDisabled;
        if (this.level != null) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    AABB getFacingBoxWithRange(Direction facing, int range) {
        BlockPos pos = getBlockPos();
        Vec3i dir = facing.getNormal();
        return new AABB(pos).expandTowards(dir.getX() * range, dir.getY() * range, dir.getZ() * range);
    }

    @Override
    public void tick() {
        boolean isBurningCheck = this.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;
        Level world = this.level;
        if (world == null) return;

        if (!this.isDisabled && this.isBurning()) {
            BlockState state = world.getBlockState(this.worldPosition);
            if (state.getBlock() instanceof TrapBlock) {
                Direction facing = state.getValue(TrapBlock.FACING);
                Class<? extends LivingEntity> entity;
                if (this.isInsidePyramid) {
                    entity = Player.class;
                } else {
                    entity = LivingEntity.class;
                }
                List<LivingEntity> entities = world.getEntitiesOfClass(entity, getFacingBoxWithRange(facing, 1).deflate(0.05D));
                for (LivingEntity livingBase : entities) {
                    if (livingBase instanceof Player ? !((Player) livingBase).isCreative() : livingBase != null) {
                        canDamageEntity = true;
                        this.triggerTrap(world, facing, livingBase);
                    } else {
                        canDamageEntity = false;
                    }
                }
            }
        }

        if (this.isInsidePyramid) {
            this.burnTime = 1;
        }

        if (this.isBurning() && !this.isDisabled && canDamageEntity && !this.isInsidePyramid) {
            --this.burnTime;
        }

        if (!world.isClientSide && !this.isDisabled) {
            ItemStack fuel = this.inventory.get(0);
            if (this.isBurning() || !fuel.isEmpty()) {
                if (!this.isBurning()) {
                    this.burnTime = ForgeHooks.getBurnTime(fuel) / 10;
                    this.currentItemBurnTime = this.burnTime;
                    if (this.isBurning()) {
                        isBurning = true;
                        if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                        }
                    }
                }
            }
            if (isBurningCheck != this.isBurning()) {
                isBurning = true;
            }
        }
        if (isBurning) {
            this.setChanged();
        }
    }

    protected void triggerTrap(Level world, Direction facing, LivingEntity livingBase) {
    }

    boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return super.stillValid(player) && !this.isInsidePyramid;
    }

    @Override
    public boolean canPlaceItem(int index, @Nonnull ItemStack stack) {
        ItemStack fuel = this.inventory.get(0);
        return !this.isInsidePyramid && (FurnaceBlockEntity.isFuel(stack) || FurnaceFuelSlot.isBucket(stack) && fuel.getItem() != Items.BUCKET);
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
    public void load(@Nonnull BlockState state, @Nonnull CompoundTag compound) {
        super.load(state, compound);
        this.burnTime = compound.getInt("BurnTime");
        this.isDisabled = compound.getBoolean("Disabled");
        this.isInsidePyramid = compound.getBoolean("InPyramid");
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag compound) {
        super.save(compound);
        compound.putInt("BurnTime", (short) this.burnTime);
        compound.putBoolean("Disabled", this.isDisabled);
        compound.putBoolean("InPyramid", this.isInsidePyramid);
        return compound;
    }

    @Override
    @Nonnull
    protected AbstractContainerMenu createMenu(int windowID, @Nonnull Inventory playerInventory) {
        return new TrapContainer(windowID, playerInventory, this);
    }

    @Override
    @Nonnull
    public ItemStack removeItem(int index, int count) {
        return !isInsidePyramid ? super.removeItem(index, count) : ItemStack.EMPTY;
    }

    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction direction) {
        return !this.isInsidePyramid ? super.getCapability(capability, direction) : LazyOptional.empty();
    }
}