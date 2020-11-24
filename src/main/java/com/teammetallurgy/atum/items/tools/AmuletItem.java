package com.teammetallurgy.atum.items.tools;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumItems;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;

import javax.annotation.Nonnull;

//@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class AmuletItem extends Item /*implements IBauble*/ { //TODO Use Curios instead?
    //public static final boolean IS_BAUBLES_INSTALLED = ModList.get().isLoaded("baubles");

    public AmuletItem(Item.Properties properties) {
        super(properties.rarity(Rarity.RARE).group(Atum.GROUP));
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
        return repair.getItem() == AtumItems.NEBU_INGOT;
    }

    /*@Override
    @Optional.Method(modid = "baubles")
    public BaubleType getBaubleType(@Nonnull ItemStack stack) {
        return BaubleType.AMULET;
    }

    @Override
    @Optional.Method(modid = "baubles")
    public boolean willAutoSync(@Nonnull ItemStack stack, LivingEntity player) {
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
    }*/
}