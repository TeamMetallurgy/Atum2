package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.blocks.BlockLimestoneBricks;
import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemScarab extends Item {

    public ItemScarab() {
        super();
        super.maxStackSize = 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (AtumConfig.ALLOW_CREATION || player.capabilities.isCreativeMode) {
            IBlockState state = world.getBlockState(pos);
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            IBlockState portalStuctureBlockState = null;
            if (state == Blocks.SANDSTONE.getDefaultState() || state == BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).setBlockUnbreakable().getDefaultState()) {
                portalStuctureBlockState = state;
            }
            if (portalStuctureBlockState != null) {
                for (int x1 = -1; x1 < 1; x1++) {
                    for (int z1 = -1; z1 < 1; z1++) {
                        IBlockState testWaterBlockState = world.getBlockState(new BlockPos(x1 + x, y + 1, z1 + z));
                        if (testWaterBlockState.getBlock() == Blocks.WATER && testWaterBlockState.getValue(BlockLiquid.LEVEL) == 0) {
                            if (AtumBlocks.PORTAL.tryToCreatePortal(world, new BlockPos(x1 + x, y, z1 + z), portalStuctureBlockState)) {
                                player.getHeldItem(hand).shrink(1);
                                return EnumActionResult.SUCCESS;
                            }
                        }
                    }
                }

                if (player.capabilities.isCreativeMode) {
                    for (int x1 = -2; x1 < 3; x1++) {
                        for (int z1 = -2; z1 < 3; z1++) {
                            for (int y1 = 0; y1 < 2; y1++) {
                                world.setBlockState(new BlockPos(x + x1, y + y1, z + z1), portalStuctureBlockState);
                            }
                        }
                    }

                    for (int x1 = -1; x1 < 2; x1++) {
                        for (int z1 = -1; z1 < 2; z1++) {
                            world.setBlockToAir(new BlockPos(x + x1, y + 1, z + z1));
                        }
                    }

                    for (int y1 = 2; y1 < 4; y1++) {
                        world.setBlockState(new BlockPos(x - 2, y + y1, z - 2), portalStuctureBlockState);
                        world.setBlockState(new BlockPos(x + 2, y + y1, z - 2), portalStuctureBlockState);
                        world.setBlockState(new BlockPos(x - 2, y + y1, z + 2), portalStuctureBlockState);
                        world.setBlockState(new BlockPos(x + 2, y + y1, z + 2), portalStuctureBlockState);
                    }
                    AtumBlocks.PORTAL.tryToCreatePortal(world, pos, portalStuctureBlockState);
                }
            }
        } else {
            player.sendMessage(new TextComponentTranslation("chat.atum.disabled"));
        }
        return EnumActionResult.PASS;
    }
}