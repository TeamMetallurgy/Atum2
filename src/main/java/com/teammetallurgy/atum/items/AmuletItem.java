package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Rarity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;

//@Optional.Interface(iface = "baubles.api.IBauble", modid = "baubles")
public class AmuletItem extends Item /*implements IBauble*/ { //TODO Fix when Baubles is updated. Optional stuff is gone due to J9 limitations, so Baubles will have to come up with a new system most likely
    public static final boolean IS_BAUBLES_INSTALLED = ModList.get().isLoaded("baubles");

    public AmuletItem(Item.Properties properties) {
        super(properties.maxStackSize(1).rarity(Rarity.RARE).group(Atum.GROUP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
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