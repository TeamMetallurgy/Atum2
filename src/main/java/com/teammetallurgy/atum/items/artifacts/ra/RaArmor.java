package com.teammetallurgy.atum.items.artifacts.ra;

import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.client.model.armor.RaArmorModel;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.artifacts.ArtifactArmor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class RaArmor extends ArtifactArmor {

    public RaArmor(EquipmentSlot slot) {
        super(AtumMats.NEBU_ARMOR, "ra_armor", slot, new Item.Properties().rarity(Rarity.RARE));
        this.setHasRender();
    }

    @Override
    public void initializeClient(@Nonnull Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public <A extends HumanoidModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, A _default) {
                return (A) new RaArmorModel(armorSlot, hasFullSet(entityLiving));
            }
        });
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
}