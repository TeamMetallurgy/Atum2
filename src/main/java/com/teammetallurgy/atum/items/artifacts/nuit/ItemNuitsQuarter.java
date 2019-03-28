package com.teammetallurgy.atum.items.artifacts.nuit;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumParticles;
import com.teammetallurgy.atum.items.tools.ItemKhopesh;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class ItemNuitsQuarter extends ItemKhopesh {
    private boolean isOffhand = false;
    private static boolean isBlocking = false;

    public ItemNuitsQuarter() {
        super(ToolMaterial.DIAMOND);
        this.addPropertyOverride(new ResourceLocation("blocking"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }

    @Override
    @Nonnull
    public EnumAction getItemUseAction(@Nonnull ItemStack stack) {
        return EnumAction.BLOCK;
    }

    @Override
    public int getMaxItemUseDuration(@Nonnull ItemStack stack) {
        return isOffhand ? 72000 : 0;
    }

    @Override
    public boolean isShield(@Nonnull ItemStack stack, @Nullable EntityLivingBase entity) {
        return isOffhand;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        if (hand == EnumHand.OFF_HAND) {
            player.setActiveHand(EnumHand.OFF_HAND);
            this.isOffhand = true;
            return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(EnumHand.OFF_HAND));
        }
        this.isOffhand = false;
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public boolean hitEntity(@Nonnull ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (itemRand.nextFloat() <= 0.25F) {
            applyWeakness(target, attacker,attacker.getHeldItemOffhand().getItem() == AtumItems.NUITS_IRE);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @SubscribeEvent
    public static void onUse(LivingEntityUseItemEvent.Tick event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity instanceof EntityPlayer && entity.getHeldItem(EnumHand.OFF_HAND).getItem() == AtumItems.NUITS_QUARTER) {
            isBlocking = true;
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        Entity trueSource = event.getSource().getImmediateSource();
        if (trueSource instanceof EntityLivingBase && event.getEntityLiving() instanceof EntityPlayer && isBlocking && itemRand.nextFloat() <= 0.25F) {
            applyWeakness((EntityLivingBase) trueSource, event.getEntityLiving(), event.getEntityLiving().getHeldItemMainhand().getItem() == AtumItems.NUITS_IRE);
            isBlocking = false;
        }
    }

    private static void applyWeakness(EntityLivingBase attacker, EntityLivingBase target, boolean isNuitsIreHeld) {
        if (attacker != target) {
            for (int l = 0; l < 8; ++l) {
                Atum.proxy.spawnParticle(AtumParticles.Types.NUIT_BLACK, target, target.posX + (itemRand.nextDouble() - 0.5D) * (double) target.width, target.posY + itemRand.nextDouble() * (double) target.height, target.posZ + (itemRand.nextDouble() - 0.5D) * (double) target.width, 0.0D, 0.0D, 0.0D);
            }
            attacker.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60, isNuitsIreHeld ? 2 : 1));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag tooltipType) {
        if (Keyboard.isKeyDown(42)) {
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }
}