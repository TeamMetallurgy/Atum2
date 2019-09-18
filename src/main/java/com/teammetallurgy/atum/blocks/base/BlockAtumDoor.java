package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class BlockAtumDoor extends DoorBlock implements IRenderMapper {
    private Material doorMaterial;

    public BlockAtumDoor(Material material) {
        super(Material.WOOD);
        this.disableStats();
        this.setHardness(3.0F);
        this.doorMaterial = material;
        if (this.doorMaterial == Material.WOOD) {
            this.setSoundType(SoundType.WOOD);
        } else {
            this.setSoundType(SoundType.STONE);
        }
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (this.doorMaterial == Material.WOOD) {
            return MapColor.WOOD;
        } else {
            return MapColor.SAND;
        }
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos pos1 = state.getValue(HALF) == BlockDoor.EnumDoorHalf.LOWER ? pos : pos.down();
        IBlockState state1 = pos.equals(pos1) ? state : world.getBlockState(pos1);

        if (state1.getBlock() != this) {
            return false;
        } else {
            state = state1.cycleProperty(OPEN);
            world.setBlockState(pos1, state, 10);
            world.markBlockRangeForRenderUpdate(pos1, pos);
            world.playSound(null, pos, state.getValue(OPEN) ? this.getOpenSound() : this.getCloseSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        }
    }

    private SoundEvent getCloseSound() {
        return this.doorMaterial == Material.ROCK ? SoundEvents.BLOCK_STONE_BREAK : SoundEvents.BLOCK_WOODEN_DOOR_CLOSE;
    }

    private SoundEvent getOpenSound() {
        return this.doorMaterial == Material.ROCK ? SoundEvents.BLOCK_STONE_BREAK : SoundEvents.BLOCK_WOODEN_TRAPDOOR_OPEN;
    }

    @Override
    @Nonnull
    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(getDoorItem(world.getBlockState(pos).getBlock()));
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return this.getItem(world, pos, state);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random random, int fortune) {
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : this.getDoorItem(this);
    }

    private Item getDoorItem(Block block) {
        return Objects.requireNonNull(Item.REGISTRY.getObject(block.getRegistryName()));
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{POWERED};
    }
}