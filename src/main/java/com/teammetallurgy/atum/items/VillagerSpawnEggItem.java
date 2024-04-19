package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.init.AtumEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public class VillagerSpawnEggItem extends DeferredSpawnEggItem { //TODO

    public VillagerSpawnEggItem() {
        super(AtumEntities.VILLAGER_MALE, 0x9D7A62, 0x452D25, new Item.Properties());
        //BY_ID.put(AtumEntities.VILLAGER_FEMALE.get(), this);
    }

    @Override
    @Nonnull
    public EntityType<?> getType(@Nullable CompoundTag nbt) {
        EntityType<? extends AtumVillagerEntity> villager = AtumEntities.VILLAGER_MALE.get();
        Random random = new Random();
        if (random.nextDouble() > 0.5D) {
            villager = AtumEntities.VILLAGER_FEMALE.get();
        }
        if (nbt != null && nbt.contains("EntityTag", 10)) {
            CompoundTag compoundnbt = nbt.getCompound("EntityTag");
            if (compoundnbt.contains("id", 8)) {
                return EntityType.byString(compoundnbt.getString("id")).orElse(villager);
            }
        }
        return villager;
    }
}