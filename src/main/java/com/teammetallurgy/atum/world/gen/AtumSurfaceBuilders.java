package com.teammetallurgy.atum.world.gen;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class AtumSurfaceBuilders {
    public static final BlockState SAND = AtumBlocks.SAND.getDefaultState();
    public static final SurfaceBuilderConfig SANDY = new SurfaceBuilderConfig(SAND, SAND, SAND);

    @SubscribeEvent
    public static void registerSurfaceBuilders(RegistryEvent.Register<SurfaceBuilder<?>> event) {

    }
}