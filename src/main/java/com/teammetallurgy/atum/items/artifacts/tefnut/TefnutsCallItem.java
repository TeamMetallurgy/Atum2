package com.teammetallurgy.atum.items.artifacts.tefnut;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.IArtifact;
import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import com.teammetallurgy.atum.entity.projectile.arrow.TefnutsCallEntity;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class TefnutsCallItem extends Item implements IArtifact {

    public TefnutsCallItem() {
        super(new Item.Properties().durability(650).rarity(Rarity.RARE).tab(Atum.GROUP));
    }

    @Override
    public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
        if (Minecraft.getInstance() == null) return;
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new ItemStackRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
            }
        });
    }

    @Override
    public God getGod() {
        return God.TEFNUT;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 7200;
    }

    @Override
    @Nonnull
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public boolean canAttackBlock(@Nonnull BlockState state, @Nonnull Level world, @Nonnull BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level world, @Nonnull LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof Player player) {
            int useDuration = this.getUseDuration(stack) - timeLeft;
            if (useDuration >= 21) {
                if (!world.isClientSide) {
                    stack.hurtAndBreak(1, player, (entity) -> {
                        entity.broadcastBreakEvent(entityLiving.getUsedItemHand());
                    });

                    TefnutsCallEntity spear = new TefnutsCallEntity(world, player, stack);
                    spear.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, (float) useDuration / 25.0F + 0.25F, 1.0F);
                    spear.setBaseDamage(spear.getBaseDamage() * 2.0D);
                    if (player.getAbilities().instabuild) {
                        spear.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                    }

                    world.addFreshEntity(spear);
                    world.playSound(null, spear, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        player.getInventory().removeItem(stack);
                    }
                }
                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(@Nonnull Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        if (heldStack.getDamageValue() >= heldStack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(heldStack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(heldStack);
        }
    }

    @Override
    public boolean hurtEnemy(@Nonnull ItemStack stack, @Nonnull LivingEntity target, @Nonnull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, (entity) -> {
            entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean mineBlock(@Nonnull ItemStack stack, @Nonnull Level world, BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) {
        if ((double) state.getDestroySpeed(world, pos) != 0.0D) {
            stack.hurtAndBreak(2, entityLiving, (entity) -> {
                entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }

    @Override
    @Nonnull
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nonnull EquipmentSlot slot, @Nonnull ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 3.0D, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -2.6D, AttributeModifier.Operation.ADDITION));
        }
        return builder.build();
    }
}