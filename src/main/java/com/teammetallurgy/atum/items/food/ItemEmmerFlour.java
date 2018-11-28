package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.IOreDictEntry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ItemEmmerFlour extends Item implements IOreDictEntry {

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        RayTraceResult rayTrace = this.rayTrace(world, player, true);

        if (rayTrace == null) {
            return new ActionResult<>(EnumActionResult.PASS, heldStack);
        } else if (rayTrace.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(EnumActionResult.PASS, heldStack);
        } else {
            BlockPos pos = rayTrace.getBlockPos();
            if (!world.isBlockModifiable(player, pos)) {
                return new ActionResult<>(EnumActionResult.FAIL, heldStack);
            } else {
                if (!player.canPlayerEdit(pos.offset(rayTrace.sideHit), rayTrace.sideHit, heldStack)) {
                    return new ActionResult<>(EnumActionResult.FAIL, heldStack);
                } else {
                    IBlockState state = world.getBlockState(pos);

                    if (state.getBlock() instanceof BlockCauldron) {
                        BlockCauldron cauldron = (BlockCauldron) state.getBlock();
                        int level = state.getValue(BlockCauldron.LEVEL);
                        if (level > 0) {
                            cauldron.setWaterLevel(world, pos, state, level - 1);
                            this.giveDough(player, hand, heldStack);
                            return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                        } else {
                            return new ActionResult<>(EnumActionResult.FAIL, heldStack);
                        }
                    } else if (state.getMaterial() == Material.WATER && state.getValue(BlockLiquid.LEVEL) == 0) {
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
                        this.giveDough(player, hand, heldStack);
                        return new ActionResult<>(EnumActionResult.SUCCESS, heldStack);
                    } else {
                        return new ActionResult<>(EnumActionResult.FAIL, heldStack);
                    }
                }
            }
        }
    }

    private void giveDough(EntityPlayer player, EnumHand hand, ItemStack heldStack) {
        player.addStat(Objects.requireNonNull(StatList.getObjectUseStats(this)));
        player.playSound(SoundEvents.BLOCK_WATERLILY_PLACE, 1.0F, 1.0F);
        StackHelper.giveItem(player, hand, new ItemStack(AtumItems.EMMER_DOUGH));
        heldStack.shrink(1);
    }

    @Override
    public void getOreDictEntries() {
        OreDictHelper.add(this, "foodFlour");
    }
}