package com.teammetallurgy.atum.integration.theoneprobe;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.utils.Constants;
import mcjty.theoneprobe.Tools;
import mcjty.theoneprobe.api.*;
import mcjty.theoneprobe.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Objects;

import static mcjty.theoneprobe.api.TextStyleClass.*;

public class AtumProbeInfoProvider implements IProbeInfoProvider, IBlockDisplayOverride {

    @Override
    public String getID() {
        return Constants.MOD_ID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        IProbeConfig config = Config.getRealConfig();

        if (Tools.show(mode, config.getShowCropPercentage())) {
            if (blockState.getBlock() instanceof BlockDate) {
                int age = blockState.getValue(BlockDate.AGE);
                int maxAge = 7;
                if (age == maxAge) {
                    probeInfo.text(OK + "Fully grown");
                } else {
                    probeInfo.text(LABEL + "Growth: " + WARNING + (age * 100) / maxAge + "%");
                }
            }
        }
    }

    @Override
    public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
        IProbeConfig config = Config.getRealConfig();

        if (mode != ProbeMode.DEBUG && !Tools.show(mode, config.getShowSilverfish())) {
            if (blockState.getBlock() instanceof BlockAtumDoor) {
                ResourceLocation location = Objects.requireNonNull(blockState.getBlock().getRegistryName());
                if (location.toString().contains("limestone")) {
                    location = new ResourceLocation(location.toString().replace("_door", ""));
                    ItemStack door = new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(location)));
                    probeInfo.horizontal()
                             .item(door)
                             .vertical()
                             .itemLabel(door)
                             .text(MODNAME + Tools.getModName(blockState.getBlock()));
                    return true;
                }
            }
        }
        return false;
    }
}