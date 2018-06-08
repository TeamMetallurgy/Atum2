package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.*;

public class BlockLeave extends BlockLeaves implements IRenderMapper, IOreDictEntry {
    private static final Map<BlockAtumPlank.WoodType, Block> LEAVES = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockLeave() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    public static void registerLeaves() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            if (type != BlockAtumPlank.WoodType.DEADWOOD) {
                Block leave = new BlockLeave();
                LEAVES.put(type, leave);
                AtumRegistry.registerBlock(leave, type.getName() + "_leaves");
            }
        }
    }

    public static Block getLeave(BlockAtumPlank.WoodType type) {
        return LEAVES.get(type);
    }

    @Override
    public void updateTick(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (!world.isRemote) {
            if (state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE)) {
                if (!nearLog(world, pos)) {
                    super.updateTick(world, pos, state, rand);
                } else {
                    world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
                }
            }
            if (this == getLeave(BlockAtumPlank.WoodType.PALM) && world.rand.nextDouble() <= 0.02F) {
                if (state.getValue(DECAYABLE) && isValidLocation(world, pos.down()) && world.isAirBlock(pos.down())) {
                    world.setBlockState(pos.down(), AtumBlocks.DATE_BLOCK.getDefaultState());
                }
            }
        }
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, BlockPos pos, IBlockState state) {
        /*if (checkLeaves(world, pos) && !world.isRemote) {
            for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(3, -1, 3), pos.add(-3, -1, -3))) {
                if (world.getBlockState(mutableBlockPos).getBlock() instanceof BlockLeave) {
                    System.out.println("Boop");
                    dropBlockAsItem(world, mutableBlockPos, state, 0);
                    world.setBlockToAir(mutableBlockPos);
                }
            }
        }*/
        super.onBlockDestroyedByPlayer(world, pos, state);
    }

    private boolean nearLog(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(3, 0, 3), pos.add(-3, 0, -3))) {
            if (world.getBlockState(mutableBlockPos).getBlock() instanceof BlockLog) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLeaves(World world, BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (world.getBlockState(pos.offset(facing)).getBlock() instanceof BlockLeave) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidLocation(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos check = pos.offset(facing);
            if (worldIn.getBlockState(check).getBlock() == BlockAtumLog.getLog(BlockAtumPlank.WoodType.PALM)) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(CHECK_DECAY, false).withProperty(DECAYABLE, false);
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, (meta & 1) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!state.getValue(DECAYABLE)) {
            i |= 1;
        }
        if (state.getValue(CHECK_DECAY)) {
            i |= 4;
        }
        return i;
    }

    @Override
    @Nonnull
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.byMetadata(0);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, @Nonnull ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(Block.REGISTRY.getObject(new ResourceLocation(String.valueOf(state.getBlock().getRegistryName()).replace("leaves", "sapling"))));
    }

    @Override
    @Nonnull
    public List<ItemStack> onSheared(@Nonnull ItemStack stack, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this));
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{CHECK_DECAY, DECAYABLE};
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return Blocks.LEAVES.getBlockLayer();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return Blocks.LEAVES.isOpaqueCube(Blocks.LEAVES.getDefaultState());
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "treeLeaves");
    }
}