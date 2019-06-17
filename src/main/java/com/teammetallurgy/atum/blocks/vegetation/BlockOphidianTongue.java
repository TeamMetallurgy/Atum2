package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockVine;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class BlockOphidianTongue extends BlockVine implements IOreDictEntry {
    private static final PropertyBool HAS_FLOWERS = PropertyBool.create("flowers");

    public BlockOphidianTongue() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(UP, Boolean.FALSE).withProperty(NORTH, Boolean.FALSE).withProperty(EAST, Boolean.FALSE).withProperty(SOUTH, Boolean.FALSE).withProperty(WEST, Boolean.FALSE).withProperty(HAS_FLOWERS, false));
        this.setHardness(0.2F);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && state.getValue(HAS_FLOWERS) && entity instanceof EntityLivingBase) {
            EntityLivingBase livingBase = (EntityLivingBase) entity;
            livingBase.addPotionEffect(new PotionEffect(MobEffects.POISON, 35));
        }
    }

    @Override
    public void updateTick(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        super.updateTick(world, pos, state, rand);
        if (!world.isRemote && !state.getValue(HAS_FLOWERS) && rand.nextDouble() <= 0.03D) {
            world.setBlockState(pos, state.withProperty(HAS_FLOWERS, true), 2);
        }
    }

    @Override
    public boolean canAttachTo(World world, BlockPos pos, EnumFacing facing) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return this.isAcceptableNeighbor(world, pos.offset(facing.getOpposite()), facing) && (block == Blocks.AIR || block == this || this.isAcceptableNeighbor(world, pos.up(), EnumFacing.UP));
    }

    private boolean isAcceptableNeighbor(World world, BlockPos pos, EnumFacing facing) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(state.getBlock());
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
            spawnAsEntity(world, pos, new ItemStack(AtumBlocks.OPHIDIAN_TONGUE, 1, 0));
        } else {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(HAS_FLOWERS) ? AtumItems.OPHIDIAN_TONGUE_FLOWER : super.getItemDropped(state, rand, fortune);
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();
        EnumFacing facing = EnumFacing.byHorizontalIndex(meta & 0x11b);
        return state.withProperty(SOUTH, facing == EnumFacing.SOUTH).withProperty(WEST, facing == EnumFacing.WEST).withProperty(NORTH, facing == EnumFacing.NORTH).withProperty(EAST, facing == EnumFacing.EAST).withProperty(HAS_FLOWERS, ((meta & 15) >> 2) == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int meta = 0;
        if (state.getValue(SOUTH)) {
            meta |= EnumFacing.SOUTH.getHorizontalIndex();
        }
        if (state.getValue(WEST)) {
            meta |= EnumFacing.WEST.getHorizontalIndex();
        }
        if (state.getValue(NORTH)) {
            meta |= EnumFacing.NORTH.getHorizontalIndex();
        }
        if (state.getValue(EAST)) {
            meta |= EnumFacing.EAST.getHorizontalIndex();
        }
        meta = meta | (state.getValue(HAS_FLOWERS) ? 4 : 0);
        return meta;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, HAS_FLOWERS);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "vine");
    }
}