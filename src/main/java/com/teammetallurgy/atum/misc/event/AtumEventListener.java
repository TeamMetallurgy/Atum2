package com.teammetallurgy.atum.misc.event;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.PortalBlock;
import com.teammetallurgy.atum.blocks.base.IUnbreakable;
import com.teammetallurgy.atum.entity.stone.StoneBaseEntity;
import com.teammetallurgy.atum.entity.undead.UndeadBaseEntity;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.init.AtumLootTables;
import com.teammetallurgy.atum.items.WandererDyeableArmor;
import com.teammetallurgy.atum.items.artifacts.atem.AtemsBountyItem;
import com.teammetallurgy.atum.misc.AtumConfig;
import com.teammetallurgy.atum.misc.StackHelper;
import com.teammetallurgy.atum.world.teleporter.TeleporterAtumStart;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class AtumEventListener {
    private static final String TAG_ATUM_START = "atum_start";

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        CompoundNBT tag = player.getPersistentData();
        CompoundNBT persistedTag = tag.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
        boolean shouldStartInAtum = AtumConfig.ATUM_START.startInAtum.get() && !persistedTag.getBoolean(TAG_ATUM_START);

        persistedTag.putBoolean(TAG_ATUM_START, true);
        tag.put(PlayerEntity.PERSISTED_NBT_TAG, persistedTag);

        if (shouldStartInAtum && player instanceof ServerPlayerEntity && player.world instanceof ServerWorld) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ServerWorld world = (ServerWorld) player.world;
            PortalBlock.changeDimension(world, serverPlayer, new TeleporterAtumStart());
            serverPlayer.func_242111_a(Atum.ATUM, serverPlayer.getPosition(), serverPlayer.getRotationYawHead(), true, false); //Set players spawn point in Atum, when starting in Atum
            world.func_241124_a__(serverPlayer.getPosition(), 16);
        }
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        if (AtumConfig.ATUM_START.startInAtum.get()) {
            LivingEntity livingEntity = event.getEntityLiving();
            if (livingEntity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) livingEntity;
                ServerWorld serverWorld = serverPlayer.getServerWorld();
                BlockPos respawnPos = serverPlayer.func_241140_K_();
                if (respawnPos != null) {
                    Optional<Vector3d> bedPos = PlayerEntity.func_242374_a(serverWorld, respawnPos, serverPlayer.func_242109_L(), serverPlayer.func_241142_M_(), false);
                    if (!bedPos.isPresent()) {
                        serverPlayer.func_242111_a(Atum.ATUM, serverWorld.getSpawnPoint(), serverPlayer.getRotationYawHead(), true, false); //Ensure that the player respawns in Atum, when bed is broken
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        World world = player.world;
        if (!world.isRemote && AtumConfig.GENERAL.allowCreation.get() && event.phase == TickEvent.Phase.END && player.ticksExisted % 20 == 0) {
            if (world.getDimensionKey() == World.OVERWORLD || world.getDimensionKey() == Atum.ATUM) {
                for (ItemEntity entityItem : world.getEntitiesWithinAABB(ItemEntity.class, player.getBoundingBox().grow(10.0F, 1.0F, 10.0F))) {
                    BlockState state = world.getBlockState(entityItem.getPosition());
                    if (entityItem.getItem().getItem() == AtumItems.SCARAB && (state.getBlock() == Blocks.WATER || state == AtumBlocks.PORTAL.getDefaultState())) {
                        if (AtumBlocks.PORTAL.trySpawnPortal(world, entityItem.getPosition())) {
                            entityItem.remove();
                            return;
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        BlockState state = event.getPlacedBlock();
        if (event.getEntity() != null && event.getEntity().world.getDimensionKey() == Atum.ATUM) {
            if (((state.getMaterial() == Material.EARTH || state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.MYCELIUM) && state.getBlock() != AtumBlocks.FERTILE_SOIL_TILLED)) {
                event.getWorld().setBlockState(event.getPos(), AtumBlocks.SAND.getDefaultState(), 3);
            }
        }
    }

    @SubscribeEvent
    public static void onArmorClean(PlayerInteractEvent.RightClickBlock event) {
        try {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            PlayerEntity player = event.getPlayer();
            ItemStack stack = player.getHeldItemMainhand();
            LazyOptional<IFluidHandler> lazyTank = FluidUtil.getFluidHandler(world, pos, event.getFace());

            if (stack.getItem() instanceof WandererDyeableArmor && ((WandererDyeableArmor) stack.getItem()).hasColor(stack)) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof CauldronBlock) {
                    int level = state.get(CauldronBlock.LEVEL);
                    if (level > 0) {
                        if (!world.isRemote) {
                            ((WandererDyeableArmor) stack.getItem()).removeColor(stack);
                            player.addStat(Stats.CLEAN_ARMOR);
                            ((CauldronBlock) state.getBlock()).setWaterLevel(world, pos, state, level - 1);
                        }
                        player.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.16F, 0.66F);
                        event.setUseItem(Event.Result.DENY);
                    }
                } else if (lazyTank.map(f -> f instanceof FluidTank).orElse(false)) {
                    lazyTank.ifPresent(tank -> {
                                FluidTank fluidTank = (FluidTank) tank;

                                if (fluidTank.getFluid().getFluid().isIn(FluidTags.WATER) && fluidTank.getFluidAmount() >= 250) {
                                    if (!world.isRemote) {
                                        ((WandererDyeableArmor) stack.getItem()).removeColor(stack);
                                        player.addStat(Stats.CLEAN_ARMOR);
                                        tank.drain(250, IFluidHandler.FluidAction.EXECUTE);
                                    }
                                    player.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.16F, 0.66F);
                                    event.setUseItem(Event.Result.ALLOW);
                                }
                            }
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void onSeedUse(PlayerInteractEvent.RightClickBlock event) {
        PlayerEntity player = event.getPlayer();
        World world = event.getWorld();
        if (player.world.getDimensionKey() == Atum.ATUM) {
            if (player.getHeldItem(event.getHand()).getItem() == Items.WHEAT_SEEDS && world.getBlockState(event.getPos()).getBlock() instanceof FarmlandBlock) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().equals(DamageSource.DROWN) && (event.getEntity() instanceof UndeadBaseEntity || event.getEntity() instanceof StoneBaseEntity)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onFishLoot(ItemFishedEvent event) {
        World world = event.getPlayer().world;
        FishingBobberEntity bobber = event.getHookEntity();
        PlayerEntity angler = bobber.func_234606_i_();
        if (angler != null) {
            ItemStack heldStack = angler.getHeldItem(angler.getActiveHand());
            LootContext.Builder builder = new LootContext.Builder((ServerWorld) world);
            builder.withLuck((float) EnchantmentHelper.getFishingLuckBonus(heldStack) + angler.getLuck()).withParameter(LootParameters.KILLER_ENTITY, angler).withParameter(LootParameters.THIS_ENTITY, bobber);
            if (world.getDimensionKey() == Atum.ATUM) {
                event.setCanceled(true); //We don't want vanillas loot table
                if (heldStack.getItem() instanceof AtemsBountyItem) {
                    catchFish((ServerWorld) world, angler, heldStack, bobber, builder, AtumLootTables.ATEMS_BOUNTY);
                    angler.world.addEntity(new ExperienceOrbEntity(angler.world, angler.getPosX(), angler.getPosY() + 0.5D, angler.getPosZ() + 0.5D, world.rand.nextInt(6) + 1));
                } else {
                    catchFish((ServerWorld) world, angler, heldStack, bobber, builder, AtumLootTables.FISHING);
                }
            }
        }
    }

    private static void catchFish(ServerWorld world, PlayerEntity angler, ItemStack heldStack, FishingBobberEntity bobber, LootContext.Builder builder, ResourceLocation lootTable) {
        List<ItemStack> loots = world.getServer().getLootTableManager().getLootTableFromLocation(lootTable).generate(builder.withParameter(LootParameters.field_237457_g_, bobber.getPositionVec()).withParameter(LootParameters.TOOL, heldStack).withParameter(LootParameters.THIS_ENTITY, bobber).withRandom(world.rand).build(LootParameterSets.FISHING));
        for (ItemStack loot : loots) {
            ItemEntity fish = new ItemEntity(bobber.world, bobber.getPosX(), bobber.getPosY(), bobber.getPosZ(), loot);
            double x = angler.getPosX() - bobber.getPosX();
            double y = angler.getPosY() - bobber.getPosY();
            double z = angler.getPosZ() - bobber.getPosZ();
            double swush = MathHelper.sqrt(x * x + y * y + z * z);
            fish.setMotion(x * 0.1D, y * 0.1D + swush * 0.08D, z * 0.1D);
            world.addEntity(fish);
        }
    }

    @SubscribeEvent
    public static void checkSpawn(LivingSpawnEvent.CheckSpawn event) { //Prevent Phantom spawning in Atum
        IWorld world = event.getWorld();
        if ((event.getEntityLiving() instanceof PhantomEntity || event.getEntityLiving().getType() == EntityType.CAT) && (world instanceof ServerWorld && ((ServerWorld) world).getDimensionKey() == Atum.ATUM)) {
            event.setCanceled(true);
        }
    }

    //IUnbreakable
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        BlockState state = event.getState();
        if (state.getBlock() instanceof IUnbreakable && state.get(IUnbreakable.UNBREAKABLE) && !event.getPlayer().isCreative()) {
            event.setCanceled(true);
        }
    }

    //Ra Armor
    @SubscribeEvent
    public static void onDamage(LivingDamageEvent event) {
        if (StackHelper.hasFullArmorSet(event.getEntityLiving(), AtumItems.HALO_OF_RA, AtumItems.BODY_OF_RA, AtumItems.LEGS_OF_RA, AtumItems.FEET_OF_RA) && event.getSource().isFireDamage()) {
            event.setAmount(0.0F);
        }
    }
}