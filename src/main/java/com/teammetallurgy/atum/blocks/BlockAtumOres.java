package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
        if (this == AtumBlocks.COAL_ORE) {
            return Items.COAL;
        } else if (this == AtumBlocks.DIAMOND_ORE) {
            return Items.DIAMOND;
        } else if (this == AtumBlocks.LAPIS_ORE) {
            return Items.DYE;
        } else if (this == AtumBlocks.EMERALD_ORE) {
            return Items.EMERALD;
        } else if (this == AtumBlocks.RELIC_ORE) {
            return new ItemStack(AtumBlocks.LIMESTONE_CRACKED).getItem();
        } else {
            return this == AtumBlocks.BONE_ORE ? AtumItems.DUSTY_BONE : Item.getItemFromBlock(this);
        }
    }

    @Override
    public int quantityDropped(@Nullable Random random) {
        return random != null ? this == AtumBlocks.LAPIS_ORE ? 4 + random.nextInt(5) : this == AtumBlocks.BONE_ORE ? MathHelper.getInt(random, 1, 3) : 1 : 0;
    }

    @Override
    public int getExpDrop(@Nullable IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        if (this.getItemDropped(state, rand, fortune) != Item.getItemFromBlock(this)) {
            int xp = 0;

            if (this == AtumBlocks.COAL_ORE) {
                xp = MathHelper.getInt(rand, 0, 2);
            } else if (this == AtumBlocks.DIAMOND_ORE) {
                xp = MathHelper.getInt(rand, 3, 7);
            } else if (this == Blocks.EMERALD_ORE) {
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
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        World world = blockAccess instanceof World ? ((World) blockAccess) : null;

        if (this == AtumBlocks.RELIC_ORE) {
            LootContext.Builder builder = new LootContext.Builder((WorldServer) Objects.requireNonNull(world));
            List<ItemStack> loot = Objects.requireNonNull(world).getLootTableManager().getLootTableFromLocation(AtumLootTables.RELIC).generateLootForPools(world.rand, builder.build());
            drops.addAll(loot);
        }
        super.getDrops(drops, blockAccess, pos, state, fortune);
    }


    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "ore", Objects.requireNonNull(this.getRegistryName()).getPath().replace("_ore", ""));
    }
}