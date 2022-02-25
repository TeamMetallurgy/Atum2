package com.teammetallurgy.atum.blocks.trap.tileentity;

import com.teammetallurgy.atum.blocks.base.tileentity.InventoryBaseTileEntity;
import com.teammetallurgy.atum.blocks.trap.TrapBlock;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceFuelSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TrapTileEntity extends InventoryBaseTileEntity {
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

    public TrapTileEntity(BlockEntityType<?> tileEntityType, BlockPos pos, BlockState state) {
        super(tileEntityType, pos, state, 1);
    }

    public void setDisabledStatus(boolean isDisabled) {
        this.isDisabled = isDisabled;
        if (this.level != null) {
            BlockState state = this.level.getBlockState(this.worldPosition);
            this.level.sendBlockUpdated(this.worldPosition, state, state, 3);
        }
    }

    public static AABB getFacingBoxWithRange(BlockPos pos, Direction facing, int range) {
        Vec3i dir = facing.getNormal();
        return new AABB(pos).expandTowards(dir.getX() * range, dir.getY() * range, dir.getZ() * range);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TrapTileEntity trap) {
        boolean isBurningCheck = trap.isBurning();
        boolean isBurning = false;
        boolean canDamageEntity = false;

        if (!trap.isDisabled && trap.isBurning()) {
            if (state.getBlock() instanceof TrapBlock) {
                Direction facing = state.getValue(TrapBlock.FACING);
                Class<? extends LivingEntity> entity;
                if (trap.isInsidePyramid) {
                    entity = Player.class;
                } else {
                    entity = LivingEntity.class;
                }
                List<? extends LivingEntity> entities = level.getEntitiesOfClass(entity, getFacingBoxWithRange(pos, facing, 1).deflate(0.05D));
                for (LivingEntity livingBase : entities) {
                    if (livingBase instanceof Player ? !((Player) livingBase).isCreative() : livingBase != null) {
                        canDamageEntity = true;
                        if (level instanceof ServerLevel serverLevel) {
                            trap.triggerTrap(serverLevel, facing, livingBase);
                        }
                    } else {
                        canDamageEntity = false;
                    }
                }
            }
        }

        if (trap.isInsidePyramid) {
            trap.burnTime = 1;
        }

        if (trap.isBurning() && !trap.isDisabled && canDamageEntity && !trap.isInsidePyramid) {
            --trap.burnTime;
        }

        if (!level.isClientSide && !trap.isDisabled) {
            ItemStack fuel = trap.inventory.get(0);
            if (trap.isBurning() || !fuel.isEmpty()) {
                if (!trap.isBurning()) {
                    trap.burnTime = ForgeHooks.getBurnTime(fuel, RecipeType.SMELTING) / 10;
                    trap.currentItemBurnTime = trap.burnTime;
                    if (trap.isBurning()) {
                        isBurning = true;
                        if (!fuel.isEmpty()) {
                            fuel.shrink(1);
                        }
                    }
                }
            }
            if (isBurningCheck != trap.isBurning()) {
                isBurning = true;
            }
        }
        if (isBurning) {
            trap.setChanged();
        }
    }

    protected void triggerTrap(ServerLevel serverLevel, Direction facing, LivingEntity livingBase) {
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
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        super.onDataPacket(manager, packet);
        if (packet.getTag() != null) {
            this.load(packet.getTag());
            this.setChanged();
        }
    }

    @Override
    @Nonnull
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        this.burnTime = tag.getInt("BurnTime");
        this.isDisabled = tag.getBoolean("Disabled");
        this.isInsidePyramid = tag.getBoolean("InPyramid");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("BurnTime", (short) this.burnTime);
        tag.putBoolean("Disabled", this.isDisabled);
        tag.putBoolean("InPyramid", this.isInsidePyramid);
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