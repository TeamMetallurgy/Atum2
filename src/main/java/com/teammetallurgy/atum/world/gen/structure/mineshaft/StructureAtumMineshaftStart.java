/*
package com.teammetallurgy.atum.world.gen.structure.mineshaft;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureStart;

import java.util.Random;

public class StructureAtumMineshaftStart extends StructureStart {
    private MapGenAtumMineshaft.Type type;

    public StructureAtumMineshaftStart() {
    }

    public StructureAtumMineshaftStart(World world, Random random, int x, int z, MapGenAtumMineshaft.Type type) {
        super(x, z);
        this.type = type;
        StructureAtumMineshaftPieces.Room room = new StructureAtumMineshaftPieces.Room(0, random, (x << 4) + 2, (z << 4) + 2, this.type);
        this.components.add(room);
        room.buildComponent(room, this.components, random);
        this.updateBoundingBox();
        this.markAvailableHeight(world, random, 10);
    }
}*/
