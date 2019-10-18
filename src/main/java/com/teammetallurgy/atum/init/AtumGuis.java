package com.teammetallurgy.atum.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(Constants.MOD_ID)
public class AtumGuis {
    public static List<ContainerType> CONTAINERS = Lists.newArrayList();
    public static final ContainerType<ContainerKiln> KILN = register(IForgeContainerType.create((windowID, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        return new ContainerKiln(windowID, pos, inv);
    }), "kiln");


    private static <T extends Container> ContainerType<T> register(@Nonnull ContainerType<T> container, @Nonnull String name) {
        container.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
        CONTAINERS.add(container);
        return container;
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        for (ContainerType container : CONTAINERS) {
            event.getRegistry().register(container);
        }
    }
}