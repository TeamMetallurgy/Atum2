package com.teammetallurgy.atum.entity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface ITexture {

    @OnlyIn(Dist.CLIENT)
    String getTexture();
}