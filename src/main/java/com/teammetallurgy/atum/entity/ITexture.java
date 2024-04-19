package com.teammetallurgy.atum.entity;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public interface ITexture {
    @OnlyIn(Dist.CLIENT)
    String getTexture();
}