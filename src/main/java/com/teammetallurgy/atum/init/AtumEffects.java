package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.misc.potion.MarkedForDeath;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(value = Atum.MOD_ID)
public class AtumEffects {
    private static final List<Effect> EFFECTS = new ArrayList<>();
    public static final Effect MARKED_FOR_DEATH = register("marked_for_death", new MarkedForDeath());

    public static Effect register(String name, Effect effect) {
        effect.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        EFFECTS.add(effect);
        return effect;
    }

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event) {
        for (Effect effect : EFFECTS) {
            event.getRegistry().register(effect);
        }
        MinecraftForge.EVENT_BUS.register(MARKED_FOR_DEATH);
    }
}