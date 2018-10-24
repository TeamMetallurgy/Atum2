package com.teammetallurgy.atum.blocks.wood;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.utils.AtumRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class BlockCrate extends BlockContainer {
    private static final Map<BlockAtumPlank.WoodType, BlockCrate> CRATES = Maps.newEnumMap(BlockAtumPlank.WoodType.class);

    private BlockCrate() {
        super(Material.WOOD);
        this.setHardness(3.0F);
        this.setSoundType(SoundType.WOOD);
    }

    public static void registerCrates() {
        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            BlockCrate crate = new BlockCrate();
            CRATES.put(type, crate);
            AtumRegistry.registerBlock(crate, type.getName() + "_crate");
        }
    }

    public static BlockCrate getCrate(BlockAtumPlank.WoodType type) {
        return CRATES.get(type);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityCrate();
    }

    @Override
    @Nonnull
    public MapColor getMapColor(IBlockState state, IBlockAccess blockAccess, BlockPos blockPos) {
        return BlockAtumPlank.WoodType.byIndex(BlockAtumPlank.WoodType.values().length).getMapColor();
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    @Nonnull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return true;
        }

        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityCrate) {
            player.openGui(Atum.instance, 1, world, pos.getX(), pos.getY(), pos.getZ());
            return true;
        }
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        if (stack.hasDisplayName()) {
            TileEntity tileentity = world.getTileEntity(pos);

            if (tileentity instanceof TileEntityCrate) {
                ((TileEntityCrate) tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (tileEntity instanceof TileEntityCrate) {
            tileEntity.updateContainingBlockInfo();
        }
    }

    @Override
    public void breakBlock(World world, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        TileEntity tileentity = world.getTileEntity(pos);

        if (tileentity instanceof TileEntityCrate) {
            world.updateComparatorOutputLevel(pos, this);
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void harvestBlock(@Nonnull World world, EntityPlayer player, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nullable TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (tileEntity instanceof TileEntityCrate) {
            InventoryHelper.dropInventoryItems(world, pos, (TileEntityCrate) tileEntity);
            world.updateComparatorOutputLevel(pos, this);
        }
        super.harvestBlock(world, player, pos, state, tileEntity, stack);
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        return Container.calcRedstoneFromInventory((TileEntityCrate) world.getTileEntity(pos));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return 0;
    }
}