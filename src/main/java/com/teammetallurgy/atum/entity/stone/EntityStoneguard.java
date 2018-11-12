package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityStoneguard extends EntityStoneBase {

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
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("41d44fff-f8a8-47c5-a753-d7eb9f715d40"), "Friendly Stoneguard health", 20.0D, 0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(FRIENDLY_HEALTH);
        this.heal(20);
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        int randomSet = MathHelper.getInt(rand, 0, 3);
        this.setStoneguardEquipment(randomSet);
    }

    private void setStoneguardEquipment(int set) {
        switch (set) {
            case 0:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
                this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));
                break;
            case 1:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_CLUB));
                this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));
                break;
            case 2:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_GREATSWORD));
                break;
            case 3:
                this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_KHOPESH));
                this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));
                break;
        }
    }

    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
        livingdata = super.onInitialSpawn(difficulty, livingdata);

        if (!this.isPlayerCreated()) {
            this.setEquipmentBasedOnDifficulty(difficulty);
            this.setEnchantmentBasedOnDifficulty(difficulty);

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