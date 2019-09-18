package com.teammetallurgy.atum.utils.event;

import com.teammetallurgy.atum.blocks.linen.BlockLinen;
import com.teammetallurgy.atum.blocks.linen.BlockLinenCarpet;
import com.teammetallurgy.atum.blocks.wood.BlockAtumPlank;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class FurnaceFuel {

    @SubscribeEvent
    public static void fuel(FurnaceFuelBurnTimeEvent event) {
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == getItem(AtumBlocks.DEADWOOD_LADDER) || stack.getItem() == getItem(AtumBlocks.PALM_LADDER)) {
            event.setBurnTime(300);
        } else if (Block.getBlockFromItem(stack.getItem()) instanceof BlockLinen && !(Block.getBlockFromItem(stack.getItem()) instanceof BlockLinenCarpet)) {
            event.setBurnTime(100);
        } else if (Block.getBlockFromItem(stack.getItem()) instanceof BlockLinenCarpet) {
            event.setBurnTime(67);
        } else if (stack.getItem() == BlockAtumPlank.getStick(BlockAtumPlank.WoodType.PALM) || stack.getItem() == BlockAtumPlank.getStick(BlockAtumPlank.WoodType.DEADWOOD)) {
            event.setBurnTime(100);
        }
    }

    private static Item getItem(Block block) {
        return Item.getItemFromBlock(block);
    }
}