package com.teammetallurgy.atum.items.food;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.projectile.QuailEggEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;

public class QuailEggItem extends EggItem {

    public QuailEggItem() {
        super(new Item.Properties().stacksTo(16).tab(Atum.GROUP));
    }

    @Override
    @Nonnull
    public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
        ItemStack heldStack = player.getItemInHand(hand);
        world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        if (!world.isClientSide) {
            QuailEggEntity quailEggEntity = new QuailEggEntity(world, player);
            quailEggEntity.setItem(heldStack);
            quailEggEntity.shootFromRotation(player, player.xRot, player.yRot, 0.0F, 1.5F, 1.0F);
            world.addFreshEntity(quailEggEntity);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.abilities.instabuild) {
            heldStack.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(heldStack, world.isClientSide());
    }
}