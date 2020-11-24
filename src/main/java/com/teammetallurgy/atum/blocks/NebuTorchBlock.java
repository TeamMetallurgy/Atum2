package com.teammetallurgy.atum.blocks;

import com.teammetallurgy.atum.entity.undead.PharaohEntity;
import com.teammetallurgy.atum.init.AtumParticles;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.material.Material;

import javax.annotation.Nullable;

public class NebuTorchBlock extends TorchBlock {

    public NebuTorchBlock(@Nullable PharaohEntity.God god) {
        super(Block.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0.0F).setLightLevel(s -> 14).sound(SoundType.METAL), AtumParticles.NEBU_FLAME); //TODO make per god. Probably need hashmap when particles added
    }
}