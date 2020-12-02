package com.teammetallurgy.atum.misc;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.blocks.base.AtumTorchBlock;
import com.teammetallurgy.atum.blocks.wood.AtumTorchUnlitBlock;
import com.teammetallurgy.atum.blocks.wood.AtumWallSignBlock;
import com.teammetallurgy.atum.blocks.wood.AtumWallTorch;
import com.teammetallurgy.atum.blocks.wood.AtumWallTorchUnlitBlock;
import com.teammetallurgy.atum.client.particle.NebuFlameParticle;
import com.teammetallurgy.atum.entity.projectile.arrow.CustomArrow;
import com.teammetallurgy.atum.init.*;
import com.teammetallurgy.atum.items.AtumScaffoldingItem;
import com.teammetallurgy.atum.items.RelicItem;
import com.teammetallurgy.atum.items.tools.ScepterItem;
import com.teammetallurgy.atum.misc.datagenerator.BlockStatesGenerator;
import com.teammetallurgy.atum.misc.datagenerator.RecipeGenerator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.WoodType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.data.BiomeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumRegistry {
    private static final List<Item> ITEMS = Lists.newArrayList();
    private static final List<Block> BLOCKS = Lists.newArrayList();
    public static final List<Biome> BIOMES = Lists.newArrayList();
    public static final List<RegistryKey<Biome>> BIOME_KEYS = Lists.newArrayList();
    private static final List<EntityType<?>> ENTITIES = Lists.newArrayList();
    private static final List<SoundEvent> SOUNDS = Lists.newArrayList();
    private static final List<ParticleType<?>> PARTICLES = Lists.newArrayList();
    public static final List<EntityType<? extends CustomArrow>> ARROWS = Lists.newArrayList();

    /**
     * Registers an item
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @return The Item that was registered
     */
    public static Item registerItem(@Nonnull Item item, @Nonnull String name) {
        item.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        ITEMS.add(item);
        return item;
    }

    /**
     * Helper method for easily registering relics
     *
     * @param type The relic type
     * @return The dirty relic item that was registered
     */
    public static RelicItem registerRelic(@Nonnull RelicItem.Type type) {
        Item.Properties nonDirty = new Item.Properties().maxStackSize(16);
        RelicItem dirty = new RelicItem(new Item.Properties().maxStackSize(64));
        registerItem(dirty, getRelicName(RelicItem.Quality.DIRTY, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SILVER, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.GOLD, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SAPPHIRE, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.RUBY, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.EMERALD, type));
        registerItem(new RelicItem(nonDirty), getRelicName(RelicItem.Quality.DIAMOND, type));
        return dirty;
    }

    public static Item registerScepter(God god) {
        ScepterItem scepter = new ScepterItem();
        ScepterItem.SCEPTERS.put(god, scepter);
        return AtumRegistry.registerItem(scepter, "scepter_" + god.getName());
    }

    private static String getRelicName(@Nonnull RelicItem.Quality quality, @Nonnull RelicItem.Type type) {
        RelicItem.RELIC_ENTRIES.add(new RelicItem.RelicEntry(quality, quality.getWeight()));
        return "relic_" + quality.getString() + "_" + type.getString();
    }

    /**
     * Helper method for easily registering torches with unlit torches
     *
     * @param torch The torch block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static Block registerTorchWithUnlit(@Nonnull AtumTorchBlock torch, @Nonnull String name) {
        Block unlitTorch = new AtumTorchUnlitBlock();
        Block wallTorchLit = new AtumWallTorch(Block.Properties.from(unlitTorch).lootFrom(torch), torch.getParticleType());
        Block wallTorchUnlit = new AtumWallTorchUnlitBlock(wallTorchLit, Block.Properties.from(unlitTorch).lootFrom(unlitTorch));
        registerBaseBlock(wallTorchLit, "wall_" + name);
        registerBaseBlock(wallTorchUnlit, "wall_" + name + "_unlit");
        registerBlockWithItem(unlitTorch, new WallOrFloorItem(unlitTorch, wallTorchUnlit, new Item.Properties()), name + "_unlit");

        AtumTorchUnlitBlock.UNLIT.put(torch, unlitTorch);
        AtumWallTorchUnlitBlock.UNLIT.put(torch, wallTorchUnlit);
        AtumTorchUnlitBlock.LIT.put(unlitTorch, torch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(torch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(unlitTorch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchLit);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchUnlit);

        return registerBlockWithItem(torch, new WallOrFloorItem(torch, wallTorchLit, new Item.Properties().group(Atum.GROUP)), name);
    }

    public static Block registerTorch(@Nonnull AtumTorchBlock torch, @Nonnull String name) {
        Block wallTorchLit = new AtumWallTorch(Block.Properties.from(torch).lootFrom(torch), torch.getParticleType());
        registerBaseBlock(wallTorchLit, "wall_" + name);
        AtumTorchUnlitBlock.ALL_TORCHES.add(torch);
        AtumTorchUnlitBlock.ALL_TORCHES.add(wallTorchLit);
        return registerBlockWithItem(torch, new WallOrFloorItem(torch, wallTorchLit, new Item.Properties().group(Atum.GROUP)), name);
    }

    /**
     * Helper method for easily registering scaffolding
     *
     * @param scaffolding The scaffolding block to be registered
     * @param name  The name to register the block with
     * @return The Block that was registered
     */
    public static Block registerScaffolding(@Nonnull Block scaffolding, @Nonnull String name) {
        return registerBlockWithItem(scaffolding, new AtumScaffoldingItem(scaffolding), name);
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item.Properties, String)}, but have an empty Item.Properties set
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull String name) {
        return registerBlock(block, new Item.Properties(), name);
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item.Properties, String)} and have an easy way to set a ISTER
     *
     * @param properties BlockItem properties, can be set to null to not have any ItemGroup
     */
    public static Block registerBlock(@Nonnull Block block, Supplier<Callable<ItemStackTileEntityRenderer>> ister, @Nullable Item.Properties properties, @Nonnull String name) {
        BlockItem blockItem = new BlockItem(block, properties == null ? new Item.Properties().setISTER(ister) : properties.setISTER(ister).group(Atum.GROUP));
        return registerBlockWithItem(block, blockItem, name);
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
        registerItem(blockItem, name);
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
        block.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        BLOCKS.add(block);
        return block;
    }

    /**
     * Allows for easy registering of signs, that handles Ground Sign, Wall Sign and Item sign registration
     */
    public static Block registerSign(@Nonnull Block signBlock, @Nonnull WoodType woodType) {
        String typeName = woodType.getName().replace("atum_", "");
        Block wallSignBlock = new AtumWallSignBlock(AbstractBlock.Properties.create(Material.WOOD).doesNotBlockMovement().hardnessAndResistance(1.0F).sound(SoundType.WOOD).lootFrom(signBlock), woodType);
        Item signItem = new SignItem((new Item.Properties()).maxStackSize(16).group(Atum.GROUP), signBlock, wallSignBlock);
        AtumWallSignBlock.WALL_SIGN_BLOCKS.put(signBlock, wallSignBlock);
        registerItem(signItem, typeName + "_sign");
        registerBaseBlock(wallSignBlock, typeName + "_wall_sign");
        return registerBaseBlock(signBlock, typeName + "_sign");
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
        Item spawnEgg = new SpawnEggItem(entityType, eggPrimary, eggSecondary, (new Item.Properties()).group(Atum.GROUP));
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
        ResourceLocation location = new ResourceLocation(Atum.MOD_ID, name);
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
    public static <T extends CustomArrow> EntityType<T> registerArrow(String name, EntityType.IFactory<T> factory) {
        EntityType.Builder<T> builder = EntityType.Builder.create(factory, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                .setTrackingRange(4)
                .func_233608_b_(20);
        EntityType<T> entityType = registerEntity(name, builder);
        ARROWS.add(entityType);
        return entityType;
    }

    /**
     * Registers a biome
     *
     * @param biome The biome to be registered
     * @param name  The name to register the biome with
     * @return The Biome that was registered
     */
    public static Biome registerBiome(Biome biome, String name) {
        biome.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        BIOMES.add(biome);
        return biome;
    }

    /**
     * Registers a biome key
     *
     * @param biomeName  The name to register the biome key with
     * @return The Biome key that was registered
     */
    public static RegistryKey<Biome> registerBiomeKey(String biomeName) {
        RegistryKey<Biome> biomeKey = RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, new ResourceLocation(Atum.MOD_ID, biomeName));
        BIOME_KEYS.add(biomeKey);
        return biomeKey;
    }

    /**
     * Registers a sound
     *
     * @param name The name to register the sound with
     * @return The Sound that was registered
     */
    public static SoundEvent registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Atum.MOD_ID, name);
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
        ResourceLocation resourceLocation = new ResourceLocation(Atum.MOD_ID, name);
        BasicParticleType particleType = new BasicParticleType(false);
        particleType.setRegistryName(resourceLocation);
        PARTICLES.add(particleType);
        return particleType;
    }

    public static BasicParticleType registerGodFlame(String name, God god) {
        BasicParticleType particleType = registerParticle(name);
        NebuFlameParticle.GOD_FLAMES.put(god, particleType);
        return particleType;
    }
        /*
     * Registry events
     */

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        new AtumItems();
        for (Item item : ITEMS) {
            event.getRegistry().register(item);
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        new AtumBlocks();
        for (Block block : BLOCKS) {
            event.getRegistry().register(block);
        }
        AtumBlocks.setBlockInfo();
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        //new AtumBiomes();
        for (Biome biome : BIOMES) {
            event.getRegistry().register(biome);
        }
        AtumBiomes.addBiomeTags();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        new AtumEntities();
        for (EntityType<?> entityType : ENTITIES) {
            event.getRegistry().register(entityType);
        }
        AtumEntities.registerSpawnPlacement();
    }

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> event) {
        new AtumSounds();
        for (SoundEvent sound : SOUNDS) {
            event.getRegistry().register(sound);
        }
    }

    @SubscribeEvent
    public static void registerParticle(RegistryEvent.Register<ParticleType<?>> event) {
        new AtumParticles();
        for (ParticleType<?> particleType : PARTICLES) {
            event.getRegistry().register(particleType);
        }
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

        if (event.includeReports()) {
            gen.addProvider(new BiomeProvider(gen));
        }
    }
}