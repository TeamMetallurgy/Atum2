package com.teammetallurgy.atum.handler.event;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(value = Side.SERVER)
public class ServerEvents { //TODO Why is this here?

    //private boolean raining; //TODO Why is this here?

    @SubscribeEvent
    public static void onServerTick(TickEvent.PlayerTickEvent event) { //TODO Why is this here?
    }
}