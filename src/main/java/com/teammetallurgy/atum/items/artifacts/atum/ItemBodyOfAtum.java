package com.teammetallurgy.atum.items.artifacts.atum;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.client.particle.ParticleLightSparkle;
import com.teammetallurgy.atum.entity.EntityUndeadBase;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.ItemTexturedArmor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.AbstractSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber
public class ItemBodyOfAtum extends ItemTexturedArmor {

    public ItemBodyOfAtum() {
        super(ArmorMaterial.DIAMOND, 1, EntityEquipmentSlot.CHEST);
        this.setTextureFile("atum_armor_1");
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        World world = entity.world;

        if (event.getEntityLiving().getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() == AtumItems.BODY_OF_ATUM && isUndeadMob(event.getSource().getTrueSource())) {
            for (int l = 0; l < 16; ++l) {
                Atum.proxy.generateParticle(new ParticleLightSparkle(entity.world, entity.posX + (world.rand.nextDouble() - 0.5D) * (double) entity.width, entity.posY + world.rand.nextDouble() * (double) entity.height, entity.posZ + (world.rand.nextDouble() - 0.5D) * (double) entity.width, 0.0D, 0.0D, 0.0D));
            }
            event.setAmount(event.getAmount() / 2);
        }
    }

    public static boolean isUndeadMob(Entity entity) {
        return entity instanceof EntityUndeadBase || entity instanceof EntityZombie || entity instanceof AbstractSkeleton || entity instanceof EntityWither;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getUnlocalizedName() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getUnlocalizedName() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}