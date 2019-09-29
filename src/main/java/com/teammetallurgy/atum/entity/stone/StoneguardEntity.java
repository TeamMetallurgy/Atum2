package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.utils.Constants;
import com.teammetallurgy.atum.utils.StackHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public class StoneguardEntity extends StoneBaseEntity implements ITexture {
    private static final ResourceLocation STONEGUARD_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/stoneguard.png");
    private static final ResourceLocation STONEGUARD_IRON_TEXTURE = new ResourceLocation(Constants.MOD_ID, "textures/entity/stoneguard_derp.png");
    private static final AttributeModifier SHIELD_ARMOR = new AttributeModifier(UUID.fromString("29c9fac8-7da1-43c0-95e7-4a3cae9bcbef"), "Stoneguard shield armor", 4, AttributeModifier.Operation.ADDITION);

    public StoneguardEntity(EntityType<? extends StoneBaseEntity> entityType, World world) {
        super(entityType, world);
        this.experienceValue = 8;
        this.setCanPickUpLoot(true);
        new GroundPathNavigator(this, world).getNodeProcessor().setCanEnterDoors(true);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10.0D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
    }

    @Override
    protected void setFriendlyAttributes() {
        super.setFriendlyAttributes();
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("41d44fff-f8a8-47c5-a753-d7eb9f715d40"), "Friendly Stoneguard health", 30.0D, AttributeModifier.Operation.ADDITION);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(FRIENDLY_HEALTH);
        this.heal(30);
    }

    @Override
    protected void dropSpecialItems(DamageSource source, int lootingModifier, boolean wasRecentlyHit) {
        if (this.isPlayerCreated()) {
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                ItemStack stack = this.getItemStackFromSlot(slot);
                if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack) && wasRecentlyHit) {
                    this.entityDropItem(stack, 0.0F);
                }
            }
        } else {
            super.dropSpecialItems(source, lootingModifier, wasRecentlyHit);
        }
    }

    @Override
    protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
        int randomWeapon = MathHelper.nextInt(this.rand, 0, 3);
        this.setStoneguardEquipment(randomWeapon);
    }

    private void setStoneguardEquipment(int randomWeapon) {
        if (randomWeapon != 2) {
            this.setItemStackToSlot(EquipmentSlotType.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));

            if (!this.world.isRemote) {
                ModifiableAttributeInstance attribute = (ModifiableAttributeInstance) this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                if (!attribute.hasModifier(SHIELD_ARMOR)) {
                    this.getAttribute(SharedMonsterAttributes.ARMOR).applyModifier(SHIELD_ARMOR);
                }
            }
        }

        switch (randomWeapon) {
            case 0:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
                break;
            case 1:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.STONEGUARD_CLUB));
                break;
            case 2:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.STONEGUARD_GREATSWORD));
                break;
            case 3:
                this.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(AtumItems.STONEGUARD_KHOPESH));
                break;
        }
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld world, DifficultyInstance difficulty, SpawnReason spawnReason, @Nullable ILivingEntityData livingdata, @Nullable CompoundNBT nbt) {
        livingdata = super.onInitialSpawn(world, difficulty, spawnReason, livingdata, nbt);

        if (!this.isPlayerCreated()) {
            this.setEquipmentBasedOnDifficulty(difficulty);

            final int variant = MathHelper.nextInt(rand, 0, 7);
            this.setVariant(variant);
        } else {
            this.setVariant(8);
        }
        return livingdata;
    }

    @Override
    public boolean isPreventingPlayerRest(PlayerEntity player) {
        return this.getVariant() != 8;
    }

    @Override
    protected boolean processInteract(PlayerEntity player, Hand hand) {
        if (this.isPlayerCreated() && player.isSneaking() && player.getHeldItem(hand).isEmpty()) {
            if (!world.isRemote) {
                for (ItemStack held : this.getHeldEquipment()) {
                    StackHelper.giveItem(player, hand, held);
                }
            }
            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    @Override
    public String getTexture() {
        ResourceLocation location = STONEGUARD_TEXTURE;
        if (this.hasCustomName() && this.getCustomName() != null) {
            String customName = this.getCustomName().getFormattedText();
            if (customName.equalsIgnoreCase("iron") || customName.equalsIgnoreCase("nutz") || customName.equalsIgnoreCase("vequinox")) {
                location = STONEGUARD_IRON_TEXTURE;
            }
        }
        return location.toString();
    }
}