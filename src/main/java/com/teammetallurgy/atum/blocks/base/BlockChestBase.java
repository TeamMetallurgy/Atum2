package com.teammetallurgy.atum.blocks.base;

import com.teammetallurgy.atum.blocks.base.tileentity.TileEntityChestBase;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class BlockChestBase extends BlockChest {
    public static final Type ATUM_CHEST_TYPE = EnumHelper.addEnum(Type.class, String.valueOf(new ResourceLocation(Constants.MOD_ID, "chest")), new Class[0]);

    public BlockChestBase() {
        super(Objects.requireNonNull(ATUM_CHEST_TYPE));
        this.setSoundType(SoundType.STONE);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        drops.add(new ItemStack(AtumBlocks.LIMESTONE_CHEST));
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return new ItemStack(AtumBlocks.LIMESTONE_CHEST);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity instanceof TileEntityChestBase && !((TileEntityChestBase) tileEntity).canBeDouble ? NOT_CONNECTED_AABB : super.getBoundingBox(state, world, pos);
    }

    @Override
    public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity tileEntity, @Nonnull ItemStack stack) {
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
        world.setBlockToAir(pos);

        if (tileEntity instanceof TileEntityChestBase) {
            TileEntityChestBase chestBase = (TileEntityChestBase) tileEntity;
            if (chestBase.canBeDouble && !chestBase.canBeSingle) {
                for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
                    if (world.getBlockState(pos.offset(horizontal)).getBlock() == this) {
                        this.breakDoubleChest(world, pos.offset(horizontal));
                    }
                }
            }
            chestBase.invalidate();
        }
    }

    private void breakDoubleChest(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof IInventory) {
            InventoryHelper.dropInventoryItems(world, pos, (IInventory) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        world.setBlockToAir(pos);
    }

    @Override
    public void onBlockAdded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, @Nonnull ItemStack stack) {
        EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 3).getOpposite();
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityChestBase) {
            TileEntityChestBase chestBase = (TileEntityChestBase) tileEntity;
            state = state.withProperty(FACING, facing);
            BlockPos posNorth = pos.north();
            BlockPos posSouth = pos.south();
            BlockPos posWest = pos.west();
            BlockPos posEast = pos.east();

            boolean canBeDouble = chestBase.canBeDouble;
            boolean isNorth = this == world.getBlockState(posNorth).getBlock() && canBeDouble;
            boolean isSouth = this == world.getBlockState(posSouth).getBlock() && canBeDouble;
            boolean isWest = this == world.getBlockState(posWest).getBlock() && canBeDouble;
            boolean isEast = this == world.getBlockState(posEast).getBlock() && canBeDouble;

            if (canBeDouble && !chestBase.canBeSingle) {
                BlockPos posRight = pos.offset(facing.rotateY().getOpposite());
                if (world.mayPlace(this, posRight, false, facing, null)) {
                    setChestState(world, posRight, state);
                }
            }
            if (!isNorth && !isSouth && !isWest && !isEast) {
                world.setBlockState(pos, state, 3);
            } else if (facing.getAxis() != EnumFacing.Axis.X || !isNorth && !isSouth) {
                if (facing.getAxis() == EnumFacing.Axis.Z && (isWest || isEast)) {
                    if (isWest) {
                        setChestState(world, posWest, state);
                    } else {
                        setChestState(world, posEast, state);
                    }
                    world.setBlockState(pos, state, 3);
                }
            } else {
                if (isNorth) {
                    setChestState(world, posNorth, state);
                } else {
                    setChestState(world, posSouth, state);
                }
                world.setBlockState(pos, state, 3);
            }

            if (stack.hasDisplayName()) {
                chestBase.setCustomName(stack.getDisplayName());
            }
        }
        onBlockAdded(world, pos, state);
    }

    private static void setChestState(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        world.setBlockState(pos, state, 3);
        if (tileEntity != null) {
            tileEntity.validate();
            world.setTileEntity(pos, tileEntity);

            if (tileEntity instanceof TileEntityChestBase) {
                ((TileEntityChestBase) tileEntity).adjacentChestChecked = false;
            }
        }
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        for (EnumFacing horizontal : EnumFacing.HORIZONTALS) {
            TileEntity tileOffset = world.getTileEntity(pos.offset(horizontal));
            if (tileOffset instanceof TileEntityChestBase) {
                TileEntityChestBase chestOffset = (TileEntityChestBase) tileOffset;
                if (!chestOffset.canBeDouble) {
                    return true;
                }
            }
        }
        return super.canPlaceBlockAt(world, pos);
    }

    @Override
    @Nullable
    public ILockableContainer getContainer(World world, @Nonnull BlockPos pos, boolean allowBlocking) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof TileEntityChestBase)) {
            return null;
        } else {
            ILockableContainer lockableContainer = (TileEntityChestBase) tileEntity;

            if (!allowBlocking && this.isBlocked(world, pos)) {
                return null;
            } else {
                for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL) {
                    BlockPos posFacing = pos.offset(facing);
                    TileEntity tileOffset = world.getTileEntity(posFacing);
                    Block block = world.getBlockState(posFacing).getBlock();

                    if (block == this && tileOffset instanceof TileEntityChestBase) {
                        TileEntityChestBase chestBase = ((TileEntityChestBase) tileOffset);
                        if (chestBase.getChestType() == ATUM_CHEST_TYPE && chestBase.canBeDouble) {
                            if (!allowBlocking && this.isBlocked(world, posFacing)) {
                                return null;
                            }

                            if (facing != EnumFacing.WEST && facing != EnumFacing.NORTH) {
                                lockableContainer = new InventoryLargeChest("container.chestDouble", lockableContainer, (TileEntityChestBase) tileOffset);
                            } else {
                                lockableContainer = new InventoryLargeChest("container.chestDouble", (TileEntityChestBase) tileOffset, lockableContainer);
                            }
                        }
                    }
                }
                return lockableContainer;
            }
        }
    }

    private boolean isBlocked(World world, BlockPos pos) {
        return isBelowSolidBlock(world, pos) || isOcelotSittingOnChest(world, pos);
    }

    private boolean isBelowSolidBlock(World world, BlockPos pos) {
        return world.getBlockState(pos.up()).isSideSolid(world, pos.up(), EnumFacing.DOWN);
    }

    private boolean isOcelotSittingOnChest(World world, BlockPos pos) {
        for (Entity entity : world.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB((double) pos.getX(), (double) (pos.getY() + 1), (double) pos.getZ(), (double) (pos.getX() + 1), (double) (pos.getY() + 2), (double) (pos.getZ() + 1)))) {
            EntityOcelot ocelot = (EntityOcelot) entity;
            if (ocelot.isSitting()) {
                return true;
            }
        }
        return false;
    }
}