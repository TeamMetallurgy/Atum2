package com.teammetallurgy.atum.inventory.container.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;

public class SlotHideableCamel extends Slot {
    public Slot parentSlot;

    public SlotHideableCamel(Slot parentSlot, Container container, int index, int x, int y) {
        super(container, index, x, y);
        this.parentSlot = parentSlot;
    }

    @Override
    public boolean isActive() {
        return this.parentSlot.hasItem();
    }
}
