package com.teammetallurgy.atum.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public class ItemFish extends Item {
    private final FishType fishType;

    public ItemFish(FishType type) {
        super();
        fishType = type;
    }

    public enum FishType implements IStringSerializable {
        FORSAKEN("forsaken"),
        MUMMIFIED("mummified"),
        JEWELED("jeweled"),
        SKELETAL("skeletal");

        private final String unlocalizedName;

        FishType(String name) {
            this.unlocalizedName = name;
        }

        @Nonnull
        public static FishType byStack(@Nonnull ItemStack stack) {
            Item item = stack.getItem();
            return item instanceof ItemFish ? ((ItemFish) item).fishType : FORSAKEN;
        }

        @Override
        @Nonnull
        public String getName() {
            return this.unlocalizedName;
        }
    }
}