package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.*;

public class BlockLeave extends BlockLeaves implements IGrowable, IRenderMapper, IOreDictEntry {
    private static final Map<BlockAtumPlank.WoodType, Block> LEAVES = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockLeave() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true));
    }

    public static void registerLeaves() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Block leave = new BlockLeave();
            LEAVES.put(type, leave);
            if (type == BlockAtumPlank.WoodType.DEADWOOD) {
                AtumRegistry.registerBlock(leave, type.getName() + "_leaves", null);
            } else {
                AtumRegistry.registerBlock(leave, type.getName() + "_leaves");
            }
        }
    }

    public static Block getLeave(BlockAtumPlank.WoodType type) {
        return LEAVES.get(type);
    }

    @Override
    public void updateTick(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, Random rand) {
        if (BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) == BlockAtumPlank.WoodType.PALM) {
            if (!world.isRemote) {
                if (state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE)) {
                    if (!nearLog(world, pos)) {
                        super.updateTick(world, pos, state, rand);
                    } else {
                        world.setBlockState(pos, state.withProperty(CHECK_DECAY, false), 4);
                    }
                }
                if (this == getLeave(BlockAtumPlank.WoodType.PALM) && world.rand.nextDouble() <= 0.05F) {
                    if (canGrow(world, pos, state, false)) {
                        world.setBlockState(pos.down(), AtumBlocks.DATE_BLOCK.getDefaultState());
                    }
                }
            }
        }
    }

    private boolean nearLog(World world, BlockPos pos) {
        for (BlockPos.MutableBlockPos mutableBlockPos : BlockPos.getAllInBoxMutable(pos.add(3, 0, 3), pos.add(-3, 0, -3))) {
            if (world.getBlockState(mutableBlockPos).getBlock() instanceof BlockLog) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidLocation(@Nonnull World worldIn, @Nonnull BlockPos pos) {
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos check = pos.offset(facing);
            if (worldIn.getBlockState(check).getBlock() == AtumBlocks.PALM_LOG) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull LivingEntity placer, Hand hand) {
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
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull IBlockState state, TileEntity te, @Nonnull ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }
    }

    @Override
    protected int getSaplingDropChance(IBlockState state) {
        return 10;
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) == BlockAtumPlank.WoodType.PALM ? Item.getItemFromBlock(Block.REGISTRY.getObject(new ResourceLocation(String.valueOf(state.getBlock().getRegistryName()).replace("leaves", "sapling")))) : Items.AIR;
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
    @OnlyIn(Dist.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return Blocks.LEAVES.getRenderLayer();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return Blocks.LEAVES.getDefaultState().isOpaqueCube();
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "treeLeaves");
    }

    @Override
    public boolean canGrow(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) == BlockAtumPlank.WoodType.PALM && state.getValue(DECAYABLE) && isValidLocation(world, pos.down()) && world.isAirBlock(pos.down());
    }

    @Override
    public boolean canUseBonemeal(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) == BlockAtumPlank.WoodType.PALM;
    }

    @Override
    public void grow(@Nonnull World world, @Nonnull Random rand, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        if (canGrow(world, pos, state, false) && rand.nextDouble() <= 0.5D) {
            world.setBlockState(pos.down(), AtumBlocks.DATE_BLOCK.getDefaultState());
        }
    }
}