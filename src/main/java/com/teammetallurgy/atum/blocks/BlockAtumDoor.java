package com.teammetallurgy.atum.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class BlockAtumDoor extends BlockDoor implements IRenderMapper {

    public BlockAtumDoor(Material material) {
        super(material);
        this.disableStats();
        this.setHardness(3.0F);
        if (material == Material.WOOD) {
            this.setSoundType(SoundType.WOOD);
        } else {
            this.setSoundType(SoundType.STONE);
        }
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
        return state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER ? Items.AIR : getDoorItem(state.getBlock());
    }

    private Item getDoorItem(Block block) {
        return Objects.requireNonNull(Item.REGISTRY.getObject(block.getRegistryName()));
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{POWERED};
    }
}