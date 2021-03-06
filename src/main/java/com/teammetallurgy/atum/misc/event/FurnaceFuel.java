package com.teammetallurgy.atum.misc.event;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.linen.LinenBlock;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.blocks.wood.AtumScaffoldingBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class FurnaceFuel {

    @SubscribeEvent
    public static void fuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (item == AtumBlocks.DEADWOOD_LADDER.asItem() || item == AtumBlocks.PALM_LADDER.asItem()) {
            event.setBurnTime(300);
        } else if (block instanceof LinenBlock) {
            event.setBurnTime(100);
        } else if (block instanceof LinenCarpetBlock) {
            event.setBurnTime(67);
        } else if (item == AtumItems.PALM_STICK || item == AtumItems.DEADWOOD_STICK) {
            event.setBurnTime(100);
        } else if (block instanceof AtumScaffoldingBlock) {
            event.setBurnTime(400);
        }
    }
}