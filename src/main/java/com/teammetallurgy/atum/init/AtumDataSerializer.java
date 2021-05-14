package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.Race;
import com.teammetallurgy.atum.misc.AtumRegistry;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DataSerializerEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumDataSerializer {
    private static final List<DataSerializerEntry> DATA_SERIALIZER_ENTRIES = new ArrayList<>();
    public static final IDataSerializer<AtumVillagerData> VILLAGER_DATA = new IDataSerializer<AtumVillagerData>() {
        @Override
        public void write(PacketBuffer buf, AtumVillagerData value) {
            buf.writeString(AtumRegistry.VILLAGER_PROFESSION.get().getKey(value.getAtumProfession()).toString());
            buf.writeVarInt(value.getLevel());
            buf.writeEnumValue(value.getRace());
        }

        @Override
        @Nonnull
        public AtumVillagerData read(PacketBuffer buf) {
            return new AtumVillagerData(AtumRegistry.VILLAGER_PROFESSION.get().getValue(new ResourceLocation(buf.readString())), buf.readVarInt(), buf.readEnumValue(Race.class));
        }

        @Override
        @Nonnull
        public AtumVillagerData copyValue(@Nonnull AtumVillagerData value) {
            return value;
        }
    };
    private static final DataSerializerEntry VILLAGER_DATA_ENTRY = register("villager_data", new DataSerializerEntry(VILLAGER_DATA));

    public static DataSerializerEntry register(String name, DataSerializerEntry dataSerializerEntry) {
        ResourceLocation id = new ResourceLocation(Atum.MOD_ID, name);
        dataSerializerEntry.setRegistryName(id);
        DATA_SERIALIZER_ENTRIES.add(dataSerializerEntry);
        return dataSerializerEntry;
    }

    @SubscribeEvent
    public static void registerDataSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
        for (DataSerializerEntry dataSerializerEntry : DATA_SERIALIZER_ENTRIES) {
            event.getRegistry().register(dataSerializerEntry);
        }
    }
}