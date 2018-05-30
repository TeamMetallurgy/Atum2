package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Objects;

public class BlockAtumLog extends BlockLog implements IOreDictEntry {
    private static final Map<BlockAtumPlank.WoodType, Block> LOGS = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockAtumLog() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
    }

    public static void registerLogs() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            Block log = new BlockAtumLog();
            LOGS.put(type, log);
            AtumRegistry.registerBlock(log, type.getName() + "_log");
        }
    }

    public static Block getLog(BlockAtumPlank.WoodType type) {
        return LOGS.get(type);
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) != BlockAtumPlank.WoodType.DEADWOOD;
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        BlockAtumPlank.WoodType type = BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length);

        switch (state.getValue(LOG_AXIS)) {
            case X:
            case Z:
            case NONE:
            default:
                switch (type) {
                    case PALM:
                    default:
                        return BlockAtumPlank.WoodType.PALM.getMapColor();
                    case DEADWOOD:
                        return BlockAtumPlank.WoodType.DEADWOOD.getMapColor();
                }
            case Y:
                return type.getMapColor();
        }
    }

    @Override
    @Nonnull
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();

        switch (meta & 12) {
            case 0:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 4:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 8:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            default:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }

        return state;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        switch (state.getValue(LOG_AXIS)) {
            case X:
                i |= 4;
                break;
            case Y:
                break;
            case Z:
                i |= 8;
                break;
            case NONE:
                i |= 12;
        }
        return i;
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, @Nonnull IBlockState state, int fortune) {
        if (BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length) == BlockAtumPlank.WoodType.DEADWOOD && RANDOM.nextInt(100) <= 25) {
            // Drop Beetles.
            int amount = MathHelper.getInt(RANDOM, 1, 2) + fortune;
            drops.add(new ItemStack(AtumItems.DEADWOOD_BEETLE, amount));
        }
        super.getDrops(drops, world, pos, state, fortune);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "log", Objects.requireNonNull(this.getRegistryName()).getResourcePath().replace("_log", ""));
        OreDictHelper.add(this, "logWood");
    }
}