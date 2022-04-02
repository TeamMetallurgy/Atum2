package com.teammetallurgy.atum.mixin;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.MixinMethods;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryLoader;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionType.class)
public class MixinAtumBiomeBuilder { //TODO This is basically just a workaround to get custom OverworldBiomeBuilder working with Atum. Only way I could figure out, after talking to a lot of people.

    @Inject(method = "defaultDimensions(Lnet/minecraft/core/RegistryAccess;J)Lnet/minecraft/core/Registry;", at = @At("RETURN"))
    private static void addDimension(RegistryAccess registryAccess, long seed, CallbackInfoReturnable<WritableRegistry<LevelStem>> cir) {
        MixinMethods.registerAtumLevelStem(registryAccess, cir.getReturnValue(), seed);
    }

    @Inject(method = "registerBuiltin(Lnet/minecraft/core/RegistryAccess$Writable;)Lnet/minecraft/core/RegistryAccess$Writable;", at = @At("RETURN"))
    private static void register (RegistryAccess.Writable registryAccess, CallbackInfoReturnable<RegistryAccess.Writable> cir) {
        WritableRegistry<DimensionType> writableRegistry = registryAccess.ownedWritableRegistryOrThrow(Registry.DIMENSION_TYPE_REGISTRY);
        writableRegistry.register(Atum.DIMENSION_TYPE, Atum.DEFAULT_ATUM, Lifecycle.stable());
    }
}