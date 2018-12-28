package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class FurnaceFuel {

    @SubscribeEvent
    public static void fuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == getItem(AtumBlocks.DEADWOOD_LADDER) || stack.getItem() == getItem(AtumBlocks.PALM_LADDER)) {
            event.setBurnTime(300);
        }
    }

    private static Item getItem(Block block) {
        return Item.getItemFromBlock(block);
    }
}