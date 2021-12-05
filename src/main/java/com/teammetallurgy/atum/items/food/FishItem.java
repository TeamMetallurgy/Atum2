package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.StringRepresentable;

import javax.annotation.Nonnull;

public class FishItem extends Item {
    private final FishType fishType;

    public FishItem(FishType type) {
        super(new Item.Properties().tab(Atum.GROUP));
        fishType = type;
    }

    public enum FishType implements StringRepresentable {
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
        public String getSerializedName() {
            return this.unlocalizedName;
        }
    }
}