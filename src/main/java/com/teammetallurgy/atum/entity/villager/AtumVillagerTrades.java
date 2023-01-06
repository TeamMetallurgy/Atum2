package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumVillagerProfession;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AtumVillagerTrades {
    public static final Map<AtumVillagerProfession, Int2ObjectMap<VillagerTrades.ItemListing[]>> VILLAGER_DEFAULT_TRADES = Util.make(Maps.newHashMap(), (trades) -> {
        trades.put(AtumVillagerProfession.ALCHEMIST.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.ROTTEN_FLESH, 32, 3, 16, 2), new ItemsForCoinsTrade(Items.REDSTONE, 3, 2, 12, 1)}, 2, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(AtumItems.EFREET_HEART.get(), 15, 1, 12, 2), new CoinsForItemsTrade(Items.GOLD_INGOT, 3, 3, 12, 2), new ItemsForCoinsTrade(Items.LAPIS_LAZULI, 3, 1, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.RABBIT_FOOT, 2, 3, 12, 20), new ItemsForCoinsTrade(Items.GLOWSTONE_DUST, 12, 1, 12, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.SCUTE, 2, 3, 12, 30), new CoinsForItemsTrade(Items.GLASS_BOTTLE, 9, 3, 12, 30), new ItemsForCoinsTrade(Items.ENDER_PEARL, 15, 1, 15)}, 5, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumItems.ANPUTS_FINGERS_SPORES.get(), 22, 4, 12, 30), new ItemsForCoinsTrade(Items.EXPERIENCE_BOTTLE, 9, 1, 30)})));
        trades.put(AtumVillagerProfession.ARMORER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_LEGS_IRON.get()), 21, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_BOOTS_IRON.get()), 12, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_HELMET_IRON.get()), 15, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.DESERT_CHEST_IRON.get()), 27, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 54, 1, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_IRON.get(), 9, 12, 5, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_IRON.get(), 3, 12, 5, 0.2F)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.LAVA_BUCKET, 1, 3, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_IRON.get(), 3, 12, 10, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_IRON.get(), 12, 12, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.BRIGAND_SHIELD.get()), 15, 1, 12, 10, 0.2F)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 5, 12, 20), new EnchantedItemForCoinsTrade(AtumItems.DESERT_LEGS_DIAMOND.get(), 17, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_BOOTS_DIAMOND.get(), 11, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new EnchantedItemForCoinsTrade(AtumItems.DESERT_HELMET_DIAMOND.get(), 11, 3, 30, 0.2F), new EnchantedItemForCoinsTrade(AtumItems.DESERT_CHEST_DIAMOND.get(), 20, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.BUTCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumItems.QUAIL_RAW.get(), 14, 3, 16, 2), new CoinsForItemsTrade(Items.RABBIT, 4, 3, 16, 2), new ItemsForCoinsTrade(Items.RABBIT_STEW, 3, 1, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(AtumItems.QUAIL_COOKED.get(), 3, 8, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumItems.CAMEL_RAW.get(), 10, 3, 16, 20), new ItemsForCoinsTrade(AtumItems.CAMEL_COOKED.get(), 3, 3, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.DRIED_KELP_BLOCK, 10, 3, 12, 30)}, 5, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.SWEET_BERRIES, 10, 3, 12, 30)})));
        trades.put(AtumVillagerProfession.CARTOGRAPHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.PAPER, 3, 24, 16, 2), new CoinsForItemsTrade(Items.MAP, 1, 21, 12, 5)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumBlocks.CRYSTAL_GLASS_PANE.get(), 11, 3, 16, 10)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.COMPASS, 1, 3, 12, 20), /*//TODO Uncomment when structures are re-added new CoinsForMapTrade(13, AtumStructures.PYRAMID_STRUCTURE, MapDecoration.Type.TARGET_X, 12, 15)*/}, 4, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.ITEM_FRAME, 21, 1, 15), new ItemsForCoinsTrade(Items.WHITE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 9, 1, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 9, 1, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.GLOBE_BANNER_PATTERN, 3, 1, 30)})));
        trades.put(AtumVillagerProfession.FARMER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumItems.EMMER_EAR.get(), 20, 3, 16, 2), new CoinsForItemsTrade(AtumItems.FLAX.get(), 26, 3, 16, 2), new CoinsForItemsTrade(AtumItems.DATE.get(), 22, 3, 16, 2), new ItemsForCoinsTrade(AtumItems.EMMER_BREAD.get(), 3, 6, 16, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Blocks.PUMPKIN, 6, 3, 12, 10), new ItemsForCoinsTrade(Items.PUMPKIN_PIE, 3, 4, 5), new ItemsForCoinsTrade(AtumItems.DATE.get(), 3, 4, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.COOKIE, 9, 18, 10), new CoinsForItemsTrade(Blocks.MELON, 4, 3, 12, 20)}, 4, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Blocks.CAKE, 3, 1, 12, 15), new SuspiciousStewForCoinsTrade(MobEffects.NIGHT_VISION, 3, 100, 15), new SuspiciousStewForCoinsTrade(MobEffects.JUMP, 3, 160, 15), new SuspiciousStewForCoinsTrade(MobEffects.WEAKNESS, 3, 140, 15), new SuspiciousStewForCoinsTrade(MobEffects.BLINDNESS, 3, 120, 15), new SuspiciousStewForCoinsTrade(MobEffects.POISON, 3, 280, 15), new SuspiciousStewForCoinsTrade(MobEffects.SATURATION, 3, 7, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(AtumItems.GOLDEN_DATE.get(), 9, 3, 30), new ItemsForCoinsTrade(AtumItems.GLISTERING_DATE.get(), 12, 3, 30)})));
        trades.put(AtumVillagerProfession.FLETCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumItems.PALM_STICK.get(), 32, 3, 16, 2), new CoinsForItemsTrade(AtumItems.DEADWOOD_STICK.get(), 32, 3, 16, 2), new ItemsForCoinsTrade(Items.ARROW, 3, 16, 1), new ItemsForCoinsAndItemsTrade(AtumBlocks.LIMESTONE_GRAVEL.get(), 10, 3, Items.FLINT, 10, 12, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.FLINT, 26, 3, 12, 10), new ItemsForCoinsTrade(AtumItems.SHORT_BOW.get(), 6, 1, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.STRING, 14, 3, 16, 20), new ItemsForCoinsTrade(Items.CROSSBOW, 9, 1, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.FEATHER, 24, 3, 16, 30), new EnchantedItemForCoinsTrade(AtumItems.SHORT_BOW.get(), 5, 3, 15)}, 5, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.TRIPWIRE_HOOK, 8, 3, 12, 30), new EnchantedItemForCoinsTrade(Items.CROSSBOW, 5, 3, 15), new ItemWithPotionForCoinsAndItemsTrade(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 6, 12, 30)})));
        trades.put(AtumVillagerProfession.GLASSBLOWER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumBlocks.CRYSTAL_GLASS.get(), 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.PALM_FRAMED_CRYSTAL_GLASS.get(), 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 18, 3, 16, 2), new ItemsForCoinsTrade(AtumBlocks.SAND.get(), 3, 64, 12, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.WHITE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.GRAY_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.BLACK_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIME_DYE, 12, 3, 16, 10), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS.get(), 1, 2, 16, 5), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_CRYSTAL_GLASS_PANE.get(), 1, 3, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.YELLOW_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.ORANGE_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.RED_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.PINK_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.WHITE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new CoinsForItemsTrade(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS.get(), 2, 1, 12, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.BROWN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.PURPLE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.BLUE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.GREEN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.MAGENTA_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.CYAN_DYE, 12, 3, 16, 30), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.WHITE_STAINED_PALM_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_BLUE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.RED_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PINK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GREEN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIME_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BLACK_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.PURPLE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.MAGENTA_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.CYAN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.BROWN_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.YELLOW_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.ORANGE_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15), new ItemsForCoinsTrade(AtumBlocks.LIGHT_GRAY_STAINED_DEADWOOD_FRAMED_CRYSTAL_GLASS_PANE.get(), 3, 1, 12, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(AtumBlocks.GLASSBLOWER_FURNACE.get(), 24, 1, 12, 30)})));
        trades.put(AtumVillagerProfession.HUNTER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.LEATHER, 6, 1, 16, 2), new DyedArmorForCoinsTrade(AtumItems.WANDERER_LEGS.get(), 9), new DyedArmorForCoinsTrade(AtumItems.WANDERER_CHEST.get(), 21)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.FLINT, 26, 1, 12, 10), new DyedArmorForCoinsTrade(AtumItems.WANDERER_HELMET.get(), 15, 12, 5), new DyedArmorForCoinsTrade(AtumItems.WANDERER_BOOTS.get(), 12, 12, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.RABBIT_HIDE, 9, 1, 12, 20), new DyedArmorForCoinsTrade(AtumItems.WANDERER_CHEST.get(), 21)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.SCUTE, 4, 2, 12, 30), new DyedArmorForCoinsTrade(AtumItems.CAMEL_IRON_ARMOR.get(), 18, 12, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(new ItemStack(Items.SADDLE), 18, 1, 12, 30, 0.2F), new DyedArmorForCoinsTrade(AtumItems.WANDERER_HELMET.get(), 18, 12, 30)})));
        trades.put(AtumVillagerProfession.LIBRARIAN.get(), gatAsIntMap(ImmutableMap.<Integer, VillagerTrades.ItemListing[]>builder().put(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.PAPER, 24, 3, 16, 2), new EnchantedBookForCoinsTrade(1), new ItemsForCoinsTrade(Blocks.BOOKSHELF, 27, 1, 12, 1)}).put(2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.BOOK, 4, 3, 12, 10), new EnchantedBookForCoinsTrade(5), new ItemsForCoinsTrade(AtumBlocks.NEBU_LANTERN.get(), 10, 1, 12, 5)}).put(3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.INK_SAC, 5, 3, 12, 20), new EnchantedBookForCoinsTrade(10), new ItemsForCoinsTrade(AtumBlocks.CRYSTAL_GLASS.get(), 3, 4, 12, 10)}).put(4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.WRITABLE_BOOK, 2, 3, 12, 30), new EnchantedBookForCoinsTrade(15), new ItemsForCoinsTrade(Items.CLOCK, 15, 1, 15), new ItemsForCoinsTrade(Items.COMPASS, 12, 1, 15)}).put(5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.NAME_TAG, 36, 1, 30)}).build()));
        trades.put(AtumVillagerProfession.MASON.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.CLAY_BALL, 10, 3, 16, 2), new ItemsForCoinsTrade(Items.BRICK, 3, 10, 16, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Blocks.STONE, 20, 3, 16, 10), new ItemsForCoinsTrade(Blocks.CHISELED_STONE_BRICKS, 3, 4, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Blocks.GRANITE, 16, 3, 16, 20), new CoinsForItemsTrade(Blocks.ANDESITE, 16, 3, 16, 20), new CoinsForItemsTrade(Blocks.DIORITE, 16, 3, 16, 20), new ItemsForCoinsTrade(AtumBlocks.ALABASTER_BRICK_POLISHED.get(), 3, 4, 16, 10), new ItemsForCoinsTrade(AtumBlocks.PORPHYRY_BRICK_POLISHED.get(), 3, 4, 16, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.QUARTZ, 12, 3, 12, 30), new ItemsForCoinsTrade(Blocks.ORANGE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.ORANGE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.WHITE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLUE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GRAY_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BLACK_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.RED_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PINK_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.MAGENTA_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.LIME_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.GREEN_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.CYAN_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.PURPLE_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.YELLOW_GLAZED_TERRACOTTA, 3, 1, 12, 15), new ItemsForCoinsTrade(Blocks.BROWN_GLAZED_TERRACOTTA, 3, 1, 12, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Blocks.QUARTZ_PILLAR, 3, 1, 12, 30), new ItemsForCoinsTrade(Blocks.QUARTZ_BLOCK, 3, 1, 12, 30)})));
        trades.put(AtumVillagerProfession.TAILOR.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(AtumBlocks.LINEN_WHITE.get(), 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_BROWN.get(), 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_BLACK.get(), 18, 3, 16, 2), new CoinsForItemsTrade(AtumBlocks.LINEN_GRAY.get(), 18, 3, 16, 2), new ItemsForCoinsTrade(Items.SHEARS, 6, 1, 1)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.WHITE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.GRAY_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.BLACK_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 3, 16, 10), new CoinsForItemsTrade(Items.LIME_DYE, 12, 3, 16, 10), new ItemsForCoinsTrade(AtumBlocks.LINEN_WHITE.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_ORANGE.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_MAGENTA.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIGHT_BLUE.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_YELLOW.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIME.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_PINK.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_GRAY.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_LIGHT_GRAY.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CYAN.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_PURPLE.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BLUE.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BROWN.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_GREEN.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_RED.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_BLACK.get(), 3, 1, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_WHITE.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_ORANGE.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_MAGENTA.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIGHT_BLUE.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_YELLOW.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIME.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_PINK.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_GRAY.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_LIGHT_GRAY.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_CYAN.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_PURPLE.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BLUE.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BROWN.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_GREEN.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_RED.get(), 3, 4, 16, 5), new ItemsForCoinsTrade(AtumBlocks.LINEN_CARPET_BLACK.get(), 3, 4, 16, 5)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.YELLOW_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.ORANGE_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.RED_DYE, 12, 3, 16, 20), new CoinsForItemsTrade(Items.PINK_DYE, 12, 3, 16, 20), new ItemsForCoinsTrade(Blocks.WHITE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.YELLOW_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.RED_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLACK_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BLUE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.BROWN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.CYAN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GRAY_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.GREEN_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_BLUE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIGHT_GRAY_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.LIME_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.MAGENTA_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.ORANGE_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PINK_BED, 9, 1, 12, 10), new ItemsForCoinsTrade(Blocks.PURPLE_BED, 9, 1, 12, 10)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.BROWN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.PURPLE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.BLUE_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.GREEN_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.MAGENTA_DYE, 12, 3, 16, 30), new CoinsForItemsTrade(Items.CYAN_DYE, 12, 3, 16, 30), new ItemsForCoinsTrade(Items.WHITE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BLUE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_BLUE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.RED_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.PINK_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.GREEN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIME_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.GRAY_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BLACK_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.PURPLE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.MAGENTA_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.CYAN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.BROWN_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.YELLOW_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.ORANGE_BANNER, 9, 1, 12, 15), new ItemsForCoinsTrade(Items.LIGHT_GRAY_BANNER, 9, 1, 12, 15)}, 5, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(Items.PAINTING, 6, 3, 30)})));
        trades.put(AtumVillagerProfession.TOOLSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_AXE.get()), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_SHOVEL.get()), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_PICKAXE.get()), 3, 1, 12, 1, 0.2F), new ItemsForCoinsTrade(new ItemStack(AtumItems.LIMESTONE_HOE.get()), 3, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10), new ItemsForCoinsTrade(new ItemStack(Items.BELL), 60, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.FLINT, 30, 3, 12, 20), new EnchantedItemForCoinsTrade(Items.IRON_AXE, 2, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_SHOVEL, 3, 3, 10, 0.2F), new EnchantedItemForCoinsTrade(Items.IRON_PICKAXE, 4, 3, 10, 0.2F), new ItemsForCoinsTrade(new ItemStack(Items.DIAMOND_HOE), 12, 1, 3, 10, 0.2F)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 6, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 15, 3, 15, 0.2F), new EnchantedItemForCoinsTrade(Items.DIAMOND_SHOVEL, 8, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_PICKAXE, 16, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.WEAPONSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.COAL, 15, 3, 16, 2), new ItemsForCoinsTrade(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ItemListing[]{new EnchantedItemForCoinsTrade(Items.IRON_SWORD, 14, 3, 1), new CoinsForItemsTrade(Items.IRON_INGOT, 4, 3, 12, 10)}, 3, new VillagerTrades.ItemListing[]{new ItemsForCoinsTrade(new ItemStack(Items.BELL), 60, 1, 12, 5, 0.2F), new CoinsForItemsTrade(Items.FLINT, 24, 3, 12, 20)}, 4, new VillagerTrades.ItemListing[]{new CoinsForItemsTrade(Items.DIAMOND, 1, 6, 12, 30), new EnchantedItemForCoinsTrade(Items.DIAMOND_AXE, 15, 3, 15, 0.2F)}, 5, new VillagerTrades.ItemListing[]{new EnchantedItemForCoinsTrade(Items.DIAMOND_SWORD, 12, 3, 30, 0.2F)})));
    });

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ItemListing[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }

    static class CoinsForItemsTrade implements VillagerTrades.ItemListing {
        private final Item tradeItem;
        private final int count;
        private final int coinCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public CoinsForItemsTrade(ItemLike tradeItem, int count, int coinCount, int maxUses, int xpValue) {
            this.tradeItem = tradeItem.asItem();
            this.count = count;
            this.coinCount = coinCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
            ItemStack tradeStack = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(tradeStack, new ItemStack(AtumItems.GOLD_COIN.get(), this.coinCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class CoinsForMapTrade implements VillagerTrades.ItemListing {
        private final int count;
        private final TagKey<Structure> destination;
        private final String displayName;
        private final MapDecoration.Type mapDecorationType;
        private final int maxUses;
        private final int xpValue;

        public CoinsForMapTrade(int count, TagKey<Structure> structureName, String displayName, MapDecoration.Type mapDecorationType, int maxUses, int xpValue) {
            this.count = count;
            this.destination = structureName;
            this.displayName = displayName;
            this.mapDecorationType = mapDecorationType;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
        }

        @Nullable
        public MerchantOffer getOffer(Entity trader, @Nonnull RandomSource rand) {
            if (!(trader.level instanceof ServerLevel serverLevel)) {
                return null;
            } else {
                BlockPos pos = serverLevel.findNearestMapStructure(this.destination, trader.blockPosition(), 100, true);
                if (pos != null) {
                    ItemStack mapStack = MapItem.create(serverLevel, pos.getX(), pos.getZ(), (byte) 2, true, true);
                    MapItem.renderBiomePreviewMap(serverLevel, mapStack);
                    MapItemSavedData.addTargetDecoration(mapStack, pos, "+", this.mapDecorationType);
                    mapStack.setHoverName(Component.translatable(this.displayName));
                    return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN.get(), this.count), new ItemStack(Items.COMPASS), mapStack, this.maxUses, this.xpValue, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    static class EnchantedBookForCoinsTrade implements VillagerTrades.ItemListing {
        private final int xpValue;

        public EnchantedBookForCoinsTrade(int xpValue) {
            this.xpValue = xpValue;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, RandomSource rand) {
            List<Enchantment> enchantments = BuiltInRegistries.ENCHANTMENT.stream().filter(Enchantment::isTradeable).collect(Collectors.toList());
            Enchantment enchantment = enchantments.get(rand.nextInt(enchantments.size()));
            int level = Mth.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack enchantedStack = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, level));
            int j = 2 + rand.nextInt(5 + level * 10) + 3 * level;
            if (enchantment.isTreasureOnly()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN.get(), j), new ItemStack(Items.BOOK), enchantedStack, 12, this.xpValue, 0.2F);
        }
    }

    static class EnchantedItemForCoinsTrade implements VillagerTrades.ItemListing {
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
        public MerchantOffer getOffer(@Nonnull Entity trader, RandomSource rand) {
            int i = 5 + rand.nextInt(15);
            ItemStack enchantedStack = EnchantmentHelper.enchantItem(rand, new ItemStack(this.sellingStack.getItem()), i, false);
            int j = Math.min(this.coinCount + i, 64);
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN.get(), j);
            return new MerchantOffer(coinStack, enchantedStack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemWithPotionForCoinsAndItemsTrade implements VillagerTrades.ItemListing {
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
        public MerchantOffer getOffer(@Nonnull Entity trader, RandomSource rand) {
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN.get(), this.coinCount);
            List<Potion> potions = BuiltInRegistries.POTION.stream().filter((potion) -> !potion.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potion)).collect(Collectors.toList());
            Potion potion = potions.get(rand.nextInt(potions.size()));
            ItemStack potionStack = PotionUtils.setPotion(new ItemStack(this.potionStack.getItem(), this.potionCount), potion);
            return new MerchantOffer(coinStack, new ItemStack(this.buyingItem, this.buyingItemCount), potionStack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForCoinsAndItemsTrade implements VillagerTrades.ItemListing {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int coinCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForCoinsAndItemsTrade(ItemLike buyingItem, int buyingItemCount, int coinCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
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
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN.get(), this.coinCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForCoinsTrade implements VillagerTrades.ItemListing {
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
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN.get(), this.coinCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class SuspiciousStewForCoinsTrade implements VillagerTrades.ItemListing {
        final MobEffect effect;
        private final int cointCount;
        final int duration;
        final int xpValue;
        private final float priceMultiplier;

        public SuspiciousStewForCoinsTrade(MobEffect effect, int cointCount, int duration, int xpValue) {
            this.effect = effect;
            this.cointCount = cointCount;
            this.duration = duration;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
            ItemStack stewStack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.saveMobEffect(stewStack, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(AtumItems.GOLD_COIN.get(), this.cointCount), stewStack, 12, this.xpValue, this.priceMultiplier);
        }
    }

    static class DyedArmorForCoinsTrade implements VillagerTrades.ItemListing {
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
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull RandomSource rand) {
            ItemStack coinStack = new ItemStack(AtumItems.GOLD_COIN.get(), this.coinCount);
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
                tradeStack = DyeableLeatherItem.dyeArmor(tradeStack, dyes);
            }

            return new MerchantOffer(coinStack, tradeStack, this.maxUses, this.xpValue, 0.2F);
        }

        private static DyeItem getRandomDyeItem(RandomSource random) {
            return DyeItem.byColor(DyeColor.byId(random.nextInt(16)));
        }
    }
}