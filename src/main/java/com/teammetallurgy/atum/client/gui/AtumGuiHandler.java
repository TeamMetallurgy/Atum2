package com.teammetallurgy.atum.client.gui;

import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.GuiLimestoneFurnace;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.blocks.trap.tileentity.ContainerTrap;
import com.teammetallurgy.atum.blocks.trap.tileentity.GuiTrap;
import com.teammetallurgy.atum.blocks.trap.tileentity.TileEntityTrap;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.ContainerCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import com.teammetallurgy.atum.client.gui.entity.GuiCamel;
import com.teammetallurgy.atum.entity.animal.EntityCamel;
import com.teammetallurgy.atum.inventory.entity.ContainerCamel;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.Objects;

public class AtumGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));


        switch (id) {
            case 0:
                return new ContainerFurnace(player.inventory, (TileEntityLimestoneFurnace) Objects.requireNonNull(tileEntity));
            case 1:
                return new ContainerCrate(player.inventory, (TileEntityCrate) Objects.requireNonNull(tileEntity), player);
            case 2:
                return new ContainerTrap(player.inventory, (TileEntityTrap) Objects.requireNonNull(tileEntity));
            case 3:
                Entity entity = world.getEntityByID(x);
                if (entity != null) {
                    EntityCamel camel = (EntityCamel) entity;
                    return new ContainerCamel(player.inventory, camel.getHorseChest(), camel, player);
                }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        switch (id) {
            case 0:
                return new GuiLimestoneFurnace(player.inventory, (TileEntityLimestoneFurnace) Objects.requireNonNull(tileEntity));
            case 1:
                return new GuiChest(player.inventory, (TileEntityCrate) Objects.requireNonNull(tileEntity));
            case 2:
                return new GuiTrap(player.inventory, (TileEntityTrap) Objects.requireNonNull(tileEntity));
            case 3:
                Entity entity = world.getEntityByID(x);
                if (entity != null) {
                    EntityCamel camel = (EntityCamel) entity;
                    return new GuiCamel(player.inventory, camel.getHorseChest(), camel);
                }

        }
        return null;
    }
}