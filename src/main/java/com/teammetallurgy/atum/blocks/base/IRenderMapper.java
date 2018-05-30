package com.teammetallurgy.atum.blocks.base;

import net.minecraft.block.properties.IProperty;

public interface IRenderMapper {
    /**
     * Used to specify {@link IProperty}s, that should not be used for rendering the block
     *
     * @return The IProperty to ignore for rendering
     */
    default IProperty[] getNonRenderingProperties() {
        return new IProperty[]{};
    }
}