package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AtumShieldItem extends ShieldItem {
    private Item repairItem;

    public AtumShieldItem(int maxDamage) {
        this(maxDamage, new Item.Properties());
    }

    public AtumShieldItem(int maxDamage, Item.Properties properties) {
        super(properties.defaultMaxDamage(maxDamage).group(Atum.GROUP).setISTER(() -> ItemStackRenderer::new));
        DispenserBlock.registerDispenseBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean isShield(@Nonnull ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    @Nonnull
    public UseAction getUseAction(@Nonnull ItemStack stack) {
        return UseAction.BLOCK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 72000;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, PlayerEntity player, @Nonnull Hand hand) {
        ItemStack heldStack = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return new ActionResult<>(ActionResultType.SUCCESS, heldStack);
    }

    public AtumShieldItem setRepairItem(Item item) {
        this.repairItem = item;
        return this;
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return this.repairItem != null && repair.getItem() == this.repairItem;
    }
}