package com.teammetallurgy.atum.world.dimension;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.SandLayersBlock;
import com.teammetallurgy.atum.init.AtumBiomes;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.network.NetworkHandler;
import com.teammetallurgy.atum.network.packet.StormStrengthPacket;
import com.teammetallurgy.atum.network.packet.WeatherPacket;
import com.teammetallurgy.atum.world.biome.AtumBiome;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProvider;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProviderSettings;
import com.teammetallurgy.atum.world.biome.provider.AtumBiomeProviderTypes;
import com.teammetallurgy.atum.world.gen.AtumChunkGenerator;
import com.teammetallurgy.atum.world.gen.AtumChunkGeneratorType;
import com.teammetallurgy.atum.world.gen.AtumGenSettings;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.ChunkGeneratorType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DerivedWorldInfo;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumDimension extends Dimension {
    private static BlockPos usePos;
    public boolean hasStartStructureSpawned;

    public AtumDimension(World world, DimensionType dimensionType) {
        super(world, dimensionType, 0.0F /*Brightness. Look into?*/);

        CompoundNBT tagCompound = world.getWorldInfo().getDimensionData(dimensionType);
        this.hasStartStructureSpawned = world instanceof ServerWorld && tagCompound.getBoolean("HasStartStructureSpawned");
        this.isStorming = world instanceof ServerWorld && tagCompound.getBoolean("IsStorming");
    }

    @SubscribeEvent
    public static void onUseBucket(PlayerInteractEvent.RightClickBlock event) {
        if (AtumConfig.WORLD_GEN.waterLevel.get() > 0) {
            usePos = event.getPos();
        } else {
            usePos = null;
        }
    }

    @Override
    public boolean doesWaterVaporize() {
        if (usePos != null) {
            return world.getBiome(usePos) != AtumBiomes.OASIS && usePos.getY() >= AtumConfig.WORLD_GEN.waterLevel.get();
        } else {
            return false;
        }
    }

    @Override
    public void setWorldTime(long time) {
        if (time == 24000L && this.world.getWorldInfo() instanceof DerivedWorldInfo) {
            ((DerivedWorldInfo) this.world.getWorldInfo()).delegate.setDayTime(time); //Workaround for making sleeping work in Atum
        }
        super.setWorldTime(time);
    }

    @Override
    @Nonnull
    public ChunkGenerator<?> createChunkGenerator() {
        BiomeProviderType<AtumBiomeProviderSettings, AtumBiomeProvider> biomeType = AtumBiomeProviderTypes.ATUM;
        ChunkGeneratorType<AtumGenSettings, AtumChunkGenerator> chunkType = AtumChunkGeneratorType.ATUM;
        AtumGenSettings genSettings = chunkType.createSettings();
        AtumBiomeProviderSettings biomeSettings = biomeType.createSettings(this.world.getWorldInfo()).setGeneratorSettings(genSettings);
        return chunkType.create(this.world, biomeType.create(biomeSettings), genSettings);
    }

    @Override
    @Nullable
    public BlockPos findSpawn(@Nonnull ChunkPos chunkPos, boolean checkValid) { //Copied from OverworldDimension
        for (int x = chunkPos.getXStart(); x <= chunkPos.getXEnd(); ++x) {
            for (int z = chunkPos.getZStart(); z <= chunkPos.getZEnd(); ++z) {
                BlockPos pos = this.findSpawn(x, z, checkValid);
                if (pos != null) {
                    return pos;
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public BlockPos findSpawn(int posX, int posZ, boolean checkValid) { //Copied from OverworldDimension. Else if removed
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(posX, 0, posZ);
        Biome biome = this.world.getBiome(mutablePos);
        BlockState state = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !state.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.world.getChunk(posX >> 4, posZ >> 4);
            int x = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, posX & 15, posZ & 15);
            if (x < 0) {
                return null;
            } else {
                for (int y = x + 1; y >= 0; --y) {
                    mutablePos.setPos(posX, y, posZ);
                    BlockState stateMutable = this.world.getBlockState(mutablePos);
                    if (!stateMutable.getFluidState().isEmpty()) {
                        break;
                    }
                    if (stateMutable.equals(state)) {
                        return mutablePos.up().toImmutable();
                    }
                }
                return null;
            }
        }
    }

    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) { //Copied from OverworldDimension
        double d0 = MathHelper.frac((double) worldTime / 24000.0D - 0.25D);
        double d1 = 0.5D - Math.cos(d0 * Math.PI) / 2.0D;
        return (float) (d0 * 2.0D + d1) / 3.0F;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    @Override
    @Nonnull
    public Vec3d getFogColor(float celestialAngle, float partialTicks) {
        float f = MathHelper.cos((float) (celestialAngle * Math.PI * 2.0F)) * 2.0F + 0.5F;
        if (f < 0.2F) {
            f = 0.2F;
        }

        if (f > 1.0F) {
            f = 1.0F;
        }

        // Darken fog as sandstorm builds
        // f *= (1 - this.stormStrength) * 0.8 + 0.2;

        float f1 = 0.9F * f;
        float f2 = 0.75F * f;
        float f3 = 0.6F * f;
        return new Vec3d(f1, f2, f3);
    }

    @Override
    public boolean isDaytime() {
        return this.getWorld().getSkylightSubtracted() < 4;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public boolean doesXZShowFog(int x, int z) {
        return true; //TODO Test
    }

    @Override
    public void onWorldSave() {
        CompoundNBT tagCompound = new CompoundNBT();
        tagCompound.putBoolean("HasStartStructureSpawned", this.hasStartStructureSpawned);
        tagCompound.putBoolean("IsStorming", this.isStorming);
        this.world.getWorldInfo().setDimensionData(this.world.getDimension().getType(), tagCompound);
    }

    //Sandstorm
    public boolean isStorming;
    public int stormTime;
    public float prevStormStrength;
    public float stormStrength;
    private long lastUpdateTime;

    @Override
    public void calculateInitialWeather() {
        super.calculateInitialWeather();
        if (this.isStorming) {
            this.stormStrength = 1;
        }
    }

    private boolean canPlaceSandAt(BlockPos pos, AtumBiome biome) {
        BlockState state = this.world.getBlockState(pos.down());
        return state.getBlock() != AtumBlocks.SAND && state.getBlock() != AtumBlocks.LIMESTONE_GRAVEL && biome.canPlaceSandLayer(this.world, pos) || state.getBlock().isIn(BlockTags.LEAVES);
    }

    @Override
    public void updateWeather(Runnable defaultLogic) {
        if (AtumConfig.SANDSTORM.sandstormEnabled.get()) {
            if (!this.world.isRemote) {
                ServerWorld serverWorld = (ServerWorld) this.world;
                int cleanWeatherTime = serverWorld.getWorldInfo().getClearWeatherTime();

                if (cleanWeatherTime > 0) {
                    --cleanWeatherTime;
                    serverWorld.getWorldInfo().setClearWeatherTime(cleanWeatherTime);
                    this.stormTime = isStorming ? 1 : 2;
                }

                if (this.stormTime <= 0) {
                    if (this.isStorming) {
                        this.stormTime = this.world.rand.nextInt(6000) + 6000;
                    } else {
                        this.stormTime = this.world.rand.nextInt(168000) + 12000;
                    }
                    NetworkHandler.sendToDimension(new WeatherPacket(this.isStorming, this.stormTime), serverWorld, this.getType());
                } else {
                    this.stormTime--;
                    if (this.stormTime <= 0) {
                        this.isStorming = !this.isStorming;
                    }
                }

                this.prevStormStrength = this.stormStrength;
                if (this.isStorming) {
                    this.stormStrength += 1 / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
                } else {
                    this.stormStrength -= 1 / (float) (20 * AtumConfig.SANDSTORM.sandstormTransitionTime.get());
                }
                this.stormStrength = MathHelper.clamp(this.stormStrength, 0, 1);

                if (this.stormStrength != this.prevStormStrength || this.lastUpdateTime < System.currentTimeMillis() - 1000) {
                    NetworkHandler.sendToDimension(new StormStrengthPacket(this.stormStrength), serverWorld, this.getType());
                    this.lastUpdateTime = System.currentTimeMillis();
                }

                try {
                    if (AtumConfig.SANDSTORM.sandstormSandLayerChance.get() > 0 && this.world.rand.nextInt(AtumConfig.SANDSTORM.sandstormSandLayerChance.get()) == 0) {
                        if (this.stormStrength > 0.9F) {
                            ChunkManager chunkManager = ((ServerWorld) this.world).getWorldServer().getChunkProvider().chunkManager;

                            chunkManager.getLoadedChunksIterable().forEach(chunkHolder -> {
                                Optional<Chunk> optionalChunk = chunkHolder.getEntityTickingFuture().getNow(ChunkHolder.UNLOADED_CHUNK).left();
                                if (optionalChunk.isPresent()) {
                                    ChunkPos chunkPos = optionalChunk.get().getPos();
                                    if (!isOutsideSpawningRadius(chunkManager, chunkPos)) {
                                        BlockPos pos = this.world.getHeight(Heightmap.Type.MOTION_BLOCKING, serverWorld.getBlockRandomPos(chunkPos.getXStart(), 0, chunkPos.getZStart(), 15));
                                        BlockPos posDown = pos.down();

                                        if (this.world.isAreaLoaded(posDown, 1)) {
                                            BlockState sandState = this.world.getBlockState(pos);
                                            Biome biome = this.world.getBiome(pos);
                                            if (biome instanceof AtumBiome) {
                                                if (sandState.getBlock() == AtumBlocks.SAND_LAYERED) {
                                                    int layers = sandState.get(SandLayersBlock.LAYERS);
                                                    if (layers < 3) {
                                                        this.world.setBlockState(pos, sandState.with(SandLayersBlock.LAYERS, ++layers));
                                                    }
                                                } else if (this.canPlaceSandAt(pos, (AtumBiome) biome)) {
                                                    this.world.setBlockState(pos, AtumBlocks.SAND_LAYERED.getDefaultState());
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                } catch (Exception e) {
                    Atum.LOG.error("Error occurred while Sandstorm attempted to place Sand Layer");
                }
            }
        }
    }

    private static double getDistanceSquaredToChunk(ChunkPos chunkPos, Entity entity) {
        double chunkX = chunkPos.x * 16 + 8;
        double chunkZ = chunkPos.z * 16 + 8;
        double x = chunkX - entity.getPosX();
        double z = chunkZ - entity.getPosZ();
        return x * x + z * z;
    }

    public boolean isOutsideSpawningRadius(ChunkManager chunkManager, ChunkPos chunkPos) {
        long i = chunkPos.asLong();
        return !chunkManager.getTicketManager().isOutsideSpawningRadius(i) || chunkManager.playerGenerationTracker.getGeneratingPlayers(i).noneMatch((p) -> !p.isSpectator() && getDistanceSquaredToChunk(chunkPos, p) < 16384.0D);
    }
}