package com.teammetallurgy.atum.blocks.beacon;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.beacon.tileentity.TileEntityRadiantBeacon;
import com.teammetallurgy.atum.blocks.glass.BlockAtumStainedGlass;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBeacon;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class BlockRadiantBeacon extends BlockBeacon {
    public static final PropertyEnum<DyeColor> COLOR = PropertyEnum.create("color", DyeColor.class);
    private static final HashMap<Integer, DyeColor> RGB_TO_DYE = Maps.newHashMap();

    public BlockRadiantBeacon() {
        this.setDefaultState(this.blockState.getBaseState().with(COLOR, DyeColor.WHITE));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileEntityRadiantBeacon();
    }

    @Override
    @Nonnull
    public Material getMaterial(BlockState state) {
        return Material.GLASS;
    }

    @Override
    @Nonnull
    public MapColor getMapColor(BlockState state, IBlockAccess world, BlockPos pos) {
        return MapColor.getBlockColor(state.getValue(COLOR));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean onBlockActivated(World world, @Nonnull BlockPos pos, BlockState state, @Nonnull PlayerEntity player, Hand hand, Direction facing, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.isEmpty()) {
            return false;
        } else {
            Item item = heldStack.getItem();
            if (!world.isRemote) {
                if (item == Item.getItemFromBlock(Blocks.STAINED_GLASS) || item == Item.getItemFromBlock(Blocks.STAINED_GLASS_PANE)) {
                    world.setBlockState(pos, AtumBlocks.RADIANT_BEACON.getDefaultState().with(COLOR, DyeColor.byId(heldStack.getMetadata())), 2);
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                } else {
                    float[] color = Block.getBlockFromItem(item).getBeaconColorMultiplier(state, world, pos, pos);
                    if (color != null) {
                        int r = (int) (color[0] * 255F);
                        int g = (int) (color[1] * 255F);
                        int b = (int) (color[2] * 255F);
                        int rgb = ((r & 0x0FF) << 16) | ((g & 0x0FF) << 8) | (b & 0x0FF);
                        DyeColor dyeColor = RGB_TO_DYE.get(rgb);

                        Block block = AtumBlocks.RADIANT_BEACON;
                        if (Block.getBlockFromItem(item) == BlockAtumStainedGlass.getGlass(AtumBlocks.FRAMED_GLASS, dyeColor) || Block.getBlockFromItem(item) == BlockAtumStainedGlass.getGlass(AtumBlocks.THIN_FRAMED_GLASS, dyeColor)) {
                            block = AtumBlocks.RADIANT_BEACON_FRAMED;
                        }

                        world.setBlockState(pos, block.getDefaultState().with(COLOR, dyeColor));
                        if (!player.isCreative()) {
                            heldStack.shrink(1);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public BlockState getStateFromMeta(int meta) {
        return this.getDefaultState().with(COLOR, DyeColor.byId(meta));
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(COLOR).getMetadata();
    }

    @Override
    @Nonnull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, COLOR);
    }

    static {
        if (FMLCommonHandler.instance().getSide() == Dist.CLIENT) {
            for (DyeColor color : DyeColor.values()) {
                RGB_TO_DYE.put(color.getColorValue(), color);
            }
        }
    }
}