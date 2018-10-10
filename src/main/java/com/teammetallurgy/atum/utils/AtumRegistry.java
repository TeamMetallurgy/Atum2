package com.teammetallurgy.atum.utils;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.base.IRenderMapper;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.proxy.ClientProxy;
import com.teammetallurgy.atum.world.biome.base.AtumBiome;
import com.teammetallurgy.atum.world.gen.structure.PyramidPieces;
import com.teammetallurgy.atum.world.gen.structure.StructureAtumMineshaftPieces;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class AtumRegistry {
    public static final NonNullList<AtumBiome> BIOMES = NonNullList.create();
    private static final NonNullList<EntityEntry> MOBS = NonNullList.create();
    private static final NonNullList<EntityEntry> ENTITIES = NonNullList.create();
    private static final NonNullList<SoundEvent> SOUNDS = NonNullList.create();
    public static final NonNullList<ItemStack> HIDE_LIST = NonNullList.create();
    //Entity tracking values
    private static int trackingRange;
    private static int updateFrequency;
    private static boolean sendsVelocityUpdates;

    /**
     * Same as {@link AtumRegistry#registerItem(Item, String, CreativeTabs, String)}, but have CreativeTab set by default and easy way to set OreDictionary name
     */
    public static Item registerItem(@Nonnull Item item, @Nonnull String name, @Nullable String oreDictName) {
        return registerItem(item, name, Atum.CREATIVE_TAB, oreDictName);
    }

    /**
     * Same as {@link AtumRegistry#registerItem(Item, String, CreativeTabs, String)}, but have CreativeTab set by default
     */
    public static Item registerItem(@Nonnull Item item, @Nonnull String name) {
        return registerItem(item, name, Atum.CREATIVE_TAB, null);
    }

    /**
     * Registers an item
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @param tab  The creative tab for the item. Set to null for no CreativeTab
     * @return The Item that was registered
     */
    public static Item registerItem(@Nonnull Item item, @Nonnull String name, @Nullable CreativeTabs tab, @Nullable String oreDictName) {
        item.setRegistryName(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)));
        item.setTranslationKey(Constants.MOD_ID + "." + AtumUtils.toUnlocalizedName(name));
        ForgeRegistries.ITEMS.register(item);

        if (tab != null) {
            item.setCreativeTab(tab);
        } else if (Constants.IS_JEI_LOADED) {
            HIDE_LIST.add(new ItemStack(item));
        }

        if (oreDictName != null) {
            OreDictHelper.add(item, oreDictName);
        }

        if (item instanceof IOreDictEntry) {
            IOreDictEntry entry = (IOreDictEntry) item;
            OreDictHelper.entries.add(entry);
        }

        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)), "inventory"));
        }

        return item;
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item, String, CreativeTabs)}, but have a basic ItemBlock and CreativeTab set by default
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull String name) {
        return registerBlock(block, new ItemBlock(block), name, Atum.CREATIVE_TAB);
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item, String, CreativeTabs)}, but have a basic ItemBlock set by default
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull String name, @Nullable CreativeTabs tab) {
        return registerBlock(block, new ItemBlock(block), name, tab);
    }

    /**
     * Same as {@link AtumRegistry#registerBlock(Block, Item, String, CreativeTabs)}, but have CreativeTab set by default
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull Item itemBlock, @Nonnull String name) {
        return registerBlock(block, itemBlock, name, Atum.CREATIVE_TAB);
    }

    /**
     * Registers a block
     *
     * @param block The block to be registered
     * @param name  The name to register the block with
     * @param tab   The creative tab for the block. Set to null for no CreativeTab
     * @return The Block that was registered
     */
    public static Block registerBlock(@Nonnull Block block, @Nonnull Item itemBlock, @Nonnull String name, @Nullable CreativeTabs tab) {
        block.setRegistryName(new ResourceLocation(Constants.MOD_ID, AtumUtils.toRegistryName(name)));
        block.setTranslationKey(Constants.MOD_ID + "." + AtumUtils.toUnlocalizedName(name));
        ForgeRegistries.BLOCKS.register(block);
        registerItem(itemBlock, AtumUtils.toRegistryName(name));

        if (tab != null) {
            block.setCreativeTab(tab);
        } else if (Constants.IS_JEI_LOADED) {
            HIDE_LIST.add(new ItemStack(block));
        }

        if (block instanceof IOreDictEntry) {
            IOreDictEntry entry = (IOreDictEntry) block;
            OreDictHelper.entries.add(entry);
        }

        if (block instanceof IRenderMapper && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            ClientProxy.ignoreRenderProperty(block);
        }

        return block;
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
        trackingRange = range;
        updateFrequency = updateFreq;
        sendsVelocityUpdates = sendVelocityUpdates;
        ENTITIES.add(entry);

        return entry;
    }

    public static AtumBiome registerBiome(AtumBiome biome, String name) {
        biome.setRegistryName(new ResourceLocation(Constants.MOD_ID, name));
        BIOMES.add(biome);
        return biome;
    }

    public static SoundEvent registerSound(String name) {
        ResourceLocation resourceLocation = new ResourceLocation(Constants.MOD_ID, name);
        SoundEvent sound = new SoundEvent(resourceLocation);
        sound.setRegistryName(resourceLocation);
        SOUNDS.add(sound);
        return sound;
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        AtumItems.registerItems();
        OreDictHelper.register();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        AtumBlocks.registerBlocks();
        AtumBlocks.setBlockInfo();
        AtumBlocks.registerTileEntities();
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        AtumBiomes.registerBiomes();
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
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
                    .tracker(trackingRange, updateFrequency, sendsVelocityUpdates)
                    .name(AtumUtils.toUnlocalizedName(entry.getName()))
                    .build());
        }
    }

    @SubscribeEvent
    public static void registerSound(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : SOUNDS) {
            event.getRegistry().register(sound);
        }
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register event) {
        StructureAtumMineshaftPieces.registerMineshaft();
        PyramidPieces.registerPyramid();
    }
}