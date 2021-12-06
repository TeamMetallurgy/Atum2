package com.teammetallurgy.atum.client.model.shield;

import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.RenderType;

public abstract class AbstractShieldModel extends Model {

    public AbstractShieldModel() {
        super(RenderType::entitySolid);
    }
}