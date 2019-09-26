package com.teammetallurgy.atum.blocks.stone.porphyry;

import com.google.common.collect.Maps;
import com.teammetallurgy.atum.utils.AtumRegistry;
import com.teammetallurgy.atum.utils.OreDictHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks.Type;

public class BlockPorphyryBricks extends Block {
    private static final Map<Type, BlockPorphyryBricks> BRICKS = Maps.newEnumMap(Type.class);

    private BlockPorphyryBricks() {
        super(Material.ROCK);
        this.setHardness(1.5F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.STONE);
    }

    @Override
    @Nonnull
    public MaterialColor getMapColor(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return MaterialColor.QUARTZ;
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return 0;
    }

    public static void registerBricks() {
        for (Type type : Type.values()) {
            BlockPorphyryBricks brick = new BlockPorphyryBricks();
            BRICKS.put(type, brick);
            AtumRegistry.registerBlock(brick, "porphyry_brick_" + type.getName());
        }
    }

    public static BlockPorphyryBricks getBrick(Type type) {
        return BRICKS.get(type);
    }

    @Override
    public void getOreDictEntries() {
        if (this == getBrick(Type.POLISHED)) {
            OreDictHelper.add(this, "stonePorphyryPolished");
        }
    }
}