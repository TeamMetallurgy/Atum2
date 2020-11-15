package com.teammetallurgy.atum.blocks.beacon;

import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RadiantBeaconBlock extends BeaconBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);

    public RadiantBeaconBlock() {
        super(AbstractBlock.Properties.create(Material.GLASS, state -> state.get(COLOR).getMapColor()).hardnessAndResistance(3.0F).setLightLevel((state) -> 15).notSolid());
        this.setDefaultState(this.stateContainer.getBaseState().with(COLOR, DyeColor.WHITE));
    }

    @Override
    @Nullable
    public TileEntity createNewTileEntity(@Nonnull IBlockReader reader) {
        return new RadiantBeaconTileEntity();
    }

    @Override
    @Nonnull
    public ActionResultType onBlockActivated(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull BlockRayTraceResult rayTraceResult) {
        /*ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.isEmpty()) {
            return ActionResultType.FAIL;
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
                    return ActionResultType.SUCCESS;
                }
            }
        }*/
        return ActionResultType.PASS;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> container) {
        container.add(COLOR);
    }
}