package com.teammetallurgy.atum.client.gui;

import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.GuiLimestoneFurnace;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.blocks.trap.tileentity.TrapTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.client.gui.block.KilnScreen;
import com.teammetallurgy.atum.client.gui.block.TrapScreen;
import com.teammetallurgy.atum.client.gui.entity.GuiAlphaDesertWolf;
import com.teammetallurgy.atum.client.gui.entity.GuiCamel;
import com.teammetallurgy.atum.entity.animal.CamelEntity;
import com.teammetallurgy.atum.entity.animal.DesertWolfEntity;
import com.teammetallurgy.atum.inventory.container.block.ContainerCrate;
import com.teammetallurgy.atum.inventory.container.block.ContainerKiln;
import com.teammetallurgy.atum.inventory.container.block.ContainerTrap;
import com.teammetallurgy.atum.inventory.container.entity.ContainerAlphaDesertWolf;
import com.teammetallurgy.atum.inventory.container.entity.ContainerCamel;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.Objects;

public class AtumGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        Entity entity = world.getEntityByID(x);

        switch (id) {
            case 0:
                return new ContainerFurnace(player.inventory, (TileEntityLimestoneFurnace) Objects.requireNonNull(tileEntity));
            case 1:
                return new ContainerCrate(player.inventory, (CrateTileEntity) Objects.requireNonNull(tileEntity), player);
            case 2:
                return new ContainerTrap(player.inventory, (TrapTileEntity) Objects.requireNonNull(tileEntity));
            case 3:
                if (entity != null) {
                    CamelEntity camel = (CamelEntity) entity;
                    return new ContainerCamel(player.inventory, camel.getHorseChest(), camel, player);
                } else {
                    return null;
                }
            case 4:
                if (entity != null) {
                    DesertWolfEntity desertWolf = (DesertWolfEntity) entity;
                    return new ContainerAlphaDesertWolf(player.inventory, desertWolf.getInventory(), desertWolf, player);
                } else {
                    return null;
                }
            case 5:
                return new ContainerKiln(player.inventory, (KilnTileEntity) Objects.requireNonNull(tileEntity));
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, PlayerEntity player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        Entity entity = world.getEntityByID(x);

        switch (id) {
            case 0:
                return new GuiLimestoneFurnace(player.inventory, (TileEntityLimestoneFurnace) Objects.requireNonNull(tileEntity));
            case 1:
                return new GuiChest(player.inventory, (CrateTileEntity) Objects.requireNonNull(tileEntity));
            case 2:
                return new TrapScreen(player.inventory, (TrapTileEntity) Objects.requireNonNull(tileEntity));
            case 3:
                if (entity != null) {
                    CamelEntity camel = (CamelEntity) entity;
                    return new GuiCamel(player.inventory, camel.getHorseChest(), camel);
                } else {
                    return null;
                }
            case 4:
                if (entity != null) {
                    DesertWolfEntity desertWolf = (DesertWolfEntity) entity;
                    return new GuiAlphaDesertWolf(player.inventory, desertWolf.getInventory(), desertWolf);
                } else {
                    return null;
                }
            case 5:
                return new KilnScreen(player.inventory, (KilnTileEntity) Objects.requireNonNull(tileEntity));
        }
        return null;
    }
}