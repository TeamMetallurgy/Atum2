package com.teammetallurgy.atum.blocks.lighting;

import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredBlock;

import javax.annotation.Nonnull;
import java.util.Map;

public class AtumWallTorchUnlitBlock extends AtumWallTorch {
    protected final Block litWallBlock;
    public static final Map<DeferredBlock<Block>, DeferredBlock<Block>> UNLIT = Maps.newHashMap();

    public AtumWallTorchUnlitBlock(Block litWallBlock, Properties properties) {
        super(properties);
        this.litWallBlock = litWallBlock;
    }

    @Override
    @Nonnull
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, Player player, @Nonnull InteractionHand hand, @Nonnull BlockHitResult rayTraceResult) { //Copied from AtumTorchUnlitBlock
        ItemStack heldStack = player.getItemInHand(hand);
        Block block = Block.byItem(heldStack.getItem());
        if ((heldStack.getItem() instanceof FlintAndSteelItem || block.getLightEmission(block.defaultBlockState(), level, pos) > 0)) {
            if (heldStack.getItem().canBeDepleted()) {
                heldStack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(hand));
            }
            level.setBlockAndUpdate(pos, this.litWallBlock.defaultBlockState().setValue(FACING, state.getValue(FACING))); //This line is changed, compared to AtumTorchUnlitBlock
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 2.5F, 1.0F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, @Nonnull RandomSource rand) {
    }
}