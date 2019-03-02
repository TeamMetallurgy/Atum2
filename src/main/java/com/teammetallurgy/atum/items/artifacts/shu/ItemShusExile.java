package com.teammetallurgy.atum.items.artifacts.shu;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.ItemBattleAxe;
import com.teammetallurgy.atum.utils.Constants;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.map.hash.TObjectFloatHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemShusExile extends ItemBattleAxe {
    private static final TObjectFloatMap<EntityPlayer> cooldown = new TObjectFloatHashMap<>();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onAttack(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.world.isRemote) return;
        if (event.getTarget() instanceof EntityLivingBase && player.getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE) {
            cooldown.put(player, player.getCooledAttackStrength(0.5F));
        }
    }

    @SubscribeEvent
    public static void onKnockback(LivingKnockBackEvent event) {
        if (event.getAttacker() instanceof EntityLivingBase && ((EntityLivingBase) event.getAttacker()).getHeldItemMainhand().getItem() == AtumItems.SHUS_EXILE) {
            EntityLivingBase target = event.getEntityLiving();
            if (cooldown.get(event.getAttacker()) == 1.0F) {
                event.setStrength(event.getStrength() * 3F);
                double x = MathHelper.nextDouble(itemRand, 0.0001D, 0.02D);
                double z = MathHelper.nextDouble(itemRand, 0.0001D, 0.02D);
                for (int l = 0; l < 12; ++l) {
                    Atum.proxy.spawnParticle(AtumParticles.Types.SHU, target, target.posX + (itemRand.nextDouble() - 0.5D) * (double) target.width, target.posY + target.getEyeHeight(), target.posZ + (itemRand.nextDouble() - 0.5D) * (double) target.width, x, 0.04D, -z);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}