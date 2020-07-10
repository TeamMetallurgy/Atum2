package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class CoinItem extends Item {

    public CoinItem() {
        super(new Item.Properties().group(Atum.GROUP));
    }

    @Override
    public boolean onEntityItemUpdate(@Nonnull ItemStack stack, ItemEntity entityItem) {
        World world = entityItem.world;
        BlockState state = world.getBlockState(new BlockPos(MathHelper.floor(entityItem.getPosX()), MathHelper.floor(entityItem.getPosY()), MathHelper.floor(entityItem.getPosZ())));
        if ((state.getFluidState().isTagged(FluidTags.WATER) || state.getBlock() instanceof CauldronBlock && state.get(CauldronBlock.LEVEL) > 0) && entityItem.getItem().getItem() == AtumItems.DIRTY_COIN) {
            if (!world.isRemote) {
                while (stack.getCount() > 0) {
                    if (random.nextFloat() <= 0.10F) {
                        stack.shrink(1);
                        world.playSound(null, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), SoundEvents.ENTITY_ITEM_BREAK, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                    } else {
                        world.addEntity(new ItemEntity(world, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), new ItemStack(AtumItems.GOLD_COIN)));
                        world.playSound(null, entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, entityItem.getSoundCategory(), 0.8F, 0.8F + entityItem.world.rand.nextFloat() * 0.4F);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.onEntityItemUpdate(stack, entityItem);
    }
}