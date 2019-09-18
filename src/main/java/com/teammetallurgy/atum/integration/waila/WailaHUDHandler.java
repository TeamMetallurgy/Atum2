package com.teammetallurgy.atum.integration.waila;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;

public class WailaHUDHandler implements IComponentProvider {
    static final WailaHUDHandler INSTANCE = new WailaHUDHandler();

    @Override
    @Nonnull
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof BlockAtumDoor && config.get(new ResourceLocation("hide_infestations"))) {
            ResourceLocation location = accessor.getBlock().getRegistryName();
            if (location != null && location.toString().contains("limestone")) {
                location = new ResourceLocation(location.toString().replace("_door", ""));
                return new ItemStack(ForgeRegistries.ITEMS.getValue(location));
            }
        }
        return accessor.getStack();
    }

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof BlockDate) {
            if (config.get(new ResourceLocation("crop_progress"))) {
                addMaturityTooltip(tooltip, accessor.getBlockState().get((BlockDate.AGE)) / 7.0F);
            }
        }
    }

    private static void addMaturityTooltip(List<ITextComponent> tooltip, float growthValue) {
        growthValue *= 100.0F;
        if (growthValue < 100.0F) {
            tooltip.add(new TranslationTextComponent("tooltip.waila.crop_growth", String.format("%.0f%%", growthValue)));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.waila.crop_growth", new TranslationTextComponent("tooltip.waila.crop_mature")));
        }
    }
}