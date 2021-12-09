package com.teammetallurgy.atum.world;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.saveddata.SavedData;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class AtumDimensionData extends SavedData {
    public static final String ID = "atum_dimension_data";
    public boolean hasStartStructureSpawned;
    public boolean isStorming;
    public final List<BoundingBox> beatenPyramids = new ArrayList<>();

    public static AtumDimensionData load(@Nonnull CompoundTag nbt) {
        AtumDimensionData data = new AtumDimensionData();
        data.hasStartStructureSpawned = nbt.getBoolean("HasStartStructureSpawned");
        data.isStorming = nbt.getBoolean("IsStorming");

        ListTag listNBT = nbt.getList("PyramidBoxes", Constant.NBT.TAG_INT_ARRAY);
        for (int i = 0; i < listNBT.size(); ++i) {
            data.beatenPyramids.add(new BoundingBox(listNBT.getIntArray(i)));
        }
    }

    @Override
    @Nonnull
    public CompoundTag save(@Nonnull CompoundTag nbt) {
        nbt.putBoolean("HasStartStructureSpawned", this.hasStartStructureSpawned);
        nbt.putBoolean("IsStorming", this.isStorming);

        if (!this.beatenPyramids.isEmpty()) {
            ListTag listNBT = new ListTag();
            for (BoundingBox box : this.beatenPyramids) {
                listNBT.add(box.createTag());
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

    public List<BoundingBox> getBeatenPyramids() {
        return this.beatenPyramids;
    }

    public void setHasStartStructureSpawned(boolean hasStartStructureSpawned) {
        this.hasStartStructureSpawned = hasStartStructureSpawned;
        this.setDirty();
    }

    public void setStorming(boolean isStorming) {
        this.isStorming = isStorming;
        this.setDirty();
    }

    public void addBeatenPyramid(BoundingBox box) {
        this.beatenPyramids.add(box);
        this.setDirty();
    }
}