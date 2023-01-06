package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.api.AtumMats;
import com.teammetallurgy.atum.api.God;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class AtumItems {
    public static final Collection<RegistryObject<Item>> ITEMS_FOR_TAB_LIST = new ArrayList<>();
    public static final DeferredRegister<Item> ITEM_DEFERRED = DeferredRegister.create(ForgeRegistries.ITEMS, Atum.MOD_ID);
    public static final RegistryObject<Item> VILLAGER_SPAWN_EGG = registerItemWithTab(VillagerSpawnEggItem::new, "villager_spawn_egg");
    public static final RegistryObject<Item> PALM_STICK = registerItemWithTab(SimpleItem::new, "palm_stick");
    public static final RegistryObject<Item> DEADWOOD_STICK = registerItemWithTab(SimpleItem::new, "deadwood_stick");
    public static final RegistryObject<Item> DUST_BONE_STICK = registerItemWithTab(SimpleItem::new, "dusty_bone_stick");
    public static final RegistryObject<Item> KHNUMITE = registerItemWithTab(SimpleItem::new, "khnumite");
    public static final RegistryObject<Item> DIRTY_COIN = registerItemWithTab(CoinItem::new, "coin_dirty");
    public static final RegistryObject<Item> GOLD_COIN = registerItemWithTab(CoinItem::new, "coin_gold");
    public static final RegistryObject<Item> NEBU_DROP = registerItemWithTab(SimpleItem::new, "nebu_drop");
    public static final RegistryObject<Item> NEBU_INGOT = registerItemWithTab(SimpleItem::new, "nebu_ingot");
    public static final RegistryObject<Item> IDOL_RELICS = registerRelic(RelicItem.Type.IDOL);
    public static final RegistryObject<Item> IDOL_NECKLACES = registerRelic(RelicItem.Type.NECKLACE);
    public static final RegistryObject<Item> IDOL_RINGS = registerRelic(RelicItem.Type.RING);
    public static final RegistryObject<Item> IDOL_BROOCHES = registerRelic(RelicItem.Type.BROOCH);
    public static final RegistryObject<Item> IDOL_BRACELETS = registerRelic(RelicItem.Type.BRACELET);
    public static final RegistryObject<Item> EFREET_HEART = registerItemWithTab(SimpleItem::new, "efreet_heart");
    public static final RegistryObject<Item> SCARAB = registerItemWithTab(ScarabItem::new, "scarab");
    public static final RegistryObject<Item> ANPUT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ANPUT), "anput_godshard");
    public static final RegistryObject<Item> ANUBIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ANUBIS), "anubis_godshard");
    public static final RegistryObject<Item> ATEM_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ATEM), "atem_godshard");
    public static final RegistryObject<Item> GEB_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.GEB), "geb_godshard");
    public static final RegistryObject<Item> HORUS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.HORUS), "horus_godshard");
    public static final RegistryObject<Item> ISIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.ISIS), "isis_godshard");
    public static final RegistryObject<Item> MONTU_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.MONTU), "montu_godshard");
    public static final RegistryObject<Item> NEPTHYS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.NEPTHYS), "nepthys_godshard");
    public static final RegistryObject<Item> NUIT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.NUIT), "nuit_godshard");
    public static final RegistryObject<Item> OSIRIS_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.OSIRIS), "osiris_godshard");
    public static final RegistryObject<Item> PTAH_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.PTAH), "ptah_godshard");
    public static final RegistryObject<Item> RA_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.RA), "ra_godshard");
    public static final RegistryObject<Item> SETH_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.SETH), "seth_godshard");
    public static final RegistryObject<Item> SHU_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.SHU), "shu_godshard");
    public static final RegistryObject<Item> TEFNUT_GODSHARD = registerItemWithTab(() -> new GodshardItem(God.TEFNUT), "tefnut_godshard");
    public static final RegistryObject<Item> IDOL_OF_LABOR = registerItemWithTab(IdolOfLaborItem::new, "idol_of_labor");
    public static final RegistryObject<Item> SHORT_BOW = registerItemWithTab(() -> new BaseBowItem(new Item.Properties().durability(384), AtumItems.LINEN_THREAD), "short_bow");
    public static final RegistryObject<Item> LIMESTONE_SHOVEL = registerItemWithTab(() -> new ShovelItem(AtumMats.LIMESTONE, 1.2F, -3.0F, new Item.Properties()), "limestone_shovel");
    public static final RegistryObject<Item> LIMESTONE_PICKAXE = registerItemWithTab(() -> new PickaxeItem(AtumMats.LIMESTONE, 1, -2.8F, new Item.Properties()), "limestone_pickaxe");
    public static final RegistryObject<Item> LIMESTONE_AXE = registerItemWithTab(() -> new AxeItem(AtumMats.LIMESTONE, 7.0F, -3.2F, new Item.Properties()), "limestone_axe");
    public static final RegistryObject<Item> LIMESTONE_SWORD = registerItemWithTab(() -> new SwordItem(AtumMats.LIMESTONE, 3, -2.4F, new Item.Properties()), "limestone_sword");
    public static final RegistryObject<Item> LIMESTONE_HOE = registerItemWithTab(() -> new HoeItem(AtumMats.LIMESTONE, -1, -1.8F, new Item.Properties()), "limestone_hoe");
    public static final RegistryObject<Item> NEBU_HAMMER = registerItemWithTab(NebuHammerItem::new, "nebu_hammer");
    public static final RegistryObject<Item> DAGGER_IRON = registerItemWithTab(() -> new DaggerItem(Tiers.IRON), "iron_dagger");
    public static final RegistryObject<Item> POISON_DAGGER = registerItemWithTab(() -> new PoisonDaggerItem(), "dagger_poison");
    public static final RegistryObject<Item> SCIMITAR_IRON = registerItemWithTab(() -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties()), "iron_scimitar");
    public static final RegistryObject<Item> GREATSWORD_IRON = registerItemWithTab(() -> new GreatswordItem(Tiers.IRON), "iron_greatsword");
    public static final RegistryObject<Item> CLUB_IRON = registerItemWithTab(() -> new ClubItem(Tiers.IRON), "iron_club");
    public static final RegistryObject<Item> KHOPESH_IRON = registerItemWithTab(() -> new KhopeshItem(Tiers.IRON), "iron_khopesh");
    public static final RegistryObject<Item> STONEGUARD_SWORD = registerItemWithTab(() -> new SwordItem(AtumMats.KHNUMITE, 3, -2.4F, new Item.Properties()), "stoneguard_sword");
    public static final RegistryObject<Item> STONEGUARD_GREATSWORD = registerItemWithTab(() -> new GreatswordItem(AtumMats.KHNUMITE), "stoneguard_greatsword");
    public static final RegistryObject<Item> STONEGUARD_CLUB = registerItemWithTab(() -> new ClubItem(AtumMats.KHNUMITE), "stoneguard_club");
    public static final RegistryObject<Item> STONEGUARD_KHOPESH = registerItemWithTab(() -> new KhopeshItem(AtumMats.KHNUMITE), "stoneguard_khopesh");
    public static final RegistryObject<Item> STONEGUARD_SHIELD = registerItemWithTab(() -> new AtumShieldItem(90).setRepairItem(KHNUMITE.get()), "stoneguard_shield");
    public static final RegistryObject<Item> BRIGAND_SHIELD = registerItemWithTab(() -> new AtumShieldItem(150), "brigand_shield");
    public static final RegistryObject<Item> SCEPTER_ANPUT = registerScepter(God.ANPUT);
    public static final RegistryObject<Item> SCEPTER_ANUBIS = registerScepter(God.ANUBIS);
    public static final RegistryObject<Item> SCEPTER_ATEM = registerScepter(God.ATEM);
    public static final RegistryObject<Item> SCEPTER_GEB = registerScepter(God.GEB);
    public static final RegistryObject<Item> SCEPTER_HORUS = registerScepter(God.HORUS);
    public static final RegistryObject<Item> SCEPTER_ISIS = registerScepter(God.ISIS);
    public static final RegistryObject<Item> SCEPTER_MONTU = registerScepter(God.MONTU);
    public static final RegistryObject<Item> SCEPTER_NEPTHYS = registerScepter(God.NEPTHYS);
    public static final RegistryObject<Item> SCEPTER_NUIT = registerScepter(God.NUIT);
    public static final RegistryObject<Item> SCEPTER_OSIRIS = registerScepter(God.OSIRIS);
    public static final RegistryObject<Item> SCEPTER_PTAH = registerScepter(God.PTAH);
    public static final RegistryObject<Item> SCEPTER_RA = registerScepter(God.RA);
    public static final RegistryObject<Item> SCEPTER_SETH = registerScepter(God.SETH);
    public static final RegistryObject<Item> SCEPTER_SHU = registerScepter(God.SHU);
    public static final RegistryObject<Item> SCEPTER_TEFNUT = registerScepter(God.TEFNUT);
    public static final RegistryObject<Item> ANPUTS_GROUNDING = registerItemWithTab(AnputsGroundingItem::new, "anputs_grounding");
    public static final RegistryObject<Item> ANPUTS_HUNGER = registerItemWithTab(AnputsHungerItem::new, "anputs_hunger");
    public static final RegistryObject<Item> ANUBIS_WRATH = registerItemWithTab(AnubisWrathItem::new, "anubis_wrath");
    public static final RegistryObject<Item> EYES_OF_ATEM = registerItem(() -> new AtemArmor(EquipmentSlot.HEAD), "eyes_of_atem");
    public static final RegistryObject<Item> BODY_OF_ATEM = registerItemWithTab(() ->new AtemArmor(EquipmentSlot.CHEST), "body_of_atem");
    public static final RegistryObject<Item> LEGS_OF_ATEM = registerItemWithTab(() ->new AtemArmor(EquipmentSlot.LEGS), "legs_of_atem");
    public static final RegistryObject<Item> FEET_OF_ATEM = registerItemWithTab(() ->new AtemArmor(EquipmentSlot.FEET), "feet_of_atem");
    public static final RegistryObject<Item> ATEMS_BOUNTY = registerItemWithTab(AtemsBountyItem::new, "atems_bounty");
    public static final RegistryObject<Item> ATEMS_HOMECOMING = registerItemWithTab(AtemsHomecomingItem::new, "atems_homecoming");
    public static final RegistryObject<Item> ATEMS_PROTECTION = registerItemWithTab(AtemsProtectionItem::new, "atems_protection");
    public static final RegistryObject<Item> ATEMS_WILL = registerItemWithTab(AtemsWillItem::new, "atems_will");
    public static final RegistryObject<Item> GEBS_MIGHT = registerItemWithTab(GebsMightItem::new, "gebs_might");
    public static final RegistryObject<Item> GEBS_TOIL = registerItemWithTab(GebsToilItem::new, "gebs_toil");
    public static final RegistryObject<Item> GEBS_UNDOING = registerItemWithTab(GebsUndoingItem::new, "gebs_undoing");
    public static final RegistryObject<Item> HORUS_ASCENSION = registerItemWithTab(HorusAscensionItem::new, "horus_ascension");
    public static final RegistryObject<Item> HORUS_SOARING = registerItemWithTab(HorusSoaringItem::new, "horus_soaring");
    public static final RegistryObject<Item> ISIS_DIVISION = registerItemWithTab(IsisDivisionItem::new, "isis_division");
    public static final RegistryObject<Item> ISIS_HEALING = registerItemWithTab(IsisHealingItem::new, "isis_healing");
    public static final RegistryObject<Item> MONTUS_BLAST = registerItemWithTab(MontusBlastItem::new, "montus_blast");
    public static final RegistryObject<Item> MONTUS_STRIKE = registerItemWithTab(MontusStrikeItem::new, "montus_strike");
    public static final RegistryObject<Item> NEPTHYS_BANISHING = registerItemWithTab(NepthysBanishingItem::new, "nepthys_banishing");
    //public static final RegistryObject<Item> NEPTHYS_CONSECRATION = registerItemWithTab(NepthysConsecrationItem::new, "nepthys_consecration");
    public static final RegistryObject<Item> NEPTHYS_GUARD = registerItemWithTab(NepthysGuardItem::new, "nepthys_guard");
    public static final RegistryObject<Item> NUITS_IRE = registerItemWithTab(NuitsIreItem::new, "nuits_ire");
    public static final RegistryObject<Item> NUITS_QUARTER = registerItemWithTab(NuitsQuarterItem::new, "nuits_quarter");
    public static final RegistryObject<Item> NUITS_VANISHING = registerItemWithTab(NuitsVanishingItem::new, "nuits_vanishing");
    public static final RegistryObject<Item> OSIRIS_BLESSING = registerItemWithTab(OsirisBlessingItem::new, "osiris_blessing");
    public static final RegistryObject<Item> OSIRIS_MERCY = registerItemWithTab(OsirisMercyItem::new, "osiris_mercy");
    public static final RegistryObject<Item> PTAHS_DECADENCE = registerItemWithTab(PtahsDecadenceItem::new, "ptahs_decadence");
    public static final RegistryObject<Item> HALO_OF_RA = registerItemWithTab(() ->new RaArmor(EquipmentSlot.HEAD), "halo_of_ra");
    public static final RegistryObject<Item> BODY_OF_RA = registerItemWithTab(() ->new RaArmor(EquipmentSlot.CHEST), "body_of_ra");
    public static final RegistryObject<Item> LEGS_OF_RA = registerItemWithTab(() ->new RaArmor(EquipmentSlot.LEGS), "legs_of_ra");
    public static final RegistryObject<Item> FEET_OF_RA = registerItemWithTab(() -> new RaArmor(EquipmentSlot.FEET), "feet_of_ra");
    public static final RegistryObject<Item> RAS_FURY = registerItemWithTab(RasFuryItem::new, "ras_fury");
    public static final RegistryObject<Item> RAS_STEP = registerItemWithTab(RasStep::new, "ras_step");
    public static final RegistryObject<Item> SETHS_STING = registerItemWithTab(SethsStingItem::new, "seths_sting");
    public static final RegistryObject<Item> SETHS_VENOM = registerItemWithTab(SethsVenomItem::new, "seths_venom");
    public static final RegistryObject<Item> SHUS_BREATH = registerItemWithTab(ShusBreathItem::new, "shus_breath");
    public static final RegistryObject<Item> SHUS_EXILE = registerItemWithTab(ShusExileItem::new, "shus_exile");
    public static final RegistryObject<Item> SHUS_SWIFTNESS = registerItemWithTab(ShusSwiftnessItem::new, "shus_swiftness");
    public static final RegistryObject<Item> TEFNUTS_CALL = registerItemWithTab(TefnutsCallItem::new, "tefnuts_call");
    public static final RegistryObject<Item> TEFNUTS_RAIN = registerItemWithTab(TefnutsRainItem::new, "tefnuts_rain");
    public static final RegistryObject<Item> MUMMY_HELMET = registerItemWithTab(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.HEAD), "mummy_helmet");
    public static final RegistryObject<Item> MUMMY_CHEST = registerItemWithTab(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.CHEST), "mummy_chest");
    public static final RegistryObject<Item> MUMMY_LEGS = registerItemWithTab(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.LEGS), "mummy_legs");
    public static final RegistryObject<Item> MUMMY_BOOTS = registerItemWithTab(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.FEET), "mummy_boots");
    public static final RegistryObject<Item> WANDERER_HELMET = registerItemWithTab(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.HEAD), "wanderer_helmet");
    public static final RegistryObject<Item> WANDERER_CHEST = registerItemWithTab(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.CHEST), "wanderer_chest");
    public static final RegistryObject<Item> WANDERER_LEGS = registerItemWithTab(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.LEGS), "wanderer_legs");
    public static final RegistryObject<Item> WANDERER_BOOTS = registerItemWithTab(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.FEET), "wanderer_boots");
    public static final RegistryObject<Item> DESERT_HELMET_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.HEAD).setDamageModifier(10), "desert_helmet_iron");
    public static final RegistryObject<Item> DESERT_CHEST_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.CHEST).setDamageModifier(10), "desert_chest_iron");
    public static final RegistryObject<Item> DESERT_LEGS_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.LEGS).setDamageModifier(10), "desert_legs_iron");
    public static final RegistryObject<Item> DESERT_BOOTS_IRON = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.FEET).setDamageModifier(10), "desert_boots_iron");
    public static final RegistryObject<Item> DESERT_HELMET_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.HEAD).setDamageModifier(20), "desert_helmet_gold");
    public static final RegistryObject<Item> DESERT_CHEST_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.CHEST).setDamageModifier(20), "desert_chest_gold");
    public static final RegistryObject<Item> DESERT_LEGS_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.LEGS).setDamageModifier(20), "desert_legs_gold");
    public static final RegistryObject<Item> DESERT_BOOTS_GOLD = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.FEET).setDamageModifier(20), "desert_boots_gold");
    public static final RegistryObject<Item> DESERT_HELMET_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.HEAD).setDamageModifier(15), "desert_helmet_diamond");
    public static final RegistryObject<Item> DESERT_CHEST_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.CHEST).setDamageModifier(15), "desert_chest_diamond");
    public static final RegistryObject<Item> DESERT_LEGS_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.LEGS).setDamageModifier(15), "desert_legs_diamond");
    public static final RegistryObject<Item> DESERT_BOOTS_DIAMOND = registerItemWithTab(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.FEET).setDamageModifier(15), "desert_boots_diamond");
    public static final RegistryObject<Item> DESERT_WOLF_IRON_ARMOR = registerItem(NonStackableItem::new, "desert_wolf_iron_armor");
    public static final RegistryObject<Item> DESERT_WOLF_GOLD_ARMOR = registerItemWithTab(NonStackableItem::new, "desert_wolf_gold_armor");
    public static final RegistryObject<Item> DESERT_WOLF_DIAMOND_ARMOR = registerItemWithTab(NonStackableItem::new, "desert_wolf_diamond_armor");
    public static final RegistryObject<Item> CAMEL_IRON_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_iron_armor");
    public static final RegistryObject<Item> CAMEL_GOLD_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_gold_armor");
    public static final RegistryObject<Item> CAMEL_DIAMOND_ARMOR = registerItemWithTab(NonStackableItem::new, "camel_diamond_armor");
    public static final RegistryObject<Item> SCROLL = registerItemWithTab(SimpleItem::new, "scroll");
    public static final RegistryObject<Item> SCRAP = registerItemWithTab(SimpleItem::new, "cloth_scrap");
    public static final RegistryObject<Item> LINEN_BANDAGE = registerItemWithTab(LinenBandageItem::new, "linen_bandage");
    public static final RegistryObject<Item> LINEN_THREAD = registerItemWithTab(SimpleItem::new, "linen_thread");
    public static final RegistryObject<Item> LINEN_CLOTH = registerItemWithTab(SimpleItem::new, "linen_cloth");
    public static final RegistryObject<Item> PAPYRUS_PLANT = registerItemWithTab(() -> new BlockItem(AtumBlocks.PAPYRUS.get(), new Item.Properties()), "papyrus_plant");
    public static final RegistryObject<Item> FLAX_SEEDS = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.FLAX.get(), new Item.Properties()), "flax_seeds");
    public static final RegistryObject<Item> FLAX = registerItemWithTab(SimpleItem::new, "flax");
    public static final RegistryObject<Item> OPHIDIAN_TONGUE_FLOWER = registerItemWithTab(SimpleItem::new, "ophidian_tongue_flower");
    public static final RegistryObject<Item> ANPUTS_FINGERS_SPORES = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.ANPUTS_FINGERS.get(), new Item.Properties()), "anputs_fingers_spores");
    public static final RegistryObject<Item> EMMER_SEEDS = registerItemWithTab(() -> new ItemNameBlockItem(AtumBlocks.EMMER_WHEAT.get(), new Item.Properties()), "emmer_seeds");
    public static final RegistryObject<Item> EMMER_EAR = registerItemWithTab(SimpleItem::new, "emmer");
    public static final RegistryObject<Item> EMMER_FLOUR = registerItemWithTab(EmmerFlourItem::new, "emmer_flour");
    public static final RegistryObject<Item> EMMER_DOUGH = registerItemWithTab(SimpleItem::new, "emmer_dough");
    public static final RegistryObject<Item> EMMER_BREAD = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.EMMER_BREAD)), "emmer_bread");
    public static final RegistryObject<Item> QUAIL_EGG = registerItemWithTab(QuailEggItem::new, "quail_egg");
    public static final RegistryObject<Item> CAMEL_RAW = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_RAW)), "camel");
    public static final RegistryObject<Item> CAMEL_COOKED = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_COOKED)), "camel_cooked");
    public static final RegistryObject<Item> QUAIL_RAW = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_RAW)), "quail");
    public static final RegistryObject<Item> QUAIL_COOKED = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_COOKED)), "quail_cooked");
    public static final RegistryObject<Item> DATE = registerItemWithTab(() -> new Item(new Item.Properties().food(Foods.APPLE)), "date");
    public static final RegistryObject<Item> GLISTERING_DATE = registerItemWithTab(SimpleItem::new, "glistering_date");
    public static final RegistryObject<Item> GOLDEN_DATE = registerItemWithTab(() -> new Item(new Item.Properties().food(AtumFoods.GOLDEN_DATE)), "golden_date");
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_DATE = registerItemWithTab(() -> new EnchantedGoldenAppleItem(new Item.Properties().food(AtumFoods.ENCHANTED_GOLDEN_DATE)), "golden_date_enchanted");
    public static final RegistryObject<Item> ECTOPLASM = registerItemWithTab(SimpleItem::new, "ectoplasm");
    public static final RegistryObject<Item> MANDIBLES = registerItemWithTab(SimpleItem::new, "mandibles");
    public static final RegistryObject<Item> DUSTY_BONE = registerItemWithTab(SimpleItem::new, "dusty_bone");
    public static final RegistryObject<Item> WOLF_PELT = registerItemWithTab(SimpleItem::new, "wolf_pelt");
    public static final RegistryObject<Item> FERTILE_SOIL_PILE = registerItemWithTab(SimpleItem::new, "fertile_soil_pile");
    public static final RegistryObject<Item> FORSAKEN_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.FORSAKEN), "fish_forsaken");
    public static final RegistryObject<Item> MUMMIFIED_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.MUMMIFIED), "fish_mummified");
    public static final RegistryObject<Item> JEWELED_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.JEWELED), "fish_jeweled");
    public static final RegistryObject<Item> SKELETAL_FISH = registerItemWithTab(() -> new FishItem(FishItem.FishType.SKELETAL), "fish_skeletal");
    public static final RegistryObject<Item> CRUNCHY_SCARAB = registerItemWithTab(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_SCARAB)), "crunchy_scarab");
    public static final RegistryObject<Item> CRUNCHY_GOLD_SCARAB = registerItemWithTab(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_GOLD_SCARAB)), "crunchy_golden_scarab");

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
     * @return The RegistryObject<Item> that was registered
     */
    public static RegistryObject<Item> registerItem(@Nonnull Supplier<Item> item, @Nonnull String name) {
        return ITEM_DEFERRED.register(name, item);
    }

    /**
     * Registers an item & adds is to the Atum creative tab
     *
     * @param item The item to be registered
     * @param name The name to register the item with
     * @return The RegistryObject<Item> that was registered
     */
    public static RegistryObject<Item> registerItemWithTab(@Nonnull Supplier<Item> item, @Nonnull String name) {
        RegistryObject<Item> itemRegistryObject = ITEM_DEFERRED.register(name, item);
        ITEMS_FOR_TAB_LIST.add(itemRegistryObject);
        return itemRegistryObject;
    }

    /**
     * Helper method for easily registering relics
     *
     * @param type The relic type
     * @return The dirty relic item that was registered
     */
    public static RegistryObject<Item> registerRelic(@Nonnull RelicItem.Type type) {
        Item.Properties nonDirty = new Item.Properties().stacksTo(16);
        RegistryObject<Item> dirty = registerItemWithTab(() -> new RelicItem(new Item.Properties().stacksTo(64)), getRelicName(RelicItem.Quality.DIRTY, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SILVER, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.GOLD, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SAPPHIRE, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.RUBY, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.EMERALD, type));
        registerItemWithTab(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.DIAMOND, type));
        return dirty;
    }

    public static RegistryObject<Item> registerScepter(God god) {
        RegistryObject<Item> scepter = registerItem(ScepterItem::new, "scepter_" + god.getName());
        ScepterItem.SCEPTERS.put(god, scepter);
        return scepter;
    }

    private static String getRelicName(@Nonnull RelicItem.Quality quality, @Nonnull RelicItem.Type type) {
        RelicItem.RELIC_ENTRIES.add(new RelicItem.RelicEntry(quality, quality.getWeight()));
        return "relic_" + quality.getSerializedName() + "_" + type.getSerializedName();
    }
}