package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.model.armor.RaArmorModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import com.teammetallurgy.atum.misc.StackHelper;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class RaArmor extends ArtifactArmor {

    public RaArmor(EquipmentSlotType slot) {
        super(AtumMats.NEBU_ARMOR, "ra_armor", slot, new Item.Properties().rarity(Rarity.RARE));
        this.setHasRender();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, @Nonnull ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return (A) new RaArmorModel(armorSlot, hasFullSet(entityLiving));
    }

    @Override
    public God getGod() {
        return God.RA;
    }

    @Override
    public Item getHelmet() {
        return AtumItems.HALO_OF_RA;
    }

    @Override
    public Item getChestplate() {
        return AtumItems.BODY_OF_RA;
    }

    @Override
    public Item getLeggings() {
        return AtumItems.LEGS_OF_RA;
    }

    @Override
    public Item getBoots() {
        return AtumItems.FEET_OF_RA;
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (StackHelper.hasFullArmorSet(player, AtumItems.HALO_OF_RA, AtumItems.BODY_OF_RA, AtumItems.LEGS_OF_RA, AtumItems.FEET_OF_RA)) {
            player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 300, 0, true, false));
        }
    }
}