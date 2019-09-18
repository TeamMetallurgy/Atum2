package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class HaloOfRaItem extends TexturedArmorItem {

    public HaloOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor_1", EquipmentSlotType.HEAD);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        LivingEntity entity = event.getEntityLiving();
        World world = entity.world;
        if (entity.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() == AtumItems.HALO_OF_RA && !(event.getSource() instanceof EntityDamageSourceIndirect)) {
            if (event.getSource().getImmediateSource() != null && event.getSource() instanceof EntityDamageSource) {
                event.getSource().getImmediateSource().setFire(8);
            }
            for (int l = 0; l < 16; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.RA_FIRE, entity, entity.posX + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), entity.posY + world.rand.nextDouble() * (double) entity.height, entity.posZ + (world.rand.nextDouble() - 0.5D) * (double) entity.getWidth(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }
}