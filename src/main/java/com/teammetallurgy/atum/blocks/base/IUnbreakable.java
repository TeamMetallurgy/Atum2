package com.teammetallurgy.atum.blocks.base;

import com.teammetallurgy.atum.Atum;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public interface IUnbreakable {
    BooleanProperty UNBREAKABLE = BooleanProperty.create("unbreakable");

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof IUnbreakable && state.get(IUnbreakable.UNBREAKABLE) && !event.getPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }
}