package com.teammetallurgy.atum.blocks;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        if (!state.getValue(DECAYABLE)) {
            i |= 4;
        }

        if (state.getValue(CHECK_DECAY)) {
            i |= 8;
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
            player.addStat(StatList.getBlockStats(this));
        } else {
            super.harvestBlock(world, player, pos, state, te, stack);
        }
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