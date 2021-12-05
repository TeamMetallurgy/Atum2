package com.teammetallurgy.atum.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AtumDimensionData extends WorldSavedData {
    public static final String ID = "atum_dimension_data";
    private boolean hasStartStructureSpawned;
    private boolean isStorming;
    private final List<MutableBoundingBox> beatenPyramids = new ArrayList<>();

    public AtumDimensionData() {
        super(ID);
    }

    @Override
    public void read(@Nonnull CompoundNBT nbt) {
        this.hasStartStructureSpawned = nbt.getBoolean("HasStartStructureSpawned");
        this.isStorming = nbt.getBoolean("IsStorming");

        ListNBT listNBT = nbt.getList("PyramidBoxes", Constants.NBT.TAG_INT_ARRAY);
        for (int i = 0; i < listNBT.size(); ++i) {
            this.beatenPyramids.add(new MutableBoundingBox(listNBT.getIntArray(i)));
        }
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbt) {
        nbt.putBoolean("HasStartStructureSpawned", this.hasStartStructureSpawned);
        nbt.putBoolean("IsStorming", this.isStorming);

        if (!this.beatenPyramids.isEmpty()) {
            ListNBT listNBT = new ListNBT();
            for (MutableBoundingBox box : this.beatenPyramids) {
                listNBT.add(box.toNBTTagIntArray());
            }
            nbt.put("PyramidBoxes", listNBT);
        }
        return nbt;
    }

    public boolean hasStartStructureSpawned() {
        return this.hasStartStructureSpawned;
    }

    public boolean isStorming() {
        return this.isStorming;
    }

    public List<MutableBoundingBox> getBeatenPyramids() {
        return this.beatenPyramids;
    }

    public void setHasStartStructureSpawned(boolean hasStartStructureSpawned) {
        this.hasStartStructureSpawned = hasStartStructureSpawned;
        this.markDirty();
    }

    public void setStorming(boolean isStorming) {
        this.isStorming = isStorming;
        this.markDirty();
    }

    public void addBeatenPyramid(MutableBoundingBox box) {
        this.beatenPyramids.add(box);
        this.markDirty();
    }
}