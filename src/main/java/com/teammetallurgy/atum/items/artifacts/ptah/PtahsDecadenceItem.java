package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.Rarity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PtahsDecadenceItem extends PickaxeItem {

    public PtahsDecadenceItem() {
        super(AtumMats.NEBU, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        IWorld world = event.getWorld();
        if (world instanceof ServerWorld && event.getPlayer().getHeldItemMainhand().getItem() == AtumItems.PTAHS_DECADENCE) {
            ServerWorld serverWorld = (ServerWorld) world;
            BlockPos pos = event.getPos();
            List<ItemStack> drops = Block.getDrops(event.getState(), serverWorld, pos, null);
            if (!drops.isEmpty()) {
                for (ItemStack itemDropped : drops) {
                    Block dropBlock = Block.getBlockFromItem(itemDropped.getItem());
                    if ((dropBlock == AtumBlocks.IRON_ORE || dropBlock == Blocks.IRON_ORE) && serverWorld.rand.nextFloat() <= 0.50F) {
                        if (dropBlock == AtumBlocks.IRON_ORE) {
                            event.setCanceled(true);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                            Block.spawnDrops(AtumBlocks.GOLD_ORE.getDefaultState(), serverWorld, pos);
                            serverWorld.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        } else {
                            event.setCanceled(true);
                            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                            Block.spawnDrops(Blocks.GOLD_ORE.getDefaultState(), serverWorld, pos);
                            serverWorld.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }
}