package com.teammetallurgy.atum.blocks.beacon;

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
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RadiantBeaconBlock extends BeaconBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

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
                DyeColor color = null;
                Block beacon = AtumBlocks.RADIANT_BEACON;
                if (block.getTranslationKey().contains("framed")) {
                    beacon = AtumBlocks.RADIANT_BEACON_FRAMED;
                }
                if (block.isIn(Tags.Blocks.GLASS_COLORLESS) || block.isIn(Tags.Blocks.GLASS_PANES_COLORLESS)) {
                    color = DyeColor.WHITE;
                } else if (block instanceof IBeaconBeamColorProvider) {
                    color = ((IBeaconBeamColorProvider) block).getColor();
                }
                if (color != null && color != state.get(COLOR)) {
                    if (beacon == this) { //Already gets played when changing between framed and crystal
                        world.playSound(null, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    }
                    world.setBlockState(pos, beacon.getDefaultState().with(COLOR, color));
                    if (!player.isCreative()) {
                        heldStack.shrink(1);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(COLOR);
    }
}