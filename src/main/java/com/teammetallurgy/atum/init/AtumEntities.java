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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import static net.minecraft.world.entity.EntityType.Builder;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_DEFERRED = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Atum.MOD_ID);
    //Mobs
    public static final RegistryObject<EntityType<AssassinEntity>> ASSASSIN = registerMob("assassin", 0x433731, 0xd99220, () -> Builder.of(AssassinEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<BarbarianEntity>> BARBARIAN = registerMob("barbarian", 0x9c7359, 0x8c8c8c, () -> Builder.of(BarbarianEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<BonestormEntity>> BONESTORM = registerMob("bonestorm", 0x74634e, 0xab9476, () -> Builder.of(BonestormEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<BrigandEntity>> BRIGAND = registerMob("brigand", 0xC2C2C2, 0x040F85, () -> Builder.of(BrigandEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<CamelEntity>> CAMEL = registerMob("camel", 0xAD835C, 0x684626, () -> Builder.of(CamelEntity::new, MobCategory.CREATURE).sized(0.9F, 1.87F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<DesertRabbitEntity>> DESERT_RABBIT = registerMob("desert_rabbit", 0xAE8652, 0x694C29, () -> Builder.of(DesertRabbitEntity::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<DesertWolfEntity>> DESERT_WOLF = registerMob("desert_wolf", 0xE7DBC8, 0xAD9467, () -> Builder.of(DesertWolfEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<ForsakenEntity>> FORSAKEN = registerMob("forsaken", 0xB59C7D, 0x6F5C43, () -> Builder.of(ForsakenEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<MummyEntity>> MUMMY = registerMob("mummy", 0x515838, 0x868F6B, () -> Builder.of(MummyEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<NomadEntity>> NOMAD = registerMob("nomad", 0xC2C2C2, 0x7E0C0C, () -> Builder.of(NomadEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<PharaohEntity>> PHARAOH = registerMob("pharaoh", 0xD4BC37, 0x3A4BE0, () -> Builder.of(PharaohEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final RegistryObject<EntityType<QuailEntity>> QUAIL = registerMob("quail", 0xCC9B72, 0xA47549, () -> Builder.of(QuailEntity::new, MobCategory.CREATURE).sized(0.35F, 0.525F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<ScarabEntity>> SCARAB = registerMob("scarab", 0x61412C, 0x2F1D10, () -> Builder.of(ScarabEntity::new, MobCategory.MONSTER).sized(0.4F, 0.3F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<SergeantEntity>> SERGEANT = registerMob("sergeant", 0x444444, 0xC2C2C2, () -> Builder.of(SergeantEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<ServalEntity>> SERVAL = registerMob("serval", 0xffe0b2, 0xa17b64, () -> Builder.of(ServalEntity::new, MobCategory.CREATURE).sized(0.7F, 0.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<StoneguardEntity>> STONEGUARD = registerMob("stoneguard", 0x918354, 0x695D37, () -> Builder.of(StoneguardEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final RegistryObject<EntityType<StoneguardEntity>> STONEGUARD_FRIENDLY = registerEntity("stoneguard_friendly", () -> Builder.of(StoneguardEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).fireImmune().clientTrackingRange(10));
    public static final RegistryObject<EntityType<StonewardenEntity>> STONEWARDEN = registerMob("stonewarden", 0x918354, 0x695D37, () -> Builder.of(StonewardenEntity::new, MobCategory.MONSTER).sized(1.4F, 2.7F).fireImmune().clientTrackingRange(10));
    public static final RegistryObject<EntityType<StonewardenEntity>> STONEWARDEN_FRIENDLY = registerEntity("stonewarden_friendly", () -> Builder.of(StonewardenEntity::new, MobCategory.MISC).sized(1.4F, 2.7F).fireImmune().clientTrackingRange(10));
    public static final RegistryObject<EntityType<TarantulaEntity>> TARANTULA = registerMob("tarantula", 0x745c47, 0xd2b193, () -> Builder.of(TarantulaEntity::new, MobCategory.MONSTER).sized(0.85F, 0.55F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<WarlordEntity>> BANDIT_WARLORD = registerMob("bandit_warlord", 0xa62d1b, 0xe59a22, () -> Builder.of(WarlordEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8));
    public static final RegistryObject<EntityType<WraithEntity>> WRAITH = registerMob("wraith", 0x544d34, 0x3e3927, () -> Builder.of(WraithEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F));
    public static final RegistryObject<EntityType<AtumVillagerEntity>> VILLAGER_MALE = registerEntity("villager_male", () -> Builder.of(AtumVillagerEntity::new, MobCategory.MISC).sized(0.6F, 1.85F).clientTrackingRange(10));
    public static final RegistryObject<EntityType<AtumVillagerEntity>> VILLAGER_FEMALE = registerEntity("villager_female", () -> Builder.of(AtumVillagerEntity::new, MobCategory.MISC).sized(0.6F, 1.85F).clientTrackingRange(10));

    //Entities
    public static final RegistryObject<EntityType<CamelSpitEntity>> CAMEL_SPIT = registerEntity("camel_spit", () -> Builder.<CamelSpitEntity>of(CamelSpitEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
            .setTrackingRange(4)
            .setUpdateInterval(10)
            .setShouldReceiveVelocityUpdates(false)
            .setCustomClientFactory(CamelSpitEntity::new));
    public static final RegistryObject<EntityType<SmallBoneEntity>> SMALL_BONE = registerEntity("small_bone", () -> Builder.<SmallBoneEntity>of(SmallBoneEntity::new, MobCategory.MISC).sized(0.3125F, 0.3125F)
            .setTrackingRange(4)
            .setUpdateInterval(10)
            .setCustomClientFactory(SmallBoneEntity::new));
    public static final RegistryObject<EntityType<ArrowDoubleEntity>> DOUBLE_ARROW = registerArrow("arrow_double", ArrowDoubleEntity::new, ArrowDoubleEntity::new);
    public static final RegistryObject<EntityType<ArrowExplosiveEntity>> EXPLOSIVE_ARROW = registerArrow("arrow_explosive", ArrowExplosiveEntity::new, ArrowExplosiveEntity::new);
    public static final RegistryObject<EntityType<ArrowFireEntity>> FIRE_ARROW = registerArrow("arrow_fire", ArrowFireEntity::new, ArrowFireEntity::new);
    public static final RegistryObject<EntityType<ArrowPoisonEntity>> POISON_ARROW = registerArrow("arrow_poison", ArrowPoisonEntity::new, ArrowPoisonEntity::new);
    public static final RegistryObject<EntityType<ArrowQuickdrawEntity>> QUICKDRAW_ARROW = registerArrow("arrow_quickdraw", ArrowQuickdrawEntity::new, ArrowQuickdrawEntity::new);
    public static final RegistryObject<EntityType<ArrowRainEntity>> RAIN_ARROW = registerArrow("arrow_rain", ArrowRainEntity::new, ArrowRainEntity::new);
    public static final RegistryObject<EntityType<ArrowSlownessEntity>> SLOWNESS_ARROW = registerArrow("arrow_slowness", ArrowSlownessEntity::new, ArrowSlownessEntity::new);
    public static final RegistryObject<EntityType<ArrowStraightEntity>> STRAIGHT_ARROW = registerArrow("arrow_straight", ArrowStraightEntity::new, ArrowStraightEntity::new);
    public static final RegistryObject<EntityType<PharaohOrbEntity>> PHARAOH_ORB = registerEntity("pharaoh_orb", () -> Builder.<PharaohOrbEntity>of(PharaohOrbEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
            .setTrackingRange(4)
            .updateInterval(20)
            .setCustomClientFactory(PharaohOrbEntity::new));
    public static final RegistryObject<EntityType<QuailEggEntity>> QUAIL_EGG = registerEntity("quail_egg", () -> Builder.<QuailEggEntity>of(QuailEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F)
            .clientTrackingRange(4)
            .updateInterval(10)
            .setCustomClientFactory(QuailEggEntity::new));
    public static final RegistryObject<EntityType<TefnutsCallEntity>> TEFNUTS_CALL = registerEntity("tefnuts_call", () -> Builder.<TefnutsCallEntity>of(TefnutsCallEntity::new, MobCategory.MISC).sized(0.5F, 0.5F)
            .setTrackingRange(4)
            .setUpdateInterval(20)
            .fireImmune()
            .setCustomClientFactory(TefnutsCallEntity::new));

    @SubscribeEvent
    public static void register(SpawnPlacementRegisterEvent event) {
        event.register(ASSASSIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BANDIT_WARLORD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BARBARIAN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BONESTORM.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(BRIGAND.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(CAMEL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DESERT_RABBIT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(DESERT_WOLF.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DesertWolfEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(FORSAKEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(MUMMY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(NOMAD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(PHARAOH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(QUAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(SCARAB.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScarabEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(SERGEANT.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BanditBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(SERVAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AtumEntities::canAnimalSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(STONEGUARD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(STONEGUARD_FRIENDLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(STONEWARDEN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(STONEWARDEN_FRIENDLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, StoneBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(TARANTULA.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TarantulaEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(WRAITH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, UndeadBaseEntity::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(VILLAGER_MALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(VILLAGER_FEMALE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ASSASSIN.get(), AssassinEntity.createAttributes().build());
        event.put(BANDIT_WARLORD.get(), WarlordEntity.createAttributes().build());
        event.put(BARBARIAN.get(), BarbarianEntity.createAttributes().build());
        event.put(BONESTORM.get(), BonestormEntity.createAttributes().build());
        event.put(BRIGAND.get(), BrigandEntity.createAttributes().build());
        event.put(CAMEL.get(), CamelEntity.createAttributes().build());
        event.put(DESERT_RABBIT.get(), DesertRabbitEntity.createAttributes().build());
        event.put(DESERT_WOLF.get(), DesertWolfEntity.createAttributes().build());
        event.put(FORSAKEN.get(), ForsakenEntity.createAttributes().build());
        event.put(MUMMY.get(), MummyEntity.createAttributes().build());
        event.put(NOMAD.get(), NomadEntity.createAttributes().build());
        event.put(PHARAOH.get(), PharaohEntity.createAttributes().build());
        event.put(QUAIL.get(), QuailEntity.createAttributes().build());
        event.put(SCARAB.get(), ScarabEntity.createAttributes().build());
        event.put(SERGEANT.get(), SergeantEntity.createAttributes().build());
        event.put(SERVAL.get(), ServalEntity.createAttributes().build());
        event.put(STONEGUARD.get(), StoneguardEntity.createAttributes().build());
        event.put(STONEGUARD_FRIENDLY.get(), StoneguardEntity.createAttributes().build());
        event.put(STONEWARDEN.get(), StonewardenEntity.createAttributes().build());
        event.put(STONEWARDEN_FRIENDLY.get(), StoneguardEntity.createAttributes().build());
        event.put(TARANTULA.get(), TarantulaEntity.createAttributes().build());
        event.put(WRAITH.get(), WraithEntity.createAttributes().build());
        event.put(VILLAGER_MALE.get(), AtumVillagerEntity.createAttributes().build());
        event.put(VILLAGER_FEMALE.get(), AtumVillagerEntity.createAttributes().build());
    }

    public static boolean canAnimalSpawn(EntityType<? extends Animal> animal, LevelAccessor level, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockState spawnState = level.getBlockState(pos.below());
        Block spawnBlock = spawnState.getBlock();
        return (spawnState.is(BlockTags.SAND) || spawnState.is(Tags.Blocks.SAND) || DimensionHelper.getSurfaceBlocks().contains(spawnBlock) ||
                spawnBlock == Blocks.GRASS_BLOCK) && level.getRawBrightness(pos, 0) > 8;
    }

    /**
     * Registers any mob, that will have a spawn egg.
     *
     * @param name         String to register the entity as
     * @param eggPrimary   Primary egg color
     * @param eggSecondary Secondary Color
     * @param builder      Builder for the entity
     * @return The EntityType that was registered
     */
    public static <T extends Mob> RegistryObject<EntityType<T>> registerMob(String name, int eggPrimary, int eggSecondary, Supplier<Builder<T>> builder) {
        RegistryObject<EntityType<T>> entityType = registerEntity(name, builder);
        RegistryObject<Item> spawnEgg = AtumItems.registerItem(() -> new ForgeSpawnEggItem(entityType, eggPrimary, eggSecondary, (new Item.Properties())), name + "_spawn_egg");
        AtumItems.ITEMS_FOR_TAB_LIST.add(spawnEgg);
        return entityType;
    }

    /**
     * Registers an entity
     *
     * @param name    String to register the entity as
     * @param builder Builder for the entity
     * @return The EntityType that was registered
     */
    public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<Builder<T>> builder) {
        ResourceLocation location = new ResourceLocation(Atum.MOD_ID, name);
        return ENTITY_DEFERRED.register(name, () -> builder.get().build(location.toString()));
    }

    /**
     * Registers arrow.
     *
     * @param name String to register the arrow with
     * @return The Arrow EntityType that was registered
     */
    public static <T extends CustomArrow> RegistryObject<EntityType<T>> registerArrow(String name, EntityType.EntityFactory<T> factory, BiFunction<PlayMessages.SpawnEntity, Level, T> customClientFactory) {
        return registerEntity(name, () -> Builder.of(factory, MobCategory.MISC)
                .sized(0.5F, 0.5F)
                .setTrackingRange(4)
                .updateInterval(20)
                .setCustomClientFactory(customClientFactory));
    }
}