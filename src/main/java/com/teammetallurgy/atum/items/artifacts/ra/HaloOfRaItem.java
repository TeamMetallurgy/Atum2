package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class HaloOfRaItem extends TexturedArmorItem {

    public HaloOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor", EquipmentSlotType.HEAD, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.world;
        DamageSource source = event.getSource();
        if (entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.HALO_OF_RA && !(source instanceof IndirectEntityDamageSource)) {
            if (source.getImmediateSource() != null && source instanceof EntityDamageSource) {
                source.getImmediateSource().setFire(8);
            }
            if (entity.world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) entity.world;
                serverWorld.spawnParticle(AtumParticles.RA_FIRE, entity.getPosX() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.getPosY() + (entity.getHeight() / 1.5D), entity.getPosZ() + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 16, 0.0D, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}