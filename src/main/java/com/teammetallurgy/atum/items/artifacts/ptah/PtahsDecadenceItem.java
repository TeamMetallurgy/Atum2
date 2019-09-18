package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class PtahsDecadenceItem extends PickaxeItem {

    public PtahsDecadenceItem() {
        super(ToolMaterial.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void harvestEvent(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld();
        if (!world.isRemote && event.getHarvester() != null && event.getHarvester().getHeldItemMainhand().getItem() == AtumItems.PTAHS_DECADENCE) {
            Item itemDropped = event.getState().getBlock().getItemDropped(event.getState(), random, 0);
            if (itemDropped == null || itemDropped == Items.AIR) return;
            Block dropBlock = Block.getBlockFromItem(itemDropped);
            if ((dropBlock == AtumBlocks.IRON_ORE || dropBlock == Blocks.IRON_ORE) && world.rand.nextFloat() <= 0.50F) {
                if (dropBlock == AtumBlocks.IRON_ORE) {
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(AtumBlocks.GOLD_ORE));
                    world.playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                } else {
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Blocks.GOLD_ORE));
                    world.playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }
}