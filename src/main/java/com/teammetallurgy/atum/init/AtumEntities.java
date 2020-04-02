package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.entity.HeartOfRaEntity;
import com.teammetallurgy.atum.entity.animal.*;
import com.teammetallurgy.atum.entity.bandit.*;
import com.teammetallurgy.atum.entity.efreet.SunspeakerEntity;
import com.teammetallurgy.atum.entity.projectile.CamelSpitEntity;
import com.teammetallurgy.atum.entity.projectile.SmallBoneEntity;
import com.teammetallurgy.atum.entity.projectile.arrow.*;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.stone.StoneguardEntity;
import com.teammetallurgy.atum.entity.stone.StonewardenEntity;
import com.teammetallurgy.atum.entity.undead.*;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import java.util.Random;

import static com.teammetallurgy.atum.utils.AtumRegistry.*;
import static net.minecraft.entity.EntityType.Builder;

@ObjectHolder(value = Atum.MOD_ID)
@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD) //Needed to load field earlier
public class AtumEntities {
    //Mobs
    public static final EntityType<AssassinEntity> ASSASSIN = registerMob("assassin", 0x433731, 0xd99220, Builder.create(AssassinEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<WarlordEntity> BANDIT_WARLORD = registerMob("bandit_warlord", 0xa62d1b, 0xe59a22, Builder.create(WarlordEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<BarbarianEntity> BARBARIAN = registerMob("barbarian", 0x9c7359, 0x8c8c8c, Builder.create(BarbarianEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<BonestormEntity> BONESTORM = registerMob("bonestorm", 0x74634e, 0xab9476, Builder.create(BonestormEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<BrigandEntity> BRIGAND = registerMob("brigand", 0xC2C2C2, 0x040F85, Builder.create(BrigandEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<CamelEntity> CAMEL = registerMob("camel", 0xAD835C, 0x684626, Builder.create(CamelEntity::new, EntityClassification.CREATURE).size(0.9F, 1.87F));
    public static final EntityType<DesertWolfEntity> DESERT_WOLF = registerMob("desert_wolf", 0xE7DBC8, 0xAD9467, Builder.create(DesertWolfEntity::new, EntityClassification.CREATURE).size(0.6F, 0.8F));
    public static final EntityType<ForsakenEntity> FORSAKEN = registerMob("forsaken", 0xB59C7D, 0x6F5C43, Builder.create(ForsakenEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<MummyEntity> MUMMY = registerMob("mummy", 0x515838, 0x868F6B, Builder.create(MummyEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<NomadEntity> NOMAD = registerMob("nomad", 0xC2C2C2, 0x7E0C0C, Builder.create(NomadEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<PharaohEntity> PHARAOH = registerMob("pharaoh", 0xD4BC37, 0x3A4BE0, Builder.create(PharaohEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F).immuneToFire());
    public static final EntityType<DesertRabbitEntity> DESERT_RABBIT = registerMob("desert_rabbit", 0xAE8652, 0x694C29, Builder.create(DesertRabbitEntity::new, EntityClassification.CREATURE).size(0.4F, 0.5F));
    public static final EntityType<ScarabEntity> SCARAB = registerMob("scarab", 0x61412C, 0x2F1D10, Builder.create(ScarabEntity::new, EntityClassification.MONSTER).size(0.4F, 0.3F));
    public static final EntityType<SergeantEntity> SERGEANT = registerMob("sergeant", 0x444444, 0xC2C2C2, Builder.create(SergeantEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));
    public static final EntityType<StoneguardEntity> STONEGUARD = registerMob("stoneguard", 0x918354, 0x695D37, Builder.create(StoneguardEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F).immuneToFire());
    public static final EntityType<StonewardenEntity> STONEWARDEN = registerMob("stonewarden", 0x918354, 0x695D37, Builder.create(StonewardenEntity::new, EntityClassification.MONSTER).size(1.4F, 2.7F).immuneToFire());
    public static final EntityType<StoneguardEntity> STONEGUARD_FRIENDLY = registerEntity("stoneguard_friendly", Builder.create(StoneguardEntity::new, EntityClassification.MISC).size(0.6F, 1.8F).immuneToFire());
    public static final EntityType<StonewardenEntity> STONEWARDEN_FRIENDLY = registerEntity("stonewarden_friendly", Builder.create(StonewardenEntity::new, EntityClassification.MISC).size(1.4F, 2.7F).immuneToFire());
    public static final EntityType<SunspeakerEntity> SUNSPEAKER = registerMob("sunspeaker", 0x464646, 0xCC5654, Builder.create(SunspeakerEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F).immuneToFire());
    public static final EntityType<TarantulaEntity> TARANTULA = registerMob("tarantula", 0x745c47, 0xd2b193, Builder.create(TarantulaEntity::new, EntityClassification.MONSTER).size(0.85F, 0.55F));
    public static final EntityType<WraithEntity> WRAITH = registerMob("wraith", 0x544d34, 0x3e3927, Builder.create(WraithEntity::new, EntityClassification.MONSTER).size(0.6F, 1.8F));

    //Entities
    public static final EntityType<CamelSpitEntity> CAMEL_SPIT = registerEntity("camel_spit", Builder.<CamelSpitEntity>create(CamelSpitEntity::new, EntityClassification.MISC).size(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false));
    public static final EntityType<HeartOfRaEntity> HEART_OF_RA = registerEntity("heart_of_ra", Builder.<HeartOfRaEntity>create(HeartOfRaEntity::new, EntityClassification.MISC).size(1.0F, 2.8F)
            .setTrackingRange(16)
            .setUpdateInterval(Integer.MAX_VALUE)
            .setShouldReceiveVelocityUpdates(false));
    public static final EntityType<SmallBoneEntity> SMALL_BONE = registerEntity("small_bone", Builder.<SmallBoneEntity>create(SmallBoneEntity::new, EntityClassification.MISC).size(0.3125F, 0.3125F)
            .setTrackingRange(4)
            .setUpdateInterval(10));
    public static final EntityType<ArrowDoubleShotBlackEntity> DOUBLE_SHOT_BLACK = registerArrow("arrow_double_shot_black", ArrowDoubleShotBlackEntity::new);
    public static final EntityType<ArrowDoubleShotWhiteEntity> DOUBLE_SHOT_WHITE = registerArrow("arrow_double_shot_white", ArrowDoubleShotWhiteEntity::new);
    public static final EntityType<ArrowExplosiveEntity> EXPLOSIVE_ARROW = registerArrow("arrow_explosive", ArrowExplosiveEntity::new);
    public static final EntityType<ArrowFireEntity> FIRE_ARROW = registerArrow("arrow_fire", ArrowFireEntity::new);
    public static final EntityType<ArrowPoisonEntity> POISON_ARROW = registerArrow("arrow_poison", ArrowPoisonEntity::new);
    public static final EntityType<ArrowQuickdrawEntity> QUICKDRAW_ARROW = registerArrow("arrow_quickdraw", ArrowQuickdrawEntity::new);
    public static final EntityType<ArrowRainEntity> RAIN_ARROW = registerArrow("arrow_rain", ArrowRainEntity::new);
    public static final EntityType<ArrowSlownessEntity> SLOWNESS_ARROW = registerArrow("arrow_slowness", ArrowSlownessEntity::new);
    public static final EntityType<ArrowStraightEntity> STRAIGHT_ARROW = registerArrow("arrow_straight", ArrowStraightEntity::new);
    public static final EntityType<TefnutsCallEntity> TEFNUTS_CALL = registerEntity("tefnuts_call", Builder.<TefnutsCallEntity>create(TefnutsCallEntity::new, EntityClassification.MISC).size(0.5F, 0.5F)
            .setTrackingRange(4)
            .setUpdateInterval(20)
            .immuneToFire());

    public static void registerSpawnPlacement() {
        EntitySpawnPlacementRegistry.register(ASSASSIN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(BANDIT_WARLORD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(BARBARIAN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(BRIGAND, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(CAMEL, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        EntitySpawnPlacementRegistry.register(DESERT_WOLF, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DesertWolfEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(FORSAKEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(MUMMY, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(NOMAD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(PHARAOH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(DESERT_RABBIT, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn);
        EntitySpawnPlacementRegistry.register(SCARAB, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ScarabEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(STONEGUARD, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(STONEWARDEN, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(SUNSPEAKER, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canSpawnOn);
        EntitySpawnPlacementRegistry.register(TARANTULA, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, TarantulaEntity::canSpawn);
        EntitySpawnPlacementRegistry.register(WRAITH, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn);
    }

    public static boolean canAnimalSpawn(EntityType<? extends AnimalEntity> animal, IWorld world, SpawnReason spawnReason, BlockPos pos, Random random) {
        Block spawnBlock = world.getBlockState(pos.down()).getBlock();
        return (spawnBlock.isIn(BlockTags.SAND) || spawnBlock.isIn(Tags.Blocks.SAND) || spawnBlock == AtumBlocks.FERTILE_SOIL || spawnBlock == AtumBlocks.LIMESTONE_GRAVEL ||
                spawnBlock == Blocks.GRASS_BLOCK) && world.getLightSubtracted(pos, 0) > 8;
    }
}