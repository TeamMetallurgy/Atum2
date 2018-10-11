package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.cbcore.LangUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
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

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        if (accessor.getBlock() instanceof BlockDate) {
            if (config.getConfig("general.showcrop")) {
                float growthValue = ((float) accessor.getMetadata() / 7.0F) * 100.0F;
                if (growthValue < 100.0F) {
                    tooltip.add(LangUtil.translateG("hud.msg.growth") + " : " + LangUtil.translateG("hud.msg.growth.value", (int) growthValue));
                } else {
                    tooltip.add(LangUtil.translateG("hud.msg.growth") + " : " + LangUtil.translateG("hud.msg.mature"));
                }
            }
        }
        return tooltip;
    }

    public static void register() {
        IWailaDataProvider provider = new WailaHUDHandler();
        ModuleRegistrar.instance().registerStackProvider(provider, BlockAtumDoor.class);
        ModuleRegistrar.instance().registerBodyProvider(provider, BlockDate.class);
    }
}