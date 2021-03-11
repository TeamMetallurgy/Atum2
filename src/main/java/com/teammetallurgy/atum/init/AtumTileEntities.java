package com.teammetallurgy.atum.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.tileentity.*;
import com.teammetallurgy.atum.blocks.machines.tileentity.GodforgeTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.KilnTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.QuernTileEntity;
import com.teammetallurgy.atum.blocks.machines.tileentity.SpinningWheelTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.LimestoneFurnaceTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.*;
import com.teammetallurgy.atum.blocks.wood.AtumWallSignBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.AtumSignTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.List;

@ObjectHolder(value = Atum.MOD_ID)
@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumTileEntities {
    private static final List<TileEntityType<?>> TILE_ENTITY_TYPES = Lists.newArrayList();
    //Chests
    public static final TileEntityType<LimestoneChestTileEntity> LIMESTONE_CHEST = null;
    public static final TileEntityType<SarcophagusTileEntity> SARCOPHAGUS = null;
    public static final TileEntityType<CrateTileEntity> CRATE = null;
    //Traps
    public static final TileEntityType<BurningTrapTileEntity> BURNING_TRAP = null;
    public static final TileEntityType<PoisonTrapTileEntity> POISON_TRAP = null;
    public static final TileEntityType<TarTrapTileEntity> TAR_TRAP = null;
    public static final TileEntityType<SmokeTrapTileEntity> SMOKE_TRAP = null;
    public static final TileEntityType<ArrowTrapTileEntity> ARROW_TRAP = null;
    //Other
    public static final TileEntityType<LimestoneFurnaceTileEntity> LIMESTONE_FURNACE = null;
    public static final TileEntityType<QuernTileEntity> QUERN = null;
    public static final TileEntityType<SpinningWheelTileEntity> SPINNING_WHEEL = null;
    public static final TileEntityType<KilnTileEntity> KILN = null;
    public static final TileEntityType<GodforgeTileEntity> GODFORGE = null;
    public static final TileEntityType<AtumSignTileEntity> SIGN = null;
    public static final TileEntityType<PalmCurioDisplayTileEntity> PALM_CURIO_DISPLAY = null;
    public static final TileEntityType<DeadwoodCurioDisplayTileEntity> DEADWOOD_CURIO_DISPLAY = null;
    public static final TileEntityType<AcaciaCurioDisplayTileEntity> ACACIA_CURIO_DISPLAY = null;
    public static final TileEntityType<LimestoneCurioDisplayTileEntity> LIMESTONE_CURIO_DISPLAY = null;
    public static final TileEntityType<AlabasterCurioDisplayTileEntity> ALABASTER_CURIO_DISPLAY = null;
    public static final TileEntityType<PorphyryCurioDisplayTileEntity> PORPHYRY_CURIO_DISPLAY = null;
    public static final TileEntityType<NebuCurioDisplayTileEntity> NEBU_CURIO_DISPLAY = null;


    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        registerTileEntity("limestone_chest", TileEntityType.Builder.create(LimestoneChestTileEntity::new, AtumBlocks.LIMESTONE_CHEST));
        registerTileEntity("sarcophagus", TileEntityType.Builder.create(SarcophagusTileEntity::new, AtumBlocks.SARCOPHAGUS));
        registerTileEntity("crate", TileEntityType.Builder.create(CrateTileEntity::new, AtumBlocks.PALM_CRATE, AtumBlocks.DEADWOOD_CRATE));
        registerTileEntity("burning_trap", TileEntityType.Builder.create(BurningTrapTileEntity::new, AtumBlocks.BURNING_TRAP));
        registerTileEntity("poison_trap", TileEntityType.Builder.create(PoisonTrapTileEntity::new, AtumBlocks.POISON_TRAP));
        registerTileEntity("tar_trap", TileEntityType.Builder.create(TarTrapTileEntity::new, AtumBlocks.TAR_TRAP));
        registerTileEntity("smoke_trap", TileEntityType.Builder.create(SmokeTrapTileEntity::new, AtumBlocks.SMOKE_TRAP));
        registerTileEntity("arrow_trap", TileEntityType.Builder.create(ArrowTrapTileEntity::new, AtumBlocks.ARROW_TRAP));
        registerTileEntity("limestone_furnace", TileEntityType.Builder.create(LimestoneFurnaceTileEntity::new, AtumBlocks.LIMESTONE_FURNACE));
        registerTileEntity("quern", TileEntityType.Builder.create(QuernTileEntity::new, AtumBlocks.QUERN));
        registerTileEntity("spinning_wheel", TileEntityType.Builder.create(SpinningWheelTileEntity::new, AtumBlocks.SPINNING_WHEEL));
        registerTileEntity("kiln", TileEntityType.Builder.create(KilnTileEntity::new, AtumBlocks.KILN, AtumBlocks.KILN_FAKE));
        registerTileEntity("godforge", TileEntityType.Builder.create(GodforgeTileEntity::new, AtumBlocks.GODFORGE));
        registerTileEntity("sign", TileEntityType.Builder.create(AtumSignTileEntity::new, AtumBlocks.PALM_SIGN, AtumBlocks.DEADWOOD_SIGN, AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.PALM_SIGN), AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.DEADWOOD_SIGN)));
        registerTileEntity("palm_curio_display", TileEntityType.Builder.create(PalmCurioDisplayTileEntity::new, AtumBlocks.PALM_CURIO_DISPLAY));
        registerTileEntity("deadwood_curio_display", TileEntityType.Builder.create(DeadwoodCurioDisplayTileEntity::new, AtumBlocks.DEADWOOD_CURIO_DISPLAY));
        registerTileEntity("acacia_curio_display", TileEntityType.Builder.create(AcaciaCurioDisplayTileEntity::new, AtumBlocks.ACACIA_CURIO_DISPLAY));
        registerTileEntity("limestone_curio_display", TileEntityType.Builder.create(LimestoneCurioDisplayTileEntity::new, AtumBlocks.LIMESTONE_CURIO_DISPLAY));
        registerTileEntity("alabaster_curio_display", TileEntityType.Builder.create(AlabasterCurioDisplayTileEntity::new, AtumBlocks.ALABASTER_CURIO_DISPLAY));
        registerTileEntity("porphyry_curio_display", TileEntityType.Builder.create(PorphyryCurioDisplayTileEntity::new, AtumBlocks.PORPHYRY_CURIO_DISPLAY));
        registerTileEntity("nebu_curio_display", TileEntityType.Builder.create(NebuCurioDisplayTileEntity::new, AtumBlocks.NEBU_CURIO_DISPLAY));

        for (TileEntityType<?> tileEntityType : TILE_ENTITY_TYPES) {
            event.getRegistry().register(tileEntityType);
        }
    }

    public static <T extends TileEntity> TileEntityType<T> registerTileEntity(@Nonnull String name, @Nonnull TileEntityType.Builder<T> builder) {
        TileEntityType<T> tileEntityType = builder.build(null);
        tileEntityType.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        TILE_ENTITY_TYPES.add(tileEntityType);
        return tileEntityType;
    }
}