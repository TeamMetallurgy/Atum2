package com.teammetallurgy.atum.integration.champion;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;

public class ChampionHelper {

    public static boolean isChampion(Entity entity) {
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBT(compound);
        if (compound.hasKey("ForgeCaps")) {
            NBTTagCompound forgeCaps = compound.getCompoundTag("ForgeCaps");
            return forgeCaps.hasKey("champions:championship");
        }
        return false;
    }

    public static int getTier(Entity entity) {
        NBTTagCompound compound = new NBTTagCompound();
        entity.writeToNBT(compound);
        if (compound.hasKey("ForgeCaps")) {
            NBTTagCompound forgeCaps = compound.getCompoundTag("ForgeCaps");
            if (forgeCaps.hasKey("champions:championship")) {
                NBTTagCompound map = forgeCaps.getCompoundTag("champions:championship");
                if (map.hasKey("tier")) {
                    return map.getInteger("tier");
                }
            }
        }
        return 0;
    }
}
