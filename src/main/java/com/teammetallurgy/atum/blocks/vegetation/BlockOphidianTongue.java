package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.*;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Random;

public class BlockOphidianTongue extends VineBlock {
    private static final BooleanProperty HAS_FLOWERS = BooleanProperty.create("flowers");

    public BlockOphidianTongue() {
        super();
        this.setDefaultState(this.stateContainer.getBaseState().with(UP, Boolean.FALSE).with(NORTH, Boolean.FALSE).with(EAST, Boolean.FALSE).with(SOUTH, Boolean.FALSE).with(WEST, Boolean.FALSE).with(HAS_FLOWERS, false));
        this.setHardness(0.2F);
        this.setSoundType(SoundType.PLANT);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isRemote && state.get(HAS_FLOWERS) && entity instanceof LivingEntity) {
            LivingEntity livingBase = (LivingEntity) entity;
            livingBase.addPotionEffect(new EffectInstance(Effects.POISON, 35));
        }
    }

    @Override
    public void tick(@Nonnull BlockState state, World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        super.tick(state, world, pos, rand);
        if (!world.isRemote && !state.get(HAS_FLOWERS) && rand.nextDouble() <= 0.03D) {
            world.setBlockState(pos, state.with(HAS_FLOWERS, true), 2);
        }
    }

    @Override
    public boolean canAttachTo(World world, BlockPos pos, Direction facing) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return this.isAcceptableNeighbor(world, pos.offset(facing.getOpposite()), facing) && (block == Blocks.AIR || block == this || this.isAcceptableNeighbor(world, pos.up(), Direction.UP));
    }

    private boolean isAcceptableNeighbor(World world, BlockPos pos, Direction facing) {
        BlockState state = world.getBlockState(pos);
        return state.getBlockFaceShape(world, pos, facing) == BlockFaceShape.SOLID && !isExceptBlockForAttaching(state.getBlock());
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (!world.isRemote && stack.getItem() == Items.SHEARS) {
            player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
            spawnAsEntity(world, pos, new ItemStack(AtumBlocks.OPHIDIAN_TONGUE, 1, 0));
        } else {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        }
    }

    @Override
    @Nonnull
    public Item getItemDropped(BlockState state, Random rand, int fortune) {
        return state.get(HAS_FLOWERS) ? AtumItems.OPHIDIAN_TONGUE_FLOWER : super.getItemDropped(state, rand, fortune);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(UP, NORTH, EAST, SOUTH, WEST, HAS_FLOWERS);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "vine");
    }
}