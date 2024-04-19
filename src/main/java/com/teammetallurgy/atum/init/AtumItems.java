package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.God;
import com.teammetallurgy.atum.api.material.AtumArmorMaterials;
import com.teammetallurgy.atum.api.material.AtumMaterialTiers;
import com.teammetallurgy.atum.items.*;
import com.teammetallurgy.atum.items.artifacts.anput.AnputsGroundingItem;
import com.teammetallurgy.atum.items.artifacts.anput.AnputsHungerItem;
import com.teammetallurgy.atum.items.artifacts.anubis.AnubisWrathItem;
import com.teammetallurgy.atum.items.artifacts.atem.*;
import com.teammetallurgy.atum.items.artifacts.geb.GebsMightItem;
import com.teammetallurgy.atum.items.artifacts.geb.GebsToilItem;
import com.teammetallurgy.atum.items.artifacts.geb.GebsUndoingItem;
import com.teammetallurgy.atum.items.artifacts.horus.HorusAscensionItem;
import com.teammetallurgy.atum.items.artifacts.horus.HorusSoaringItem;
import com.teammetallurgy.atum.items.artifacts.isis.IsisDivisionItem;
import com.teammetallurgy.atum.items.artifacts.isis.IsisHealingItem;
import com.teammetallurgy.atum.items.artifacts.montu.MontusBlastItem;
import com.teammetallurgy.atum.items.artifacts.montu.MontusStrikeItem;
import com.teammetallurgy.atum.items.artifacts.nepthys.NepthysBanishingItem;
import com.teammetallurgy.atum.items.artifacts.nepthys.NepthysGuardItem;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsIreItem;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsQuarterItem;
import com.teammetallurgy.atum.items.artifacts.nuit.NuitsVanishingItem;
import com.teammetallurgy.atum.items.artifacts.osiris.OsirisBlessingItem;
import com.teammetallurgy.atum.items.artifacts.osiris.OsirisMercyItem;
import com.teammetallurgy.atum.items.artifacts.ptah.PtahsDecadenceItem;
import com.teammetallurgy.atum.items.artifacts.ra.RaArmor;
import com.teammetallurgy.atum.items.artifacts.ra.RasFuryItem;
import com.teammetallurgy.atum.items.artifacts.ra.RasStep;
import com.teammetallurgy.atum.items.artifacts.seth.SethsStingItem;
import com.teammetallurgy.atum.items.artifacts.seth.SethsVenomItem;
import com.teammetallurgy.atum.items.artifacts.shu.ShusBreathItem;
import com.teammetallurgy.atum.items.artifacts.shu.ShusExileItem;
import com.teammetallurgy.atum.items.artifacts.shu.ShusSwiftnessItem;
import com.teammetallurgy.atum.items.artifacts.tefnut.TefnutsCallItem;
import com.teammetallurgy.atum.items.artifacts.tefnut.TefnutsRainItem;
import com.teammetallurgy.atum.items.food.CrunchyScarabItem;
import com.teammetallurgy.atum.items.food.EmmerFlourItem;
import com.teammetallurgy.atum.items.food.FishItem;
import com.teammetallurgy.atum.items.food.QuailEggItem;
import com.teammetallurgy.atum.items.tools.*;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class AtumItems {
    public static final Collection<DeferredItem<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();
    public static final DeferredRegister.Items ITEM_DEFERRED = DeferredRegister.createItems(Atum.MOD_ID);
    public static final DeferredItem<Item> VILLAGER_SPAWN_EGG = registerItemWithTab(VillagerSpawnEggItem::new, "villager_spawn_egg");
    public static final DeferredItem<Item> PALM_STICK = registerItemWithTab(SimpleItem::new, "palm_stick");
    public static final DeferredItem<Item> DEADWOOD_STICK = registerItemWithTab(SimpleItem::new, "deadwood_stick");
    public static final DeferredItem<Item> DUST_BONE_STICK = registerItemWithTab(SimpleItem::new, "dusty_bone_stick");
    public static final DeferredItem<Item> KHNUMITE = registerItemWithTab(SimpleItem::new, "khnumite");
    public static final DeferredItem<Item> DIRTY_COIN = registerItemWithTab(CoinItem::new, "coin_dirty");
    public static final DeferredItem<Item> GOLD_COIN = registerItemWithTab(CoinItem::new, "coin_gold");
    public static final DeferredItem<Item> NEBU_DROP = registerItemWithTab(SimpleItem::new, "nebu_drop");
    public static final DeferredItem<Item> NEBU_INGOT = registerItemWithTab(SimpleItem::new, "nebu_ingot");
    public static final DeferredItem<Item> IDOL_RELICS = registerRelic(RelicItem.Type.IDOL);
    public static final DeferredItem<Item> IDOL_NECKLACES = registerRelic(RelicItem.Type.NECKLACE);
    public static final DeferredItem<Item> IDOL_RINGS = registerRelic(RelicItem.Type.RING);
    public static final DeferredItem<Item> IDOL_BROOCHES = registerRelic(RelicItem.Type.BROOCH);
    public static final DeferredItem<Item> IDOL_BRACELETS = registerRelic(RelicItem.Type.BRACELET);
    public static final DeferredItem<Item> EFREET_HEART = registerItemWithTab(SimpleItem::new, "efreet_heart");
    public static final DeferredItem<Item> SCARAB = registerItemWithTab(ScarabItem::new, "scarab");
    public static final DeferredItem<Item> ANPUT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ANPUT), "anput_godshard");
    public static final DeferredItem<Item> ANUBIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ANUBIS), "anubis_godshard");
    public static final DeferredItem<Item> ATEM_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ATEM), "atem_godshard");
    public static final DeferredItem<Item> GEB_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.GEB), "geb_godshard");
    public static final DeferredItem<Item> HORUS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.HORUS), "horus_godshard");
    public static final DeferredItem<Item> ISIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ISIS), "isis_godshard");
    public static final DeferredItem<Item> MONTU_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.MONTU), "montu_godshard");
    public static final DeferredItem<Item> NEPTHYS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.NEPTHYS), "nepthys_godshard");
    public static final DeferredItem<Item> NUIT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.NUIT), "nuit_godshard");
    public static final DeferredItem<Item> OSIRIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.OSIRIS), "osiris_godshard");
    public static final DeferredItem<Item> PTAH_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.PTAH), "ptah_godshard");
    public static final DeferredItem<Item> RA_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.RA), "ra_godshard");
    public static final DeferredItem<Item> SETH_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.SETH), "seth_godshard");
    public static final DeferredItem<Item> SHU_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.SHU), "shu_godshard");
    public static final DeferredItem<Item> TEFNUT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.TEFNUT), "tefnut_godshard");
    public static final DeferredItem<Item> IDOL_OF_LABOR = registerItemWithTab(IdolOfLaborItem::new, "idol_of_labor");
    public static final DeferredItem<Item> SHORT_BOW = registerItemWithTab(() -> new BaseBowItem(new Item.Properties().durability(384), AtumItems.LINEN_THREAD), "short_bow");
    public static final DeferredItem<Item> LIMESTONE_SHOVEL = registerItemWithTab(() -> new ShovelItem(AtumMaterialTiers.LIMESTONE, 1.2F, -3.0F, new Item.Properties()), "limestone_shovel");
    public static final DeferredItem<Item> LIMESTONE_PICKAXE = registerItemWithTab(() -> new PickaxeItem(AtumMaterialTiers.LIMESTONE, 1, -2.8F, new Item.Properties()), "limestone_pickaxe");
    public static final DeferredItem<Item> LIMESTONE_AXE = registerItemWithTab(() -> new AxeItem(AtumMaterialTiers.LIMESTONE, 7.0F, -3.2F, new Item.Properties()), "limestone_axe");
    public static final DeferredItem<Item> LIMESTONE_SWORD = registerItemWithTab(() -> new SwordItem(AtumMaterialTiers.LIMESTONE, 3, -2.4F, new Item.Properties()), "limestone_sword");
    public static final DeferredItem<Item> LIMESTONE_HOE = registerItemWithTab(() -> new HoeItem(AtumMaterialTiers.LIMESTONE, -1, -1.8F, new Item.Properties()), "limestone_hoe");
    public static final DeferredItem<Item> NEBU_HAMMER = registerItemWithTab(NebuHammerItem::new, "nebu_hammer");
    public static final DeferredItem<Item> DAGGER_IRON = registerItemWithTab(() -> new DaggerItem(Tiers.IRON), "iron_dagger");
    public static final DeferredItem<Item> POISON_DAGGER = registerItemWithTab(() -> new PoisonDaggerItem(), "dagger_poison");
    public static final DeferredItem<Item> SCIMITAR_IRON = registerItemWithTab(() -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()), "iron_scimitar");
    public static final DeferredItem<Item> GREATSWORD_IRON = registerItemWithTab(() -> new GreatswordItem(Tiers.IRON), "iron_greatsword");
    public static final DeferredItem<Item> CLUB_IRON = registerItemWithTab(() -> new ClubItem(Tiers.IRON), "iron_club");
    public static final DeferredItem<Item> KHOPESH_IRON = registerItemWithTab(() -> new KhopeshItem(Tiers.IRON), "iron_khopesh");
    public static final DeferredItem<Item> STONEGUARD_SWORD = registerItemWithTab(() -> new SwordItem(AtumMaterialTiers.KHNUMITE, 3, -2.4F, new Item.Properties()), "stoneguard_sword");
    public static final DeferredItem<Item> STONEGUARD_GREATSWORD = registerItemWithTab(() -> new GreatswordItem(AtumMaterialTiers.KHNUMITE), "stoneguard_greatsword");
    public static final DeferredItem<Item> STONEGUARD_CLUB = registerItemWithTab(() -> new ClubItem(AtumMaterialTiers.KHNUMITE), "stoneguard_club");
    public static final DeferredItem<Item> STONEGUARD_KHOPESH = registerItemWithTab(() -> new KhopeshItem(AtumMaterialTiers.KHNUMITE), "stoneguard_khopesh");
    public static final DeferredItem<Item> STONEGUARD_SHIELD = registerItemWithTab(() -> new AtumShieldItem(90).setRepairItem(KHNUMITE.get()), "stoneguard_shield");
    public static final DeferredItem<Item> BRIGAND_SHIELD = registerItemWithTab(() -> new AtumShieldItem(150), "brigand_shield");
    public static final DeferredItem<Item> SCEPTER_ANPUT = registerScepter(God.ANPUT);
    public static final DeferredItem<Item> SCEPTER_ANUBIS = registerScepter(God.ANUBIS);
    public static final DeferredItem<Item> SCEPTER_ATEM = registerScepter(God.ATEM);
    public static final DeferredItem<Item> SCEPTER_GEB = registerScepter(God.GEB);
    public static final DeferredItem<Item> SCEPTER_HORUS = registerScepter(God.HORUS);
    public static final DeferredItem<Item> SCEPTER_ISIS = registerScepter(God.ISIS);
    public static final DeferredItem<Item> SCEPTER_MONTU = registerScepter(God.MONTU);
    public static final DeferredItem<Item> SCEPTER_NEPTHYS = registerScepter(God.NEPTHYS);
    public static final DeferredItem<Item> SCEPTER_NUIT = registerScepter(God.NUIT);
    public static final DeferredItem<Item> SCEPTER_OSIRIS = registerScepter(God.OSIRIS);
    public static final DeferredItem<Item> SCEPTER_PTAH = registerScepter(God.PTAH);
    public static final DeferredItem<Item> SCEPTER_RA = registerScepter(God.RA);
    public static final DeferredItem<Item> SCEPTER_SETH = registerScepter(God.SETH);
    public static final DeferredItem<Item> SCEPTER_SHU = registerScepter(God.SHU);
    public static final DeferredItem<Item> SCEPTER_TEFNUT = registerScepter(God.TEFNUT);
    public static final DeferredItem<Item> ANPUTS_GROUNDING = registerItemWithTab(AnputsGroundingItem::new, "anputs_grounding");
    public static final DeferredItem<Item> ANPUTS_HUNGER = registerItemWithTab(AnputsHungerItem::new, "anputs_hunger");
    public static final DeferredItem<Item> ANUBIS_WRATH = registerItemWithTab(AnubisWrathItem::new, "anubis_wrath");
    public static final DeferredItem<Item> EYES_OF_ATEM = registerItemWithTab(() -> new AtemArmor(ArmorItem.Type.HELMET), "eyes_of_atem");
    public static final DeferredItem<Item> BODY_OF_ATEM = registerItemWithTab(() ->new AtemArmor(ArmorItem.Type.CHESTPLATE), "body_of_atem");
    public static final DeferredItem<Item> LEGS_OF_ATEM = registerItemWithTab(() ->new AtemArmor(ArmorItem.Type.LEGGINGS), "legs_of_atem");
    public static final DeferredItem<Item> FEET_OF_ATEM = registerItemWithTab(() ->new AtemArmor(ArmorItem.Type.BOOTS), "feet_of_atem");
    public static final DeferredItem<Item> ATEMS_BOUNTY = registerItemWithTab(AtemsBountyItem::new, "atems_bounty");
    public static final DeferredItem<Item> ATEMS_HOMECOMING = registerItemWithTab(AtemsHomecomingItem::new, "atems_homecoming");
    public static final DeferredItem<Item> ATEMS_PROTECTION = registerItemWithTab(AtemsProtectionItem::new, "atems_protection");
    public static final DeferredItem<Item> ATEMS_WILL = registerItemWithTab(AtemsWillItem::new, "atems_will");
    public static final DeferredItem<Item> GEBS_MIGHT = registerItemWithTab(GebsMightItem::new, "gebs_might");
    public static final DeferredItem<Item> GEBS_TOIL = registerItemWithTab(GebsToilItem::new, "gebs_toil");
    public static final DeferredItem<Item> GEBS_UNDOING = registerItemWithTab(GebsUndoingItem::new, "gebs_undoing");
    public static final DeferredItem<Item> HORUS_ASCENSION = registerItemWithTab(HorusAscensionItem::new, "horus_ascension");
    public static final DeferredItem<Item> HORUS_SOARING = registerItemWithTab(HorusSoaringItem::new, "horus_soaring");
    public static final DeferredItem<Item> ISIS_DIVISION = registerItemWithTab(IsisDivisionItem::new, "isis_division");
    public static final DeferredItem<Item> ISIS_HEALING = registerItemWithTab(IsisHealingItem::new, "isis_healing");
    public static final DeferredItem<Item> MONTUS_BLAST = registerItemWithTab(MontusBlastItem::new, "montus_blast");
    public static final DeferredItem<Item> MONTUS_STRIKE = registerItemWithTab(MontusStrikeItem::new, "montus_strike");
    public static final DeferredItem<Item> NEPTHYS_BANISHING = registerItemWithTab(NepthysBanishingItem::new, "nepthys_banishing");
    //public static final DeferredItem<Item> NEPTHYS_CONSECRATION = registerItemWithTab(NepthysConsecrationItem::new, "nepthys_consecration");
    public static final DeferredItem<Item> NEPTHYS_GUARD = registerItemWithTab(NepthysGuardItem::new, "nepthys_guard");
    public static final DeferredItem<Item> NUITS_IRE = registerItemWithTab(NuitsIreItem::new, "nuits_ire");
    public static final DeferredItem<Item> NUITS_QUARTER = registerItemWithTab(NuitsQuarterItem::new, "nuits_quarter");
    public static final DeferredItem<Item> NUITS_VANISHING = registerItemWithTab(NuitsVanishingItem::new, "nuits_vanishing");
    public static final DeferredItem<Item> OSIRIS_BLESSING = registerItemWithTab(OsirisBlessingItem::new, "osiris_blessing");
    public static final DeferredItem<Item> OSIRIS_MERCY = registerItemWithTab(OsirisMercyItem::new, "osiris_mercy");
    public static final DeferredItem<Item> PTAHS_DECADENCE = registerItemWithTab(PtahsDecadenceItem::new, "ptahs_decadence");
    public static final DeferredItem<Item> HALO_OF_RA = registerItemWithTab(() ->new RaArmor(ArmorItem.Type.HELMET), "halo_of_ra");
    public static final DeferredItem<Item> BODY_OF_RA = registerItemWithTab(() ->new RaArmor(ArmorItem.Type.CHESTPLATE), "body_of_ra");
    public static final DeferredItem<Item> LEGS_OF_RA = registerItemWithTab(() ->new RaArmor(ArmorItem.Type.LEGGINGS), "legs_of_ra");
    public static final DeferredItem<Item> FEET_OF_RA = registerItemWithTab(() -> new RaArmor(ArmorItem.Type.BOOTS), "feet_of_ra");
    public static final DeferredItem<Item> RAS_FURY = registerItemWithTab(RasFuryItem::new, "ras_fury");
    public static final DeferredItem<Item> RAS_STEP = registerItemWithTab(RasStep::new, "ras_step");
    public static final DeferredItem<Item> SETHS_STING = registerItemWithTab(SethsStingItem::new, "seths_sting");
    public static final DeferredItem<Item> SETHS_VENOM = registerItemWithTab(SethsVenomItem::new, "seths_venom");
    public static final DeferredItem<Item> SHUS_BREATH = registerItemWithTab(ShusBreathItem::new, "shus_breath");
    public static final DeferredItem<Item> SHUS_EXILE = registerItemWithTab(ShusExileItem::new, "shus_exile");
    public static final DeferredItem<Item> SHUS_SWIFTNESS = registerItemWithTab(ShusSwiftnessItem::new, "shus_swiftness");
    public static final DeferredItem<Item> TEFNUTS_CALL = registerItemWithTab(TefnutsCallItem::new, "tefnuts_call");
    public static final DeferredItem<Item> TEFNUTS_RAIN = registerItemWithTab(TefnutsRainItem::new, "tefnuts_rain");
    public static final DeferredItem<Item> MUMMY_HELMET = registerItemWithTab(() -> new TexturedArmorItem(AtumArmorMaterials.MUMMY, "mummy_armor", ArmorItem.Type.HELMET), "mummy_helmet");
    public static final DeferredItem<Item> MUMMY_CHEST = registerItemWithTab(() -> new TexturedArmorItem(AtumArmorMaterials.MUMMY, "mummy_armor", ArmorItem.Type.CHESTPLATE), "mummy_chest");
    public static final DeferredItem<Item> MUMMY_LEGS = registerItemWithTab(() -> new TexturedArmorItem(AtumArmorMaterials.MUMMY, "mummy_armor", ArmorItem.Type.LEGGINGS), "mummy_legs");
    public static final DeferredItem<Item> MUMMY_BOOTS = registerItemWithTab(() -> new TexturedArmorItem(AtumArmorMaterials.MUMMY, "mummy_armor", ArmorItem.Type.BOOTS), "mummy_boots");
    public static final DeferredItem<Item> WANDERER_HELMET = registerItemWithTab(() -> new WandererDyeableArmor(AtumArmorMaterials.WANDERER, "wanderer_armor", ArmorItem.Type.HELMET), "wanderer_helmet");
    public static final DeferredItem<Item> WANDERER_CHEST = registerItemWithTab(() -> new WandererDyeableArmor(AtumArmorMaterials.WANDERER, "wanderer_armor", ArmorItem.Type.CHESTPLATE), "wanderer_chest");
    public static final DeferredItem<Item> WANDERER_LEGS = registerItemWithTab(() -> new WandererDyeableArmor(AtumArmorMaterials.WANDERER, "wanderer_armor", ArmorItem.Type.LEGGINGS), "wanderer_legs");
    public static final DeferredItem<Item> WANDERER_BOOTS = registerItemWithTab(() -> new WandererDyeableArmor(AtumArmorMaterials.WANDERER, "wanderer_armor", ArmorItem.Type.BOOTS), "wanderer_boots");
    public static final DeferredItem<Item> DESERT_HELMET_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", ArmorItem.Type.HELMET).setDamageModifier(10), "desert_helmet_iron");
    public static final DeferredItem<Item> DESERT_CHEST_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", ArmorItem.Type.CHESTPLATE).setDamageModifier(10), "desert_chest_iron");
    public static final DeferredItem<Item> DESERT_LEGS_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", ArmorItem.Type.LEGGINGS).setDamageModifier(10), "desert_legs_iron");
    public static final DeferredItem<Item> DESERT_BOOTS_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", ArmorItem.Type.BOOTS).setDamageModifier(10), "desert_boots_iron");
    public static final DeferredItem<Item> DESERT_HELMET_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", ArmorItem.Type.HELMET).setDamageModifier(20), "desert_helmet_gold");
    public static final DeferredItem<Item> DESERT_CHEST_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", ArmorItem.Type.CHESTPLATE).setDamageModifier(20), "desert_chest_gold");
    public static final DeferredItem<Item> DESERT_LEGS_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", ArmorItem.Type.LEGGINGS).setDamageModifier(20), "desert_legs_gold");
    public static final DeferredItem<Item> DESERT_BOOTS_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", ArmorItem.Type.BOOTS).setDamageModifier(20), "desert_boots_gold");
    public static final DeferredItem<Item> DESERT_HELMET_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", ArmorItem.Type.HELMET).setDamageModifier(15), "desert_helmet_diamond");
    public static final DeferredItem<Item> DESERT_CHEST_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", ArmorItem.Type.CHESTPLATE).setDamageModifier(15), "desert_chest_diamond");
    public static final DeferredItem<Item> DESERT_LEGS_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", ArmorItem.Type.LEGGINGS).setDamageModifier(15), "desert_legs_diamond");
    public static final DeferredItem<Item> DESERT_BOOTS_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", ArmorItem.Type.BOOTS).setDamageModifier(15), "desert_boots_diamond");
    public static final DeferredItem<Item> DESERT_WOLF_IRON_ARMOR = registerItemWithTab(NonStackableItem::new, "desert_wolf_iron_armor");
    public static final DeferredItem<Item> DESERT_WOLF_GOLD_ARMOR = registerItemWithTab(NonStackableItem::new, "desert_wolf_gold_armor");
    public static final DeferredItem<Item> DESERT_WOLF_DIAMOND_ARMOR = registerItemWithTab(NonStackableItem::new, "desert_wolf_diamond_armor");
    public static final DeferredItem<Item> CAMEL_IRON_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_iron_armor");
    public static final DeferredItem<Item> CAMEL_GOLD_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_gold_armor");
    public static final DeferredItem<Item> CAMEL_DIAMOND_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_diamond_armor");
    public static final DeferredItem<Item> SCROLL = registerItemWithTab(SimpleItem::new, "scroll");
    public static final DeferredItem<Item> SCRAP = registerItemWithTab(SimpleItem::new, "cloth_scrap");
    public static final DeferredItem<Item> LINEN_BANDAGE = registerItemWithTab(LinenBandageItem::new, "linen_bandage");
    public static final DeferredItem<Item> LINEN_THREAD = registerItemWithTab(SimpleItem::new, "linen_thread");
    public static final DeferredItem<Item> LINEN_CLOTH = registerItemWithTab(SimpleItem::new, "linen_cloth");
    public static final DeferredItem<Item> PAPYRUS_PLANT = registerItemWithTab(() -> new BlockItem(AtumBlocks.PAPYRUS.get(), new Item.Properties()), "papyrus_plant");
    public static final DeferredItem<Item> FLAX_SEEDS = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.FLAX.get(), new Item.Properties()), "flax_seeds");
    public static final DeferredItem<Item> FLAX = registerItemWithTab(SimpleItem::new, "flax");
    public static final DeferredItem<Item> OPHIDIAN_TONGUE_FLOWER = registerItemWithTab(SimpleItem::new, "ophidian_tongue_flower");
    public static final DeferredItem<Item> ANPUTS_FINGERS_SPORES = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.ANPUTS_FINGERS.get(), new Item.Properties()), "anputs_fingers_spores");
    public static final DeferredItem<Item> EMMER_SEEDS = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.EMMER_WHEAT.get(), new Item.Properties()), "emmer_seeds");
    public static final DeferredItem<Item> EMMER_EAR = registerItemWithTab(SimpleItem::new, "emmer");
    public static final DeferredItem<Item> EMMER_FLOUR = registerItemWithTab(EmmerFlourItem::new, "emmer_flour");
    public static final DeferredItem<Item> EMMER_DOUGH = registerItemWithTab(SimpleItem::new, "emmer_dough");
    public static final DeferredItem<Item> EMMER_BREAD = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.EMMER_BREAD)), "emmer_bread");
    public static final DeferredItem<Item> QUAIL_EGG = registerItemWithTab(QuailEggItem::new, "quail_egg");
    public static final DeferredItem<Item> CAMEL_RAW = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_RAW)), "camel");
    public static final DeferredItem<Item> CAMEL_COOKED = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_COOKED)), "camel_cooked");
    public static final DeferredItem<Item> QUAIL_RAW = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_RAW)), "quail");
    public static final DeferredItem<Item> QUAIL_COOKED = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_COOKED)), "quail_cooked");
    public static final DeferredItem<Item> DATE = registerItemWithTab(() -> new Item(new Item.Properties().food(Foods.APPLE)), "date");
    public static final DeferredItem<Item> GLISTERING_DATE = registerItemWithTab(SimpleItem::new, "glistering_date");
    public static final DeferredItem<Item> GOLDEN_DATE = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.GOLDEN_DATE)), "golden_date");
    public static final DeferredItem<Item> ENCHANTED_GOLDEN_DATE = registerItemWithTab(() -> new EnchantedGoldenAppleItem(new Item.Properties().food(AtumFoods.ENCHANTED_GOLDEN_DATE)), "golden_date_enchanted");
    public static final DeferredItem<Item> ECTOPLASM = registerItemWithTab(SimpleItem::new, "ectoplasm");
    public static final DeferredItem<Item> MANDIBLES = registerItemWithTab(SimpleItem::new, "mandibles");
    public static final DeferredItem<Item> DUSTY_BONE = registerItemWithTab(SimpleItem::new, "dusty_bone");
    public static final DeferredItem<Item> WOLF_PELT = registerItemWithTab(SimpleItem::new, "wolf_pelt");
    public static final DeferredItem<Item> FERTILE_SOIL_PILE = registerItemWithTab(SimpleItem::new, "fertile_soil_pile");
    public static final DeferredItem<Item> FORSAKEN_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.FORSAKEN), "fish_forsaken");
    public static final DeferredItem<Item> MUMMIFIED_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.MUMMIFIED), "fish_mummified");
    public static final DeferredItem<Item> JEWELED_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.JEWELED), "fish_jeweled");
    public static final DeferredItem<Item> SKELETAL_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.SKELETAL), "fish_skeletal");
    public static final DeferredItem<Item> CRUNCHY_SCARAB = registerItemWithTab(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_SCARAB)), "crunchy_scarab");
    public static final DeferredItem<Item> CRUNCHY_GOLD_SCARAB = registerItemWithTab(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_GOLD_SCARAB)), "crunchy_golden_scarab");

    public static void setItemInfo() {
        ComposterBlock.COMPOSTABLES.put(EMMER_SEEDS.get(), 0.3F);
        ComposterBlock.COMPOSTABLES.put(EMMER_EAR.get(), 0.65F);
        ComposterBlock.COMPOSTABLES.put(EMMER_BREAD.get(), 0.85F);
        ComposterBlock.COMPOSTABLES.put(FLAX.get(), 0.50F);
    }

    public static class AtumFoods {
        public static final FoodProperties EMMER_BREAD = new FoodProperties.Builder().nutrition(4).saturationMod(0.9F).build();
        public static final FoodProperties CAMEL_RAW = new FoodProperties.Builder().nutrition(3).saturationMod(0.2F).meat().build();
        public static final FoodProperties CAMEL_COOKED = new FoodProperties.Builder().nutrition(9).saturationMod(0.7F).meat().build();
        public static final FoodProperties QUAIL_RAW = new FoodProperties.Builder().nutrition(1).saturationMod(0.4F).meat().build();
        public static final FoodProperties QUAIL_COOKED = new FoodProperties.Builder().nutrition(5).saturationMod(0.8F).meat().build();
        public static final FoodProperties GOLDEN_DATE = new FoodProperties.Builder().nutrition(5).saturationMod(1.5F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 100, 1), 1.0F).effect(new MobEffectInstance(MobEffects.ABSORPTION, 2400, 0), 1.0F).alwaysEat().build();
        public static final FoodProperties ENCHANTED_GOLDEN_DATE = new FoodProperties.Builder().nutrition(5).saturationMod(1.5F).effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400, 1), 1.0F).effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 6000, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 6000, 0), 1.0F).effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 2400, 3), 1.0F).alwaysEat().build();
        public static final FoodProperties CRUNCHY_SCARAB = new FoodProperties.Builder().nutrition(3).saturationMod(0.1F).meat().build();
        public static final FoodProperties CRUNCHY_GOLD_SCARAB = new FoodProperties.Builder().nutrition(6).saturationMod(1.3F).meat().build();
    }

    /**
     * Registers an item
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @return The DeferredItem<Item> that was registered
     */
    public static DeferredItem<Item> registerItem(@Nonnull Supplier<Item> item, @Nonnull String name) {
        return ITEM_DEFERRED.register(name, item);
    }

    /**
     * Registers an item & adds is to the Atum creative tab
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @return The DeferredItem<Item> that was registered
     */
    public static DeferredItem<Item> registerItemWithTab(@Nonnull Supplier<Item> item, @Nonnull String name) {
        DeferredItem<Item> itemRegistryObject = ITEM_DEFERRED.register(name, item);
        ITEMS_FOR_TAB_LIST.add(itemRegistryObject);
        return itemRegistryObject;
    }

    /**
     * Helper method for easily registering relics
     *
     * @param type The relic type
     * @return The dirty relic item that was registered
     */
    public static DeferredItem<Item> registerRelic(@Nonnull RelicItem.Type type) {
        Item.Properties nonDirty = new Item.Properties().stacksTo(16);
        DeferredItem<Item> dirty = registerItemWithTab(() -> new RelicItem(new Item.Properties().stacksTo(64)), getRelicName(RelicItem.Quality.DIRTY, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SILVER, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.GOLD, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SAPPHIRE, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.RUBY, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.EMERALD, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.DIAMOND, type));
        return dirty;
    }

    public static DeferredItem<Item> registerScepter(God god) {
        DeferredItem<Item> scepter = registerItem(ScepterItem::new, "scepter_" + god.getName());
        ScepterItem.SCEPTERS.put(god, scepter);
        return scepter;
    }

    private static String getRelicName(@Nonnull RelicItem.Quality quality, @Nonnull RelicItem.Type type) {
        RelicItem.RELIC_ENTRIES.add(new RelicItem.RelicEntry(quality, quality.getWeight()));
        return "relic_" + quality.getSerializedName() + "_" + type.getSerializedName();
    }
}