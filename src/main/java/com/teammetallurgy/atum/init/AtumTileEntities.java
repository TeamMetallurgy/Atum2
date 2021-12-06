package com.teammetallurgy.atum.init;

import com.google.common.collect.Lists;
import com.teammetallurgy.atum.Atum;
import com.teammetallurgy.atum.blocks.curio.tileentity.*;
import com.teammetallurgy.atum.blocks.machines.tileentity.*;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.LimestoneChestTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.chest.tileentity.SarcophagusTileEntity;
import com.teammetallurgy.atum.blocks.stone.limestone.tileentity.LimestoneFurnaceTileEntity;
import com.teammetallurgy.atum.blocks.trap.tileentity.*;
import com.teammetallurgy.atum.blocks.wood.AtumWallSignBlock;
import com.teammetallurgy.atum.blocks.wood.tileentity.AtumSignTileEntity;
import com.teammetallurgy.atum.blocks.wood.tileentity.crate.CrateTileEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nonnull;
import java.util.List;

@ObjectHolder(value = Atum.MOD_ID)
@Mod.EventBusSubscriber(modid = Atum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtumTileEntities { //TODO Move to deferred registry
    private static final List<BlockEntityType<?>> TILE_ENTITY_TYPES = Lists.newArrayList();
    //Chests
    public static final BlockEntityType<LimestoneChestTileEntity> LIMESTONE_CHEST = null;
    public static final BlockEntityType<SarcophagusTileEntity> SARCOPHAGUS = null;
    public static final BlockEntityType<CrateTileEntity> CRATE = null;
    //Traps
    public static final BlockEntityType<BurningTrapTileEntity> BURNING_TRAP = null;
    public static final BlockEntityType<PoisonTrapTileEntity> POISON_TRAP = null;
    public static final BlockEntityType<TarTrapTileEntity> TAR_TRAP = null;
    public static final BlockEntityType<SmokeTrapTileEntity> SMOKE_TRAP = null;
    public static final BlockEntityType<ArrowTrapTileEntity> ARROW_TRAP = null;
    //Other
    public static final BlockEntityType<LimestoneFurnaceTileEntity> LIMESTONE_FURNACE = null;
    public static final BlockEntityType<GlassblowerFurnaceTileEntity> GLASSBLOWER_FURNACE = null;
    public static final BlockEntityType<QuernTileEntity> QUERN = null;
    public static final BlockEntityType<SpinningWheelTileEntity> SPINNING_WHEEL = null;
    public static final BlockEntityType<KilnTileEntity> KILN = null;
    public static final BlockEntityType<GodforgeTileEntity> GODFORGE = null;
    public static final BlockEntityType<AtumSignTileEntity> SIGN = null;
    public static final BlockEntityType<PalmCurioDisplayTileEntity> PALM_CURIO_DISPLAY = null;
    public static final BlockEntityType<DeadwoodCurioDisplayTileEntity> DEADWOOD_CURIO_DISPLAY = null;
    public static final BlockEntityType<AcaciaCurioDisplayTileEntity> ACACIA_CURIO_DISPLAY = null;
    public static final BlockEntityType<LimestoneCurioDisplayTileEntity> LIMESTONE_CURIO_DISPLAY = null;
    public static final BlockEntityType<AlabasterCurioDisplayTileEntity> ALABASTER_CURIO_DISPLAY = null;
    public static final BlockEntityType<PorphyryCurioDisplayTileEntity> PORPHYRY_CURIO_DISPLAY = null;
    public static final BlockEntityType<NebuCurioDisplayTileEntity> NEBU_CURIO_DISPLAY = null;


    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<BlockEntityType<?>> event) {
        registerTileEntity("limestone_chest", BlockEntityType.Builder.of(LimestoneChestTileEntity::new, AtumBlocks.LIMESTONE_CHEST));
        registerTileEntity("sarcophagus", BlockEntityType.Builder.of(SarcophagusTileEntity::new, AtumBlocks.SARCOPHAGUS));
        registerTileEntity("crate", BlockEntityType.Builder.of(CrateTileEntity::new, AtumBlocks.PALM_CRATE, AtumBlocks.DEADWOOD_CRATE));
        registerTileEntity("burning_trap", BlockEntityType.Builder.of(BurningTrapTileEntity::new, AtumBlocks.BURNING_TRAP));
        registerTileEntity("poison_trap", BlockEntityType.Builder.of(PoisonTrapTileEntity::new, AtumBlocks.POISON_TRAP));
        registerTileEntity("tar_trap", BlockEntityType.Builder.of(TarTrapTileEntity::new, AtumBlocks.TAR_TRAP));
        registerTileEntity("smoke_trap", BlockEntityType.Builder.of(SmokeTrapTileEntity::new, AtumBlocks.SMOKE_TRAP));
        registerTileEntity("arrow_trap", BlockEntityType.Builder.of(ArrowTrapTileEntity::new, AtumBlocks.ARROW_TRAP));
        registerTileEntity("limestone_furnace", BlockEntityType.Builder.of(LimestoneFurnaceTileEntity::new, AtumBlocks.LIMESTONE_FURNACE));
        registerTileEntity("glassblower_furnace", BlockEntityType.Builder.of(GlassblowerFurnaceTileEntity::new, AtumBlocks.GLASSBLOWER_FURNACE));
        registerTileEntity("quern", BlockEntityType.Builder.of(QuernTileEntity::new, AtumBlocks.QUERN));
        registerTileEntity("spinning_wheel", BlockEntityType.Builder.of(SpinningWheelTileEntity::new, AtumBlocks.SPINNING_WHEEL));
        registerTileEntity("kiln", BlockEntityType.Builder.of(KilnTileEntity::new, AtumBlocks.KILN, AtumBlocks.KILN_FAKE));
        registerTileEntity("godforge", BlockEntityType.Builder.of(GodforgeTileEntity::new, AtumBlocks.GODFORGE));
        registerTileEntity("sign", BlockEntityType.Builder.of(AtumSignTileEntity::new, AtumBlocks.PALM_SIGN, AtumBlocks.DEADWOOD_SIGN, AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.PALM_SIGN), AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.DEADWOOD_SIGN)));
        registerTileEntity("palm_curio_display", BlockEntityType.Builder.of(PalmCurioDisplayTileEntity::new, AtumBlocks.PALM_CURIO_DISPLAY));
        registerTileEntity("deadwood_curio_display", BlockEntityType.Builder.of(DeadwoodCurioDisplayTileEntity::new, AtumBlocks.DEADWOOD_CURIO_DISPLAY));
        registerTileEntity("acacia_curio_display", BlockEntityType.Builder.of(AcaciaCurioDisplayTileEntity::new, AtumBlocks.ACACIA_CURIO_DISPLAY));
        registerTileEntity("limestone_curio_display", BlockEntityType.Builder.of(LimestoneCurioDisplayTileEntity::new, AtumBlocks.LIMESTONE_CURIO_DISPLAY));
        registerTileEntity("alabaster_curio_display", BlockEntityType.Builder.of(AlabasterCurioDisplayTileEntity::new, AtumBlocks.ALABASTER_CURIO_DISPLAY));
        registerTileEntity("porphyry_curio_display", BlockEntityType.Builder.of(PorphyryCurioDisplayTileEntity::new, AtumBlocks.PORPHYRY_CURIO_DISPLAY));
        registerTileEntity("nebu_curio_display", BlockEntityType.Builder.of(NebuCurioDisplayTileEntity::new, AtumBlocks.NEBU_CURIO_DISPLAY));

        for (BlockEntityType<?> tileEntityType : TILE_ENTITY_TYPES) {
            event.getRegistry().register(tileEntityType);
        }
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerTileEntity(@Nonnull String name, @Nonnull BlockEntityType.Builder<T> builder) {
        BlockEntityType<T> tileEntityType = builder.build(null);
        tileEntityType.setRegistryName(new ResourceLocation(Atum.MOD_ID, name));
        TILE_ENTITY_TYPES.add(tileEntityType);
        return tileEntityType;
    }
}