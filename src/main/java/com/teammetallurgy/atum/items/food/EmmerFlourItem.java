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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;

public class EmmerFlourItem extends Item {

    public EmmerFlourItem() {
        super(new Item.Properties().tab(Atum.GROUP));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        HitResult rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.SOURCE_ONLY);

        if (rayTrace.getType() == HitResult.Type.MISS) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldStack);
        } else if (rayTrace.getType() != HitResult.Type.BLOCK) {
            return new InteractionResultHolder<>(InteractionResult.PASS, heldStack);
        } else {
            BlockHitResult blockRayTrace = (BlockHitResult) rayTrace;
            BlockPos pos = blockRayTrace.getBlockPos();
            if (world.mayInteract(player, pos) && player.mayUseItemAt(pos, blockRayTrace.getDirection(), heldStack)) {
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof CauldronBlock) {
                    CauldronBlock cauldron = (CauldronBlock) state.getBlock();
                    int level = state.getValue(CauldronBlock.LEVEL);
                    if (level > 0) {
                        cauldron.setWaterLevel(world, pos, state, level - 1);
                        this.giveDough(player, hand, heldStack);
                        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
                    } else {
                        return new InteractionResultHolder<>(InteractionResult.FAIL, heldStack);
                    }
                } else if (state.getBlock() instanceof BucketPickup) {
                    Fluid fluid = ((BucketPickup) state.getBlock()).takeLiquid(world, pos, state);
                    if (fluid.is(FluidTags.WATER)) {
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 11);
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
        StackHelper.giveItem(player, hand, new ItemStack(AtumItems.EMMER_DOUGH));
        heldStack.shrink(1);
    }
}