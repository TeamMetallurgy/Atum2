package com.teammetallurgy.atum.items.artifacts.ra;

import com.google.common.base.Objects;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.items.TexturedArmorItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nonnull;

public class FeetOfRaItem extends TexturedArmorItem {
    private BlockPos prevBlockpos;

    public FeetOfRaItem() {
        super(ArmorMaterial.DIAMOND, "ra_armor_1", EquipmentSlotType.FEET, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void onArmorTick(@Nonnull ItemStack stack, World world, PlayerEntity player) {
        super.onArmorTick(stack, world, player);
        if (player.isAlive() && !world.isRemote) {
            BlockPos pos = new BlockPos(player);

            if (!Objects.equal(this.prevBlockpos, pos)) {
                this.prevBlockpos = pos;
                this.lavaWalk(player, player.world, pos);
            }
        }
    }

    private void lavaWalk(LivingEntity living, World world, BlockPos pos) {
        if (living.onGround) {
            BlockState raStone = AtumBlocks.RA_STONE.getDefaultState();
            float area = (float) Math.min(16, 2);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            for (BlockPos posBox : BlockPos.getAllInBoxMutable(pos.add(-area, -1.0D, -area), pos.add(area, -1.0D, area))) {
                if (posBox.withinDistance(living.getPositionVec(), area)) {
                    mutablePos.setPos(posBox.getX(), posBox.getY() + 1, posBox.getZ());
                    BlockState state = world.getBlockState(mutablePos);
                    if (state.isAir(world, mutablePos)) {
                        BlockState checkState = world.getBlockState(posBox);
                        boolean isFull = checkState.getFluidState().isTagged(FluidTags.LAVA) && checkState.get(FlowingFluidBlock.LEVEL) == 0;
                        if (checkState.getMaterial() == Material.LAVA && isFull && raStone.isValidPosition(world, posBox) && world.func_217350_a(raStone, posBox, ISelectionContext.dummy()) && !ForgeEventFactory.onBlockPlace(living, new BlockSnapshot(world, posBox, checkState), Direction.UP)) {
                            world.setBlockState(posBox, raStone);
                            world.getPendingBlockTicks().scheduleTick(posBox, raStone.getBlock(), MathHelper.nextInt(living.getRNG(), 60, 120));
                        }
                    }
                }
            }

        }
    }
}