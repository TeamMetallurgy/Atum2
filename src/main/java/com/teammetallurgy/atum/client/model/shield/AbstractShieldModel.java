package com.teammetallurgy.atum.client.model.shield;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;

public abstract class AbstractShieldModel extends Model {

    public AbstractShieldModel() {
        super(RenderType::getEntitySolid);
    }
}