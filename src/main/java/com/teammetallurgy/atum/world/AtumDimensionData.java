package com.teammetallurgy.atum.world;

import com.teammetallurgy.atum.Atum;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.storage.WorldSavedData;

import javax.annotation.Nonnull;

public class AtumDimensionData extends WorldSavedData {
    private boolean hasStartStructureSpawned;
    private boolean isStorming;

    public AtumDimensionData() {
        super(Atum.MOD_ID);
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        this.hasStartStructureSpawned = nbt.getBoolean("HasStartStructureSpawned");
        this.isStorming = nbt.getBoolean("IsStorming");
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.putBoolean("HasStartStructureSpawned", this.hasStartStructureSpawned);
        nbt.putBoolean("IsStorming", this.isStorming);
        return nbt;
    }

    public boolean hasStartStructureSpawned() {
        return this.hasStartStructureSpawned;
    }

    public boolean isStorming() {
        return this.isStorming;
    }

    public void setHasStartStructureSpawned(boolean hasStartStructureSpawned) {
        this.hasStartStructureSpawned = hasStartStructureSpawned;
        this.markDirty();
    }

    public void setStorming(boolean isStorming) {
        this.isStorming = isStorming;
        this.markDirty();
    }
}