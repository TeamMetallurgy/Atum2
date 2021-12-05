package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;

public class FishItem extends Item {
    private final FishType fishType;

    public FishItem(FishType type) {
        super(new Item.Properties().group(Atum.GROUP));
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
            return item instanceof FishItem ? ((FishItem) item).fishType : FORSAKEN;
        }

        @Override
        @Nonnull
        public String getString() {
            return this.unlocalizedName;
        }
    }
}