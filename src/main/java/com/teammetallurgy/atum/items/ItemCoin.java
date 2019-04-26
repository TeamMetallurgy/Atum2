package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemCoin extends Item {

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        World world = entityItem.world;
        IBlockState state = world.getBlockState(new BlockPos(MathHelper.floor(entityItem.posX), MathHelper.floor(entityItem.posY), MathHelper.floor(entityItem.posZ)));
        if ((state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER) || state.getBlock() instanceof BlockCauldron && state.getValue(BlockCauldron.LEVEL) > 0 && entityItem.getItem().getItem() == AtumItems.DIRTY_COIN) {
            ItemStack stack = entityItem.getItem();
            if (!world.isRemote) {
                while (stack.getCount() > 0) {
                    if (itemRand.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_ITEM_BREAK, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                    } else {
                        world.spawnEntity(new EntityItem(world, entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(AtumItems.GOLD_COIN)));
                        world.playSound(null, entityItem.posX, entityItem.posY, entityItem.posZ, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(entityItem);
    }
}