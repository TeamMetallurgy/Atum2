package com.teammetallurgy.atum.entity.stone;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ITexture;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class StoneguardEntity extends StoneBaseEntity implements ITexture {
    private static final ResourceLocation STONEGUARD_IRON_TEXTURE = new ResourceLocation(Atum.MOD_ID, "textures/entity/stoneguard_derp.png");
    private static final AttributeModifier SHIELD_ARMOR = new AttributeModifier(UUID.fromString("29c9fac8-7da1-43c0-95e7-4a3cae9bcbef"), "Stoneguard shield armor", 4, AttributeModifier.Operation.ADDITION);

    public StoneguardEntity(EntityType<? extends StoneguardEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward = 8;
        this.setCanPickUpLoot(true);
        new GroundPathNavigation(this, world).getNodeEvaluator().setCanPassDoors(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return getBaseAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.ATTACK_DAMAGE, 3.0D).add(Attributes.ARMOR, 10.0F);
    }

    @Override
    protected void setFriendlyAttributes() {
        super.setFriendlyAttributes();
        final AttributeModifier FRIENDLY_HEALTH = new AttributeModifier(UUID.fromString("41d44fff-f8a8-47c5-a753-d7eb9f715d40"), "Friendly Stoneguard health", 30.0D, AttributeModifier.Operation.ADDITION);
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(FRIENDLY_HEALTH);
        this.heal(30);
    }

    @Override
    protected void dropCustomDeathLoot(@Nonnull DamageSource source, int lootingModifier, boolean wasRecentlyHit) {
        if (this.isPlayerCreated()) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                ItemStack stack = this.getItemBySlot(slot);
                if (!stack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(stack) && wasRecentlyHit) {
                    this.spawnAtLocation(stack, 0.0F);
                }
            }
        } else {
            super.dropCustomDeathLoot(source, lootingModifier, wasRecentlyHit);
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(@Nonnull DifficultyInstance difficulty) {
        int randomWeapon = Mth.nextInt(this.random, 0, 3);
        this.setStoneguardEquipment(randomWeapon);
    }

    private void setStoneguardEquipment(int randomWeapon) {
        if (randomWeapon != 2) {
            this.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(AtumItems.STONEGUARD_SHIELD));

            if (!this.level.isClientSide) {
                AttributeInstance attribute = this.getAttribute(Attributes.MOVEMENT_SPEED);

                if (attribute != null && !attribute.hasModifier(SHIELD_ARMOR)) {
                    this.getAttribute(Attributes.ARMOR).addPermanentModifier(SHIELD_ARMOR);
                }
            }
        }

        switch (randomWeapon) {
            case 0:
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_SWORD));
                break;
            case 1:
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_CLUB));
                break;
            case 2:
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_GREATSWORD));
                break;
            case 3:
                this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(AtumItems.STONEGUARD_KHOPESH));
                break;
        }
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@Nonnull ServerLevelAccessor world, @Nonnull DifficultyInstance difficulty, @Nonnull MobSpawnType spawnReason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag nbt) {
        livingdata = super.finalizeSpawn(world, difficulty, spawnReason, livingdata, nbt);

        if (!this.isPlayerCreated()) {
            this.populateDefaultEquipmentSlots(difficulty);
            final int variant = Mth.nextInt(random, 0, 7);
            this.setVariant(variant);
        } else {
            this.setVariant(8);
        }
        return livingdata;
    }

    @Override
    public boolean isPreventingPlayerRest(@Nonnull Player player) { //isPreventingPlayerRest
        return this.getVariant() != 8;
    }

    @Override
    @Nonnull
    protected InteractionResult mobInteract(Player player, @Nonnull InteractionHand hand) {
        if (this.isPlayerCreated() && player.isCrouching() && player.getItemInHand(hand).isEmpty()) {
            if (!level.isClientSide) {
                for (ItemStack held : this.getHandSlots()) {
                    StackHelper.giveItem(player, hand, held);
                }
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public String getTexture() {
        if (this.hasCustomName() && this.getCustomName() != null) {
            String customName = this.getCustomName().getString();
            if (customName.equalsIgnoreCase("iron") || customName.equalsIgnoreCase("nutz") || customName.equalsIgnoreCase("vequinox")) {
                return STONEGUARD_IRON_TEXTURE.toString();
            }
        }
        return new ResourceLocation(Atum.MOD_ID, "textures/entity/stoneguard_" + this.getVariant() + ".png").toString();
    }
}