package com.teammetallurgy.atum.items.artifacts.ptah;

import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.init.AtumBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.List;

@Mod.EventBusSubscriber(modid = Atum.MOD_ID)
public class PtahsDecadenceItem extends PickaxeItem { //TODO Test

    public PtahsDecadenceItem() {
        super(ItemTier.DIAMOND, 1, -2.8F, new Item.Properties().rarity(Rarity.RARE));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean hasEffect(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean onBlockDestroyed(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull BlockState state, @Nonnull BlockPos pos, @Nonnull LivingEntity entityLiving) { //TODO Test
        if (!world.isRemote && world instanceof ServerWorld && entityLiving.getHeldItemMainhand().getItem() == this) {
            ServerWorld serverWorld = (ServerWorld) world;
            List<ItemStack> drops = Block.getDrops(state, serverWorld, pos, null);
            if (!drops.isEmpty()) {
                for (ItemStack itemDropped : drops) {
                    Block dropBlock = Block.getBlockFromItem(itemDropped.getItem());
                    if ((dropBlock == AtumBlocks.IRON_ORE || dropBlock == Blocks.IRON_ORE) && serverWorld.rand.nextFloat() <= 0.50F) {
                        if (dropBlock == AtumBlocks.IRON_ORE) {
                            entityLiving.captureDrops().clear(); //TODO Test
                            entityLiving.entityDropItem(AtumBlocks.GOLD_ORE);
                            serverWorld.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        } else {
                            entityLiving.captureDrops().clear(); //TODO Test
                            entityLiving.entityDropItem(Blocks.GOLD_ORE);
                            serverWorld.playSound(null, pos, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
        return super.onBlockDestroyed(stack, world, state, pos, entityLiving);
    }
}