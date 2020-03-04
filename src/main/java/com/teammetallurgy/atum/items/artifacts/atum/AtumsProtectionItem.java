package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.client.render.ItemStackRenderer;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.AtumShieldItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class AtumsProtectionItem extends AtumShieldItem {
    private static boolean isBlocking = false;

    public AtumsProtectionItem() {
        super(500, new Item.Properties().rarity(Rarity.RARE).setISTER(() -> ItemStackRenderer::new));
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Tick event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof PlayerEntity && entity.getHeldItem(entity.getActiveHand()).getItem() == AtumItems.ATUMS_PROTECTION) {
            isBlocking = true;
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getTrueSource();
        if (trueSource instanceof LivingEntity && isBlocking && ((LivingEntity) trueSource).getCreatureAttribute() == CreatureAttribute.UNDEAD && random.nextFloat() <= 0.50F) {
            LivingEntity entity = event.getEntityLiving();
            trueSource.setFire(8);
            trueSource.attackEntityFrom(DamageSource.GENERIC, 2.0F);
            for (int l = 0; l < 26; ++l) {
                entity.world.addParticle(AtumParticles.LIGHT_SPARKLE, entity.getPosX() + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.getPosY() + random.nextDouble() * (double) entity.getHeight(), entity.getPosZ() + (random.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, 0.0D, 0.0D);
            }
            isBlocking = false;
        }
    }
}