package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
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
        trades.put(AtumVillagerProfession.ALCHEMIST.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.ROTTEN_FLESH, 32, 3, 16, 2), new ItemsForCoinsTrade(Items.REDSTONE, 3, 2, 12, 1)}, 2, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(AtumItems.EFREET_HEART, 15, 1, 12, 2), new CoinsForItemsTrade(Items.GOLD_INGOT, 3, 3, 12, 2), new ItemsForCoinsTrade(Items.LAPIS_LAZULI, 3, 1, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.RABBIT_FOOT, 2, 3, 12, 20), new ItemsForCoinsTrade(Items.GLOWSTONE_DUST, 12, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.SCUTE, 2, 3, 12, 30), new CoinsForItemsTrade(Items.GLASS_BOTTLE, 9, 3, 12, 30), new ItemsForCoinsTrade(Items.ENDER_PEARL, 15, 1, 15)}, 5, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.ANPUTS_FINGERS_SPORES, 22, 4, 12, 30), new ItemsForCoinsTrade(Items.EXPERIENCE_BOTTLE, 9, 1, 30)})));
        trades.put(AtumVillagerProfession.ARMORER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_LEGS_IRON), 21, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_BOOTS_IRON), 12, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_HELMET_IRON), 15, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_CHEST_IRON), 27, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 54, 1, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_IRON, 9, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_IRON, 3, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.LAVA_BUCKET, 1, 3, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_IRON, 3, 12, 10, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_IRON, 12, 12, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.BRIGAND_SHIELD), 15, 1, 12, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 5, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_DIAMOND, 17, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_DIAMOND, 11, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_DIAMOND, 11, 3, 30, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_DIAMOND, 20, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.BUTCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.QUAIL_RAW, 14, 3, 16, 2), new CoinsForItemsTrade(Items.RABBIT, 4, 3, 16, 2), new ItemsForCoinsTrade(Items.RABBIT_STEW, 3, 1, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(AtumItems.QUAIL_COOKED, 3, 8, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.CAMEL_RAW, 10, 3, 16, 20), new ItemsForCoinsTrade(AtumItems.CAMEL_COOKED, 3, 3, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DRIED_KELP_BLOCK, 10, 3, 12, 30)}, 5, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.SWEET_BERRIES, 10, 3, 12, 30)})));
        trades.put(AtumVillagerProfession.CARTOGRAPHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.PAPER, 3, 24, 16, 2), new CoinsForItemsTrade(Items.MAP, 1, 21, 12, 5)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumBlocks.CRYSTAL_GLASS_PANE, 11, 3, 16, 10)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COMPASS, 1, 3, 12, 20), new CoinsForMapTrade(13, AtumStructures.PYRAMID_STRUCTURE, MapDecoration.Type.TARGET_X, 12, 15)}, 4, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.ITEM_FRAME, 21, 1, 15), new ItemsForCoinsTrade(Items.WHITE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 9, 1, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.GLOBE_BANNER_PATTERN, 3, 1, 30)})));
        trades.put(AtumVillagerProfession.FARMER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.EMMER, 20, 3, 16, 2), new CoinsForItemsTrade(AtumItems.FLAX, 26, 3, 16, 2), new CoinsForItemsTrade(AtumItems.DATE, 22, 3, 16, 2), new ItemsForCoinsTrade(AtumItems.EMMER_BREAD, 3, 6, 16, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Blocks.PUMPKIN, 6, 3, 12, 10), new ItemsForCoinsTrade(Items.PUMPKIN_PIE, 3, 4, 5), new ItemsForCoinsTrade(AtumItems.DATE, 3, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.COOKIE, 9, 18, 10), new CoinsForItemsTrade(Blocks.MELON, 4, 3, 12, 20)}, 4, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Blocks.CAKE, 3, 1, 12, 15), new SuspiciousStewForCoinsTrade(Effects.NIGHT_VISION, 3, 100, 15), new SuspiciousStewForCoinsTrade(Effects.JUMP_BOOST, 3, 160, 15), new SuspiciousStewForCoinsTrade(Effects.WEAKNESS, 3, 140, 15), new SuspiciousStewForCoinsTrade(Effects.BLINDNESS, 3, 120, 15), new SuspiciousStewForCoinsTrade(Effects.POISON, 3, 280, 15), new SuspiciousStewForCoinsTrade(Effects.SATURATION, 3, 7, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(AtumItems.GOLDEN_DATE, 9, 3, 30), new ItemsForCoinsTrade(AtumItems.GLISTERING_DATE, 12, 3, 30)})));
        trades.put(AtumVillagerProfession.FLETCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumItems.PALM_STICK, 32, 3, 16, 2), new CoinsForItemsTrade(AtumItems.DEADWOOD_STICK, 32, 3, 16, 2), new ItemsForCoinsTrade(Items.ARROW, 3, 16, 1), new ItemsForCoinsAndItemsTrade(AtumBlocks.LIMESTONE_GRAVEL, 10, 3, Items.FLINT, 10, 12, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.FLINT, 26, 3, 12, 10), new ItemsForCoinsTrade(AtumItems.SHORT_BOW, 6, 1, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.STRING, 14, 3, 16, 20), new ItemsForCoinsTrade(Items.CROSSBOW, 9, 1, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.FEATHER, 24, 3, 16, 30), new EnchantedItemForCoinsTrade(AtumItems.SHORT_BOW, 5, 3, 15)}, 5, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.TRIPWIRE_HOOK, 8, 3, 12, 30), new EnchantedItemForCoinsTrade(Items.CROSSBOW, 5, 3, 15), new ItemWithPotionForCoinsAndItemsTrade(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 6, 12, 30)})));
        trades.put(AtumVillagerProfession.GLASSBLOWER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumBlocks.CRYSTAL_GLASS, 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS, 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS, 18, 3, 16, 2), new ItemsForCoinsTrade(AtumBlocks.SAND, 3, 64, 12, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.WHITE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.GRAY_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.BLACK_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIME_DYE, 12, 3, 16, 10), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS, 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS_PANE, 1, 3, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.YELLOW_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.ORANGE_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.RED_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.PINK_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS, 2, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.BROWN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.PURPLE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.BLUE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.GREEN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.MAGENTA_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.CYAN_DYE, 12, 3, 16, 30), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE, 3, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(AtumBlocks.GLASSBLOWER_FURNACE, 24, 1, 12, 30)})));
        trades.put(AtumVillagerProfession.HUNTER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.LEATHER, 6, 1, 16, 2), new DyedArmorForCoinsTrade(AtumItems.WANDERER_LEGS, 9), new DyedArmorForCoinsTrade(AtumItems.WANDERER_CHEST, 21)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.FLINT, 26, 1, 12, 10), new DyedArmorForCoinsTrade(AtumItems.WANDERER_HELMET, 15, 12, 5), new DyedArmorForCoinsTrade(AtumItems.WANDERER_BOOTS, 12, 12, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.RABBIT_HIDE, 9, 1, 12, 20), new DyedArmorForCoinsTrade(AtumItems.WANDERER_CHEST, 21)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.SCUTE, 4, 2, 12, 30), new DyedArmorForCoinsTrade(AtumItems.CAMEL_IRON_ARMOR, 18, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(new ItemStack(Items.SADDLE), 18, 1, 12, 30, 0.2F), new DyedArmorForCoinsTrade(AtumItems.WANDERER_HELMET, 18, 12, 30)})));
        trades.put(AtumVillagerProfession.LIBRARIAN.get(), gatAsIntMap(ImmutableMap.<Integer, VillagerTrades.ITrade[]>builder().put(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.PAPER, 24, 3, 16, 2), new EnchantedBookForCoinsTrade(1), new ItemsForCoinsTrade(Blocks.BOOKSHELF, 27, 1, 12, 1)}).put(2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.BOOK, 4, 3, 12, 10), new EnchantedBookForCoinsTrade(5), new ItemsForCoinsTrade(AtumBlocks.NEBU_LANTERN, 10, 1, 12, 5)}).put(3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.INK_SAC, 5, 3, 12, 20), new EnchantedBookForCoinsTrade(10), new ItemsForCoinsTrade(AtumBlocks.CRYSTAL_GLASS, 3, 4, 12, 10)}).put(4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.WRITABLE_BOOK, 2, 3, 12, 30), new EnchantedBookForCoinsTrade(15), new ItemsForCoinsTrade(Items.CLOCK, 15, 1, 15), new ItemsForCoinsTrade(Items.COMPASS, 12, 1, 15)}).put(5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.NAME_TAG, 36, 1, 30)}).build()));
        trades.put(AtumVillagerProfession.MASON.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.CLAY_BALL, 10, 3, 16, 2), new ItemsForCoinsTrade(Items.BRICK, 3, 10, 16, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Blocks.STONE, 20, 3, 16, 10), new ItemsForCoinsTrade(Blocks.CHISELED_STONE_BRICKS, 3, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Blocks.GRANITE, 16, 3, 16, 20), new CoinsForItemsTrade(Blocks.ANDESITE, 16, 3, 16, 20), new CoinsForItemsTrade(Blocks.DIORITE, 16, 3, 16, 20), new ItemsForCoinsTrade(AtumBlocks.ALABASTER_BRICK_POLISHED, 3, 4, 16, 10), new ItemsForCoinsTrade(AtumBlocks.PORPHYRY_BRICK_POLISHED, 3, 4, 16, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.QUARTZ, 12, 3, 12, 30), new ItemsForCoinsTrade(Blocks.ORANGE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.ORANGE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_GLAZED_TERRACOTTA, 3, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Blocks.QUARTZ_PILLAR, 3, 1, 12, 30), new ItemsForCoinsTrade(Blocks.QUARTZ_BLOCK, 3, 1, 12, 30)})));
        trades.put(AtumVillagerProfession.TAILOR.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(AtumBlocks.LINEN_WHITE, 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_BROWN, 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_BLACK, 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_GRAY, 18, 3, 16, 2), new ItemsForCoinsTrade(Items.SHEARS, 6, 1, 1)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.WHITE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.GRAY_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.BLACK_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIME_DYE, 12, 3, 16, 10), new ItemsForCoinsTrade(AtumBlocks.LINEN_WHITE, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_ORANGE, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_MAGENTA, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIGHT_BLUE, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_YELLOW, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIME, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_PINK, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_GRAY, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIGHT_GRAY, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CYAN, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_PURPLE, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BLUE, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BROWN, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_GREEN, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_RED, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BLACK, 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_WHITE, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_ORANGE, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_MAGENTA, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIGHT_BLUE, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_YELLOW, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIME, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_PINK, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_GRAY, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIGHT_GRAY, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_CYAN, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_PURPLE, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BLUE, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BROWN, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_GREEN, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_RED, 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BLACK, 3, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.YELLOW_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.ORANGE_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.RED_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.PINK_DYE, 12, 3, 16, 20), new ItemsForCoinsTrade(Blocks.WHITE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.YELLOW_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.RED_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLACK_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLUE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BROWN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.CYAN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GRAY_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GREEN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIME_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.MAGENTA_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.ORANGE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PINK_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PURPLE_BED, 9, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.BROWN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.PURPLE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.BLUE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.GREEN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.MAGENTA_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.CYAN_DYE, 12, 3, 16, 30), new ItemsForCoinsTrade(Items.WHITE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 9, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(Items.PAINTING, 6, 3, 30)})));
        trades.put(AtumVillagerProfession.TOOLSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_AXE), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_SHOVEL), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_PICKAXE), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_HOE), 3, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 60, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.FLINT, 30, 3, 12, 20), new EnchantedItemForCoinsTrade(Items.IRON_AXE, 2, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_SHOVEL, 3, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_PICKAXE, 4, 3, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.DIAMOND_HOE), 12, 1, 3, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 6, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 15, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(Items.DIAMOND_SHOVEL, 8, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_PICKAXE, 16, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.WEAPONSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(Items.IRON_SWORD, 14, 3, 1), new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10)}, 3, new VillagerTrades.ITrade[]{new ItemsForCoinsTrade(new ItemStack(Items.BELL), 60, 1, 12, 5, 0.2F), new CoinsForItemsTrade(Items.FLINT, 24, 3, 12, 20)}, 4, new VillagerTrades.ITrade[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 6, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 15, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_SWORD, 12, 3, 30, 0.2F)})));
    });

    private static Int2ObjectMap<VillagerTrades.ITrade[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
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

    static class SuspiciousStewForCoinsTrade implements VillagerTrades.ITrade {
        final Effect effect;
        private final int cointCount;
        final int duration;
        final int xpValue;
        private final float priceMultiplier;

        public SuspiciousStewForCoinsTrade(Effect effect, int cointCount, int duration, int xpValue) {
            this.effect = effect;
            this.cointCount = cointCount;
            this.duration = duration;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack stewStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffect(stewStack, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN, this.cointCount), stewStack, 12, this.xpValue, this.priceMultiplier);
        }
    }

    static class DyedArmorForCoinsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int coinCount;
        private final int maxUses;
        private final int xpValue;

        public DyedArmorForCoinsTrade(Item item, int coinCount) {
            this(item, coinCount, 12, 1);
        }

        public DyedArmorForCoinsTrade(Item tradeItem, int coinCount, int maxUses, int xpValue) {
            this.tradeItem = tradeItem;
            this.coinCount = coinCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN, this.coinCount);
            ItemStack tradeStack = new ItemStack(this.tradeItem);
            if (this.tradeItem instanceof DyeableArmorItem) {
                List<DyeItem> dyes = Lists.newArrayList();
                dyes.add(getRandomDyeItem(rand));
                if (rand.nextFloat() > 0.7F) {
                    dyes.add(getRandomDyeItem(rand));
                }

                if (rand.nextFloat() > 0.8F) {
                    dyes.add(getRandomDyeItem(rand));
                }
                tradeStack = IDyeableArmorItem.dyeItem(tradeStack, dyes);
            }

            return new MerchantOffer(coinStack, tradeStack, this.maxUses, this.xpValue, 0.2F);
        }

        private static DyeItem getRandomDyeItem(Random random) {
            return DyeItem.getItem(DyeColor.byId(random.nextInt(16)));
        }
    }
}