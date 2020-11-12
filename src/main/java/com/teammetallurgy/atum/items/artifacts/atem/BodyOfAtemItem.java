package com.teammetallurgy.atum.items.artifacts.atem;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class BodyOfAtemItem extends AtemArmor {

    public BodyOfAtemItem() {
        super(ArmorMaterial.DIAMOND, "atem_armor", EquipmentSlotType.CHEST);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntityLiving();
        Entity target = event.getSource().getTrueSource();
        World world = entity.world;

        if (event.getEntityLiving().getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == AtumItems.BODY_OF_ATEM && target instanceof LivingEntity && ((LivingEntity) target).getCreatureAttribute() == CreatureAttribute.UNDEAD) {
            if (entity.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) entity.world;
                serverWorld.spawnParticle(AtumParticles.LIGHT_SPARKLE, entity.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.getPosY() + world.rand.nextDouble() * (double) entity.getHeight(), entity.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 16, 0.0D, 0.0D, 0.0D, 0.01D);
            }
            event.setAmount(event.getAmount() / 2);
        }
    }
}