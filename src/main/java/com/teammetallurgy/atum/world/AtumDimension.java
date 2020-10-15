/*package com.teammetallurgy.atum.world.dimension;

public class AtumDimension {

    //TODO Reimplement sandstorm somehow
    //Sandstorm
    public int stormTime;
    public float prevStormStrength;
    public float stormStrength;
    private long lastUpdateTime;

    @Override
    public void calculateInitialWeather() {
        super.calculateInitialWeather();
        if (DATA.isStorming()) {
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
                    this.stormTime = DATA.isStorming() ? 1 : 2;
                }

                if (this.stormTime <= 0) {
                    if (DATA.isStorming()) {
                        this.stormTime = this.world.rand.nextInt(6000) + 6000;
                    } else {
                        this.stormTime = this.world.rand.nextInt(168000) + 12000;
                    }
                    NetworkHandler.sendToDimension(new WeatherPacket(DATA.isStorming(), this.stormTime), serverWorld, this.getType());
                } else {
                    this.stormTime--;
                    if (this.stormTime <= 0) {
                        DATA.setStorming(!DATA.isStorming());
                    }
                }

                this.prevStormStrength = this.stormStrength;
                if (DATA.isStorming()) {
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
}*/