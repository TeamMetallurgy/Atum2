package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.utils.AtumUtils;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
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

        this.addPropertyOverride(new ResourceLocation(Constants.MOD_ID, "pull"), new IItemPropertyGetter() {
            @Override
            @SideOnly(Side.CLIENT)
            public float apply(@Nonnull ItemStack stack, World world, EntityLivingBase entity) {
                if (entity == null) {
                    return 0.0F;
                } else {
                    ItemStack activeStack = entity.getActiveItemStack();
                    return !(activeStack.getItem() instanceof ItemBaseBow) ? 0.0F : (float)(stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20.0F;
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

    @Override
    @Nonnull
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }
}