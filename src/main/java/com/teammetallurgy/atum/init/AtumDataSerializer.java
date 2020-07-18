package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.efreet.SunspeakerData;
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
    public static final IDataSerializer<SunspeakerData> SUNSPEAKER_DATA = new IDataSerializer<SunspeakerData>() {
        @Override
        public void write(@Nonnull PacketBuffer buf, @Nonnull SunspeakerData sunspeakerData) {
            buf.writeVarInt(sunspeakerData.getLevel());
        }

        @Override
        @Nonnull
        public SunspeakerData read(@Nonnull PacketBuffer buf) {
            return new SunspeakerData(buf.readVarInt());
        }

        @Override
        @Nonnull
        public SunspeakerData copyValue(@Nonnull SunspeakerData sunspeakerData) {
            return sunspeakerData;
        }
    };
    private static final DataSerializerEntry SUNSPEAKER_DATA_ENTRY = register("sunspeaker_data", new DataSerializerEntry(SUNSPEAKER_DATA));

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
