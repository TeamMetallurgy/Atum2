package com.teammetallurgy.atum.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
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
    public static List<ContainerType<?>> CONTAINERS = Lists.newArrayList();
    public static final ContainerType<AlphaDesertWolfContainer> ALPHA_DESERT_WOLF = register(IForgeContainerType.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new AlphaDesertWolfContainer(windowID, inv, entityID);
    }), "alpha_desert_wolf");
    public static final ContainerType<CamelContainer> CAMEL = register(IForgeContainerType.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new CamelContainer(windowID, inv, entityID);
    }), "camel");
    public static final ContainerType<KilnContainer> KILN = register(IForgeContainerType.create((windowID, inv, data) -> {
        KilnTileEntity kiln = (KilnTileEntity) inv.player.world.getTileEntity(data.readBlockPos());
        return new KilnContainer(windowID, inv, kiln);
    }), "kiln");
    public static final ContainerType<TrapContainer> TRAP = register(IForgeContainerType.create((windowID, inv, data) -> {
        TrapTileEntity trap = (TrapTileEntity) inv.player.world.getTileEntity(data.readBlockPos());
        return new TrapContainer(windowID, inv, trap);
    }), "trap");


    private static <T extends Container> ContainerType<T> register(@Nonnull ContainerType<T> container, @Nonnull String name) {
        container.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
        CONTAINERS.add(container);
        return container;
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        for (ContainerType<?> container : CONTAINERS) {
            event.getRegistry().register(container);
        }
    }
}