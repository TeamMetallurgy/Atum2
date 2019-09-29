package com.teammetallurgy.atum.blocks.machines;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnBaseTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock;
import com.teammetallurgy.atum.blocks.stone.limestone.LimestoneBrickBlock.BrickType;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class BlockKiln extends ContainerBlock {
    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public static final PropertyBool IS_BURNING = PropertyBool.create("is_burning");
    static final PropertyBool MULTIBLOCK_PRIMARY = PropertyBool.create("multiblock_primary");
    private static final PropertyBool MULTIBLOCK_SECONDARY = PropertyBool.create("multiblock_secondary");

    public BlockKiln() {
        super(Material.ROCK, MaterialColor.SAND);
        this.setHardness(3.5F);
        this.setSoundType(SoundType.STONE);
        this.setHarvestLevel("pickaxe", 0);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(IS_BURNING, false).with(MULTIBLOCK_PRIMARY, false).with(MULTIBLOCK_SECONDARY, false));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new KilnTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        BlockPos tepos = getPrimaryKilnBlock(world, pos);
        if (tepos != null) {
            TileEntity tileEntity = world.getTileEntity(tepos);
            if (tileEntity instanceof KilnTileEntity) {
                player.openGui(Atum.instance, 5, world, tepos.getX(), tepos.getY(), tepos.getZ());
                return true;
            }
        }
        return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(IS_BURNING) ? (int) (15.0F * 0.875F) : 0;
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        if (state.get(MULTIBLOCK_PRIMARY)) {
            this.destroyMultiblock(world, pos, state.get(FACING));
        } else {
            BlockPos primaryPos = pos.offset(state.get(FACING).rotateYCCW());
            BlockState primaryState = world.getBlockState(primaryPos);
            if (primaryState.getBlock() == AtumBlocks.KILN && primaryState.getValue(MULTIBLOCK_PRIMARY)) {
                this.destroyMultiblock(world, primaryPos, primaryState.getValue(FACING));
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, @Nonnull ItemStack stack) {
        world.setBlockState(pos, state.with(FACING, placer.getHorizontalFacing().getOpposite()), 2);

        if (stack.hasDisplayName()) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity instanceof KilnTileEntity) {
                ((KilnTileEntity) tileEntity).setCustomName(stack.getDisplayName());
            }
        }
        state = world.getBlockState(pos);
        tryMakeMultiblock(world, pos, state);
    }

    public void tryMakeMultiblock(World world, BlockPos pos, BlockState state) {
        Direction facing = state.get(FACING);
        if (checkMultiblock(world, pos, facing)) {
            world.setBlockState(pos, state.with(MULTIBLOCK_PRIMARY, true));
            world.setBlockState(pos.offset(facing.rotateY()), state.with(MULTIBLOCK_PRIMARY, false));
            createMultiblock(world, pos);
        } else if (checkMultiblock(world, pos.offset(facing.rotateYCCW()), facing)) {
            world.setBlockState(pos, state.with(MULTIBLOCK_PRIMARY, false));
            world.setBlockState(pos.offset(facing.rotateYCCW()), state.with(MULTIBLOCK_PRIMARY, true));
            createMultiblock(world, pos.offset(facing.rotateYCCW()));
        }
    }

    public static BlockPos getSecondaryKilnFromPrimary(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof KilnTileEntity) {
            if (state.getBlock() == AtumBlocks.KILN && ((KilnTileEntity) tileEntity).isPrimary()) {
                return pos.offset(state.get(FACING).rotateY());
            }
        }
        return null;
    }

    private BlockPos getPrimaryKilnBlock(World world, BlockPos pos) {
        if (world != null && !world.isAirBlock(pos)) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() == AtumBlocks.KILN && state.get(MULTIBLOCK_PRIMARY)) {
                return pos;
            } else {
                state = world.getBlockState(pos.offset(state.get(FACING).rotateYCCW()));
                if (state.getBlock() == AtumBlocks.KILN && state.get(MULTIBLOCK_PRIMARY)) {
                    return pos.offset(state.get(FACING).rotateYCCW());
                }
            }
        }
        return null;
    }

    private void createMultiblock(World world, BlockPos primaryPos) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, world.getBlockState(primaryPos).getValue(FACING));
        for (BlockPos brickPos : brickPositions) {
            world.setBlockState(brickPos, AtumBlocks.KILN_FAKE.getDefaultState().with(BlockKilnFake.UP, primaryPos.getY() - 1 == brickPos.getY()));
            TileEntity tileEntity = world.getTileEntity(brickPos);
            if (tileEntity != null) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
        }

        BlockPos secondaryPos = getSecondaryKilnFromPrimary(world, primaryPos);

        if (secondaryPos != null) {
            tileEntity = world.getTileEntity(secondaryPos);
            if (tileEntity instanceof KilnBaseTileEntity) {
                ((KilnBaseTileEntity) tileEntity).setPrimaryPos(primaryPos);
            }
        }
    }

    void destroyMultiblock(World world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        BlockState primaryState = world.getBlockState(primaryPos);
        BlockPos secondaryPos = primaryPos.offset(facing.rotateY());
        BlockState secondaryState = world.getBlockState(secondaryPos);
        BlockPos dropPos = primaryPos;

        if (primaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(primaryPos, primaryState.with(MULTIBLOCK_PRIMARY, false).with(IS_BURNING, false));
        }
        if (secondaryState.getBlock() == AtumBlocks.KILN) {
            world.setBlockState(secondaryPos, secondaryState.with(IS_BURNING, false));
        } else {
            dropPos = secondaryPos;
        }
        for (BlockPos brickPos : brickPositions) {
            if (world.getBlockState(brickPos).getBlock() == AtumBlocks.KILN_FAKE) {
                world.setBlockState(brickPos, LimestoneBrickBlock.getBrick(BrickType.SMALL).getDefaultState());
            } else {
                dropPos = brickPos;
            }
        }

        TileEntity tileEntity = world.getTileEntity(primaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
            InventoryHelper.dropInventoryItems(world, dropPos, kilnBase);
            kilnBase.invalidate();
        }

        tileEntity = world.getTileEntity(secondaryPos);
        if (tileEntity instanceof KilnBaseTileEntity) {
            KilnBaseTileEntity kilnBase = (KilnBaseTileEntity) tileEntity;
            kilnBase.setPrimaryPos(null);
        }
    }

    private boolean checkMultiblock(World world, BlockPos primaryPos, Direction facing) {
        List<BlockPos> brickPositions = getKilnBrickPositions(primaryPos, facing);
        if (world.getBlockState(primaryPos).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        if (world.getBlockState(primaryPos.offset(facing.rotateY())).getBlock() != AtumBlocks.KILN) {
            return false;
        }
        for (BlockPos brickPos : brickPositions) {
            BlockState brickState = world.getBlockState(brickPos);
            if (brickState.getBlock() != LimestoneBrickBlock.getBrick(BrickType.SMALL)) {
                return false;
            }
        }
        return true;
    }

    private List<BlockPos> getKilnBrickPositions(BlockPos pos, Direction facing) {
        List<BlockPos> positions = new LinkedList<>();
        positions.add(pos.offset(Direction.DOWN));
        positions.add(pos.offset(facing.getOpposite()));
        positions.add(pos.offset(facing.getOpposite()).offset(Direction.DOWN));

        BlockPos offset = pos.offset(facing.rotateY());
        positions.add(offset.offset(Direction.DOWN));
        positions.add(offset.offset(facing.getOpposite()));
        positions.add(offset.offset(facing.getOpposite()).offset(Direction.DOWN));

        return positions;
    }

    @Override
    @Nonnull
    public BlockState getActualState(@Nonnull BlockState state, IBlockReader world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof KilnTileEntity) {
            KilnTileEntity kiln = (KilnTileEntity) tileEntity;
            if (this.getPrimaryKilnBlock(kiln.getWorld(), pos) != null) {
                return state.with(MULTIBLOCK_SECONDARY, !kiln.isPrimary());
            }
        }
        return state;
    }

    @Override
    @Nonnull
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer) {
        return this.getDefaultState().with(FACING, placer.getHorizontalFacing().getOpposite()).with(MULTIBLOCK_PRIMARY, false);
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(BlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(IS_BURNING, (meta & 4) != 0).with(FACING, Direction.byHorizontalIndex(meta & 3)).with(MULTIBLOCK_PRIMARY, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        int meta = 0;
        meta = meta | (state.get(FACING)).getHorizontalIndex();
        if (state.get(IS_BURNING)) {
            meta |= 4;
        }
        if (state.get(MULTIBLOCK_PRIMARY)) {
            meta |= 8;
        }
        return meta;
    }

    @Override
    @Nonnull
    public BlockState withRotation(@Nonnull BlockState state, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    @Nonnull
    public BlockState withMirror(@Nonnull BlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.get(FACING)));
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, IS_BURNING, MULTIBLOCK_PRIMARY, MULTIBLOCK_SECONDARY);
    }
}