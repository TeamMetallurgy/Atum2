package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import javax.annotation.Nonnull;

public class AtumShieldItem extends ShieldItem {
    private Item repairItem;

    public AtumShieldItem(int maxDamage) {
        this(maxDamage, new Item.Properties());
    }

    public AtumShieldItem(int maxDamage, Item.Properties properties) {
        super(properties.defaultDurability(maxDamage).tab(Atum.GROUP).setISTER(() -> ItemStackRenderer::new));
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public boolean canPerformAction(@Nonnull ItemStack stack, @Nonnull ToolAction toolAction) {
        return toolAction.equals(ToolActions.SHIELD_BLOCK);
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 72000;
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldStack);
    }

    public AtumShieldItem setRepairItem(Item item) {
        this.repairItem = item;
        return this;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return this.repairItem != null && repair.getItem() == this.repairItem;
    }
}