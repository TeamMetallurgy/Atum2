package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;

public class AtumMenuType {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_DEFERRED = DeferredRegister.create(Registries.MENU, Atum.MOD_ID);
    public static final DeferredHolder<MenuType<?>, MenuType<AlphaDesertWolfContainer>> ALPHA_DESERT_WOLF = register(IMenuTypeExtension.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new AlphaDesertWolfContainer(windowID, inv, entityID);
    }), "alpha_desert_wolf");
    public static final DeferredHolder<MenuType<?>, MenuType<CamelContainer>> CAMEL = register(IMenuTypeExtension.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new CamelContainer(windowID, inv, entityID);
    }), "camel");
    public static final DeferredHolder<MenuType<?>, MenuType<KilnContainer>> KILN = register(IMenuTypeExtension.create((windowID, inv, data) -> new KilnContainer(windowID, inv, data.readBlockPos())), "kiln");
    public static final DeferredHolder<MenuType<?>, MenuType<TrapContainer>> TRAP = register(IMenuTypeExtension.create((windowID, inv, data) -> {
        TrapTileEntity trap = (TrapTileEntity) inv.player.level().getBlockEntity(data.readBlockPos());
        return new TrapContainer(windowID, inv, trap);
    }), "trap");
    public static final DeferredHolder<MenuType<?>, MenuType<GodforgeContainer>> GODFORGE = register(IMenuTypeExtension.create((windowID, inv, data) -> {
        GodforgeTileEntity godforge = (GodforgeTileEntity) inv.player.level().getBlockEntity(data.readBlockPos());
        return new GodforgeContainer(windowID, inv, godforge);
    }), "godforge");


    private static <T extends AbstractContainerMenu> DeferredHolder<MenuType<?>, MenuType<T>> register(@Nonnull MenuType<T> container, @Nonnull String name) {
        return MENU_TYPE_DEFERRED.register(name, () -> container);
    }
}