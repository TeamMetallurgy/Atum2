package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class EmmerFlourItem extends Item {

    public EmmerFlourItem() {
        super(new Item.Properties().tab(Atum.GROUP));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldStack);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldStack);
        } else {
            BlockPos pos = blockHitResult.getBlockPos();
            if (level.mayInteract(player, pos) && player.mayUseItemAt(pos, blockHitResult.getDirection(), heldStack)) {
                BlockState state = level.getBlockState(pos);

                if (state.getBlock() instanceof LayeredCauldronBlock) {
                    int cauldronLevel = state.getValue(LayeredCauldronBlock.LEVEL);
                    if (cauldronLevel > 0) {
                        LayeredCauldronBlock.lowerFillLevel(state, level, pos);
                        this.giveDough(player, hand, heldStack);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
                    } else {
                        return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
                    }
                } else if (state.getBlock() instanceof BucketPickup bucketPickup) {
                    ItemStack stack = ((BucketPickup) state.getBlock()).pickupBlock(level, pos, state);
                    if (!stack.isEmpty()) {
                        player.awardStat(Stats.ITEM_USED.get(this));
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
                        this.giveDough(player, hand, heldStack);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
                    }
                }
                return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
            } else {
                return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
            }
        }
    }

    private void giveDough(Player player, InteractionHand hand, @Nonnull ItemStack heldStack) {
        player.awardStat(Stats.ITEM_USED.get(this));
        player.playSound(SoundEvents.LILY_PAD_PLACE, 1.0F, 1.0F);
        StackHelper.giveItem(player, hand, new ItemStack(AtumItems.EMMER_DOUGH.get()));
        heldStack.shrink(1);
    }
}