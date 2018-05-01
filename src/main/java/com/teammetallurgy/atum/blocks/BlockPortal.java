package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.handler.AtumConfig;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.world.AtumTeleporter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlockPortal extends BlockBreakable { //TODO Redo for 1.9. Switch over to having a sub-class with the size
    protected static final AxisAlignedBB PORTAL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);

    public BlockPortal() {
        super(Material.PORTAL, true);
        this.setTickRandomly(true);
        this.setHardness(-1.0F);
        this.setSoundType(SoundType.GLASS);
        this.setLightLevel(0.75F);
    }

    @Override
    @Nonnull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return PORTAL_AABB;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        super.updateTick(world, pos, state, rand);

        if (world.provider.isSurfaceWorld() && world.getGameRules().getBoolean("doMobSpawning") && rand.nextInt(2000) < world.getDifficulty().getDifficultyId()) {
            int i = pos.getY();
            BlockPos blockpos;

            for (blockpos = pos; !world.getBlockState(blockpos).isTopSolid() && blockpos.getY() > 0; blockpos = blockpos.down()) {
                ;
            }

            if (i > 0 && !world.getBlockState(blockpos.up()).isNormalCube()) {
                Entity entity = ItemMonsterPlacer.spawnCreature(world, EntityList.getKey(EntityPigZombie.class), (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 1.1D, (double) blockpos.getZ() + 0.5D);

                if (entity != null) {
                    entity.timeUntilPortal = entity.getPortalCooldown();
                }
            }
        }
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighborBlock, BlockPos neighborPos) { //TODO
        for (int x = -1; x < 2; x++) {
            for (int z = -1; z < 2; z++) {
                for (int y = -1; y < 1; y++) {
                    IBlockState blockState = world.getBlockState(pos.add(x, y, z));
                    if (blockState != Blocks.SANDSTONE.getDefaultState() && blockState != this && blockState != BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.LARGE).setBlockUnbreakable().getDefaultState()) {
                        world.setBlockToAir(pos);
                    }
                }
            }
        }
    }

    public boolean tryToCreatePortal(World world, BlockPos pos, IBlockState state) { //TODO
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int x1 = -2; x1 < 3; x1++) {
            for (int z1 = -2; z1 < 3; z1++) {
                if (world.getBlockState(pos.add(x1, 0, z1)) != state) {
                    return false;
                }
            }
        }
        for (int x1 = -2; x1 < 3; x1++) {
            for (int z1 = -2; z1 < 3; z1++) {
                if (x1 + x == x + 2 || z1 + z == z + 2 || x1 + x == x - 2 || z1 + z == z - 2) {
                    if (world.getBlockState(pos.add(x1, 1, z1)) != state) {
                        return false;
                    }
                }
            }
        }
        for (int y1 = 2; y1 < 4; y1++) {
            for (int x1 = -2; x1 < 3; x1++) {
                for (int z1 = -2; z1 < 3; z1++) {
                    if ((x1 + x == x + 2 && z1 + z == z + 2) || (x1 + x == x - 2 && z1 + z == z + 2) || (x1 + x == x + 2 && z1 + z == z - 2) || (x1 + x == x - 2 && z1 + z == z - 2)) {
                        if (world.getBlockState(pos.add(x1, y1, z1)) != state) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int x1 = -1; x1 < 2; x1++) {
            for (int z1 = -1; z1 < 2; z1++) {
                world.setBlockState(pos.add(x1, 1, z1), AtumBlocks.PORTAL.getDefaultState(), 2);
            }
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, IBlockAccess world, BlockPos pos, @Nonnull EnumFacing side) { //TODO
        pos = pos.offset(side);

        boolean flag = world.getBlockState(pos.west()).getBlock() == this && world.getBlockState(pos.west(2)).getBlock() != this;
        boolean flag1 = world.getBlockState(pos.east()).getBlock() == this && world.getBlockState(pos.east(2)).getBlock() != this;
        boolean flag2 = world.getBlockState(pos.north()).getBlock() == this && world.getBlockState(pos.north(2)).getBlock() != this;
        boolean flag3 = world.getBlockState(pos.south()).getBlock() == this && world.getBlockState(pos.south(2)).getBlock() != this;
        boolean flag4 = flag || flag1;
        boolean flag5 = flag2 || flag3;
        return flag4 && side == EnumFacing.WEST || (flag4 && side == EnumFacing.EAST || (flag5 && side == EnumFacing.NORTH || flag5 && side == EnumFacing.SOUTH));
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) { //TODO
        if (!entity.isRiding() && !entity.isBeingRidden() && entity.isNonBoss() && entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (entity.timeUntilPortal == 0) {
                entity.timeUntilPortal = 100;
                MinecraftServer minecraftserver = FMLCommonHandler.instance().getMinecraftServerInstance();
                int dimID = entity.dimension;
                int atumId = AtumConfig.DIMENSION_ID;
                WorldServer worldserver = minecraftserver.getWorld(0);
                WorldServer worldserver1 = minecraftserver.getWorld(atumId);
                if (dimID == atumId) {
                    minecraftserver.getPlayerList().transferPlayerToDimension((EntityPlayerMP) entity, 0, new AtumTeleporter(worldserver));
                } else {
                    minecraftserver.getPlayerList().transferPlayerToDimension((EntityPlayerMP) entity, atumId, new AtumTeleporter(worldserver1));
                }

                try {
                    ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, -1, "lastExperience", "cp", "field_71144_ck");
                    ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, -1, "lastHealth", "cm", "field_71149_ch");
                    ObfuscationReflectionHelper.setPrivateValue(EntityPlayerMP.class, player, -1, "lastFoodLevel", "cn", "field_71146_ci");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    @Nonnull
    public ItemStack getPickBlock(@Nonnull IBlockState state, RayTraceResult target, @Nonnull World world, @Nonnull BlockPos pos, EntityPlayer player) {
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
}