package com.teammetallurgy.atum.blocks.beacon;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBeaconBeamColorProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;

public class RadiantBeaconBlock extends BeaconBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    private static final HashMap<Integer, DyeColor> RGB_TO_DYE = Maps.newHashMap();

    public RadiantBeaconBlock() {
        super(Block.Properties.create(Material.GLASS));
        this.setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(IBlockReader reader) {
        return new RadiantBeaconTileEntity();
    }

    @Override
    @Nonnull
    public MaterialColor getMaterialColor(BlockState state, IBlockReader world, BlockPos pos) {
        return state.get(COLOR).getMapColor();
    }

    @Override
    @Nonnull
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.isEmpty()) {
            return false;
        } else {
            Block block = Block.getBlockFromItem(heldStack.getItem());
            if (!world.isRemote) {
                if (block instanceof IBeaconBeamColorProvider) {
                    world.setBlockState(pos, AtumBlocks.RADIANT_BEACON.getDefaultState().with(COLOR, ((IBeaconBeamColorProvider) block).getColor()), 2);
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                } else {
                    float[] color = block.getBeaconColorMultiplier(state, world, pos, pos);
                    if (color != null) {
                        int r = (int) (color[0] * 255F);
                        int g = (int) (color[1] * 255F);
                        int b = (int) (color[2] * 255F);
                        int rgb = ((r & 0x0FF) << 16) | ((g & 0x0FF) << 8) | (b & 0x0FF);
                        DyeColor dyeColor = RGB_TO_DYE.get(rgb);

                        Block beacon = AtumBlocks.RADIANT_BEACON;
                        if (block.getTranslationKey().contains("framed")) { //TODO Test
                            beacon = AtumBlocks.RADIANT_BEACON_FRAMED;
                        }

                        world.setBlockState(pos, beacon.getDefaultState().with(COLOR, dyeColor));
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
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(COLOR);
    }

    static {
        for (DyeColor color : DyeColor.values()) {
            RGB_TO_DYE.put(color.getColorValue(), color);
        }
    }
}