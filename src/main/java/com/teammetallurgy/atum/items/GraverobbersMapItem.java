package com.teammetallurgy.atum.items;

import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.world.gen.structure.pyramid.PyramidPieces;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MapItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GraverobbersMapItem extends MapItem {

    @Override
    public void onUpdate(@Nonnull ItemStack stack, World world, @Nullable Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            Hand hand = player.getHeldItem(Hand.OFF_HAND).getItem() == this ? Hand.OFF_HAND : Hand.MAIN_HAND;
            ItemStack heldStack = player.getHeldItem(hand);
            if (heldStack.getItem() == this && heldStack.getMetadata() == 0) {
                BlockPos pyramidPos = world.findNearestStructure(PyramidPieces.PYRAMID.toString(), new BlockPos(entity.posX, 0, entity.posZ), true);

                if (pyramidPos != null) {
                    ItemStack mapStack = setupNewMap(world, (double) pyramidPos.getX(), (double) pyramidPos.getZ(), (byte) 3, true, true);
                    renderBiomePreviewMap(world, mapStack);
                    MapData.addTargetDecoration(mapStack, pyramidPos, "+", MapDecoration.Type.TARGET_X);

                    heldStack.shrink(1);
                    if (!player.inventory.addItemStackToInventory(mapStack.copy())) {
                        player.dropItem(mapStack, false);
                    }
                }
            }
        }
        super.onUpdate(stack, world, entity, itemSlot, isSelected);
    }

    @Nonnull
    public static ItemStack setupNewMap(World world, double x, double z, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
        ItemStack graveMap = new ItemStack(AtumItems.GRAVEROBBERS_MAP, 1, world.getUniqueDataId("map"));
        String s = "map_" + graveMap.getMetadata();
        MapData mapdata = new MapData(s);
        world.setData(s, mapdata);
        mapdata.scale = scale;
        mapdata.calculateMapCenter(x, z, mapdata.scale);
        mapdata.dimension = world.provider.getDimension();
        mapdata.trackingPosition = trackingPosition;
        mapdata.unlimitedTracking = unlimitedTracking;
        mapdata.markDirty();
        return graveMap;
    }
}