package com.teammetallurgy.atum.blocks.vegetation;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class BlockAnputsFingers extends BlockCrops {
    private static final PropertyInteger ANPUTS_FINGERS_AGE = PropertyInteger.create("age", 0, 3);
    private static final AxisAlignedBB[] AABB = new AxisAlignedBB[]{new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6875D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D)};
    private HashMap<UUID, Integer> lastTouchedTick = new HashMap<>();

    public BlockAnputsFingers() {
        super();
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.GRAY;
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB[this.getAge(state)];
    }

    @Override
    @Nonnull
    protected Item getSeed() {
        return AtumItems.ANPUTS_FINGERS_SPORES;
    }

    @Override
    @Nonnull
    protected Item getCrop() {
        return AtumItems.ANPUTS_FINGERS_SPORES;
    }

    @Override
    @Nonnull
    protected PropertyInteger getAgeProperty() {
        return ANPUTS_FINGERS_AGE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected boolean canSustainBush(IBlockState state) {
        return state.getBlock() == AtumBlocks.SAND;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return super.canPlaceBlockAt(world, pos) && this.canBlockStay(world, pos, this.getDefaultState());
    }

    @Override
    public void updateTick(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        int age = this.getAge(state);
        if (age < this.getMaxAge() && ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt(8) == 0)) {
            IBlockState newState = state.withProperty(this.getAgeProperty(), age + 1);
            world.setBlockState(pos, newState, 2);
            ForgeHooks.onCropsGrowPost(world, pos, state, newState);
        }
        this.checkAndDropBlock(world, pos, state);
    }

    @Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            MinecraftServer server = world.getMinecraftServer();
            Integer lastTouched = this.lastTouchedTick.get(player.getUniqueID());
            if (server != null) {
                if (lastTouched != null && server.getTickCounter() - lastTouched < 35) return;
                if (player.getFoodStats().getFoodLevel() > 0) {
                    player.getFoodStats().addStats(-1, -0.1F);
                    this.lastTouchedTick.put(player.getUniqueID(), server.getTickCounter());
                }
            }
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
        IBlockState stateDown = world.getBlockState(pos.down());
        return world.getLightFor(EnumSkyBlock.SKY, pos) < 15 && stateDown.getBlock().canSustainPlant(stateDown, world, pos.down(), EnumFacing.UP, this);
    }

    @Override
    public boolean canGrow(World world, BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return false;
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
        return false;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ANPUTS_FINGERS_AGE);
    }
}