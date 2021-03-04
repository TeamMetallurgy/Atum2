package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.villager.AtumVillagerData;
import com.teammetallurgy.atum.entity.villager.Race;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
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
        public void write(PacketBuffer buf, AtumVillagerData value) {
            buf.writeVarInt(Registry.VILLAGER_PROFESSION.getId(value.getProfession()));
            buf.writeVarInt(value.getLevel());
            buf.writeEnumValue(value.getRace());
            buf.writeBoolean(value.isFemale());
        }

        @Override
        public AtumVillagerData read(PacketBuffer buf) {
            return new AtumVillagerData(Registry.VILLAGER_PROFESSION.getByValue(buf.readVarInt()), buf.readVarInt(), buf.readEnumValue(Race.class), buf.readBoolean());
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