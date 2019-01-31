package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.utils.AtumUtils;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
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
                    tooltip.add(AtumUtils.format("hud.msg.growth") + " : " + AtumUtils.format("hud.msg.growth.value", (int) growthValue));
                } else {
                    tooltip.add(AtumUtils.format("hud.msg.growth") + " : " + AtumUtils.format("hud.msg.mature"));
                }
            }
        }
        return tooltip;
    }
}