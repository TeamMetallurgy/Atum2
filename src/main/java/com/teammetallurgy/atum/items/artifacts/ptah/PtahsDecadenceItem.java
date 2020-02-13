package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class PtahsDecadenceItem extends PickaxeItem {

    public PtahsDecadenceItem() {
        super(ItemTier.DIAMOND, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void harvestEvent(BlockEvent.HarvestDropsEvent event) {
        World world = event.getWorld().getWorld();
        if (!world.isRemote && world instanceof ServerWorld && event.getHarvester() != null && event.getHarvester().getHeldItemMainhand().getItem() == AtumItems.PTAHS_DECADENCE) {
            ServerWorld serverWorld = (ServerWorld) world;
            List<ItemStack> drops = Block.func_220070_a(event.getState(), serverWorld, event.getPos(), null);
            if (!drops.isEmpty()) {
                for (ItemStack itemDropped : drops) {
                    Block dropBlock = Block.getBlockFromItem(itemDropped.getItem());
                    if ((dropBlock == AtumBlocks.IRON_ORE || dropBlock == Blocks.IRON_ORE) && serverWorld.rand.nextFloat() <= 0.50F) {
                        if (dropBlock == AtumBlocks.IRON_ORE) {
                            event.getDrops().clear();
                            event.getDrops().add(new ItemStack(AtumBlocks.GOLD_ORE));
                            serverWorld.playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        } else {
                            event.getDrops().clear();
                            event.getDrops().add(new ItemStack(Blocks.GOLD_ORE));
                            serverWorld.playSound(null, event.getPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }
}