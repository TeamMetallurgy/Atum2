package com.teammetallurgy.atum.blocks.base;

import net.minecraft.state.Property;

public interface IRenderMapper {
    /**
     * Used to specify {@link Property}s, that should not be used for rendering the block
     *
     * @return The Property to ignore for rendering
     */
    default Property[] getNonRenderingProperties() {
        return new Property[]{};
    }
}