package com.teammetallurgy.atum.client.gui;

import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.GuiLimestoneFurnace;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.furnace.TileEntityLimestoneFurnace;
import com.teammetallurgy.atum.blocks.trap.tileentity.ContainerTrap;
import com.teammetallurgy.atum.blocks.trap.tileentity.GuiTrap;
import com.teammetallurgy.atum.blocks.trap.tileentity.TileEntityTrap;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.ContainerCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.GuiCrate;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.TileEntityCrate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class AtumGuiHandler implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null) {
            switch (id) {
                case 0:
                    return new ContainerFurnace(player.inventory, (TileEntityLimestoneFurnace) tileEntity);
                case 1:
                    return new ContainerCrate(player.inventory, (TileEntityCrate) tileEntity, player);
                case 2:
                    return new ContainerTrap(player.inventory, (TileEntityTrap) tileEntity);
            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity != null) {
            switch (id) {
                case 0:
                    return new GuiLimestoneFurnace(player.inventory, (TileEntityLimestoneFurnace) tileEntity);
                case 1:
                    return new GuiCrate(player.inventory, (TileEntityCrate) tileEntity);
                case 2:
                    return new GuiTrap(player.inventory, (TileEntityTrap) tileEntity);
            }
        }
        return null;
    }
}