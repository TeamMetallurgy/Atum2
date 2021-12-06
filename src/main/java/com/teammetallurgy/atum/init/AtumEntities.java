package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.animal.*;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.entity.projectile.PharaohOrbEntity;
import com.teammetallurgy.atum.entity.projectile.QuailEggEntity;
import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.*;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.entity.undead.*;
import com.teammetallurgy.atum.entity.villager.AtumVillagerEntity;
import com.teammetallurgy.atum.world.DimensionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

import static com.teammetallurgy.atum.misc.AtumRegistry.*;
import static net.minecraft.world.entity.EntityType.Builder;

@ObjectHolder(value = Atum.MOD_ID)
@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumEntities {
    //Mobs
    public static final EntityType<AssassinEntity> ASSASSIN = registerMob("assassin", 0x433731, 0xd99220, Builder.of(AssassinEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<BarbarianEntity> BARBARIAN = registerMob("barbarian", 0x9c7359, 0x8c8c8c, Builder.of(BarbarianEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<BonestormEntity> BONESTORM = registerMob("bonestorm", 0x74634e, 0xab9476, Builder.of(BonestormEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<BrigandEntity> BRIGAND = registerMob("brigand", 0xC2C2C2, 0x040F85, Builder.of(BrigandEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<CamelEntity> CAMEL = registerMob("camel", 0xAD835C, 0x684626, Builder.of(CamelEntity::new, MobCategory.CREATURE).sized(0.9F, 1.87F).clientTrackingRange(10));
    public static final EntityType<DesertRabbitEntity> DESERT_RABBIT = registerMob("desert_rabbit", 0xAE8652, 0x694C29, Builder.of(DesertRabbitEntity::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8));
    public static final EntityType<DesertWolfEntity> DESERT_WOLF = registerMob("desert_wolf", 0xE7DBC8, 0xAD9467, Builder.of(DesertWolfEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(10));
    public static final EntityType<ForsakenEntity> FORSAKEN = registerMob("forsaken", 0xB59C7D, 0x6F5C43, Builder.of(ForsakenEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<MummyEntity> MUMMY = registerMob("mummy", 0x515838, 0x868F6B, Builder.of(MummyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<NomadEntity> NOMAD = registerMob("nomad", 0xC2C2C2, 0x7E0C0C, Builder.of(NomadEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<PharaohEntity> PHARAOH = registerMob("pharaoh", 0xD4BC37, 0x3A4BE0, Builder.of(PharaohEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final EntityType<QuailEntity> QUAIL = registerMob("quail", 0xCC9B72, 0xA47549, Builder.of(QuailEntity::new, MobCategory.CREATURE).sized(0.35F, 0.525F).clientTrackingRange(10));
    public static final EntityType<ScarabEntity> SCARAB = registerMob("scarab", 0x61412C, 0x2F1D10, Builder.of(ScarabEntity::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8));
    public static final EntityType<SergeantEntity> SERGEANT = registerMob("sergeant", 0x444444, 0xC2C2C2, Builder.of(SergeantEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<ServalEntity> SERVAL = registerMob("serval", 0xffe0b2, 0xa17b64, Builder.of(ServalEntity::new, MobCategory.CREATURE).sized(0.7F, 0.8F).clientTrackingRange(8));
    public static final EntityType<StoneguardEntity> STONEGUARD = registerMob("stoneguard", 0x918354, 0x695D37, Builder.of(StoneguardEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final EntityType<StoneguardEntity> STONEGUARD_FRIENDLY = registerEntity("stoneguard_friendly", Builder.of(StoneguardEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final EntityType<StonewardenEntity> STONEWARDEN = registerMob("stonewarden", 0x918354, 0x695D37, Builder.of(StonewardenEntity::new, MobCategory.MONSTER).sized(1.4F, 2.7F).fireImmune().clientTrackingRange(10));
    public static final EntityType<StonewardenEntity> STONEWARDEN_FRIENDLY = registerEntity("stonewarden_friendly", Builder.of(StonewardenEntity::new, MobCategory.MISC).sized(1.4F, 2.7F).fireImmune().clientTrackingRange(10));
    public static final EntityType<TarantulaEntity> TARANTULA = registerMob("tarantula", 0x745c47, 0xd2b193, Builder.of(TarantulaEntity::new, MobCategory.MONSTER).sized(0.85F, 0.55F).clientTrackingRange(8));
    public static final EntityType<WarlordEntity> BANDIT_WARLORD = registerMob("bandit_warlord", 0xa62d1b, 0xe59a22, Builder.of(WarlordEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final EntityType<WraithEntity> WRAITH = registerMob("wraith", 0x544d34, 0x3e3927, Builder.of(WraithEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final EntityType<AtumVillagerEntity> VILLAGER_MALE = registerEntity("villager_male", Builder.of(AtumVillagerEntity::new, MobCategory.MISC).sized(0.6F, 1.85F).clientTrackingRange(10));
    public static final EntityType<AtumVillagerEntity> VILLAGER_FEMALE = registerEntity("villager_female", Builder.of(AtumVillagerEntity::new, MobCategory.MISC).sized(0.6F, 1.85F).clientTrackingRange(10));

    //Entities
    public static final EntityType<CamelSpitEntity> CAMEL_SPIT = registerEntity("camel_spit", Builder.<CamelSpitEntity>of(CamelSpitEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false)
            .setCustomClientFactory(CamelSpitEntity::new));
    public static final EntityType<SmallBoneEntity> SMALL_BONE = registerEntity("small_bone", Builder.<SmallBoneEntity>of(SmallBoneEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
            .setTrackingRange(4)
            .setUpdateInterval(10)
            .setCustomClientFactory(SmallBoneEntity::new));
    public static final EntityType<ArrowDoubleEntity> DOUBLE_ARROW = registerArrow("arrow_double", ArrowDoubleEntity::new, ArrowDoubleEntity::new);
    public static final EntityType<ArrowExplosiveEntity> EXPLOSIVE_ARROW = registerArrow("arrow_explosive", ArrowExplosiveEntity::new, ArrowExplosiveEntity::new);
    public static final EntityType<ArrowFireEntity> FIRE_ARROW = registerArrow("arrow_fire", ArrowFireEntity::new, ArrowFireEntity::new);
    public static final EntityType<ArrowPoisonEntity> POISON_ARROW = registerArrow("arrow_poison", ArrowPoisonEntity::new, ArrowPoisonEntity::new);
    public static final EntityType<ArrowQuickdrawEntity> QUICKDRAW_ARROW = registerArrow("arrow_quickdraw", ArrowQuickdrawEntity::new, ArrowQuickdrawEntity::new);
    public static final EntityType<ArrowRainEntity> RAIN_ARROW = registerArrow("arrow_rain", ArrowRainEntity::new, ArrowRainEntity::new);
    public static final EntityType<ArrowSlownessEntity> SLOWNESS_ARROW = registerArrow("arrow_slowness", ArrowSlownessEntity::new, ArrowSlownessEntity::new);
    public static final EntityType<ArrowStraightEntity> STRAIGHT_ARROW = registerArrow("arrow_straight", ArrowStraightEntity::new, ArrowStraightEntity::new);
    public static final EntityType<PharaohOrbEntity> PHARAOH_ORB = registerEntity("pharaoh_orb", Builder.<PharaohOrbEntity>of(PharaohOrbEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
            .setTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory(PharaohOrbEntity::new));
    public static final EntityType<QuailEggEntity> QUAIL_EGG = registerEntity("quail_egg", Builder.<QuailEggEntity>of(QuailEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .setCustomClientFactory(QuailEggEntity::new));
    public static final EntityType<TefnutsCallEntity> TEFNUTS_CALL = registerEntity("tefnuts_call", Builder.<TefnutsCallEntity>of(TefnutsCallEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
            .setTrackingRange(4)
            .setUpdateInterval(20)
            .fireImmune()
            .setCustomClientFactory(TefnutsCallEntity::new));

    public static void registerSpawnPlacement() {
        SpawnPlacements.register(ASSASSIN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(BANDIT_WARLORD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(BARBARIAN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(BONESTORM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        SpawnPlacements.register(BRIGAND, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(CAMEL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        SpawnPlacements.register(DESERT_RABBIT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        SpawnPlacements.register(DESERT_WOLF, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DesertWolfEntity::canSpawn);
        SpawnPlacements.register(FORSAKEN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        SpawnPlacements.register(MUMMY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        SpawnPlacements.register(NOMAD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(PHARAOH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        SpawnPlacements.register(QUAIL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        SpawnPlacements.register(SCARAB, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScarabEntity::canSpawn);
        SpawnPlacements.register(SERGEANT, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        SpawnPlacements.register(SERVAL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        SpawnPlacements.register(STONEGUARD, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        SpawnPlacements.register(STONEGUARD_FRIENDLY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        SpawnPlacements.register(STONEWARDEN, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        SpawnPlacements.register(STONEWARDEN_FRIENDLY, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        SpawnPlacements.register(TARANTULA, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TarantulaEntity::canSpawn);
        SpawnPlacements.register(WRAITH, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        SpawnPlacements.register(VILLAGER_MALE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        SpawnPlacements.register(VILLAGER_FEMALE, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ASSASSIN, AssassinEntity.createAttributes().build());
        event.put(BANDIT_WARLORD, WarlordEntity.createAttributes().build());
        event.put(BARBARIAN, BarbarianEntity.createAttributes().build());
        event.put(BONESTORM, BonestormEntity.createAttributes().build());
        event.put(BRIGAND, BrigandEntity.createAttributes().build());
        event.put(CAMEL, CamelEntity.createAttributes().build());
        event.put(DESERT_RABBIT, DesertRabbitEntity.createAttributes().build());
        event.put(DESERT_WOLF, DesertWolfEntity.createAttributes().build());
        event.put(FORSAKEN, ForsakenEntity.createAttributes().build());
        event.put(MUMMY, MummyEntity.createAttributes().build());
        event.put(NOMAD, NomadEntity.createAttributes().build());
        event.put(PHARAOH, PharaohEntity.createAttributes().build());
        event.put(QUAIL, QuailEntity.createAttributes().build());
        event.put(SCARAB, ScarabEntity.createAttributes().build());
        event.put(SERGEANT, SergeantEntity.createAttributes().build());
        event.put(SERVAL, ServalEntity.createAttributes().build());
        event.put(STONEGUARD, StoneguardEntity.createAttributes().build());
        event.put(STONEGUARD_FRIENDLY, StoneguardEntity.createAttributes().build());
        event.put(STONEWARDEN, StonewardenEntity.createAttributes().build());
        event.put(STONEWARDEN_FRIENDLY, StoneguardEntity.createAttributes().build());
        event.put(TARANTULA, TarantulaEntity.createAttributes().build());
        event.put(WRAITH, WraithEntity.createAttributes().build());
        event.put(VILLAGER_MALE, AtumVillagerEntity.createAttributes().build());
        event.put(VILLAGER_FEMALE, AtumVillagerEntity.createAttributes().build());
    }

    public static boolean canAnimalSpawn(EntityType<? extends Animal> animal, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, Random random) {
        Block spawnBlock = world.getBlockState(pos.below()).getBlock();
        return (spawnBlock.is(BlockTags.SAND) || spawnBlock.is(Tags.Blocks.SAND) || DimensionHelper.SURFACE_BLOCKS.contains(spawnBlock) ||
                spawnBlock == Blocks.GRASS_BLOCK) && world.getRawBrightness(pos, 0) > 8;
    }
}