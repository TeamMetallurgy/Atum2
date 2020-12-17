package com.teammetallurgy.atum.blocks.lighting;

import com.teammetallurgy.atum.api.God;

public interface INebuTorch {

    boolean isNebuTorch();

    /**
     * Make sure to have isNebuTorch check, before calling this.
     */
    God getGod();
}