package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class BlockCrate extends ContainerBlock {
    private static final PropertyDirection FACING = BlockHorizontal.FACING;
    private static final Map<BlockAtumPlank.WoodType, BlockCrate> CRATES = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockCrate() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(this.blockState.getBaseState().with(FACING, Direction.NORTH));
    }

    public static void registerCrates() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            BlockCrate crate = new BlockCrate();
            CRATES.put(type, crate);
            AtumRegistry.registerBlock(crate, type.getName() + "_crate");
        }
    }

    public static BlockCrate getCrate(BlockAtumPlank.WoodType type) {
        return CRATES.get(type);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityCrate();
    }

    @Override
    @Nonnull
    public MapColor getMapColor(BlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length).getMapColor();
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(BlockState state) {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasCustomBreakingProgress(BlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCrate) {
            player.openGui(Atum.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Direction facing, float hitX, float hitY, float hitZ, int meta, @Nonnull LivingEntity placer, Hand hand) {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing());
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction facing = Direction.byHorizontalIndex(MathHelper.floor((double) (placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3).getOpposite();
        state = state.with(FACING, facing);
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posWest = pos.west();
        BlockPos posEast = pos.east();
        boolean isNorth = this == world.getBlockState(posNorth).getBlock();
        boolean isSouth = this == world.getBlockState(posSouth).getBlock();
        boolean isWest = this == world.getBlockState(posWest).getBlock();
        boolean isEast = this == world.getBlockState(posEast).getBlock();

        if (!isNorth && !isSouth && !isWest && !isEast) {
            world.setBlockState(pos, state, 3);
        } else if (facing.getAxis() != Direction.Axis.X || !isNorth && !isSouth) {
            if (facing.getAxis() == Direction.Axis.Z && (isWest || isEast)) {
                if (isWest) {
                    world.setBlockState(posWest, state, 3);
                } else {
                    world.setBlockState(posEast, state, 3);
                }
                world.setBlockState(pos, state, 3);
            }
        } else {
            if (isNorth) {
                world.setBlockState(posNorth, state, 3);
            } else {
                world.setBlockState(posSouth, state, 3);
            }
            world.setBlockState(pos, state, 3);
        }

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof TileEntityCrate) {
                ((TileEntityCrate) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCrate) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCrate) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof TileEntityCrate) {
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityCrate) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstoneFromInventory((TileEntityCrate) world.getTileEntity(pos));
    }

    public BlockState correctFacing(World world, BlockPos pos, BlockState state) {
        Direction facingCheck = null;
        for (Direction horizontal : Direction.Plane.HORIZONTAL) {
            BlockState stateHorizontal = world.getBlockState(pos.offset(horizontal));
            if (stateHorizontal.getBlock() == this) {
                return state;
            }
            if (stateHorizontal.isFullBlock()) {
                if (facingCheck != null) {
                    facingCheck = null;
                    break;
                }
                facingCheck = horizontal;
            }
        }

        if (facingCheck != null) {
            return state.with(FACING, facingCheck.getOpposite());
        } else {
            Direction facing = state.getValue(FACING);

            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.rotateY();
            }
            if (world.getBlockState(pos.offset(facing)).isFullBlock()) {
                facing = facing.getOpposite();
            }
            return state.with(FACING, facing);
        }
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        Direction facing = Direction.byIndex(meta);

        if (facing.getAxis() == Direction.Axis.Y) {
            facing = Direction.NORTH;
        }
        return this.getDefaultState().with(FACING, facing);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    @Nonnull
    public BlockFaceShape getBlockFaceShape(IBlockAccess world, BlockState state, BlockPos pos, Direction facing) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "crate");
        OreDictHelper.add(this, "chest");
        OreDictHelper.add(this, "chestWood");
    }
}