package com.teammetallurgy.atum.misc.event;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.linen.LinenBlock;
import com.teammetallurgy.atum.blocks.linen.LinenCarpetBlock;
import com.teammetallurgy.atum.blocks.wood.AtumScaffoldingBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class FurnaceFuel {

    @SubscribeEvent
    public static void fuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Block block = Block.byItem(item);
        if (item == AtumBlocks.DEADWOOD_LADDER.get().asItem() || item == AtumBlocks.PALM_LADDER.get().asItem()) {
            event.setBurnTime(300);
        } else if (block instanceof LinenBlock) {
            event.setBurnTime(100);
        } else if (block instanceof LinenCarpetBlock) {
            event.setBurnTime(67);
        } else if (item == AtumItems.PALM_STICK.get() || item == AtumItems.DEADWOOD_STICK.get()) {
            event.setBurnTime(100);
        } else if (block instanceof AtumScaffoldingBlock) {
            event.setBurnTime(400);
        }
    }
}