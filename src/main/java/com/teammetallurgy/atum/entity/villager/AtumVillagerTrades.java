package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumStructures;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.potion.*;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class AtumVillagerTrades {
    public static final Map<AtumVillagerProfession, Int2ObjectMap<VillagerTrades.ITrade[]>> VILLAGER_DEFAULT_TRADES = Util.make(Maps.newHashMap(), (trades) -> {
        trades.put(AtumVillagerProfession.ALCHEMIST.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.ROTTEN_FLESH, 32, 3, 16, 2), new ItemsForCoinsTrade(Items.REDSTONE, 3, 2, 12, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.GOLD_INGOT, 3, 3, 12, 2), new ItemsForCoinsTrade(Items.LAPIS_LAZULI, 3, 1, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.RABBIT_FOOT, 2, 3, 12, 20), new ItemsForCoinsTrade(Items.GLOWSTONE_DUST, 12, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.SCUTE, 2, 3, 12, 30), new CoinsForItemsTrade(Items.GLASS_BOTTLE, 9, 3, 12, 30), new ItemsForCoinsTrade(Items.ENDER_PEARL, 15, 1, 15)}, 5, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.ANPUTS_FINGERS_SPORES, 22, 4, 12, 30), new ItemsForCoinsTrade(Items.EXPERIENCE_BOTTLE, 9, 1, 30)})));
        trades.put(AtumVillagerProfession.ARMORER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_LEGS_IRON), 21, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_BOOTS_IRON), 12, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_HELMET_IRON), 15, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_CHEST_IRON), 27, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 54, 1, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_IRON, 9, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_IRON, 3, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.LAVA_BUCKET, 1, 3, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_IRON, 3, 12, 10, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_IRON, 12, 12, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.BRIGAND_SHIELD), 15, 1, 12, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 5, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_DIAMOND, 37, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_DIAMOND, 20, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_DIAMOND, 20, 3, 30, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_DIAMOND, 45, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.BUTCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.QUAIL_RAW, 14, 3, 16, 2), new CoinsForItemsTrade(Items.RABBIT, 4, 3, 16, 2), new ItemsForCoinsTrade(Items.RABBIT_STEW, 3, 1, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(AtumItems.QUAIL_COOKED, 3, 8, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.CAMEL_RAW, 10, 3, 16, 20), new ItemsForCoinsTrade(AtumItems.CAMEL_COOKED, 3, 3, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DRIED_KELP_BLOCK, 10, 3, 12, 30)}, 5, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.SWEET_BERRIES, 10, 3, 12, 30)})));
        trades.put(AtumVillagerProfession.CARTOGRAPHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.PAPER, 3, 24, 16, 2), new ItemsForCoinsTrade(Items.MAP, 1, 21, 5)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumBlocks.CRYSTAL_GLASS_PANE, 11, 3, 16, 10)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COMPASS, 1, 3, 12, 20), new CoinsForMapTrade(13, AtumStructures.PYRAMID_STRUCTURE, MapDecoration.Type.TARGET_X, 12, 15)}, 4, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.ITEM_FRAME, 21, 1, 15), new ItemsForCoinsTrade(Items.WHITE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 9, 1, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.GLOBE_BANNER_PATTERN, 3, 1, 30)})));
        trades.put(AtumVillagerProfession.FARMER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.WHEAT, 20, 16, 2), new CoinForItemsTrade(Items.POTATO, 26, 16, 2), new CoinForItemsTrade(Items.CARROT, 22, 16, 2), new CoinForItemsTrade(Items.BEETROOT, 15, 16, 2), new ItemsForCoinsTrade(Items.BREAD, 1, 6, 16, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Blocks.PUMPKIN, 6, 12, 10), new ItemsForCoinsTrade(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForCoinsTrade(Items.APPLE, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.COOKIE, 3, 18, 10), new CoinForItemsTrade(Blocks.MELON, 4, 12, 20)}, 4, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Blocks.CAKE, 1, 1, 12, 15), new SuspiciousStewForCoinTrade(Effects.NIGHT_VISION, 100, 15), new SuspiciousStewForCoinTrade(Effects.JUMP_BOOST, 160, 15), new SuspiciousStewForCoinTrade(Effects.WEAKNESS, 140, 15), new SuspiciousStewForCoinTrade(Effects.BLINDNESS, 120, 15), new SuspiciousStewForCoinTrade(Effects.POISON, 280, 15), new SuspiciousStewForCoinTrade(Effects.SATURATION, 7, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForCoinsTrade(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
        trades.put(AtumVillagerProfession.FLETCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.STICK, 32, 16, 2), new ItemsForCoinsTrade(Items.ARROW, 1, 16, 1), new ItemsForCoinsAndItemsTrade(Blocks.GRAVEL, 10, Items.FLINT, 10, 12, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.FLINT, 26, 12, 10), new ItemsForCoinsTrade(Items.BOW, 2, 1, 5)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.STRING, 14, 16, 20), new ItemsForCoinsTrade(Items.CROSSBOW, 3, 1, 10)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.FEATHER, 24, 16, 30), new EnchantedItemForCoinsTrade(Items.BOW, 2, 3, 15)}, 5, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.TRIPWIRE_HOOK, 8, 12, 30), new EnchantedItemForCoinsTrade(Items.CROSSBOW, 3, 3, 15), new ItemWithPotionForCoinsAndItemsTrade(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 30)})));
        trades.put(AtumVillagerProfession.GLASSBLOWER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.ROTTEN_FLESH, 32, 16, 2), new ItemsForCoinsTrade(Items.REDSTONE, 1, 2, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.GOLD_INGOT, 3, 12, 10), new ItemsForCoinsTrade(Items.LAPIS_LAZULI, 1, 1, 5)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.RABBIT_FOOT, 2, 12, 20), new ItemsForCoinsTrade(Blocks.GLOWSTONE, 4, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.SCUTE, 4, 12, 30), new CoinForItemsTrade(Items.GLASS_BOTTLE, 9, 12, 30), new ItemsForCoinsTrade(Items.ENDER_PEARL, 5, 1, 15)}, 5, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.NETHER_WART, 22, 12, 30), new ItemsForCoinsTrade(Items.EXPERIENCE_BOTTLE, 3, 1, 30)})));
        trades.put(AtumVillagerProfession.LIBRARIAN.get(), gatAsIntMap(ImmutableMap.<Integer, VillagerTrades.ITrade[]>builder().put(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.PAPER, 24, 16, 2), new EnchantedBookForCoinsTrade(1), new ItemsForCoinsTrade(Blocks.BOOKSHELF, 9, 1, 12, 1)}).put(2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.BOOK, 4, 12, 10), new EnchantedBookForCoinsTrade(5), new ItemsForCoinsTrade(Items.LANTERN, 1, 1, 5)}).put(3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.INK_SAC, 5, 12, 20), new EnchantedBookForCoinsTrade(10), new ItemsForCoinsTrade(Items.GLASS, 1, 4, 10)}).put(4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.WRITABLE_BOOK, 2, 12, 30), new EnchantedBookForCoinsTrade(15), new ItemsForCoinsTrade(Items.CLOCK, 5, 1, 15), new ItemsForCoinsTrade(Items.COMPASS, 4, 1, 15)}).put(5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.NAME_TAG, 20, 1, 30)}).build()));
        trades.put(AtumVillagerProfession.MASON.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.CLAY_BALL, 10, 16, 2), new ItemsForCoinsTrade(Items.BRICK, 1, 10, 16, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Blocks.STONE, 20, 16, 10), new ItemsForCoinsTrade(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Blocks.GRANITE, 16, 16, 20), new CoinForItemsTrade(Blocks.ANDESITE, 16, 16, 20), new CoinForItemsTrade(Blocks.DIORITE, 16, 16, 20), new ItemsForCoinsTrade(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForCoinsTrade(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForCoinsTrade(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.QUARTZ, 12, 12, 30), new ItemsForCoinsTrade(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Blocks.QUARTZ_PILLAR, 1, 1, 12, 30), new ItemsForCoinsTrade(Blocks.QUARTZ_BLOCK, 1, 1, 12, 30)})));
        trades.put(AtumVillagerProfession.TAILOR.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Blocks.WHITE_WOOL, 18, 16, 2), new CoinForItemsTrade(Blocks.BROWN_WOOL, 18, 16, 2), new CoinForItemsTrade(Blocks.BLACK_WOOL, 18, 16, 2), new CoinForItemsTrade(Blocks.GRAY_WOOL, 18, 16, 2), new ItemsForCoinsTrade(Items.SHEARS, 2, 1, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.WHITE_DYE, 12, 16, 10), new CoinForItemsTrade(Items.GRAY_DYE, 12, 16, 10), new CoinForItemsTrade(Items.BLACK_DYE, 12, 16, 10), new CoinForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 16, 10), new CoinForItemsTrade(Items.LIME_DYE, 12, 16, 10), new ItemsForCoinsTrade(Blocks.WHITE_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.ORANGE_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.MAGENTA_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.YELLOW_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.LIME_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.PINK_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.GRAY_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.CYAN_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.PURPLE_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.BLUE_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.BROWN_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.GREEN_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.RED_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.BLACK_WOOL, 1, 1, 16, 5), new ItemsForCoinsTrade(Blocks.WHITE_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.ORANGE_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.MAGENTA_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.YELLOW_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.LIME_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.PINK_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.GRAY_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.CYAN_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.PURPLE_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.BLUE_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.BROWN_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.GREEN_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.RED_CARPET, 1, 4, 16, 5), new ItemsForCoinsTrade(Blocks.BLACK_CARPET, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.YELLOW_DYE, 12, 16, 20), new CoinForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 16, 20), new CoinForItemsTrade(Items.ORANGE_DYE, 12, 16, 20), new CoinForItemsTrade(Items.RED_DYE, 12, 16, 20), new CoinForItemsTrade(Items.PINK_DYE, 12, 16, 20), new ItemsForCoinsTrade(Blocks.WHITE_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.YELLOW_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.RED_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLACK_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLUE_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BROWN_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.CYAN_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GRAY_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GREEN_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIME_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.MAGENTA_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.ORANGE_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PINK_BED, 3, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PURPLE_BED, 3, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.BROWN_DYE, 12, 16, 30), new CoinForItemsTrade(Items.PURPLE_DYE, 12, 16, 30), new CoinForItemsTrade(Items.BLUE_DYE, 12, 16, 30), new CoinForItemsTrade(Items.GREEN_DYE, 12, 16, 30), new CoinForItemsTrade(Items.MAGENTA_DYE, 12, 16, 30), new CoinForItemsTrade(Items.CYAN_DYE, 12, 16, 30), new ItemsForCoinsTrade(Items.WHITE_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 3, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.PAINTING, 2, 3, 30)})));
        trades.put(AtumVillagerProfession.TOOLSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForCoinsTrade(new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.IRON_INGOT, 4, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.FLINT, 30, 12, 20), new EnchantedItemForCoinsTrade(Items.IRON_AXE, 1, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_SHOVEL, 2, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_PICKAXE, 3, 3, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.DIAMOND, 1, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 12, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.WEAPONSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForCoinsTrade(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_SWORD, 2, 3, 1)}, 2, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.IRON_INGOT, 4, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.FLINT, 24, 12, 20)}, 4, new VillagerTrades.ITrade[]{new CoinForItemsTrade(Items.DIAMOND, 1, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 12, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F)})));
    });

    private static Int2ObjectMap<VillagerTrades.ITrade[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }

    static class CoinForItemsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public CoinForItemsTrade(IItemProvider tradeItem, int count, int maxUses, int xpValue) {
            this.tradeItem = tradeItem.asItem();
            this.count = count;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack tradeStack = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(tradeStack, new ItemStack(AtumItems.GOLD_COIN), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class CoinsForItemsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int count;
        private final int coinCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public CoinsForItemsTrade(IItemProvider tradeItem, int count, int coinCount, int maxUses, int xpValue) {
            this.tradeItem = tradeItem.asItem();
            this.count = count;
            this.coinCount = coinCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack tradeStack = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(tradeStack, new ItemStack(AtumItems.GOLD_COIN, this.coinCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class CoinsForMapTrade implements VillagerTrades.ITrade {
        private final int count;
        private final Structure<?> structureName;
        private final MapDecoration.Type mapDecorationType;
        private final int maxUses;
        private final int xpValue;

        public CoinsForMapTrade(int count, Structure<?> structureName, MapDecoration.Type mapDecorationType, int maxUses, int xpValue) {
            this.count = count;
            this.structureName = structureName;
            this.mapDecorationType = mapDecorationType;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
        }

        @Nullable
        public MerchantOffer getOffer(Entity trader, @Nonnull Random rand) {
            if (!(trader.world instanceof ServerWorld)) {
                return null;
            } else {
                ServerWorld serverWorld = (ServerWorld) trader.world;
                BlockPos pos = serverWorld.func_241117_a_(this.structureName, trader.getPosition(), 100, true);
                if (pos != null) {
                    ItemStack mapStack = FilledMapItem.setupNewMap(serverWorld, pos.getX(), pos.getZ(), (byte) 2, true, true);
                    FilledMapItem.func_226642_a_(serverWorld, mapStack);
                    MapData.addTargetDecoration(mapStack, pos, "+", this.mapDecorationType);
                    mapStack.setDisplayName(new TranslationTextComponent("filled_map." + this.structureName.getStructureName().toLowerCase(Locale.ROOT)));
                    return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, this.count), new ItemStack(Items.COMPASS), mapStack, this.maxUses, this.xpValue, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    static class EnchantedBookForCoinsTrade implements VillagerTrades.ITrade {
        private final int xpValue;

        public EnchantedBookForCoinsTrade(int xpValue) {
            this.xpValue = xpValue;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            List<Enchantment> enchantments = Registry.ENCHANTMENT.stream().filter(Enchantment::canVillagerTrade).collect(Collectors.toList());
            Enchantment enchantment = enchantments.get(rand.nextInt(enchantments.size()));
            int level = MathHelper.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack enchantedStack = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, level));
            int j = 2 + rand.nextInt(5 + level * 10) + 3 * level;
            if (enchantment.isTreasureEnchantment()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, j), new ItemStack(Items.BOOK), enchantedStack, 12, this.xpValue, 0.2F);
        }
    }

    static class EnchantedItemForCoinsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingStack;
        private final int coinCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EnchantedItemForCoinsTrade(Item sellItem, int coinCount, int maxUses, int xpValue) {
            this(sellItem, coinCount, maxUses, xpValue, 0.05F);
        }

        public EnchantedItemForCoinsTrade(Item sellItem, int coinCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingStack = new ItemStack(sellItem);
            this.coinCount = coinCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            int i = 5 + rand.nextInt(15);
            ItemStack enchantedStack = EnchantmentHelper.addRandomEnchantment(rand, new ItemStack(this.sellingStack.getItem()), i, false);
            int j = Math.min(this.coinCount + i, 64);
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN, j);
            return new MerchantOffer(coinStack, enchantedStack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemWithPotionForCoinsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack potionStack;
        private final int potionCount;
        private final int coinCount;
        private final int maxUses;
        private final int xpValue;
        private final Item buyingItem;
        private final int buyingItemCount;
        private final float priceMultiplier;

        public ItemWithPotionForCoinsAndItemsTrade(Item buyingItem, int buyingItemCount, Item potionItem, int potionCount, int coins, int maxUses, int xpValue) {
            this.potionStack = new ItemStack(potionItem);
            this.coinCount = coins;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.buyingItem = buyingItem;
            this.buyingItemCount = buyingItemCount;
            this.potionCount = potionCount;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN, this.coinCount);
            List<Potion> potions = Registry.POTION.stream().filter((potion) -> !potion.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potion)).collect(Collectors.toList());
            Potion potion = potions.get(rand.nextInt(potions.size()));
            ItemStack potionStack = PotionUtils.addPotionToItemStack(new ItemStack(this.potionStack.getItem(), this.potionCount), potion);
            return new MerchantOffer(coinStack, new ItemStack(this.buyingItem, this.buyingItemCount), potionStack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForCoinsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int coinCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForCoinsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForCoinsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, int coinCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this.buyingItem = new ItemStack(buyingItem);
            this.buyingItemCount = buyingItemCount;
            this.coinCount = coinCount;
            this.sellingItem = new ItemStack(sellingItem);
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, this.coinCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForCoinsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingItem;
        private final int coinCount;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForCoinsTrade(Block sellingItem, int coinCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), coinCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForCoinsTrade(Item sellingItem, int coinCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), coinCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForCoinsTrade(Item sellingItem, int coinCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), coinCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForCoinsTrade(@Nonnull ItemStack sellingItem, int coinCount, int sellingItemCount, int maxUses, int xpValue) {
            this(sellingItem, coinCount, sellingItemCount, maxUses, xpValue, 0.05F);
        }

        public ItemsForCoinsTrade(@Nonnull ItemStack sellingItem, int coinCount, int sellingItemCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingItem = sellingItem;
            this.coinCount = coinCount;
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, this.coinCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class SuspiciousStewForCoinTrade implements VillagerTrades.ITrade {
        final Effect effect;
        final int duration;
        final int xpValue;
        private final float priceMultiplier;

        public SuspiciousStewForCoinTrade(Effect effect, int duration, int xpValue) {
            this.effect = effect;
            this.duration = duration;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack stewStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffect(stewStack, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, 1), stewStack, 12, this.xpValue, this.priceMultiplier);
        }
    }
}