package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VillagerSpawnEggItem extends SpawnEggItem {

    public VillagerSpawnEggItem() {
        super(AtumEntities.VILLAGER_MALE, 0x9D7A62, 0x452D25, new Item.Properties().group(Atum.GROUP));
        EGGS.put(AtumEntities.VILLAGER_FEMALE, this);
    }

    @Override
    @Nonnull
    public EntityType<?> getType(@Nullable CompoundNBT nbt) {
        EntityType<? extends AtumVillagerEntity> villager = AtumEntities.VILLAGER_MALE;
        if (random.nextDouble() > 0.5D) {
            villager = AtumEntities.VILLAGER_FEMALE;
        }
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundNBT compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byKey(compoundnbt.getString("id")).orElse(villager);
            }
        }
        return villager;
    }
}