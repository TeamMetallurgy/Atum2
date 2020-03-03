package com.teammetallurgy.atum.blocks.wood;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

public class AtumWallTorchUnlitBlock extends AtumWallTorch {
    protected final Block litWallBlock;

    public AtumWallTorchUnlitBlock(Block litWallBlock, Properties properties) {
        super(properties);
        this.litWallBlock = litWallBlock;
    }

    @Override
    public boolean onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) { //Copied from AtumTorchUnlitBlock
        ItemStack heldStack = player.getHeldItem(hand);
        Block block = Block.getBlockFromItem(heldStack.getItem());
        if ((heldStack.getItem() instanceof FlintAndSteelItem || block.getLightValue(block.getDefaultState(), world, pos) > 0)) {
            if (heldStack.getItem().isDamageable()) {
                heldStack.damageItem(1, player, (p) -> p.sendBreakAnimation(hand));
            }
            world.setBlockState(pos, this.litWallBlock.getDefaultState().with(HORIZONTAL_FACING, state.get(HORIZONTAL_FACING))); //This line is changed, compared to AtumTorchUnlitBlock
            world.playSound(null, pos, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 2.5F, 1.0F);
            return true;
        }
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {
    }
}