package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EmmerFlourItem extends Item {

    public EmmerFlourItem() {
        super(new Item.Properties().group(Atum.GROUP));
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        RayTraceResult rayTrace = rayTrace(world, player, RayTraceContext.FluidMode.SOURCE_ONLY);

        if (rayTrace.getType() == RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, heldStack);
        } else if (rayTrace.getType() != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(ActionResultType.PASS, heldStack);
        } else {
            BlockRayTraceResult blockRayTrace = (BlockRayTraceResult) rayTrace;
            BlockPos pos = blockRayTrace.getPos();
            if (world.isBlockModifiable(player, pos) && player.canPlayerEdit(pos, blockRayTrace.getFace(), heldStack)) {
                BlockState state = world.getBlockState(pos);

                if (state.getBlock() instanceof CauldronBlock) {
                    CauldronBlock cauldron = (CauldronBlock) state.getBlock();
                    int level = state.get(CauldronBlock.LEVEL);
                    if (level > 0) {
                        cauldron.setWaterLevel(world, pos, state, level - 1);
                        this.giveDough(player, hand, heldStack);
                        return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
                    } else {
                        return new ActionResult<>(ActionResultType.FAIL, heldStack);
                    }
                } else if (state.getBlock() instanceof IBucketPickupHandler) {
                    Fluid fluid = ((IBucketPickupHandler) state.getBlock()).pickupFluid(world, pos, state);
                    if (fluid.isIn(FluidTags.WATER)) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                        this.giveDough(player, hand, heldStack);
                        return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
                    }
                }
                return new ActionResult<>(ActionResultType.FAIL, heldStack);
            } else {
                return new ActionResult<>(ActionResultType.FAIL, heldStack);
            }
        }
    }

    private void giveDough(PlayerEntity player, Hand hand, @Nonnull ItemStack heldStack) {
        player.addStat(Stats.ITEM_USED.get(this));
        player.playSound(SoundEvents.BLOCK_LILY_PAD_PLACE, 1.0F, 1.0F);
        StackHelper.giveItem(player, hand, new ItemStack(AtumItems.EMMER_DOUGH));
        heldStack.shrink(1);
    }
}