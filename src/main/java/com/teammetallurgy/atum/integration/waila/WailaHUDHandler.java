package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.BlockAtumDoor;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;

public class WailaHUDHandler implements IWailaDataProvider {
    @Override
    @Nonnull
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockAtumDoor && config.getConfig("vanilla.silverfish")) {
            ResourceLocation location = Objects.requireNonNull(accessor.getBlock().getRegistryName());
            if (location.toString().contains("limestone")) {
                location = new ResourceLocation(location.toString().replace("_door", ""));
                return new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(location)));
            }
        }
        return accessor.getStack();
    }

    public static void register() {
        IWailaDataProvider provider = new WailaHUDHandler();
        ModuleRegistrar.instance().registerStackProvider(provider, BlockAtumDoor.class);
    }
}