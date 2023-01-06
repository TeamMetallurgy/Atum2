package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.inventory.container.block.GodforgeContainer;
import com.teammetallurgy.atum.inventory.container.block.KilnContainer;
import com.teammetallurgy.atum.inventory.container.block.TrapContainer;
import com.teammetallurgy.atum.inventory.container.entity.AlphaDesertWolfContainer;
import com.teammetallurgy.atum.inventory.container.entity.CamelContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class AtumMenuType {
    public static final DeferredRegister<MenuType<?>> MENU_TYPE_DEFERRED = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Atum.MOD_ID);
    public static final RegistryObject<MenuType<AlphaDesertWolfContainer>> ALPHA_DESERT_WOLF = register(IForgeMenuType.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new AlphaDesertWolfContainer(windowID, inv, entityID);
    }), "alpha_desert_wolf");
    public static final RegistryObject<MenuType<CamelContainer>> CAMEL = register(IForgeMenuType.create((windowID, inv, data) -> {
        int entityID = data.readInt();
        return new CamelContainer(windowID, inv, entityID);
    }), "camel");
    public static final RegistryObject<MenuType<KilnContainer>> KILN = register(IForgeMenuType.create((windowID, inv, data) -> new KilnContainer(windowID, inv, data.readBlockPos())), "kiln");
    public static final RegistryObject<MenuType<TrapContainer>> TRAP = register(IForgeMenuType.create((windowID, inv, data) -> {
        TrapTileEntity trap = (TrapTileEntity) inv.player.level.getBlockEntity(data.readBlockPos());
        return new TrapContainer(windowID, inv, trap);
    }), "trap");
    public static final RegistryObject<MenuType<GodforgeContainer>> GODFORGE = register(IForgeMenuType.create((windowID, inv, data) -> {
        GodforgeTileEntity godforge = (GodforgeTileEntity) inv.player.level.getBlockEntity(data.readBlockPos());
        return new GodforgeContainer(windowID, inv, godforge);
    }), "godforge");


    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> register(@Nonnull MenuType<T> container, @Nonnull String name) {
        return MENU_TYPE_DEFERRED.register(name, () -> container);
    }
}