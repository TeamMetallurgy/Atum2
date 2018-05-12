package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

public class BlockAtumOres extends BlockOre implements IOreDictEntry {

    public BlockAtumOres() {
        super();
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    @Nonnull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this == AtumBlocks.COAL_ORE ? Items.COAL : (this == AtumBlocks.DIAMOND_ORE ? Items.DIAMOND : (this == AtumBlocks.LAPIS_ORE ? Items.DYE : Item.getItemFromBlock(this)));
    }

    @Override
    public int quantityDropped(Random random) {
        return this == AtumBlocks.LAPIS_ORE ? 4 + random.nextInt(5) : 1;
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            int xp = 0;

            if (this == AtumBlocks.COAL_ORE) {
                xp = MathHelper.getInt(rand, 0, 2);
            } else if (this == AtumBlocks.DIAMOND_ORE) {
                xp = MathHelper.getInt(rand, 3, 7);
            } else if (this == AtumBlocks.LAPIS_ORE) {
                xp = MathHelper.getInt(rand, 2, 5);
            }
            return xp;
        }
        return 0;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this == AtumBlocks.LAPIS_ORE ? EnumDyeColor.BLUE.getDyeDamage() : 0;
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "ore", Objects.requireNonNull(this.getRegistryName()).getResourcePath().replace("_ore", ""));
    }
}