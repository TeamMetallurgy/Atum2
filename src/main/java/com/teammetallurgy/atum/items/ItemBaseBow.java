package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBaseBow extends ItemBow {

    public ItemBaseBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);

        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                if (entity == null) {
                    return 0.0F;
                } else {
                    ItemStack activeStack = entity.getActiveItemStack();
                    return !(activeStack.getItem() instanceof ItemBaseBow) ? 0.0F : (float) (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20.0F;
                }
            }
        });
        String bowName = AtumUtils.toRegistryName(AtumUtils.toUnlocalizedName(this.getUnlocalizedName()));
        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, bowName + "_pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World worldIn, EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }

    protected ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }
}