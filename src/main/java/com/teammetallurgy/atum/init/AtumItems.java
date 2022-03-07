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
import java.util.function.Supplier;

public class AtumItems {
    public static final DeferredRegister<Item> ITEM_DEFERRED = DeferredRegister.create(ForgeRegistries.ITEMS, Atum.MOD_ID);
    public static final RegistryObject<Item> VILLAGER_SPAWN_EGG = registerItem(VillagerSpawnEggItem::new, "villager_spawn_egg");
    public static final RegistryObject<Item> PALM_STICK = registerItem(SimpleItem::new, "palm_stick");
    public static final RegistryObject<Item> DEADWOOD_STICK = registerItem(SimpleItem::new, "deadwood_stick");
    public static final RegistryObject<Item> DUST_BONE_STICK = registerItem(SimpleItem::new, "dusty_bone_stick");
    public static final RegistryObject<Item> KHNUMITE = registerItem(SimpleItem::new, "khnumite");
    public static final RegistryObject<Item> DIRTY_COIN = registerItem(CoinItem::new, "coin_dirty");
    public static final RegistryObject<Item> GOLD_COIN = registerItem(CoinItem::new, "coin_gold");
    public static final RegistryObject<Item> NEBU_DROP = registerItem(SimpleItem::new, "nebu_drop");
    public static final RegistryObject<Item> NEBU_INGOT = registerItem(SimpleItem::new, "nebu_ingot");
    public static final RegistryObject<Item> IDOL_RELICS = registerRelic(RelicItem.Type.IDOL);
    public static final RegistryObject<Item> IDOL_NECKLACES = registerRelic(RelicItem.Type.NECKLACE);
    public static final RegistryObject<Item> IDOL_RINGS = registerRelic(RelicItem.Type.RING);
    public static final RegistryObject<Item> IDOL_BROOCHES = registerRelic(RelicItem.Type.BROOCH);
    public static final RegistryObject<Item> IDOL_BRACELETS = registerRelic(RelicItem.Type.BRACELET);
    public static final RegistryObject<Item> EFREET_HEART = registerItem(SimpleItem::new, "efreet_heart");
    public static final RegistryObject<Item> SCARAB = registerItem(ScarabItem::new, "scarab");
    public static final RegistryObject<Item> ANPUT_GODSHARD = registerItem(() -> new GodshardItem(God.ANPUT), "anput_godshard");
    public static final RegistryObject<Item> ANUBIS_GODSHARD = registerItem(() -> new GodshardItem(God.ANUBIS), "anubis_godshard");
    public static final RegistryObject<Item> ATEM_GODSHARD = registerItem(() -> new GodshardItem(God.ATEM), "atem_godshard");
    public static final RegistryObject<Item> GEB_GODSHARD = registerItem(() -> new GodshardItem(God.GEB), "geb_godshard");
    public static final RegistryObject<Item> HORUS_GODSHARD = registerItem(() -> new GodshardItem(God.HORUS), "horus_godshard");
    public static final RegistryObject<Item> ISIS_GODSHARD = registerItem(() -> new GodshardItem(God.ISIS), "isis_godshard");
    public static final RegistryObject<Item> MONTU_GODSHARD = registerItem(() -> new GodshardItem(God.MONTU), "montu_godshard");
    public static final RegistryObject<Item> NEPTHYS_GODSHARD = registerItem(() -> new GodshardItem(God.NEPTHYS), "nepthys_godshard");
    public static final RegistryObject<Item> NUIT_GODSHARD = registerItem(() -> new GodshardItem(God.NUIT), "nuit_godshard");
    public static final RegistryObject<Item> OSIRIS_GODSHARD = registerItem(() -> new GodshardItem(God.OSIRIS), "osiris_godshard");
    public static final RegistryObject<Item> PTAH_GODSHARD = registerItem(() -> new GodshardItem(God.PTAH), "ptah_godshard");
    public static final RegistryObject<Item> RA_GODSHARD = registerItem(() -> new GodshardItem(God.RA), "ra_godshard");
    public static final RegistryObject<Item> SETH_GODSHARD = registerItem(() -> new GodshardItem(God.SETH), "seth_godshard");
    public static final RegistryObject<Item> SHU_GODSHARD = registerItem(() -> new GodshardItem(God.SHU), "shu_godshard");
    public static final RegistryObject<Item> TEFNUT_GODSHARD = registerItem(() -> new GodshardItem(God.TEFNUT), "tefnut_godshard");
    public static final RegistryObject<Item> IDOL_OF_LABOR = registerItem(IdolOfLaborItem::new, "idol_of_labor");
    public static final RegistryObject<Item> SHORT_BOW = registerItem(() -> new BaseBowItem(new Item.Properties().durability(384), AtumItems.LINEN_THREAD), "short_bow");
    public static final RegistryObject<Item> LIMESTONE_SHOVEL = registerItem(() -> new ShovelItem(AtumMats.LIMESTONE, 1.2F, -3.0F, new Item.Properties().tab(Atum.GROUP)), "limestone_shovel");
    public static final RegistryObject<Item> LIMESTONE_PICKAXE = registerItem(() -> new PickaxeItem(AtumMats.LIMESTONE, 1, -2.8F, new Item.Properties().tab(Atum.GROUP)), "limestone_pickaxe");
    public static final RegistryObject<Item> LIMESTONE_AXE = registerItem(() -> new AxeItem(AtumMats.LIMESTONE, 7.0F, -3.2F, new Item.Properties().tab(Atum.GROUP)), "limestone_axe");
    public static final RegistryObject<Item> LIMESTONE_SWORD = registerItem(() -> new SwordItem(AtumMats.LIMESTONE, 3, -2.4F, new Item.Properties().tab(Atum.GROUP)), "limestone_sword");
    public static final RegistryObject<Item> LIMESTONE_HOE = registerItem(() -> new HoeItem(AtumMats.LIMESTONE, -1, -1.8F, new Item.Properties().tab(Atum.GROUP)), "limestone_hoe");
    public static final RegistryObject<Item> NEBU_HAMMER = registerItem(NebuHammerItem::new, "nebu_hammer");
    public static final RegistryObject<Item> DAGGER_IRON = registerItem(() -> new DaggerItem(Tiers.IRON), "iron_dagger");
    public static final RegistryObject<Item> POISON_DAGGER = registerItem(() -> new PoisonDaggerItem(), "dagger_poison");
    public static final RegistryObject<Item> SCIMITAR_IRON = registerItem(() -> new SwordItem(Tiers.IRON, 3, -2.4F, new Item.Properties().tab(Atum.GROUP)), "iron_scimitar");
    public static final RegistryObject<Item> GREATSWORD_IRON = registerItem(() -> new GreatswordItem(Tiers.IRON), "iron_greatsword");
    public static final RegistryObject<Item> CLUB_IRON = registerItem(() -> new ClubItem(Tiers.IRON), "iron_club");
    public static final RegistryObject<Item> KHOPESH_IRON = registerItem(() -> new KhopeshItem(Tiers.IRON), "iron_khopesh");
    public static final RegistryObject<Item> STONEGUARD_SWORD = registerItem(() -> new SwordItem(AtumMats.KHNUMITE, 3, -2.4F, new Item.Properties().tab(Atum.GROUP)), "stoneguard_sword");
    public static final RegistryObject<Item> STONEGUARD_GREATSWORD = registerItem(() -> new GreatswordItem(AtumMats.KHNUMITE), "stoneguard_greatsword");
    public static final RegistryObject<Item> STONEGUARD_CLUB = registerItem(() -> new ClubItem(AtumMats.KHNUMITE), "stoneguard_club");
    public static final RegistryObject<Item> STONEGUARD_KHOPESH = registerItem(() -> new KhopeshItem(AtumMats.KHNUMITE), "stoneguard_khopesh");
    public static final RegistryObject<Item> STONEGUARD_SHIELD = registerItem(() -> new AtumShieldItem(90).setRepairItem(KHNUMITE.get()), "stoneguard_shield");
    public static final RegistryObject<Item> BRIGAND_SHIELD = registerItem(() -> new AtumShieldItem(150), "brigand_shield");
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
    public static final RegistryObject<Item> ANPUTS_GROUNDING = registerItem(AnputsGroundingItem::new, "anputs_grounding");
    public static final RegistryObject<Item> ANPUTS_HUNGER = registerItem(AnputsHungerItem::new, "anputs_hunger");
    public static final RegistryObject<Item> ANUBIS_WRATH = registerItem(AnubisWrathItem::new, "anubis_wrath");
    public static final RegistryObject<Item> EYES_OF_ATEM = registerItem(() -> new AtemArmor(EquipmentSlot.HEAD), "eyes_of_atem");
    public static final RegistryObject<Item> BODY_OF_ATEM = registerItem(() ->new AtemArmor(EquipmentSlot.CHEST), "body_of_atem");
    public static final RegistryObject<Item> LEGS_OF_ATEM = registerItem(() ->new AtemArmor(EquipmentSlot.LEGS), "legs_of_atem");
    public static final RegistryObject<Item> FEET_OF_ATEM = registerItem(() ->new AtemArmor(EquipmentSlot.FEET), "feet_of_atem");
    public static final RegistryObject<Item> ATEMS_BOUNTY = registerItem(AtemsBountyItem::new, "atems_bounty");
    public static final RegistryObject<Item> ATEMS_HOMECOMING = registerItem(AtemsHomecomingItem::new, "atems_homecoming");
    public static final RegistryObject<Item> ATEMS_PROTECTION = registerItem(AtemsProtectionItem::new, "atems_protection");
    public static final RegistryObject<Item> ATEMS_WILL = registerItem(AtemsWillItem::new, "atems_will");
    public static final RegistryObject<Item> GEBS_MIGHT = registerItem(GebsMightItem::new, "gebs_might");
    public static final RegistryObject<Item> GEBS_TOIL = registerItem(GebsToilItem::new, "gebs_toil");
    public static final RegistryObject<Item> GEBS_UNDOING = registerItem(GebsUndoingItem::new, "gebs_undoing");
    public static final RegistryObject<Item> HORUS_ASCENSION = registerItem(HorusAscensionItem::new, "horus_ascension");
    public static final RegistryObject<Item> HORUS_SOARING = registerItem(HorusSoaringItem::new, "horus_soaring");
    public static final RegistryObject<Item> ISIS_DIVISION = registerItem(IsisDivisionItem::new, "isis_division");
    public static final RegistryObject<Item> ISIS_HEALING = registerItem(IsisHealingItem::new, "isis_healing");
    public static final RegistryObject<Item> MONTUS_BLAST = registerItem(MontusBlastItem::new, "montus_blast");
    public static final RegistryObject<Item> MONTUS_STRIKE = registerItem(MontusStrikeItem::new, "montus_strike");
    public static final RegistryObject<Item> NEPTHYS_BANISHING = registerItem(NepthysBanishingItem::new, "nepthys_banishing");
    //public static final RegistryObject<Item> NEPTHYS_CONSECRATION = registerItem(NepthysConsecrationItem::new, "nepthys_consecration");
    public static final RegistryObject<Item> NEPTHYS_GUARD = registerItem(NepthysGuardItem::new, "nepthys_guard");
    public static final RegistryObject<Item> NUITS_IRE = registerItem(NuitsIreItem::new, "nuits_ire");
    public static final RegistryObject<Item> NUITS_QUARTER = registerItem(NuitsQuarterItem::new, "nuits_quarter");
    public static final RegistryObject<Item> NUITS_VANISHING = registerItem(NuitsVanishingItem::new, "nuits_vanishing");
    public static final RegistryObject<Item> OSIRIS_BLESSING = registerItem(OsirisBlessingItem::new, "osiris_blessing");
    public static final RegistryObject<Item> OSIRIS_MERCY = registerItem(OsirisMercyItem::new, "osiris_mercy");
    public static final RegistryObject<Item> PTAHS_DECADENCE = registerItem(PtahsDecadenceItem::new, "ptahs_decadence");
    public static final RegistryObject<Item> HALO_OF_RA = registerItem(() ->new RaArmor(EquipmentSlot.HEAD), "halo_of_ra");
    public static final RegistryObject<Item> BODY_OF_RA = registerItem(() ->new RaArmor(EquipmentSlot.CHEST), "body_of_ra");
    public static final RegistryObject<Item> LEGS_OF_RA = registerItem(() ->new RaArmor(EquipmentSlot.LEGS), "legs_of_ra");
    public static final RegistryObject<Item> FEET_OF_RA = registerItem(() -> new RaArmor(EquipmentSlot.FEET), "feet_of_ra");
    public static final RegistryObject<Item> RAS_FURY = registerItem(RasFuryItem::new, "ras_fury");
    public static final RegistryObject<Item> RAS_STEP = registerItem(RasStep::new, "ras_step");
    public static final RegistryObject<Item> SETHS_STING = registerItem(SethsStingItem::new, "seths_sting");
    public static final RegistryObject<Item> SETHS_VENOM = registerItem(SethsVenomItem::new, "seths_venom");
    public static final RegistryObject<Item> SHUS_BREATH = registerItem(ShusBreathItem::new, "shus_breath");
    public static final RegistryObject<Item> SHUS_EXILE = registerItem(ShusExileItem::new, "shus_exile");
    public static final RegistryObject<Item> SHUS_SWIFTNESS = registerItem(ShusSwiftnessItem::new, "shus_swiftness");
    public static final RegistryObject<Item> TEFNUTS_CALL = registerItem(TefnutsCallItem::new, "tefnuts_call");
    public static final RegistryObject<Item> TEFNUTS_RAIN = registerItem(TefnutsRainItem::new, "tefnuts_rain");
    public static final RegistryObject<Item> MUMMY_HELMET = registerItem(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.HEAD), "mummy_helmet");
    public static final RegistryObject<Item> MUMMY_CHEST = registerItem(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.CHEST), "mummy_chest");
    public static final RegistryObject<Item> MUMMY_LEGS = registerItem(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.LEGS), "mummy_legs");
    public static final RegistryObject<Item> MUMMY_BOOTS = registerItem(() -> new TexturedArmorItem(AtumMats.MUMMY_ARMOR, "mummy_armor", EquipmentSlot.FEET), "mummy_boots");
    public static final RegistryObject<Item> WANDERER_HELMET = registerItem(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.HEAD), "wanderer_helmet");
    public static final RegistryObject<Item> WANDERER_CHEST = registerItem(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.CHEST), "wanderer_chest");
    public static final RegistryObject<Item> WANDERER_LEGS = registerItem(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.LEGS), "wanderer_legs");
    public static final RegistryObject<Item> WANDERER_BOOTS = registerItem(() -> new WandererDyeableArmor(AtumMats.WANDERER_ARMOR, "wanderer_armor", EquipmentSlot.FEET), "wanderer_boots");
    public static final RegistryObject<Item> DESERT_HELMET_IRON = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.HEAD).setDamageModifier(10), "desert_helmet_iron");
    public static final RegistryObject<Item> DESERT_CHEST_IRON = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.CHEST).setDamageModifier(10), "desert_chest_iron");
    public static final RegistryObject<Item> DESERT_LEGS_IRON = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.LEGS).setDamageModifier(10), "desert_legs_iron");
    public static final RegistryObject<Item> DESERT_BOOTS_IRON = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.IRON, "desert_iron_armor", EquipmentSlot.FEET).setDamageModifier(10), "desert_boots_iron");
    public static final RegistryObject<Item> DESERT_HELMET_GOLD = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.HEAD).setDamageModifier(20), "desert_helmet_gold");
    public static final RegistryObject<Item> DESERT_CHEST_GOLD = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.CHEST).setDamageModifier(20), "desert_chest_gold");
    public static final RegistryObject<Item> DESERT_LEGS_GOLD = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.LEGS).setDamageModifier(20), "desert_legs_gold");
    public static final RegistryObject<Item> DESERT_BOOTS_GOLD = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.GOLD, "desert_gold_armor", EquipmentSlot.FEET).setDamageModifier(20), "desert_boots_gold");
    public static final RegistryObject<Item> DESERT_HELMET_DIAMOND = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.HEAD).setDamageModifier(15), "desert_helmet_diamond");
    public static final RegistryObject<Item> DESERT_CHEST_DIAMOND = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.CHEST).setDamageModifier(15), "desert_chest_diamond");
    public static final RegistryObject<Item> DESERT_LEGS_DIAMOND = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.LEGS).setDamageModifier(15), "desert_legs_diamond");
    public static final RegistryObject<Item> DESERT_BOOTS_DIAMOND = registerItem(() -> new WandererDyeableArmor(ArmorMaterials.DIAMOND, "desert_diamond_armor", EquipmentSlot.FEET).setDamageModifier(15), "desert_boots_diamond");
    public static final RegistryObject<Item> DESERT_WOLF_IRON_ARMOR = registerItem(NonStackableItem::new, "desert_wolf_iron_armor");
    public static final RegistryObject<Item> DESERT_WOLF_GOLD_ARMOR = registerItem(NonStackableItem::new, "desert_wolf_gold_armor");
    public static final RegistryObject<Item> DESERT_WOLF_DIAMOND_ARMOR = registerItem(NonStackableItem::new, "desert_wolf_diamond_armor");
    public static final RegistryObject<Item> CAMEL_IRON_ARMOR = registerItem(NonStackableItem::new, "camel_iron_armor");
    public static final RegistryObject<Item> CAMEL_GOLD_ARMOR = registerItem(NonStackableItem::new, "camel_gold_armor");
    public static final RegistryObject<Item> CAMEL_DIAMOND_ARMOR = registerItem(NonStackableItem::new, "camel_diamond_armor");
    public static final RegistryObject<Item> SCROLL = registerItem(SimpleItem::new, "scroll");
    public static final RegistryObject<Item> SCRAP = registerItem(SimpleItem::new, "cloth_scrap");
    public static final RegistryObject<Item> LINEN_BANDAGE = registerItem(LinenBandageItem::new, "linen_bandage");
    public static final RegistryObject<Item> LINEN_THREAD = registerItem(SimpleItem::new, "linen_thread");
    public static final RegistryObject<Item> LINEN_CLOTH = registerItem(SimpleItem::new, "linen_cloth");
    public static final RegistryObject<Item> PAPYRUS_PLANT = registerItem(() -> new BlockItem(AtumBlocks.PAPYRUS.get(), new Item.Properties().tab(Atum.GROUP)), "papyrus_plant");
    public static final RegistryObject<Item> FLAX_SEEDS = registerItem(() -> new ItemNameBlockItem(AtumBlocks.FLAX.get(), new Item.Properties().tab(Atum.GROUP)), "flax_seeds");
    public static final RegistryObject<Item> FLAX = registerItem(SimpleItem::new, "flax");
    public static final RegistryObject<Item> OPHIDIAN_TONGUE_FLOWER = registerItem(SimpleItem::new, "ophidian_tongue_flower");
    public static final RegistryObject<Item> ANPUTS_FINGERS_SPORES = registerItem(() -> new ItemNameBlockItem(AtumBlocks.ANPUTS_FINGERS.get(), new Item.Properties().tab(Atum.GROUP)), "anputs_fingers_spores");
    public static final RegistryObject<Item> EMMER_SEEDS = registerItem(() -> new ItemNameBlockItem(AtumBlocks.EMMER_WHEAT.get(), new Item.Properties().tab(Atum.GROUP)), "emmer_seeds");
    public static final RegistryObject<Item> EMMER_EAR = registerItem(SimpleItem::new, "emmer");
    public static final RegistryObject<Item> EMMER_FLOUR = registerItem(EmmerFlourItem::new, "emmer_flour");
    public static final RegistryObject<Item> EMMER_DOUGH = registerItem(SimpleItem::new, "emmer_dough");
    public static final RegistryObject<Item> EMMER_BREAD = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.EMMER_BREAD).tab(Atum.GROUP)), "emmer_bread");
    public static final RegistryObject<Item> QUAIL_EGG = registerItem(QuailEggItem::new, "quail_egg");
    public static final RegistryObject<Item> CAMEL_RAW = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_RAW).tab(Atum.GROUP)), "camel");
    public static final RegistryObject<Item> CAMEL_COOKED = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.CAMEL_COOKED).tab(Atum.GROUP)), "camel_cooked");
    public static final RegistryObject<Item> QUAIL_RAW = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_RAW).tab(Atum.GROUP)), "quail");
    public static final RegistryObject<Item> QUAIL_COOKED = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.QUAIL_COOKED).tab(Atum.GROUP)), "quail_cooked");
    public static final RegistryObject<Item> DATE = registerItem(() -> new Item(new Item.Properties().food(Foods.APPLE).tab(Atum.GROUP)), "date");
    public static final RegistryObject<Item> GLISTERING_DATE = registerItem(SimpleItem::new, "glistering_date");
    public static final RegistryObject<Item> GOLDEN_DATE = registerItem(() -> new Item(new Item.Properties().food(AtumFoods.GOLDEN_DATE).tab(Atum.GROUP)), "golden_date");
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_DATE = registerItem(() -> new EnchantedGoldenAppleItem(new Item.Properties().food(AtumFoods.ENCHANTED_GOLDEN_DATE).tab(Atum.GROUP)), "golden_date_enchanted");
    public static final RegistryObject<Item> ECTOPLASM = registerItem(SimpleItem::new, "ectoplasm");
    public static final RegistryObject<Item> MANDIBLES = registerItem(SimpleItem::new, "mandibles");
    public static final RegistryObject<Item> DUSTY_BONE = registerItem(SimpleItem::new, "dusty_bone");
    public static final RegistryObject<Item> WOLF_PELT = registerItem(SimpleItem::new, "wolf_pelt");
    public static final RegistryObject<Item> FERTILE_SOIL_PILE = registerItem(SimpleItem::new, "fertile_soil_pile");
    public static final RegistryObject<Item> FORSAKEN_FISH = registerItem(() -> new FishItem(FishItem.FishType.FORSAKEN), "fish_forsaken");
    public static final RegistryObject<Item> MUMMIFIED_FISH = registerItem(() -> new FishItem(FishItem.FishType.MUMMIFIED), "fish_mummified");
    public static final RegistryObject<Item> JEWELED_FISH = registerItem(() -> new FishItem(FishItem.FishType.JEWELED), "fish_jeweled");
    public static final RegistryObject<Item> SKELETAL_FISH = registerItem(() -> new FishItem(FishItem.FishType.SKELETAL), "fish_skeletal");
    public static final RegistryObject<Item> CRUNCHY_SCARAB = registerItem(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_SCARAB)), "crunchy_scarab");
    public static final RegistryObject<Item> CRUNCHY_GOLD_SCARAB = registerItem(() -> new CrunchyScarabItem(new Item.Properties().food(AtumFoods.CRUNCHY_GOLD_SCARAB)), "crunchy_golden_scarab");

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
     * Helper method for easily registering relics
     *
     * @param type The relic type
     * @return The dirty relic item that was registered
     */
    public static RegistryObject<Item> registerRelic(@Nonnull RelicItem.Type type) {
        Item.Properties nonDirty = new Item.Properties().stacksTo(16);
        RegistryObject<Item> dirty = registerItem(() -> new RelicItem(new Item.Properties().stacksTo(64)), getRelicName(RelicItem.Quality.DIRTY, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SILVER, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.GOLD, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.SAPPHIRE, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.RUBY, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.EMERALD, type));
        registerItem(() -> new RelicItem(nonDirty), getRelicName(RelicItem.Quality.DIAMOND, type));
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