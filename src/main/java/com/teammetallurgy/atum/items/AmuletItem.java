package com.teammetallurgy.atum.items;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import baubles.api.cap.IBaublesItemHandler;
import baubles.api.inv.BaublesInventoryWrapper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class AmuletItem extends Item implements IBauble {
    public static final boolean IS_BAUBLES_INSTALLED = Loader.isModLoaded("baubles");

    public AmuletItem() {
        super();
        this.setMaxStackSize(1);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    @Nonnull
    public Rarity getRarity(@Nonnull ItemStack stack) {
        return Rarity.RARE;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.VANISHING_CURSE || enchantment == Enchantments.MENDING || enchantment == Enchantments.UNBREAKING;
    }

    @Override
    public int getItemEnchantability(@Nonnull ItemStack stack) {
        return 1;
    }

    @Override
    public boolean getIsRepairable(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return repair.getItem() == Items.DIAMOND;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(@Nonnull ItemStack itemstack) {
        return BaubleType.AMULET;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(ItemStack itemstack, LivingEntity player) {
        return true;
    }

    @Optional.Method(modid = "baubles")
    public static ItemStack getAmulet(PlayerEntity player) {
        return getBaublesInventory(player).getStackInSlot(0);
    }

    @Optional.Method(modid = "baubles")
    protected static IInventory getBaublesInventory(PlayerEntity player) {
        IBaublesItemHandler baublesItemHandler = BaublesApi.getBaublesHandler(player);
        return new BaublesInventoryWrapper(baublesItemHandler, player);
    }
}