package com.teammetallurgy.atum.entity.ai.brain.sensor;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.ai.brain.AtumCamelAi;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.sensing.TemptingSensor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AtumSensorTypes {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPE_DEFERRED = DeferredRegister.create(Registries.SENSOR_TYPE, Atum.MOD_ID);
    public static final DeferredHolder<SensorType<?>, SensorType<AtumSecondaryPositionSensor>> SECONDARY_POIS = register("secondary_pois", AtumSecondaryPositionSensor::new);
    public static final DeferredHolder<SensorType<?>, SensorType<TemptingSensor>> CAMEL_TEMPTATIONS = register("camel_temptations", () -> new TemptingSensor(AtumCamelAi.getTemptations()));

    public static <U extends Sensor<?>> DeferredHolder<SensorType<?>, SensorType<U>> register(String name, Supplier<U> sensorSupplier) {
        return SENSOR_TYPE_DEFERRED.register(name, () -> new SensorType<>(sensorSupplier));
    }
}