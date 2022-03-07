package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.*;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PtahsDecadenceItem extends PickaxeItem implements IArtifact {

    public PtahsDecadenceItem() {
        super(AtumMats.NEBU, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public God getGod() {
        return God.PTAH;
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getWorld();
        if (world instanceof ServerLevel serverLevel && event.getPlayer().getMainHandItem().getItem() == AtumItems.PTAHS_DECADENCE.get()) {
            BlockPos pos = event.getPos();
            List<ItemStack> drops = Block.getDrops(event.getState(), serverLevel, pos, null);
            if (!drops.isEmpty()) {
                for (ItemStack itemDropped : drops) {
                    Item dropItem = itemDropped.getItem();
                    if ((dropItem == Items.RAW_IRON) && serverLevel.random.nextFloat() <= 0.35F) {
                        event.setCanceled(true);
                        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                        Block.popResource(serverLevel, pos, new ItemStack(Items.RAW_GOLD));
                        serverLevel.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
    }
}