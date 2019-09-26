package com.teammetallurgy.atum.utils;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.client.ClientHandler;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRegistry {
    private static final List<Item> ITEMS = Lists.newArrayList();
    private static final List<Block> BLOCKS = Lists.newArrayList();
    private static final List<TileEntityType> TILE_ENTITIES = Lists.newArrayList();
    public static final List<AtumBiome> BIOMES = Lists.newArrayList();
    private static final List<EntityType<?>> ENTITIES = Lists.newArrayList();
    private static final List<SoundEvent> SOUNDS = Lists.newArrayList();
    private static final List<ParticleType> PARTICLES = Lists.newArrayList();

    /**
     * Registers an item
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @return The Item that was registered
     */
    public static Item registerItem(@Nonnull Item item, @Nonnull String name) {
        item.setRegistryName(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)));
        ITEMS.add(item);
        return item;
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item.Properties, String)}, but have an empty Item.Properties set
     *
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull String name) {
        return registerBlock(block, new Item.Properties(), name);
    }

    /**
     * Same as {@link AtumRegistry#registerBlockWithItem(Block, BlockItem, String)}, but have a basic BlockItem
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static Block registerBlock(@Nonnull Block block, @Nullable Item.Properties properties, @Nonnull String name) {
        BlockItem blockItem = new BlockItem(block, properties == null ? new Item.Properties() : properties.group(Atum.GROUP));
        return registerBlockWithItem(block, blockItem, name);
    }

    /**
     * Same as {@link AtumRegistry#registerBaseBlock(Block, String)}, but allows for registering an BlockItem at the same time
     */
    public static Block registerBlockWithItem(@Nonnull Block block, BlockItem blockItem, @Nonnull String name) {
        registerItem(blockItem, AtumUtils.toRegistryName(name));
        return registerBaseBlock(block, name);
    }

    /**
     * Registers a block
     *
     * @param block The block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static Block registerBaseBlock(@Nonnull Block block, @Nonnull String name) {
        block.setRegistryName(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)));
        BLOCKS.add(block);

        if (block instanceof IRenderMapper && EffectiveSide.get() == LogicalSide.CLIENT) {
            ClientHandler.ignoreRenderProperty(block);
        }
        return block;
    }

    /**
     * Registers a TileEntityType
     *
     * @param name    The name to register the TileEntity with
     * @param builder The TileEntityType builder
     * @return The TileEntity that was registered
     */
    public static <T extends TileEntity> TileEntityType<T> registerTileEntity(@Nonnull String name, @Nonnull TileEntityType.Builder<T> builder) {
        TileEntityType<T> tileEntityType = builder.build(null);
        tileEntityType.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
        TILE_ENTITIES.add(tileEntityType);
        return tileEntityType;
    }

    /**
     * Registers any mob, that will have a spawn egg.
     *
     * @param name         String to register the entity as
     * @param eggPrimary   Primary egg color
     * @param eggSecondary Secondary Color
     * @param builder      Builder for the entity
     * @return The EntityType that was registered
     */
    public static <T extends Entity> EntityType<T> registerMob(String name, int eggPrimary, int eggSecondary, EntityType.Builder<T> builder) {
        EntityType<T> entityType = registerEntity(name, builder);
        Item spawnEgg = new SpawnEggItem(entityType, eggPrimary, eggSecondary, (new Item.Properties()).group(ItemGroup.MISC));
        registerItem(spawnEgg, name + "_spawn_egg");
        return entityType;
    }

    /**
     * Registers an entity
     *
     * @param name    String to register the entity as
     * @param builder Builder for the entity
     * @return The EntityType that was registered
     */
    public static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.Builder<T> builder) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, name);
        EntityType<T> entityType = builder.build(location.toString());
        entityType.setRegistryName(location);
        ENTITIES.add(entityType);
        return entityType;
    }

    /**
     * Registers arrow.
     *
     * @param name String to register the arrow with
     * @return The Arrow EntityType that was registered
     */
    public static <T extends AbstractArrowEntity> EntityType<T> registerArrow(String name, EntityType.IFactory<T> factory) {
        EntityType.Builder<T> builder = EntityType.Builder.create(factory, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setTrackingRange(4)
                .setUpdateInterval(20);
        return registerEntity(name, builder);
    }

    /**
     * Registers a biome
     *
     * @param biome The biome to be registered
     * @param name  The name to register the biome with
     * @return The Biome that was registered
     */
    public static AtumBiome registerBiome(AtumBiome biome, String name) {
        biome.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
        BIOMES.add(biome);
        return biome;
    }

    /**
     * Registers a sound
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static SoundEvent registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Constants.MOD_ID, name);
        SoundEvent sound = new SoundEvent(resourceLocation);
        sound.setRegistryName(resourceLocation);
        SOUNDS.add(sound);
        return sound;
    }

    /**
     * Registers a particle
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static BasicParticleType registerParticle(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Constants.MOD_ID, name);
        BasicParticleType particleType = new BasicParticleType(false);
        particleType.setRegistryName(resourceLocation);
        PARTICLES.add(particleType);
        return particleType;
    }

    /**
     * Makes it easier to register a new recipe. Should be called in the RegistryEvent.Register event
     *
     * @param registryName the unique name for the recipe
     * @param entry        the recipe
     * @param event        the RegistryEvent.Register event
     */
    public static <T extends IForgeRegistryEntry<T>> T registerRecipe(String registryName, T entry, RegistryEvent.Register<T> event) {
        entry.setRegistryName(new ResourceLocation(Constants.MOD_ID, registryName));
        event.getRegistry().register(entry);
        return entry;
    }

    /**
     * Used to register a new registry
     *
     * @param registryName the unique string to register the registry as
     * @param type         the class that the registry is for
     * @return a new registry
     */
    public static <T extends IForgeRegistryEntry<T>> IForgeRegistry<T> makeRegistry(String registryName, Class<T> type) {
        return new RegistryBuilder<T>().setName(new ResourceLocation(Constants.MOD_ID, registryName)).setType(type).setMaxID(Integer.MAX_VALUE >> 5).allowModification().create();
    }

    /*
     * Registry events
     */

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
        AtumItems.setItemInfo();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }
        AtumBlocks.setBlockInfo();
    }

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        for (TileEntityType tileEntityType : TILE_ENTITIES) {
            event.getRegistry().register(tileEntityType);
        }
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        for (Biome biome : BIOMES) {
            event.getRegistry().register(biome);
        }
        AtumBiomes.addBiomeTags();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        new AtumEntities();

        for (EntityType entityType : ENTITIES) {
            event.getRegistry().register(entityType);
        }
        //EntityRegistry.instance().lookupModSpawn(EntityCamelSpit.class, true).setCustomSpawning(null, true); //TODO Check if this is needed
    }

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : SOUNDS) {
            event.getRegistry().register(sound);
        }
    }

    @SubscribeEvent
    public static void registerParticle(RegistryEvent.Register<ParticleType<?>> event) {
        for (ParticleType particleType : PARTICLES) {
            event.getRegistry().register(particleType);
        }
    }
}