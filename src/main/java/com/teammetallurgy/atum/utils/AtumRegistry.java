package com.teammetallurgy.atum.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.entity.projectile.EntityCamelSpit;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumEntities;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.proxy.ClientProxy;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
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
    private static final List<AtumBiome> BIOMES = Lists.newArrayList();
    private static final List<EntityType<?>> MOBS = Lists.newArrayList();
    private static final List<EntityType<?>> ENTITIES = Lists.newArrayList();
    private static final List<SoundEvent> SOUNDS = Lists.newArrayList();

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
     * Same as {@link AtumRegistry#registerBlock(Block, BlockItem, String)}, but have a basic ItemBlock
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static Block registerBlock(@Nonnull Block block, @Nullable Item.Properties properties, @Nonnull String name) {
        BlockItem blockItem = new BlockItem(block, properties == null ? new Item.Properties() : properties.group(Atum.GROUP));
        return registerBlock(block, blockItem, name);
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, String)}, but allows for registering an BlockItem at the same time
     *
     */
    public static Block registerBlock(@Nonnull Block block, BlockItem blockItem, @Nonnull String name) {
        registerItem(blockItem, AtumUtils.toRegistryName(name));
        return registerBlock(block, name);
    }

    /**
     * Registers a block
     *
     * @param block The block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull String name) {
        block.setRegistryName(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)));
        BLOCKS.add(block);

        if (block instanceof IRenderMapper && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ClientProxy.ignoreRenderProperty(block);
        }
        return block;
    }

    /**
     * Registers a TileEntityType
     *
     * @param name  The name to register the TileEntity with
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
     * @param entityClass The entity class
     * @return The EntityEntry that was registered
     */
    public static EntityEntry registerMob(@Nonnull Class<? extends Entity> entityClass, int eggPrimary, int eggSecondary) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", ""));
        EntityEntry entry = new EntityEntry(entityClass, location.toString());
        entry.setRegistryName(location);
        entry.setEgg(new EntityList.EntityEggInfo(location, eggPrimary, eggSecondary));
        MOBS.add(entry);
        return entry;
    }

    /**
     * Registers arrow.
     *
     * @param entityClass The arrow entity class
     * @return The EntityEntry that was registered
     */
    public static EntityEntry registerArrow(@Nonnull Class<? extends Entity> entityClass) {
        return registerEntity(entityClass, 64, 20, false);
    }

    /**
     * Registers any kind of entity, that is not a mob.
     *
     * @param entityClass The entity class
     * @return The EntityEntry that was registered
     */
    public static EntityEntry registerEntity(@Nonnull Class<? extends Entity> entityClass, int range, int updateFreq, boolean sendVelocityUpdates) {
        ResourceLocation location = new ResourceLocation(Constants.MOD_ID, CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", ""));
        EntityEntry entry = new EntityEntry(entityClass, location.toString());
        entry.setRegistryName(location);
        trackingRange.put(location, range);
        updateFrequency.put(location, updateFreq);
        sendsVelocityUpdates.put(location, sendVelocityUpdates);
        ENTITIES.add(entry);

        return entry;
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
     * @param name  The name to register the sound with
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
        AtumBiomes.registerBiomes();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        new AtumEntities();

        int networkIdMob = 0;
        for (EntityEntry entry : MOBS) {
            Preconditions.checkNotNull(entry.getRegistryName(), "registryName");
            networkIdMob++;
            event.getRegistry().register(EntityEntryBuilder.create()
                    .entity(entry.getEntityClass())
                    .id(entry.getRegistryName(), networkIdMob)
                    .name(AtumUtils.toUnlocalizedName(entry.getName()))
                    .tracker(80, 3, true)
                    .egg(entry.getEgg().primaryColor, entry.getEgg().secondaryColor)
                    .build());
        }
        int networkIdEntity = MOBS.size() + 1;
        for (EntityEntry entry : ENTITIES) {
            Preconditions.checkNotNull(entry.getRegistryName(), "registryName");
            networkIdEntity++;
            event.getRegistry().register(EntityEntryBuilder.create()
                    .entity(entry.getEntityClass())
                    .id(entry.getRegistryName(), networkIdEntity)
                    .tracker(trackingRange.get(entry.getRegistryName()), updateFrequency.get(entry.getRegistryName()), sendsVelocityUpdates.get(entry.getRegistryName()))
                    .name(AtumUtils.toUnlocalizedName(entry.getName()))
                    .build());
        }
        
        EntityRegistry.instance().lookupModSpawn(EntityCamelSpit.class, true).setCustomSpawning(null, true);
    }

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : SOUNDS) {
            event.getRegistry().register(sound);
        }
    }
}