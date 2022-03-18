package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.Race;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class AtumDataSerializer {
    public static final DeferredRegister<DataSerializerEntry> DATA_SERIALIZER_DEFERRED = DeferredRegister.create(ForgeRegistries.Keys.DATA_SERIALIZERS, Atum.MOD_ID);
    public static final EntityDataSerializer<AtumVillagerData> VILLAGER_DATA = new EntityDataSerializer<AtumVillagerData>() {
        @Override
        public void write(FriendlyByteBuf buf, AtumVillagerData value) {
            buf.writeUtf(String.valueOf(Atum.villagerProfession.get().getKey(value.getAtumProfession())));
            buf.writeVarInt(value.getLevel());
            buf.writeEnum(value.getRace());
        }

        @Override
        @Nonnull
        public AtumVillagerData read(FriendlyByteBuf buf) {
            return new AtumVillagerData(Atum.villagerProfession.get().getValue(new ResourceLocation(buf.readUtf())), buf.readVarInt(), buf.readEnum(Race.class));
        }

        @Override
        @Nonnull
        public AtumVillagerData copy(@Nonnull AtumVillagerData value) {
            return value;
        }
    };
    private static final RegistryObject<DataSerializerEntry> VILLAGER_DATA_ENTRY = register("villager_data", new DataSerializerEntry(VILLAGER_DATA));

    public static RegistryObject<DataSerializerEntry> register(String name, DataSerializerEntry dataSerializerEntry) {
        return DATA_SERIALIZER_DEFERRED.register(name, () -> dataSerializerEntry);
    }
}