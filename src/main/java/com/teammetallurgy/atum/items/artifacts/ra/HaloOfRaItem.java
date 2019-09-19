package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class HaloOfRaItem extends TexturedArmorItem {

    public HaloOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor_1", EquipmentSlotType.HEAD, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.world;
        if (entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.HALO_OF_RA && !(event.getSource() instanceof IndirectEntityDamageSource)) {
            if (event.getSource().getImmediateSource() != null && event.getSource() instanceof EntityDamageSource) {
                event.getSource().getImmediateSource().setFire(8);
            }
            for (int l = 0; l < 16; ++l) {
                entity.world.addParticle(AtumParticles.RA_FIRE, entity.posX + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.posY + world.rand.nextDouble() * (double) entity.getHeight(), entity.posZ + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
    }
}