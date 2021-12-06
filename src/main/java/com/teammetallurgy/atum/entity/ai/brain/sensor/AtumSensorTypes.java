package com.teammetallurgy.atum.entity.ai.brain.sensor;

import com.teammetallurgy.atum.Atum;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AtumSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPE_DEFERRED = DeferredRegister.create(ForgeRegistries.SENSOR_TYPES, Atum.MOD_ID);
    public static final RegistryObject<SensorType<AtumSecondaryPositionSensor>> SECONDARY_POIS = register("secondary_pois", AtumSecondaryPositionSensor::new);

    public static <U extends Sensor<?>> RegistryObject<SensorType<U>> register(String name, Supplier<U> sensorSupplier) {
        return SENSOR_TYPE_DEFERRED.register(name, () -> new SensorType<>(sensorSupplier));
    }
}