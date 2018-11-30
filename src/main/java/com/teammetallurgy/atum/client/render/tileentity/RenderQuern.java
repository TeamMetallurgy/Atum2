package com.teammetallurgy.atum.client.render.tileentity;

import com.teammetallurgy.atum.blocks.machines.tileentity.TileEntityQuern;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderQuern extends TileEntitySpecialRenderer<TileEntityQuern> {

    //TODO Render as fastTesr, if not using GLStateManager stuff
    @Override
    public void render(@Nonnull TileEntityQuern quern, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        super.render(quern, x, y, z, partialTicks, destroyStage, alpha);
    }
}