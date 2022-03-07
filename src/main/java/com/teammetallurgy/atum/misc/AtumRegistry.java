package com.teammetallurgy.atum.misc;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.lighting.AtumTorchBlock;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.misc.datagenerator.BlockStatesGenerator;
import com.teammetallurgy.atum.misc.datagenerator.RecipeGenerator;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRegistry {
    /**
     * Registers a particle
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static SimpleParticleType registerParticle(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Atum.MOD_ID, name);
        SimpleParticleType particleType = new SimpleParticleType(false);
        particleType.setRegistryName(resourceLocation);
        return particleType;
    }

    public static SimpleParticleType registerGodFlame(String name, God god) {
        SimpleParticleType particleType = registerParticle(name);
        AtumTorchBlock.GOD_FLAMES.put(god, particleType);
        AtumTorchBlock.GODS.put(particleType, god);
        return particleType;
    }

    /*
     * Registry events
     */

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        AtumItems.setItemInfo();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        AtumBlocks.setBlockInfo();
    }

    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();

        if (event.includeClient()) {
            gen.addProvider(new BlockStatesGenerator(gen, event.getExistingFileHelper()));
        }

        if (event.includeServer()) {
            gen.addProvider(new RecipeGenerator(gen));
        }
    }
}