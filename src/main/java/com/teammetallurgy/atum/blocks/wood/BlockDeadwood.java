package com.teammetallurgy.atum.blocks.wood;

import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.entity.EntityScarab;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockDeadwood extends BlockAtumLog implements IRenderMapper {
    public static final PropertyBool HAS_SCARAB = PropertyBool.create("has_scarab");

    public BlockDeadwood() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(HAS_SCARAB, false));
        this.setHardness(1.0F);
    }

    @Override
    public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
        return false;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, float chance, int fortune) {
        if (!world.isRemote && world.getGameRules().getBoolean("doTileDrops") && state.getValue(HAS_SCARAB) && RANDOM.nextDouble() <= 0.40D) {
            EntityScarab scarab = new EntityScarab(world);
            scarab.setLocationAndAngles((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, 0.0F, 0.0F);
            world.spawnEntity(scarab);
            scarab.spawnExplosionParticle();
        }
        super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
    }

    @Nonnull
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return BlockAtumPlank.WoodType.DEADWOOD.getMapColor();
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState state = this.getDefaultState();

        switch (meta) {
            case 0:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
                break;
            case 1:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
                break;
            case 2:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
                break;
            case 3:
                state = state.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
        }
        return state.withProperty(HAS_SCARAB, meta > 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        switch (state.getValue(LOG_AXIS)) {
            case X:
                i = 1;
                break;
            case Y:
                break;
            case Z:
                i = 2;
                break;
            case NONE:
                i = 3;
        }
        return i + (state.getValue(HAS_SCARAB) ? 4 : 0);
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS, HAS_SCARAB);
    }

    @Override
    public IProperty[] getNonRenderingProperties() {
        return new IProperty[]{HAS_SCARAB};
    }
}