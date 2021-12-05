package com.teammetallurgy.atum.integration.curios;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import top.theillusivec4.curios.api.type.capability.ICurio;

import javax.annotation.Nonnull;
import java.util.List;

import top.theillusivec4.curios.api.type.capability.ICurio.DropRule;

public class SimpleCurioItem implements ICurio { //TODO Remove in 1.17
    private final ItemStack stack;
    private final ISimpleCurioItem simpleCurioItem;

    public SimpleCurioItem(ISimpleCurioItem simpleCurioItem, @Nonnull ItemStack stack) {
        this.simpleCurioItem = simpleCurioItem;
        this.stack = stack;
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity) {
        this.simpleCurioItem.curioTick(identifier, index, livingEntity, this.stack);
    }

    @Override
    public void curioAnimate(String identifier, int index, LivingEntity livingEntity) {
        this.simpleCurioItem.curioAnimate(identifier, index, livingEntity, this.stack);
    }

    @Override
    public void onEquip(String identifier, int index, LivingEntity livingEntity) {
        this.simpleCurioItem.onEquip(identifier, index, livingEntity, this.stack);
    }

    @Override
    public void onUnequip(String identifier, int index, LivingEntity livingEntity) {
        this.simpleCurioItem.onUnequip(identifier, index, livingEntity, this.stack);
    }

    @Override
    public boolean canEquip(String identifier, LivingEntity livingEntity) {
        return this.simpleCurioItem.canEquip(identifier, livingEntity, this.stack);
    }

    @Override
    public boolean canUnequip(String identifier, LivingEntity livingEntity) {
        return this.simpleCurioItem.canUnequip(identifier, livingEntity, this.stack);
    }

    @Override
    public boolean canRightClickEquip() {
        return this.simpleCurioItem.canRightClickEquip(this.stack);
    }

    @Override
    public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
        return this.simpleCurioItem.canSync(identifier, index, livingEntity, this.stack);
    }

    @Override
    public void curioBreak(@Nonnull ItemStack stack, LivingEntity livingEntity) {
        this.simpleCurioItem.curioBreak(stack, livingEntity);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
        return this.simpleCurioItem.getAttributeModifiers(identifier, this.stack);
    }

    @Override
    public boolean showAttributesTooltip(String identifier) {
        return this.simpleCurioItem.showAttributesTooltip(identifier, this.stack);
    }

    @Nonnull
    @Override
    public DropRule getDropRule(LivingEntity livingEntity) {
        return this.simpleCurioItem.getDropRule(livingEntity, this.stack);
    }

    @Override
    public int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curioStack, int index) {
        return this.simpleCurioItem.getFortuneBonus(identifier, livingEntity, curioStack, index);
    }

    @Override
    public int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curioStack, int index) {
        return this.simpleCurioItem.getLootingBonus(identifier, livingEntity, curioStack, index);
    }

    @Override
    public List<Component> getTagsTooltip(List<Component> tooltips) {
        return this.simpleCurioItem.getTagsTooltip(tooltips, this.stack);
    }

    @Override
    public void playRightClickEquipSound(LivingEntity livingEntity) {
        this.simpleCurioItem.playRightClickEquipSound(livingEntity, this.stack);
    }

    @Override
    public void readSyncData(CompoundTag nbt) {
        this.simpleCurioItem.readSyncData(nbt, this.stack);
    }

    @Override
    @Nonnull
    public CompoundTag writeSyncData() {
        return this.simpleCurioItem.writeSyncData(this.stack);
    }
}