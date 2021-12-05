package com.teammetallurgy.atum.integration.curios;

import com.google.common.collect.Multimap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import java.util.List;

public interface ISimpleCurioItem { //TODO Remove in 1.17. Temporary workaround for Curios support
    ICurio defaultInstance = new ICurio() {
    };

    default boolean hasCurioCapability(@Nonnull ItemStack stack) {
        return true;
    }

    default void curioTick(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        defaultInstance.curioTick(identifier, index, livingEntity);
    }

    default void curioAnimate(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        defaultInstance.curioAnimate(identifier, index, livingEntity);
    }

    default void onEquip(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        defaultInstance.onEquip(identifier, index, livingEntity);
    }

    default void onUnequip(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        defaultInstance.onUnequip(identifier, index, livingEntity);
    }

    default boolean canEquip(String identifier, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        return defaultInstance.canEquip(identifier, livingEntity);
    }

    default boolean canUnequip(String identifier, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        return defaultInstance.canUnequip(identifier, livingEntity);
    }

    default List<ITextComponent> getTagsTooltip(List<ITextComponent> tagTooltips, @Nonnull ItemStack stack) {
        return defaultInstance.getTagsTooltip(tagTooltips);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, @Nonnull ItemStack stack) {
        return defaultInstance.getAttributeModifiers(identifier);
    }

    default void playRightClickEquipSound(LivingEntity livingEntity, @Nonnull ItemStack stack) {
        defaultInstance.playRightClickEquipSound(livingEntity);
    }

    default boolean canRightClickEquip(@Nonnull ItemStack stack) {
        return defaultInstance.canRightClickEquip();
    }

    default void curioBreak(@Nonnull ItemStack stack, LivingEntity livingEntity) {
        defaultInstance.curioBreak(stack, livingEntity);
    }

    default boolean canSync(String identifier, int index, LivingEntity livingEntity, @Nonnull ItemStack stack) {
        return defaultInstance.canSync(identifier, index, livingEntity);
    }

    @Nonnull
    default CompoundNBT writeSyncData(@Nonnull ItemStack stack) {
        return defaultInstance.writeSyncData();
    }

    default void readSyncData(CompoundNBT compound, @Nonnull ItemStack stack) {
        defaultInstance.readSyncData(compound);
    }

    @Nonnull
    default ICurio.DropRule getDropRule(LivingEntity livingEntity, @Nonnull ItemStack stack) {
        return defaultInstance.getDropRule(livingEntity);
    }

    default boolean showAttributesTooltip(String identifier, @Nonnull ItemStack stack) {
        return defaultInstance.showAttributesTooltip(identifier);
    }

    default int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getFortuneBonus(identifier, livingEntity, curio, index);
    }

    default int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getLootingBonus(identifier, livingEntity, curio, index);
    }
}