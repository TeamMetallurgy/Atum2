package com.teammetallurgy.atum.items.artifacts.atum;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.ItemTexturedArmor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(value = Side.CLIENT) 
public class ItemEyesOfAtum extends ItemTexturedArmor {
    private static PotionEffect savedNightVision;

    public ItemEyesOfAtum() {
        super(ArmorMaterial.DIAMOND, "atum_armor_1", EntityEquipmentSlot.HEAD);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, @Nonnull ItemStack stack) {
        super.onArmorTick(world, player, stack);

        if (world.getTotalWorldTime() % 15L == 0L) {
            //player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 255, 0, false, false));
        }
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
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line1"));
            tooltip.add(TextFormatting.DARK_PURPLE + I18n.format(this.getTranslationKey() + ".line2"));
        } else {
            tooltip.add(I18n.format(this.getTranslationKey() + ".line3") + " " + TextFormatting.DARK_GRAY + "[SHIFT]");
        }
    }

    @SubscribeEvent
    public static void drawScreen(DrawScreenEvent.Pre event) {
    	EntityPlayer player = Minecraft.getMinecraft().player;
    	System.out.println(player.getActivePotionEffects());
    	if(player != null && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == AtumItems.EYES_OF_ATUM)
    	{
    		if(savedNightVision != null && savedNightVision.getDuration() == 0) {
    			savedNightVision = null;
    		}
			player.removePotionEffect(MobEffects.NIGHT_VISION);
			if(savedNightVision != null)
				player.addPotionEffect(savedNightVision);
    	}
    }
    
    @SubscribeEvent
    public static void nightVision(RenderTickEvent event) {
    	EntityPlayer player = Minecraft.getMinecraft().player;
    	if(player != null && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == AtumItems.EYES_OF_ATUM)
    	{
	    	if(event.phase == Phase.START) {
	    		PotionEffect temp = player.getActivePotionEffect(MobEffects.NIGHT_VISION);
	    		if(temp != null && !temp.isCurativeItem(new ItemStack(AtumItems.EYES_OF_ATUM)))
	    			savedNightVision = temp;
	    		
	    		player.removePotionEffect(MobEffects.NIGHT_VISION);
	    		PotionEffect eyes = new PotionEffect(MobEffects.NIGHT_VISION, 1200, 0, false, false);
	    		eyes.addCurativeItem(new ItemStack(AtumItems.EYES_OF_ATUM));
	    		player.addPotionEffect(eyes);
	    	}
    	}
    }
}