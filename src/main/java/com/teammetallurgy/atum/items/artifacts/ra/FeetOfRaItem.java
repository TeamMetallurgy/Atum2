package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class FeetOfRaItem extends TexturedArmorItem {
    private BlockPos prevBlockpos;

    public FeetOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor_1", EquipmentSlotType.FEET);
        this.setRepairItem(Items.DIAMOND);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(World world, PlayerEntity player, @Nonnull ItemStack stack) {
        super.onArmorTick(world, player, stack);
        if (player.isEntityAlive() && !world.isRemote) {
            BlockPos pos = new BlockPos(player);

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(player, player.world, pos);
            }
        }
    }

    private void lavaWalk(LivingEntity living, World world, BlockPos pos) {
        if (living.onGround) {
            float f = (float) Math.min(16, 3);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(0, 0, 0);

            for (BlockPos.MutableBlockPos mutablePosBox : BlockPos.getAllInBoxMutable(pos.add((double) (-f), -1.0D, (double) (-f)), pos.add((double) f, -1.0D, (double) f))) {
                if (mutablePosBox.distanceSqToCenter(living.posX, living.posY, living.posZ) <= (double) (f * f)) {
                    mutablePos.setPos(mutablePosBox.getX(), mutablePosBox.getY() + 1, mutablePosBox.getZ());
                    IBlockState state = world.getBlockState(mutablePos);

                    if (state.getMaterial() == Material.AIR) {
                        IBlockState airState = world.getBlockState(mutablePosBox);

                        if (airState.getMaterial() == Material.LAVA && (airState.getBlock() == Blocks.LAVA || airState.getBlock() == Blocks.FLOWING_LAVA) && airState.getValue(BlockLiquid.LEVEL) == 0 && world.mayPlace(AtumBlocks.RA_STONE, mutablePosBox, false, EnumFacing.DOWN, null)) {
                            world.setBlockState(mutablePosBox, AtumBlocks.RA_STONE.getDefaultState());
                            world.scheduleUpdate(mutablePosBox.toImmutable(), AtumBlocks.RA_STONE, MathHelper.getInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nonnull
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return EnumRarity.RARE;
    }
}