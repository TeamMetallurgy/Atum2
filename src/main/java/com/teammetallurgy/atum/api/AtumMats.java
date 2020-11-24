package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.Nonnull;

public class AtumMats {
    public static final IArmorMaterial MUMMY_ARMOR = new IArmorMaterial() {
        private final int[] MAX_DAMAGE_ARRAY = new int[]{11, 12, 13, 10};
        private final int MAX_DAMAGE_FACTOR = 4;

        @Override
        public int getDurability(@Nonnull EquipmentSlotType slotType) {
            return MAX_DAMAGE_ARRAY[slotType.getIndex()] * MAX_DAMAGE_FACTOR;
        }

        @Override
        public int getDamageReductionAmount(@Nonnull EquipmentSlotType slotType) {
            int[] damageReduction = new int[]{1, 2, 2, 1};
            return damageReduction[slotType.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 12;
        }

        @Override
        @Nonnull
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AtumItems.SCRAP);
        }

        @Override
        @Nonnull
        public String getName() {
            return "mummy";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    };
    public static final IArmorMaterial WANDERER_ARMOR = new IArmorMaterial() {
        private final int[] MAX_DAMAGE_ARRAY = new int[]{14, 15, 16, 7};
        private final int MAX_DAMAGE_FACTOR = 10;


        @Override
        public int getDurability(@Nonnull EquipmentSlotType slotType) {
            return MAX_DAMAGE_ARRAY[slotType.getIndex()] * MAX_DAMAGE_FACTOR;
        }

        @Override
        public int getDamageReductionAmount(@Nonnull EquipmentSlotType slotType) {
            int[] damageReduction = new int[]{1, 2, 3, 1};
            return damageReduction[slotType.getIndex()];
        }

        @Override
        public int getEnchantability() {
            return 14;
        }

        @Override
        @Nonnull
        public SoundEvent getSoundEvent() {
            return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
        }

        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AtumItems.LINEN_CLOTH);
        }

        @Override
        @Nonnull
        public String getName() {
            return "wanderer";
        }

        @Override
        public float getToughness() {
            return 0.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    };
    public static final IItemTier KHNUMITE = new IItemTier() {
        @Override
        public int getMaxUses() {
            return 160;
        }

        @Override
        public float getEfficiency() {
            return 3.6F;
        }

        @Override
        public float getAttackDamage() {
            return 1.1F;
        }

        @Override
        public int getHarvestLevel() {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 10;
        }

        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AtumItems.KHNUMITE);
        }
    };
    public static final IItemTier LIMESTONE = new IItemTier() {
        @Override
        public int getMaxUses() {
            return 131;
        }

        @Override
        public float getEfficiency() {
            return 4.0F;
        }

        @Override
        public float getAttackDamage() {
            return 1.0F;
        }

        @Override
        public int getHarvestLevel() {
            return 1;
        }

        @Override
        public int getEnchantability() {
            return 6;
        }

        @Override
        @Nonnull
        public Ingredient getRepairMaterial() {
            return Ingredient.fromItems(AtumBlocks.LIMESTONE_CRACKED);
        }
    };
    public static final IItemTier NEBU = new IItemTier() {
        @Override
        public int getMaxUses() {
            return 2250;
        }

        @Override
        public float getEfficiency() {
            return 9.0F;
        }

        @Override
        public float getAttackDamage() {
            return 4.0F;
        }

        @Override
        public int getHarvestLevel() {
            return 4;
        }

        @Override
        public int getEnchantability() {
            return 22;
        }

        @Override
        @Nonnull
        public Ingredient getRepairMaterial() { //TODO Add nebu
            return Ingredient.fromItems(Items.NETHERITE_INGOT);
        }
    };

    public static final Material HEART_OF_RA = new Material.Builder(MaterialColor.GOLD).notOpaque().notSolid().build();
}