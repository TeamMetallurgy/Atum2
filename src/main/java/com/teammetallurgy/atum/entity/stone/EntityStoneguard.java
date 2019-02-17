package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityStoneguard extends EntityStoneBase {
    private static final AttributeModifier SHIELD_ARMOR = new AttributeModifier(UUID.fromString("29c9fac8-7da1-43c0-95e7-4a3cae9bcbef"), "Stoneguard shield armor", 4, 0);

    public EntityStoneguard(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.experienceValue = 8;
        this.setCanPickUpLoot(true);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
    protected void setFriendlyAttributes() {
        super.setFriendlyAttributes();
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("41d44fff-f8a8-47c5-a753-d7eb9f715d40"), "Friendly Stoneguard health", 30.0D, 0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(FRIENDLY_HEALTH);
        this.heal(30);
    }

    @Override
    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier) {
        if (this.isPlayerCreated()) {
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                ItemStack stack = this.getItemStackFromSlot(slot);
                if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack) && wasRecentlyHit) {
                    this.entityDropItem(stack, 0.0F);
                }
            }
        } else {
            super.dropEquipment(wasRecentlyHit, lootingModifier);
        }
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        int randomWeapon = MathHelper.getInt(rand, 0, 3);
        this.setStoneguardEquipment(randomWeapon);
    }

    private void setStoneguardEquipment(int randomWeapon) {
        if (randomWeapon != 2) {
            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));

            if (!this.world.isRemote) {
                ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                if (!attribute.hasModifier(SHIELD_ARMOR)) {
                    this.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(SHIELD_ARMOR);
                }
            }
        }

        switch (randomWeapon) {
            case 0:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
                break;
            case 1:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_CLUB));
                break;
            case 2:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_GREATSWORD));
                break;
            case 3:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_KHOPESH));
                break;
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (!this.isPlayerCreated()) {
            this.setEquipmentBasedOnDifficulty(difficulty);

            final int variant = MathHelper.getInt(world.rand, 0, 7);
            this.setVariant(variant);
        } else {
            this.setVariant(8);
        }
        return livingdata;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return AtumLootTables.STONEGUARD;
    }
}