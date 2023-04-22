package com.teammetallurgy.atum.api.material;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nonnull;

public class AtumMaterialTiers {
    public static final Tier KHNUMITE = new Tier() {
        @Override
        public int getUses() {
            return 160;
        }

        @Override
        public float getSpeed() {
            return 3.6F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 1.1F;
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public int getEnchantmentValue() {
            return 10;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumItems.KHNUMITE.get());
        }
    };
    public static final Tier LIMESTONE = new Tier() {
        @Override
        public int getUses() {
            return 131;
        }

        @Override
        public float getSpeed() {
            return 4.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 1.0F;
        }

        @Override
        public int getLevel() {
            return 1;
        }

        @Override
        public int getEnchantmentValue() {
            return 6;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumBlocks.LIMESTONE_CRACKED.get());
        }
    };
    public static final Tier NEBU = new Tier() {
        @Override
        public int getUses() {
            return 2250;
        }

        @Override
        public float getSpeed() {
            return 9.0F;
        }

        @Override
        public float getAttackDamageBonus() {
            return 4.0F;
        }

        @Override
        public int getLevel() {
            return 4;
        }

        @Override
        public int getEnchantmentValue() {
            return 22;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumItems.NEBU_INGOT.get());
        }
    };
}