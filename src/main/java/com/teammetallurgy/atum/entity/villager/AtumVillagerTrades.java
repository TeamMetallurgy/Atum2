package com.teammetallurgy.atum.entity.villager;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
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
        trades.put(AtumVillagerProfession.FARMER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.WHEAT, 20, 16, 2), new EmeraldForItemsTrade(Items.POTATO, 26, 16, 2), new EmeraldForItemsTrade(Items.CARROT, 22, 16, 2), new EmeraldForItemsTrade(Items.BEETROOT, 15, 16, 2), new ItemsForEmeraldsTrade(Items.BREAD, 1, 6, 16, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Blocks.PUMPKIN, 6, 12, 10), new ItemsForEmeraldsTrade(Items.PUMPKIN_PIE, 1, 4, 5), new ItemsForEmeraldsTrade(Items.APPLE, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.COOKIE, 3, 18, 10), new EmeraldForItemsTrade(Blocks.MELON, 4, 12, 20)}, 4, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Blocks.CAKE, 1, 1, 12, 15), new SuspiciousStewForEmeraldTrade(Effects.NIGHT_VISION, 100, 15), new SuspiciousStewForEmeraldTrade(Effects.JUMP_BOOST, 160, 15), new SuspiciousStewForEmeraldTrade(Effects.WEAKNESS, 140, 15), new SuspiciousStewForEmeraldTrade(Effects.BLINDNESS, 120, 15), new SuspiciousStewForEmeraldTrade(Effects.POISON, 280, 15), new SuspiciousStewForEmeraldTrade(Effects.SATURATION, 7, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.GOLDEN_CARROT, 3, 3, 30), new ItemsForEmeraldsTrade(Items.GLISTERING_MELON_SLICE, 4, 3, 30)})));
        trades.put(AtumVillagerProfession.TAILOR.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Blocks.WHITE_WOOL, 18, 16, 2), new EmeraldForItemsTrade(Blocks.BROWN_WOOL, 18, 16, 2), new EmeraldForItemsTrade(Blocks.BLACK_WOOL, 18, 16, 2), new EmeraldForItemsTrade(Blocks.GRAY_WOOL, 18, 16, 2), new ItemsForEmeraldsTrade(Items.SHEARS, 2, 1, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.WHITE_DYE, 12, 16, 10), new EmeraldForItemsTrade(Items.GRAY_DYE, 12, 16, 10), new EmeraldForItemsTrade(Items.BLACK_DYE, 12, 16, 10), new EmeraldForItemsTrade(Items.LIGHT_BLUE_DYE, 12, 16, 10), new EmeraldForItemsTrade(Items.LIME_DYE, 12, 16, 10), new ItemsForEmeraldsTrade(Blocks.WHITE_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.ORANGE_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.MAGENTA_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIGHT_BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.YELLOW_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIME_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.PINK_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIGHT_GRAY_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.CYAN_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.PURPLE_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.BLUE_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.BROWN_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.GREEN_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.RED_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.BLACK_WOOL, 1, 1, 16, 5), new ItemsForEmeraldsTrade(Blocks.WHITE_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.ORANGE_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.MAGENTA_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIGHT_BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.YELLOW_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIME_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.PINK_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.LIGHT_GRAY_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.CYAN_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.PURPLE_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.BLUE_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.BROWN_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.GREEN_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.RED_CARPET, 1, 4, 16, 5), new ItemsForEmeraldsTrade(Blocks.BLACK_CARPET, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.YELLOW_DYE, 12, 16, 20), new EmeraldForItemsTrade(Items.LIGHT_GRAY_DYE, 12, 16, 20), new EmeraldForItemsTrade(Items.ORANGE_DYE, 12, 16, 20), new EmeraldForItemsTrade(Items.RED_DYE, 12, 16, 20), new EmeraldForItemsTrade(Items.PINK_DYE, 12, 16, 20), new ItemsForEmeraldsTrade(Blocks.WHITE_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.YELLOW_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.RED_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.BLACK_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.BLUE_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.BROWN_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.CYAN_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.GRAY_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.GREEN_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.LIGHT_BLUE_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.LIGHT_GRAY_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.LIME_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.MAGENTA_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.ORANGE_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.PINK_BED, 3, 1, 12, 10), new ItemsForEmeraldsTrade(Blocks.PURPLE_BED, 3, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.BROWN_DYE, 12, 16, 30), new EmeraldForItemsTrade(Items.PURPLE_DYE, 12, 16, 30), new EmeraldForItemsTrade(Items.BLUE_DYE, 12, 16, 30), new EmeraldForItemsTrade(Items.GREEN_DYE, 12, 16, 30), new EmeraldForItemsTrade(Items.MAGENTA_DYE, 12, 16, 30), new EmeraldForItemsTrade(Items.CYAN_DYE, 12, 16, 30), new ItemsForEmeraldsTrade(Items.WHITE_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.LIGHT_BLUE_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.RED_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.PINK_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.GREEN_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.LIME_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.GRAY_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.BLACK_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.PURPLE_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.MAGENTA_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.CYAN_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.BROWN_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.YELLOW_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.ORANGE_BANNER, 3, 1, 12, 15), new ItemsForEmeraldsTrade(Items.LIGHT_GRAY_BANNER, 3, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.PAINTING, 2, 3, 30)})));
        trades.put(AtumVillagerProfession.FLETCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.STICK, 32, 16, 2), new ItemsForEmeraldsTrade(Items.ARROW, 1, 16, 1), new ItemsForEmeraldsAndItemsTrade(Blocks.GRAVEL, 10, Items.FLINT, 10, 12, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.FLINT, 26, 12, 10), new ItemsForEmeraldsTrade(Items.BOW, 2, 1, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.STRING, 14, 16, 20), new ItemsForEmeraldsTrade(Items.CROSSBOW, 3, 1, 10)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.FEATHER, 24, 16, 30), new EnchantedItemForEmeraldsTrade(Items.BOW, 2, 3, 15)}, 5, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.TRIPWIRE_HOOK, 8, 12, 30), new EnchantedItemForEmeraldsTrade(Items.CROSSBOW, 3, 3, 15), new ItemWithPotionForEmeraldsAndItemsTrade(Items.ARROW, 5, Items.TIPPED_ARROW, 5, 2, 12, 30)})));
        trades.put(AtumVillagerProfession.LIBRARIAN.get(), gatAsIntMap(ImmutableMap.<Integer, VillagerTrades.ITrade[]>builder().put(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.PAPER, 24, 16, 2), new EnchantedBookForEmeraldsTrade(1), new ItemsForEmeraldsTrade(Blocks.BOOKSHELF, 9, 1, 12, 1)}).put(2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.BOOK, 4, 12, 10), new EnchantedBookForEmeraldsTrade(5), new ItemsForEmeraldsTrade(Items.LANTERN, 1, 1, 5)}).put(3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.INK_SAC, 5, 12, 20), new EnchantedBookForEmeraldsTrade(10), new ItemsForEmeraldsTrade(Items.GLASS, 1, 4, 10)}).put(4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.WRITABLE_BOOK, 2, 12, 30), new EnchantedBookForEmeraldsTrade(15), new ItemsForEmeraldsTrade(Items.CLOCK, 5, 1, 15), new ItemsForEmeraldsTrade(Items.COMPASS, 4, 1, 15)}).put(5, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.NAME_TAG, 20, 1, 30)}).build()));
        trades.put(AtumVillagerProfession.CARTOGRAPHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.PAPER, 24, 16, 2), new ItemsForEmeraldsTrade(Items.MAP, 7, 1, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.GLASS_PANE, 11, 16, 10), new EmeraldForMapTrade(13, Structure.MONUMENT, MapDecoration.Type.MONUMENT, 12, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.COMPASS, 1, 12, 20), new EmeraldForMapTrade(14, Structure.WOODLAND_MANSION, MapDecoration.Type.MANSION, 12, 10)}, 4, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.ITEM_FRAME, 7, 1, 15), new ItemsForEmeraldsTrade(Items.WHITE_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.BLUE_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.LIGHT_BLUE_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.RED_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.PINK_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.GREEN_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.LIME_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.GRAY_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.BLACK_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.PURPLE_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.MAGENTA_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.CYAN_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.BROWN_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.YELLOW_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.ORANGE_BANNER, 3, 1, 15), new ItemsForEmeraldsTrade(Items.LIGHT_GRAY_BANNER, 3, 1, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Items.GLOBE_BANNER_PATTERN, 8, 1, 30)})));
        trades.put(AtumVillagerProfession.CLERIC.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.ROTTEN_FLESH, 32, 16, 2), new ItemsForEmeraldsTrade(Items.REDSTONE, 1, 2, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.GOLD_INGOT, 3, 12, 10), new ItemsForEmeraldsTrade(Items.LAPIS_LAZULI, 1, 1, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.RABBIT_FOOT, 2, 12, 20), new ItemsForEmeraldsTrade(Blocks.GLOWSTONE, 4, 1, 12, 10)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.SCUTE, 4, 12, 30), new EmeraldForItemsTrade(Items.GLASS_BOTTLE, 9, 12, 30), new ItemsForEmeraldsTrade(Items.ENDER_PEARL, 5, 1, 15)}, 5, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.NETHER_WART, 22, 12, 30), new ItemsForEmeraldsTrade(Items.EXPERIENCE_BOTTLE, 3, 1, 30)})));
        trades.put(AtumVillagerProfession.ARMORER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForEmeraldsTrade(new ItemStack(Items.IRON_LEGGINGS), 7, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.IRON_BOOTS), 4, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.IRON_HELMET), 5, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.IRON_CHESTPLATE), 9, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeraldsTrade(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.CHAINMAIL_BOOTS), 1, 1, 12, 5, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.CHAINMAIL_LEGGINGS), 3, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.LAVA_BUCKET, 1, 12, 20), new EmeraldForItemsTrade(Items.DIAMOND, 1, 12, 20), new ItemsForEmeraldsTrade(new ItemStack(Items.CHAINMAIL_HELMET), 1, 1, 12, 10, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.CHAINMAIL_CHESTPLATE), 4, 1, 12, 10, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.SHIELD), 5, 1, 12, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new EnchantedItemForEmeraldsTrade(Items.DIAMOND_LEGGINGS, 14, 3, 15, 0.2F), new EnchantedItemForEmeraldsTrade(Items.DIAMOND_BOOTS, 8, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForEmeraldsTrade(Items.DIAMOND_HELMET, 8, 3, 30, 0.2F), new EnchantedItemForEmeraldsTrade(Items.DIAMOND_CHESTPLATE, 16, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.WEAPONSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForEmeraldsTrade(new ItemStack(Items.IRON_AXE), 3, 1, 12, 1, 0.2F), new EnchantedItemForEmeraldsTrade(Items.IRON_SWORD, 2, 3, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeraldsTrade(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.FLINT, 24, 12, 20)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeraldsTrade(Items.DIAMOND_AXE, 12, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForEmeraldsTrade(Items.DIAMOND_SWORD, 8, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.TOOLSMITH.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForEmeraldsTrade(new ItemStack(Items.STONE_AXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.STONE_SHOVEL), 1, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.STONE_PICKAXE), 1, 1, 12, 1, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.STONE_HOE), 1, 1, 12, 1, 0.2F)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.IRON_INGOT, 4, 12, 10), new ItemsForEmeraldsTrade(new ItemStack(Items.BELL), 36, 1, 12, 5, 0.2F)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.FLINT, 30, 12, 20), new EnchantedItemForEmeraldsTrade(Items.IRON_AXE, 1, 3, 10, 0.2F), new EnchantedItemForEmeraldsTrade(Items.IRON_SHOVEL, 2, 3, 10, 0.2F), new EnchantedItemForEmeraldsTrade(Items.IRON_PICKAXE, 3, 3, 10, 0.2F), new ItemsForEmeraldsTrade(new ItemStack(Items.DIAMOND_HOE), 4, 1, 3, 10, 0.2F)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.DIAMOND, 1, 12, 30), new EnchantedItemForEmeraldsTrade(Items.DIAMOND_AXE, 12, 3, 15, 0.2F), new EnchantedItemForEmeraldsTrade(Items.DIAMOND_SHOVEL, 5, 3, 15, 0.2F)}, 5, new VillagerTrades.ITrade[]{new EnchantedItemForEmeraldsTrade(Items.DIAMOND_PICKAXE, 13, 3, 30, 0.2F)})));
        trades.put(AtumVillagerProfession.BUTCHER.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.CHICKEN, 14, 16, 2), new EmeraldForItemsTrade(Items.PORKCHOP, 7, 16, 2), new EmeraldForItemsTrade(Items.RABBIT, 4, 16, 2), new ItemsForEmeraldsTrade(Items.RABBIT_STEW, 1, 1, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.COAL, 15, 16, 2), new ItemsForEmeraldsTrade(Items.COOKED_PORKCHOP, 1, 5, 16, 5), new ItemsForEmeraldsTrade(Items.COOKED_CHICKEN, 1, 8, 16, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.MUTTON, 7, 16, 20), new EmeraldForItemsTrade(Items.BEEF, 10, 16, 20)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.DRIED_KELP_BLOCK, 10, 12, 30)}, 5, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.SWEET_BERRIES, 10, 12, 30)})));
        trades.put(AtumVillagerProfession.MASON.get(), gatAsIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.CLAY_BALL, 10, 16, 2), new ItemsForEmeraldsTrade(Items.BRICK, 1, 10, 16, 1)}, 2, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Blocks.STONE, 20, 16, 10), new ItemsForEmeraldsTrade(Blocks.CHISELED_STONE_BRICKS, 1, 4, 16, 5)}, 3, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Blocks.GRANITE, 16, 16, 20), new EmeraldForItemsTrade(Blocks.ANDESITE, 16, 16, 20), new EmeraldForItemsTrade(Blocks.DIORITE, 16, 16, 20), new ItemsForEmeraldsTrade(Blocks.POLISHED_ANDESITE, 1, 4, 16, 10), new ItemsForEmeraldsTrade(Blocks.POLISHED_DIORITE, 1, 4, 16, 10), new ItemsForEmeraldsTrade(Blocks.POLISHED_GRANITE, 1, 4, 16, 10)}, 4, new VillagerTrades.ITrade[]{new EmeraldForItemsTrade(Items.QUARTZ, 12, 12, 30), new ItemsForEmeraldsTrade(Blocks.ORANGE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.WHITE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIGHT_BLUE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIGHT_GRAY_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BLACK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.RED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.PINK_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.MAGENTA_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIME_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.GREEN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.CYAN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.PURPLE_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.YELLOW_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BROWN_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.ORANGE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.WHITE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BLACK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.RED_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.PINK_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.MAGENTA_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.LIME_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.GREEN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.CYAN_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.PURPLE_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.YELLOW_GLAZED_TERRACOTTA, 1, 1, 12, 15), new ItemsForEmeraldsTrade(Blocks.BROWN_GLAZED_TERRACOTTA, 1, 1, 12, 15)}, 5, new VillagerTrades.ITrade[]{new ItemsForEmeraldsTrade(Blocks.QUARTZ_PILLAR, 1, 1, 12, 30), new ItemsForEmeraldsTrade(Blocks.QUARTZ_BLOCK, 1, 1, 12, 30)})));
    });

    private static Int2ObjectMap<VillagerTrades.ITrade[]> gatAsIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> trades) {
        return new Int2ObjectOpenHashMap<>(trades);
    }

    static class EmeraldForItemsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EmeraldForItemsTrade(IItemProvider tradeItem, int count, int maxUses, int xpValue) {
            this.tradeItem = tradeItem.asItem();
            this.count = count;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack tradeStack = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(tradeStack, new ItemStack(Items.EMERALD), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class EmeraldForMapTrade implements VillagerTrades.ITrade {
        private final int count;
        private final Structure<?> structureName;
        private final MapDecoration.Type mapDecorationType;
        private final int maxUses;
        private final int xpValue;

        public EmeraldForMapTrade(int count, Structure<?> structureName, MapDecoration.Type mapDecorationType, int maxUses, int xpValue) {
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
                ServerWorld serverworld = (ServerWorld) trader.world;
                BlockPos blockpos = serverworld.func_241117_a_(this.structureName, trader.getPosition(), 100, true);
                if (blockpos != null) {
                    ItemStack itemstack = FilledMapItem.setupNewMap(serverworld, blockpos.getX(), blockpos.getZ(), (byte) 2, true, true);
                    FilledMapItem.func_226642_a_(serverworld, itemstack);
                    MapData.addTargetDecoration(itemstack, blockpos, "+", this.mapDecorationType);
                    itemstack.setDisplayName(new TranslationTextComponent("filled_map." + this.structureName.getStructureName().toLowerCase(Locale.ROOT)));
                    return new MerchantOffer(new ItemStack(Items.EMERALD, this.count), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.xpValue, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    static class EnchantedBookForEmeraldsTrade implements VillagerTrades.ITrade {
        private final int xpValue;

        public EnchantedBookForEmeraldsTrade(int xpValueIn) {
            this.xpValue = xpValueIn;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            List<Enchantment> list = Registry.ENCHANTMENT.stream().filter(Enchantment::canVillagerTrade).collect(Collectors.toList());
            Enchantment enchantment = list.get(rand.nextInt(list.size()));
            int i = MathHelper.nextInt(rand, enchantment.getMinLevel(), enchantment.getMaxLevel());
            ItemStack itemstack = EnchantedBookItem.getEnchantedItemStack(new EnchantmentData(enchantment, i));
            int j = 2 + rand.nextInt(5 + i * 10) + 3 * i;
            if (enchantment.isTreasureEnchantment()) {
                j *= 2;
            }

            if (j > 64) {
                j = 64;
            }
            return new MerchantOffer(new ItemStack(Items.EMERALD, j), new ItemStack(Items.BOOK), itemstack, 12, this.xpValue, 0.2F);
        }
    }

    static class EnchantedItemForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingStack;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EnchantedItemForEmeraldsTrade(Item sellItem, int emeraldCount, int maxUses, int xpValue) {
            this(sellItem, emeraldCount, maxUses, xpValue, 0.05F);
        }

        public EnchantedItemForEmeraldsTrade(Item sellItem, int emeraldCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingStack = new ItemStack(sellItem);
            this.emeraldCount = emeraldCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            int i = 5 + rand.nextInt(15);
            ItemStack enchantedStack = EnchantmentHelper.addRandomEnchantment(rand, new ItemStack(this.sellingStack.getItem()), i, false);
            int j = Math.min(this.emeraldCount + i, 64);
            ItemStack emerald = new ItemStack(Items.EMERALD, j);
            return new MerchantOffer(emerald, enchantedStack, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemWithPotionForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack potionStack;
        private final int potionCount;
        private final int emeraldCount;
        private final int maxUses;
        private final int xpValue;
        private final Item buyingItem;
        private final int buyingItemCount;
        private final float priceMultiplier;

        public ItemWithPotionForEmeraldsAndItemsTrade(Item buyingItem, int buyingItemCount, Item potionItem, int potionCount, int emeralds, int maxUses, int xpValue) {
            this.potionStack = new ItemStack(potionItem);
            this.emeraldCount = emeralds;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.buyingItem = buyingItem;
            this.buyingItemCount = buyingItemCount;
            this.potionCount = potionCount;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, Random rand) {
            ItemStack itemstack = new ItemStack(Items.EMERALD, this.emeraldCount);
            List<Potion> list = Registry.POTION.stream().filter((potion) -> !potion.getEffects().isEmpty() && PotionBrewing.isBrewablePotion(potion)).collect(Collectors.toList());
            Potion potion = list.get(rand.nextInt(list.size()));
            ItemStack itemstack1 = PotionUtils.addPotionToItemStack(new ItemStack(this.potionStack.getItem(), this.potionCount), potion);
            return new MerchantOffer(itemstack, new ItemStack(this.buyingItem, this.buyingItemCount), itemstack1, this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int emeraldCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this(buyingItem, buyingItemCount, 1, sellingItem, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsAndItemsTrade(IItemProvider buyingItem, int buyingItemCount, int emeraldCount, Item sellingItem, int sellingItemCount, int maxUses, int xpValue) {
            this.buyingItem = new ItemStack(buyingItem);
            this.buyingItemCount = buyingItemCount;
            this.emeraldCount = emeraldCount;
            this.sellingItem = new ItemStack(sellingItem);
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class ItemsForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingItem;
        private final int emeraldCount;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(@Nonnull ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(sellingItem, emeraldCount, sellingItemCount, maxUses, xpValue, 0.05F);
        }

        public ItemsForEmeraldsTrade(@Nonnull ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingItem = sellingItem;
            this.emeraldCount = emeraldCount;
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    static class SuspiciousStewForEmeraldTrade implements VillagerTrades.ITrade {
        final Effect effect;
        final int duration;
        final int xpValue;
        private final float priceMultiplier;

        public SuspiciousStewForEmeraldTrade(Effect effect, int duration, int xpValue) {
            this.effect = effect;
            this.duration = duration;
            this.xpValue = xpValue;
            this.priceMultiplier = 0.05F;
        }

        @Override
        public MerchantOffer getOffer(@Nonnull Entity trader, @Nonnull Random rand) {
            ItemStack itemstack = new ItemStack(Items.SUSPICIOUS_STEW, 1);
            SuspiciousStewItem.addEffect(itemstack, this.effect, this.duration);
            return new MerchantOffer(new ItemStack(Items.EMERALD, 1), itemstack, 12, this.xpValue, this.priceMultiplier);
        }
    }
}