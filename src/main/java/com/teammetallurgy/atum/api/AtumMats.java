package com.teammetallurgy.atum.api;

import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

import javax.annotation.Nonnull;

public class AtumMats {
    public static final ArmorMaterial MUMMY_ARMOR = new ArmorMaterial() {
        private final int[] MAX_DAMAGE_ARRAY = new int[]{11, 12, 13, 10};
        private final int MAX_DAMAGE_FACTOR = 4;

        @Override
        public int getDurabilityForSlot(@Nonnull EquipmentSlot slotType) {
            return MAX_DAMAGE_ARRAY[slotType.getIndex()] * MAX_DAMAGE_FACTOR;
        }

        @Override
        public int getDefenseForSlot(@Nonnull EquipmentSlot slotType) {
            int[] damageReduction = new int[]{1, 2, 2, 1};
            return damageReduction[slotType.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 12;
        }

        @Override
        @Nonnull
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumItems.SCRAP);
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
    public static final ArmorMaterial WANDERER_ARMOR = new ArmorMaterial() {
        private final int[] MAX_DAMAGE_ARRAY = new int[]{14, 15, 16, 7};
        private final int MAX_DAMAGE_FACTOR = 10;


        @Override
        public int getDurabilityForSlot(@Nonnull EquipmentSlot slotType) {
            return MAX_DAMAGE_ARRAY[slotType.getIndex()] * MAX_DAMAGE_FACTOR;
        }

        @Override
        public int getDefenseForSlot(@Nonnull EquipmentSlot slotType) {
            int[] damageReduction = new int[]{1, 2, 3, 1};
            return damageReduction[slotType.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        @Nonnull
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_LEATHER;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumItems.LINEN_CLOTH);
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
            return Ingredient.of(AtumItems.KHNUMITE);
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
            return Ingredient.of(AtumBlocks.LIMESTONE_CRACKED);
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
            return Ingredient.of(AtumItems.NEBU_INGOT);
        }
    };
    public static final ArmorMaterial NEBU_ARMOR = new ArmorMaterial() {
        private final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};
        private final int MAX_DAMAGE_FACTOR = 37;

        @Override
        public int getDurabilityForSlot(@Nonnull EquipmentSlot slotType) {
            return MAX_DAMAGE_ARRAY[slotType.getIndex()] * MAX_DAMAGE_FACTOR;
        }

        @Override
        public int getDefenseForSlot(@Nonnull EquipmentSlot slotType) {
            int[] damageReduction = new int[]{3, 6, 8, 3};
            return damageReduction[slotType.getIndex()];
        }

        @Override
        public int getEnchantmentValue() {
            return 25;
        }

        @Override
        @Nonnull
        public SoundEvent getEquipSound() {
            return SoundEvents.ARMOR_EQUIP_DIAMOND;
        }

        @Override
        @Nonnull
        public Ingredient getRepairIngredient() {
            return Ingredient.of(AtumItems.NEBU_INGOT);
        }

        @Override
        @Nonnull
        public String getName() {
            return "nebu";
        }

        @Override
        public float getToughness() {
            return 3.0F;
        }

        @Override
        public float getKnockbackResistance() {
            return 0.0F;
        }
    };

    public static final Material HEART_OF_RA = new Material.Builder(MaterialColor.GOLD).notSolidBlocking().nonSolid().build();
}