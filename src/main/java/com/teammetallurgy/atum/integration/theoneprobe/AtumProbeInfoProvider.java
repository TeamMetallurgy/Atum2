package com.teammetallurgy.atum.integration.theoneprobe;

import com.teammetallurgy.atum.blocks.base.BlockAtumDoor;
import com.teammetallurgy.atum.blocks.vegetation.BlockDate;
import com.teammetallurgy.atum.utils.Constants;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Objects;

import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.EXTENDED;
import static mcjty.theoneprobe.api.IProbeConfig.ConfigMode.NORMAL;
import static mcjty.theoneprobe.api.TextStyleClass.*;

public class AtumProbeInfoProvider implements IProbeInfoProvider, IBlockDisplayOverride {

    @Override
    public String getID() {
        return Constants.MOD_ID;
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        IProbeConfig config = TOPSupport.getProbeConfig();

        if (this.show(mode, config.getShowCropPercentage())) {
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
    public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        IProbeConfig config = TOPSupport.getProbeConfig();

        if (mode != ProbeMode.DEBUG && !this.show(mode, config.getShowSilverfish())) {
            if (blockState.getBlock() instanceof BlockAtumDoor) {
                ResourceLocation location = Objects.requireNonNull(blockState.getBlock().getRegistryName());
                if (location.toString().contains("limestone")) {
                    location = new ResourceLocation(location.toString().replace("_door", ""));
                    ItemStack door = new ItemStack(Objects.requireNonNull(Item.REGISTRY.getObject(location)));
                    probeInfo.horizontal()
                            .item(door)
                            .vertical()
                            .itemLabel(door)
                            .text(MODNAME + Constants.MOD_NAME.replace("2", " 2")); //Lazy way to get it to show properly
                    return true;
                }
            }
        }
        return false;
    }

    private boolean show(ProbeMode mode, IProbeConfig.ConfigMode cfg) {
        return cfg == NORMAL || (cfg == EXTENDED && mode == ProbeMode.EXTENDED);
    }
}