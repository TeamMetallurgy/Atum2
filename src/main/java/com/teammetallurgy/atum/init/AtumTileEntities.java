package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.beacon.tileentity.HeartOfRaTileEntity;
import com.teammetallurgy.atum.blocks.beacon.tileentity.RadiantBeaconTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.ChestSpawnerTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.LimestoneFurnaceTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.*;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import com.teammetallurgy.atum.utils.Constants;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

import static com.teammetallurgy.atum.utils.AtumRegistry.registerTileEntity;

@ObjectHolder(value = Constants.MOD_ID)
public class AtumTileEntities {
    //Chests
    public static final TileEntityType<LimestoneChestTileEntity> LIMESTONE_CHEST = registerTileEntity("limestone_chest", TileEntityType.Builder.create(LimestoneChestTileEntity::new, AtumBlocks.LIMESTONE_CHEST));
    public static final TileEntityType<ChestSpawnerTileEntity> CHEST_SPAWNER = registerTileEntity("chest_spawner", TileEntityType.Builder.create(ChestSpawnerTileEntity::new, AtumBlocks.CHEST_SPAWNER));
    public static final TileEntityType<SarcophagusTileEntity> SARCOPHAGUS = registerTileEntity("sarcophagus", TileEntityType.Builder.create(SarcophagusTileEntity::new, AtumBlocks.SARCOPHAGUS));
    public static final TileEntityType<CrateTileEntity> CRATE = registerTileEntity("crate", TileEntityType.Builder.create(CrateTileEntity::new, AtumBlocks.PALM_CRATE, AtumBlocks.DEADWOOD_CRATE));
    //Traps
    public static final TileEntityType<BurningTrapTileEntity> BURNING_TRAP = registerTileEntity("burning_trap", TileEntityType.Builder.create(BurningTrapTileEntity::new, AtumBlocks.BURNING_TRAP));
    public static final TileEntityType<PoisonTrapTileEntity> POISON_TRAP = registerTileEntity("poison_trap", TileEntityType.Builder.create(PoisonTrapTileEntity::new, AtumBlocks.POISON_TRAP));
    public static final TileEntityType<TarTrapTileEntity> TAR_TRAP = registerTileEntity("tar_trap", TileEntityType.Builder.create(TarTrapTileEntity::new, AtumBlocks.TAR_TRAP));
    public static final TileEntityType<SmokeTrapTileEntity> SMOKE_TRAP = registerTileEntity("smoke_trap", TileEntityType.Builder.create(SmokeTrapTileEntity::new, AtumBlocks.SMOKE_TRAP));
    public static final TileEntityType<ArrowTrapTileEntity> ARROW_TRAP = registerTileEntity("arrow_trap", TileEntityType.Builder.create(ArrowTrapTileEntity::new, AtumBlocks.ARROW_TRAP));
    //Other
    public static final TileEntityType<HeartOfRaTileEntity> HEART_OF_RA = registerTileEntity("heart_of_ra", TileEntityType.Builder.create(HeartOfRaTileEntity::new, AtumBlocks.HEART_OF_RA));
    public static final TileEntityType<RadiantBeaconTileEntity> RADIANT_BEACON = registerTileEntity("radiant_beacon", TileEntityType.Builder.create(RadiantBeaconTileEntity::new, AtumBlocks.RADIANT_BEACON, AtumBlocks.RADIANT_BEACON_FRAMED));
    public static final TileEntityType<LimestoneFurnaceTileEntity> LIMESTONE_FURNACE = registerTileEntity("limestone_furnace", TileEntityType.Builder.create(LimestoneFurnaceTileEntity::new, AtumBlocks.LIMESTONE_FURNACE));
    public static final TileEntityType<QuernTileEntity> QUERN = registerTileEntity("quern", TileEntityType.Builder.create(QuernTileEntity::new, AtumBlocks.QUERN));
    public static final TileEntityType<SpinningWheelTileEntity> SPINNING_WHEEL = registerTileEntity("spinning_wheel", TileEntityType.Builder.create(SpinningWheelTileEntity::new, AtumBlocks.SPINNING_WHEEL));
    public static final TileEntityType<KilnTileEntity> KILN = registerTileEntity("kiln", TileEntityType.Builder.create(KilnTileEntity::new, AtumBlocks.KILN));
}