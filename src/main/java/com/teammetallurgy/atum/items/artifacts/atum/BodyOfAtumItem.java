package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class BodyOfAtumItem extends TexturedArmorItem {

    public BodyOfAtumItem() {
        super(ArmorMaterial.DIAMOND, "atum_armor", EquipmentSlotType.CHEST, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Entity target = event.getSource().getTrueSource();
        World world = entity.world;

        if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == AtumItems.BODY_OF_ATUM && target instanceof LivingEntity && ((LivingEntity) target).getCreatureAttribute() == CreatureAttribute.UNDEAD) {
            if (entity.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) entity.world;
                serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, entity.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.getPosY() + world.rand.nextDouble() * (double) entity.getHeight(), entity.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 16, 0.0D, 0.0D, 0.0D, 0.01D);
            }
            event.setAmount(event.getAmount() / 2);
        }
    }
}