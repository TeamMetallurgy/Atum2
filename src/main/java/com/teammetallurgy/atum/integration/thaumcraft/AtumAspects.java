package com.teammetallurgy.atum.integration.thaumcraft;

import com.teammetallurgy.atum.blocks.stone.alabaster.BlockAlabasterBricks;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneBricks;
import com.teammetallurgy.atum.blocks.stone.limestone.BlockLimestoneSlab;
import com.teammetallurgy.atum.blocks.stone.porphyry.BlockPorphyryBricks;
import com.teammetallurgy.atum.init.AtumBlocks;
import com.teammetallurgy.atum.init.AtumItems;
import com.teammetallurgy.atum.items.ItemLoot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.AspectRegistryEvent;

public class AtumAspects {

    @SubscribeEvent
    public void registerAspects(AspectRegistryEvent event) {
        event.register.registerObjectTag(new ItemStack(AtumBlocks.SAND_LAYERED), (new AspectList()).add(Aspect.EARTH, 1).add(Aspect.ENTROPY, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.DATE_BLOCK), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.LIFE, 6));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.OASIS_GRASS), (new AspectList()).add(Aspect.PLANT, 5).add(Aspect.AIR, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.DEAD_GRASS), (new AspectList()).add(Aspect.PLANT, 1).add(Aspect.ENTROPY, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.SHRUB), (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.ENTROPY, 2));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.WEED), (new AspectList()).add(Aspect.PLANT, 3).add(Aspect.ENTROPY, 1).add(Aspect.AIR, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.PAPYRUS), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.WATER, 2).add(Aspect.MIND, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.FLAX), (new AspectList()).add(Aspect.PLANT, 5).add(Aspect.CRAFT, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.FERTILE_SOIL), (new AspectList()).add(Aspect.EARTH, 5).add(Aspect.PLANT, 2));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.FERTILE_SOIL_TILLED), (new AspectList()).add(Aspect.WATER, 2).add(Aspect.AIR, 2));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.QUERN), (new AspectList()).add(Aspect.EARTH, 21).add(Aspect.MECHANISM, 4));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.SARCOPHAGUS), (new AspectList()).add(Aspect.EARTH, 30).add(Aspect.DEATH, 10).add(Aspect.SOUL, 2).add(Aspect.UNDEAD, 6));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.COAL_ORE), (new AspectList()).add(Aspect.ENERGY, 15).add(Aspect.FIRE, 15).add(Aspect.EARTH, 5));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.BONE_ORE), (new AspectList()).add(Aspect.DEATH, 5).add(Aspect.LIFE, 2).add(Aspect.EARTH, 5));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.RELIC_ORE), (new AspectList()).add(Aspect.DESIRE, 3).add(Aspect.METAL, 2).add(Aspect.EARTH, 5));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.KHNUMITE_RAW), (new AspectList()).add(Aspect.WATER, 10).add(Aspect.EARTH, 18).add(Aspect.MOTION, 5));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.BONE_DIRTY), (new AspectList()).add(Aspect.SENSES, 30).add(Aspect.EARTH, 6));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.BONE_DIRTY_SLAB), (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.EARTH, 2));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.LIMESTONE_FURNACE), (new AspectList()).add(Aspect.EARTH, 30).add(Aspect.FIRE, 10).add(Aspect.ENTROPY, 6));
        event.register.registerObjectTag(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CRACKED)), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.ENTROPY, 1));
        event.register.registerObjectTag(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CHISELED)), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.AIR, 1));
        event.register.registerObjectTag(new ItemStack(BlockLimestoneBricks.getBrick(BlockLimestoneBricks.BrickType.CARVED)), (new AspectList()).add(Aspect.EARTH, 3).add(Aspect.CRAFT, 1));
        event.register.registerObjectTag(new ItemStack(BlockLimestoneSlab.getSlab(BlockLimestoneBricks.BrickType.SMALL)), (new AspectList()).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.MARL), (new AspectList()).add(Aspect.EARTH, 16).add(Aspect.WATER, 4).add(Aspect.ENTROPY, 2));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.ALABASTER), (new AspectList()).add(Aspect.EARTH, 5).add(Aspect.AIR, 1));
        event.register.registerObjectTag(new ItemStack(BlockAlabasterBricks.getBrick(BlockAlabasterBricks.Type.TILED)), (new AspectList()).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(BlockAlabasterBricks.getBrick(BlockAlabasterBricks.Type.PILLAR)), (new AspectList()).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.PORPHYRY), (new AspectList()).add(Aspect.EARTH, 5).add(Aspect.ENTROPY, 1));
        event.register.registerObjectTag(new ItemStack(BlockPorphyryBricks.getBrick(BlockAlabasterBricks.Type.TILED)), (new AspectList()).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(BlockPorphyryBricks.getBrick(BlockAlabasterBricks.Type.PILLAR)), (new AspectList()).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.DEADWOOD_LOG), (new AspectList()).add(Aspect.PLANT, 8).add(Aspect.ENTROPY, 6));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.DEADWOOD_BRANCH), (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.ENTROPY, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.KHNUMITE), (new AspectList()).add(Aspect.WATER, 3).add(Aspect.EARTH, 7).add(Aspect.MOTION, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.DIRTY_COIN), (new AspectList()).add(Aspect.METAL, 1).add(Aspect.DESIRE, 1).add(Aspect.EARTH, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.GOLD_COIN), (new AspectList()).add(Aspect.METAL, 1).add(Aspect.DESIRE, 4).add(Aspect.EXCHANGE, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.IDOL_OF_LABOR), (new AspectList()).add(Aspect.ENTROPY, 8).add(Aspect.METAL, 1).add(Aspect.COLD, 2).add(Aspect.DEATH, 8).add(Aspect.MECHANISM, 3).add(Aspect.TRAP, 10));
        event.register.registerObjectTag(new ItemStack(AtumBlocks.HEART_OF_RA), (new AspectList()).add(Aspect.MAGIC, 8).add(Aspect.FIRE, 25).add(Aspect.SENSES, 3).add(Aspect.MOTION, 5).add(Aspect.ENERGY, 8));
        event.register.registerObjectTag(new ItemStack(AtumItems.SCRAP), (new AspectList()).add(Aspect.BEAST, 1).add(Aspect.CRAFT, 3));
        event.register.registerObjectTag(new ItemStack(AtumItems.DATE), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.LIFE, 6));
        event.register.registerObjectTag(new ItemStack(AtumItems.BRIGAND_SHIELD), (new AspectList()).add(Aspect.PROTECT, 15).add(Aspect.PLANT, 5).add(Aspect.METAL, 3));
        event.register.registerObjectTag(new ItemStack(AtumItems.PAPYRUS_PLANT), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.WATER, 2).add(Aspect.MIND, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.ECTOPLASM), (new AspectList()).add(Aspect.DEATH, 4).add(Aspect.MIND, 3).add(Aspect.ELDRITCH, 1).add(Aspect.ALCHEMY, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.MANDIBLES), (new AspectList()).add(Aspect.SENSES, 6).add(Aspect.BEAST, 3).add(Aspect.DEATH, 6));
        event.register.registerObjectTag(new ItemStack(AtumItems.SCROLL), (new AspectList()).add(Aspect.MIND, 7).add(Aspect.PLANT, 3).add(Aspect.WATER, 4));
        event.register.registerObjectTag(new ItemStack(AtumItems.WOLF_PELT), (new AspectList()).add(Aspect.BEAST, 7).add(Aspect.PROTECT, 3));
        event.register.registerObjectTag(new ItemStack(AtumItems.FERTILE_SOIL_PILE), (new AspectList()).add(Aspect.EARTH, 2).add(Aspect.PLANT, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.FLAX_SEEDS), (new AspectList()).add(Aspect.PLANT, 6));
        event.register.registerObjectTag(new ItemStack(AtumItems.FLAX), (new AspectList()).add(Aspect.PLANT, 6).add(Aspect.CRAFT, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.EMMER_SEEDS), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.LIFE, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.EMMER), (new AspectList()).add(Aspect.PLANT, 4).add(Aspect.LIFE, 6));
        event.register.registerObjectTag(new ItemStack(AtumItems.EMMER_FLOUR), (new AspectList()).add(Aspect.PLANT, 2).add(Aspect.LIFE, 4).add(Aspect.CRAFT, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.EMMER_DOUGH), (new AspectList()).add(Aspect.WATER, 2).add(Aspect.LIFE, 2).add(Aspect.CRAFT, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.EMMER_BREAD), (new AspectList()).add(Aspect.PLANT, 6).add(Aspect.LIFE, 12));
        event.register.registerObjectTag(new ItemStack(AtumItems.FORSAKEN_FISH), (new AspectList()).add(Aspect.DEATH, 2).add(Aspect.BEAST, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.MUMMIFIED_FISH), (new AspectList()).add(Aspect.CRAFT, 1).add(Aspect.BEAST, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.JEWELED_FISH), (new AspectList()).add(Aspect.CRYSTAL, 1).add(Aspect.DESIRE, 1).add(Aspect.BEAST, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.SKELETAL_FISH), (new AspectList()).add(Aspect.DEATH, 5).add(Aspect.LIFE, 5).add(Aspect.BEAST, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.CRUNCHY_SCARAB), (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 1).add(Aspect.LIFE, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.CRUNCHY_GOLD_SCARAB), (new AspectList()).add(Aspect.BEAST, 3).add(Aspect.EARTH, 1).add(Aspect.LIFE, 1).add(Aspect.DESIRE, 16));

        //Temporary until Godforge is added, if we can make aspects get added depending on recipe inputs
        event.register.registerObjectTag(new ItemStack(AtumItems.ATUMS_PROTECTION), (new AspectList()).add(Aspect.PROTECT, 15).add(Aspect.METAL, 8).add(Aspect.DESIRE, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.ATUMS_BOUNTY), (new AspectList()).add(Aspect.WATER, 8).add(Aspect.MAN, 4).add(Aspect.TOOL, 5).add(Aspect.DESIRE, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.ATUMS_HOMECOMING), (new AspectList()).add(Aspect.EXCHANGE, 6).add(Aspect.MOTION, 8).add(Aspect.MAGIC, 4));
        event.register.registerObjectTag(new ItemStack(AtumItems.TEFNUTS_CALL), (new AspectList()).add(Aspect.FIRE, 6).add(Aspect.MOTION, 4).add(Aspect.MAGIC, 2));
        event.register.registerObjectTag(new ItemStack(AtumItems.SHUS_SWIFTNESS), (new AspectList()).add(Aspect.AIR, 8).add(Aspect.MOTION, 12).add(Aspect.MAGIC, 1));
        event.register.registerObjectTag(new ItemStack(AtumItems.ISIS_HEALING), (new AspectList()).add(Aspect.LIFE, 12).add(Aspect.MAGIC, 6));
        event.register.registerObjectTag(new ItemStack(AtumItems.ANUBIS_MERCY), (new AspectList()).add(Aspect.DEATH, 18).add(Aspect.MAGIC, 5));
        event.register.registerObjectTag(new ItemStack(AtumItems.NUITS_VANISHING), (new AspectList()).add(Aspect.SENSES, 10).add(Aspect.MAGIC, 5).add(Aspect.EXCHANGE, 4).add(Aspect.MOTION, 2));

        AspectList lootAspects = new AspectList().add(Aspect.METAL, 1).add(Aspect.DESIRE, 1);
        for (ItemLoot.Type type : ItemLoot.Type.values()) {
            for (ItemLoot.Quality quality : ItemLoot.Quality.values()) {
                event.register.registerObjectTag(new ItemStack(ItemLoot.getLootItem(type, quality)), lootAspects);
            }
        }
    }
}