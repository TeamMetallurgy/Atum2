package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.blocks.linen.LinenBlock;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class FurnaceFuel {

    @SubscribeEvent
    public static void fuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == AtumBlocks.DEADWOOD_LADDER.asItem() || stack.getItem() == AtumBlocks.PALM_LADDER.asItem()) {
            event.setBurnTime(300);
        } else if (Block.getBlockFromItem(stack.getItem()) instanceof LinenBlock && !(Block.getBlockFromItem(stack.getItem()) instanceof LinenCarpetBlock)) {
            event.setBurnTime(100);
        } else if (Block.getBlockFromItem(stack.getItem()) instanceof LinenCarpetBlock) {
            event.setBurnTime(67);
        } else if (stack.getItem() == AtumItems.PALM_STICK || stack.getItem() == AtumItems.DEADWOOD_STICK) {
            event.setBurnTime(100);
        }
    }
}