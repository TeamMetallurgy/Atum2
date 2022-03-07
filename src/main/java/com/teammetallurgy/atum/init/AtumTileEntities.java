package com.teammetallurgy.atum.init;

import com.mojang.datafixers.types.Type;
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
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class AtumTileEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_DEFERRED = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Atum.MOD_ID);
    //Chests
    public static final RegistryObject<BlockEntityType<LimestoneChestTileEntity>> LIMESTONE_CHEST = register("limestone_chest", () -> BlockEntityType.Builder.of(LimestoneChestTileEntity::new, AtumBlocks.LIMESTONE_CHEST.get()));
    public static final RegistryObject<BlockEntityType<SarcophagusTileEntity>> SARCOPHAGUS = register("sarcophagus", () -> BlockEntityType.Builder.of(SarcophagusTileEntity::new, AtumBlocks.SARCOPHAGUS.get()));
    public static final RegistryObject<BlockEntityType<CrateTileEntity>> CRATE = register("crate", () -> BlockEntityType.Builder.of(CrateTileEntity::new, AtumBlocks.PALM_CRATE.get(), AtumBlocks.DEADWOOD_CRATE.get()));
    //Traps
    public static final RegistryObject<BlockEntityType<BurningTrapTileEntity>> BURNING_TRAP = register("burning_trap", () -> BlockEntityType.Builder.of(BurningTrapTileEntity::new, AtumBlocks.BURNING_TRAP.get()));
    public static final RegistryObject<BlockEntityType<PoisonTrapTileEntity>> POISON_TRAP = register("poison_trap", () -> BlockEntityType.Builder.of(PoisonTrapTileEntity::new, AtumBlocks.POISON_TRAP.get()));
    public static final RegistryObject<BlockEntityType<TarTrapTileEntity>> TAR_TRAP = register("tar_trap", () -> BlockEntityType.Builder.of(TarTrapTileEntity::new, AtumBlocks.TAR_TRAP.get()));
    public static final RegistryObject<BlockEntityType<SmokeTrapTileEntity>> SMOKE_TRAP = register("smoke_trap", () -> BlockEntityType.Builder.of(SmokeTrapTileEntity::new, AtumBlocks.SMOKE_TRAP.get()));
    public static final RegistryObject<BlockEntityType<ArrowTrapTileEntity>> ARROW_TRAP = register("arrow_trap", () -> BlockEntityType.Builder.of(ArrowTrapTileEntity::new, AtumBlocks.ARROW_TRAP.get()));
    //Other
    public static final RegistryObject<BlockEntityType<LimestoneFurnaceTileEntity>> LIMESTONE_FURNACE = register("limestone_furnace", () -> BlockEntityType.Builder.of(LimestoneFurnaceTileEntity::new, AtumBlocks.LIMESTONE_FURNACE.get()));
    public static final RegistryObject<BlockEntityType<GlassblowerFurnaceTileEntity>> GLASSBLOWER_FURNACE = register("glassblower_furnace", () -> BlockEntityType.Builder.of(GlassblowerFurnaceTileEntity::new, AtumBlocks.GLASSBLOWER_FURNACE.get()));
    public static final RegistryObject<BlockEntityType<QuernTileEntity>> QUERN = register("quern", () -> BlockEntityType.Builder.of(QuernTileEntity::new, AtumBlocks.QUERN.get()));
    public static final RegistryObject<BlockEntityType<SpinningWheelTileEntity>> SPINNING_WHEEL = register("spinning_wheel", () -> BlockEntityType.Builder.of(SpinningWheelTileEntity::new, AtumBlocks.SPINNING_WHEEL.get()));
    public static final RegistryObject<BlockEntityType<KilnTileEntity>> KILN = register("kiln", () -> BlockEntityType.Builder.of(KilnTileEntity::new, AtumBlocks.KILN.get(), AtumBlocks.KILN_FAKE.get()));
    public static final RegistryObject<BlockEntityType<GodforgeTileEntity>> GODFORGE = register("godforge", () -> BlockEntityType.Builder.of(GodforgeTileEntity::new, AtumBlocks.GODFORGE.get()));
    public static final RegistryObject<BlockEntityType<AtumSignTileEntity>> SIGN = register("sign", () -> BlockEntityType.Builder.of(AtumSignTileEntity::new, AtumBlocks.PALM_SIGN.get(), AtumBlocks.DEADWOOD_SIGN.get(), AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.PALM_SIGN.get()), AtumWallSignBlock.WALL_SIGN_BLOCKS.get(AtumBlocks.DEADWOOD_SIGN.get())));
    public static final RegistryObject<BlockEntityType<PalmCurioDisplayTileEntity>> PALM_CURIO_DISPLAY = register("palm_curio_display", () -> BlockEntityType.Builder.of(PalmCurioDisplayTileEntity::new, AtumBlocks.PALM_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<DeadwoodCurioDisplayTileEntity>> DEADWOOD_CURIO_DISPLAY = register("deadwood_curio_display", () -> BlockEntityType.Builder.of(DeadwoodCurioDisplayTileEntity::new, AtumBlocks.DEADWOOD_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<AcaciaCurioDisplayTileEntity>> ACACIA_CURIO_DISPLAY = register("acacia_curio_display", () -> BlockEntityType.Builder.of(AcaciaCurioDisplayTileEntity::new, AtumBlocks.ACACIA_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<LimestoneCurioDisplayTileEntity>> LIMESTONE_CURIO_DISPLAY = register("limestone_curio_display", () -> BlockEntityType.Builder.of(LimestoneCurioDisplayTileEntity::new, AtumBlocks.LIMESTONE_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<AlabasterCurioDisplayTileEntity>> ALABASTER_CURIO_DISPLAY = register("alabaster_curio_display", () -> BlockEntityType.Builder.of(AlabasterCurioDisplayTileEntity::new, AtumBlocks.ALABASTER_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<PorphyryCurioDisplayTileEntity>> PORPHYRY_CURIO_DISPLAY = register("porphyry_curio_display", () -> BlockEntityType.Builder.of(PorphyryCurioDisplayTileEntity::new, AtumBlocks.PORPHYRY_CURIO_DISPLAY.get()));
    public static final RegistryObject<BlockEntityType<NebuCurioDisplayTileEntity>> NEBU_CURIO_DISPLAY = register("nebu_curio_display", () -> BlockEntityType.Builder.of(NebuCurioDisplayTileEntity::new, AtumBlocks.NEBU_CURIO_DISPLAY.get()));

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(@Nonnull String name, @Nonnull Supplier<BlockEntityType.Builder<T>> initializer) {
        Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, Atum.MOD_ID + ":" + name);
        return BLOCK_ENTITY_DEFERRED.register(name, () -> initializer.get().build(type));
    }
}