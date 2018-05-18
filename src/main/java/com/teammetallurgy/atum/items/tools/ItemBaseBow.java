package com.teammetallurgy.atum.items.tools;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemBaseBow extends ItemBow {

    public ItemBaseBow() {
        this.maxStackSize = 1;
        this.setMaxDamage(384);

        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
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
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                return entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F;
            }
        });
    }
}