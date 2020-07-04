package com.teammetallurgy.atum.entity.ai.brain;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.memory.MemoryModuleStatus;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.EntityPosWrapper;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ShowWaresTask extends Task<SunspeakerEntity> {
    @Nullable
    private ItemStack field_220559_a;
    private final List<ItemStack> field_220560_b = Lists.newArrayList();
    private int field_220561_c;
    private int field_220562_d;
    private int field_220563_e;

    public ShowWaresTask(int durationMin, int durationMax) {
        super(ImmutableMap.of(MemoryModuleType.INTERACTION_TARGET, MemoryModuleStatus.VALUE_PRESENT), durationMin, durationMax);
    }

    @Override
    public boolean shouldExecute(@Nonnull ServerWorld world, SunspeakerEntity owner) {
        Brain<?> brain = owner.getBrain();
        if (!brain.getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent()) {
            return false;
        } else {
            LivingEntity livingentity = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
            return livingentity.getType() == EntityType.PLAYER && owner.isAlive() && livingentity.isAlive() && !owner.isChild() && owner.getDistanceSq(livingentity) <= 17.0D;
        }
    }

    @Override
    public boolean shouldContinueExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTime) {
        return this.shouldExecute(world, sunspeaker) && this.field_220563_e > 0 && sunspeaker.getBrain().getMemory(MemoryModuleType.INTERACTION_TARGET).isPresent();
    }

    @Override
    public void startExecuting(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTimeIn) {
        super.startExecuting(world, sunspeaker, gameTimeIn);
        this.func_220557_c(sunspeaker);
        this.field_220561_c = 0;
        this.field_220562_d = 0;
        this.field_220563_e = 40;
    }

    @Override
    public void updateTask(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity owner, long gameTime) {
        LivingEntity livingentity = this.func_220557_c(owner);
        this.func_220556_a(livingentity, owner);
        if (!this.field_220560_b.isEmpty()) {
            this.func_220553_d(owner);
        } else {
            owner.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
            this.field_220563_e = Math.min(this.field_220563_e, 40);
        }

        --this.field_220563_e;
    }

    @Override
    public void resetTask(@Nonnull ServerWorld world, @Nonnull SunspeakerEntity sunspeaker, long gameTimeIn) {
        super.resetTask(world, sunspeaker, gameTimeIn);
        sunspeaker.getBrain().removeMemory(MemoryModuleType.INTERACTION_TARGET);
        sunspeaker.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        this.field_220559_a = null;
    }

    private void func_220556_a(LivingEntity p_220556_1_, SunspeakerEntity p_220556_2_) {
        boolean flag = false;
        ItemStack itemstack = p_220556_1_.getHeldItemMainhand();
        if (this.field_220559_a == null || !ItemStack.areItemsEqual(this.field_220559_a, itemstack)) {
            this.field_220559_a = itemstack;
            flag = true;
            this.field_220560_b.clear();
        }

        if (flag && !this.field_220559_a.isEmpty()) {
            this.func_220555_b(p_220556_2_);
            if (!this.field_220560_b.isEmpty()) {
                this.field_220563_e = 900;
                this.func_220558_a(p_220556_2_);
            }
        }

    }

    private void func_220558_a(SunspeakerEntity p_220558_1_) {
        p_220558_1_.setItemStackToSlot(EquipmentSlotType.OFFHAND, this.field_220560_b.get(0));
    }

    private void func_220555_b(SunspeakerEntity p_220555_1_) {
        for (MerchantOffer merchantoffer : p_220555_1_.getOffers()) {
            if (!merchantoffer.hasNoUsesLeft() && this.func_220554_a(merchantoffer)) {
                this.field_220560_b.add(merchantoffer.getSellingStack());
            }
        }

    }

    private boolean func_220554_a(MerchantOffer p_220554_1_) {
        return ItemStack.areItemsEqual(this.field_220559_a, p_220554_1_.func_222205_b()) || ItemStack.areItemsEqual(this.field_220559_a, p_220554_1_.getBuyingStackSecond());
    }

    private LivingEntity func_220557_c(SunspeakerEntity p_220557_1_) {
        Brain<?> brain = p_220557_1_.getBrain();
        LivingEntity livingentity = brain.getMemory(MemoryModuleType.INTERACTION_TARGET).get();
        brain.setMemory(MemoryModuleType.LOOK_TARGET, new EntityPosWrapper(livingentity));
        return livingentity;
    }

    private void func_220553_d(SunspeakerEntity p_220553_1_) {
        if (this.field_220560_b.size() >= 2 && ++this.field_220561_c >= 40) {
            ++this.field_220562_d;
            this.field_220561_c = 0;
            if (this.field_220562_d > this.field_220560_b.size() - 1) {
                this.field_220562_d = 0;
            }

            p_220553_1_.setItemStackToSlot(EquipmentSlotType.OFFHAND, this.field_220560_b.get(this.field_220562_d));
        }

    }
}