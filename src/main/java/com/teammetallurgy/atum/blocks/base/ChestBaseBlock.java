package com.teammetallurgy.atum.blocks.base;

import com.teammetallurgy.atum.blocks.base.tileentity.ChestBaseTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

public class ChestBaseBlock extends ChestBlock { //TODO

    protected ChestBaseBlock() {
        super(Block.Properties.create(Material.ROCK, MaterialColor.SAND).hardnessAndResistance(3.0F, 10.0F).sound(SoundType.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(0));
    }

    @Override
    public void onBlockHarvested(World world, @Nonnull BlockPos pos, BlockState state, @Nonnull PlayerEntity player) {
        super.onBlockHarvested(world, pos, state, player);

        TileEntity tileEntity = world.getTileEntity(pos);
        if (player.isCreative() && tileEntity instanceof ChestBaseTileEntity) {
            this.harvestBlock(world, player, pos, state, tileEntity, player.getHeldItemMainhand());
        }
    }

    @Override
    public void harvestBlock(@Nonnull World world, PlayerEntity player, @Nonnull BlockPos pos, @Nonnull BlockState state, TileEntity tileEntity, @Nonnull ItemStack stack) {
        if (!player.isCreative()) {
            super.harvestBlock(world, player, pos, state, tileEntity, stack);
        }
        world.removeBlock(pos, false);

        if (tileEntity instanceof ChestBaseTileEntity) {
            ChestBaseTileEntity chestBase = (ChestBaseTileEntity) tileEntity;
            if (chestBase.canBeDouble && !chestBase.canBeSingle) {
                for (int i = 0; i < 4; i++) {
                    Direction horizontal = Direction.byHorizontalIndex((5 - i) % 4); //[W, S, E, N]
                    if (world.getBlockState(pos.offset(horizontal)).getBlock() == this) {
                        this.breakDoubleChest(world, pos.offset(horizontal));
                    }
                }
            }
            chestBase.remove();
        }
    }

    private void breakDoubleChest(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity instanceof ChestBaseTileEntity) {
            ChestBaseTileEntity chestBase = (ChestBaseTileEntity) tileEntity;
            if (!chestBase.isEmpty()) {
                InventoryHelper.dropInventoryItems(world, pos, chestBase);
            }
            world.updateComparatorOutputLevel(pos, this);
        }
        world.removeBlock(pos, false);
    }
}