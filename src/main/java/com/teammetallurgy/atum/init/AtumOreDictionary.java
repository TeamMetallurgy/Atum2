package com.teammetallurgy.atum.init;

import com.teammetallurgy.atum.blocks.BlockAtumLog;
import com.teammetallurgy.atum.blocks.BlockAtumPlank;
import com.teammetallurgy.atum.blocks.BlockAtumSapling;
import com.teammetallurgy.atum.blocks.BlockLeave;
import net.minecraftforge.oredict.OreDictionary;

public class AtumOreDictionary { //Temporary
    public static void init() {
        OreDictionary.registerOre("blockLimestone", AtumBlocks.LIMESTONE);

        for (BlockAtumPlank.WoodType type : BlockAtumPlank.WoodType.values()) {
            OreDictionary.registerOre("logWood", BlockAtumLog.getLog(type));
            OreDictionary.registerOre("plankWood", BlockAtumPlank.getPlank(type));
            OreDictionary.registerOre("treeLeaves", BlockLeave.getLeave(type));
            OreDictionary.registerOre("treeSapling", BlockAtumSapling.getSapling(type));
        }
        OreDictionary.registerOre("oreGold", AtumBlocks.GOLD_ORE);
        OreDictionary.registerOre("oreIron", AtumBlocks.IRON_ORE);
        OreDictionary.registerOre("oreLapis", AtumBlocks.LAPIS_ORE);
        OreDictionary.registerOre("oreDiamond", AtumBlocks.DIAMOND_ORE);
        OreDictionary.registerOre("oreRedstone", AtumBlocks.REDSTONE_ORE);
        OreDictionary.registerOre("oreCoal", AtumBlocks.COAL_ORE);

        OreDictionary.registerOre("blockGlass", AtumBlocks.CRYSTAL_GLASS);
        /*OreDictionary.registerOre("blockGlass", new ItemStack(AtumBlocks.CRYSTAL_STAINED_GLASS, 1, OreDictionary.WILDCARD_VALUE));

        OreDictionary.registerOre("paneGlassColorless", AtumBlocks.THIN_CRYSTAL_GLASS);
        OreDictionary.registerOre("paneGlass", AtumBlocks.THIN_CRYSTAL_GLASS);
        OreDictionary.registerOre("paneGlass", new ItemStack(AtumBlocks.FRAMED_STAINED_GLASS, 1, OreDictionary.WILDCARD_VALUE));

        EnumDyeColor[] oreColours = EnumDyeColor.values();
        for (int i = 0; i < oreColours.length; i++) {
            ItemStack glass = new ItemStack(AtumBlocks.CRYSTAL_STAINED_GLASS, 1, i);
            ItemStack pane = new ItemStack(AtumBlocks.FRAMED_STAINED_GLASS, 1, i);

            OreDictionary.registerOre("blockGlass" + WordUtils.capitalize(oreColours[i].getUnlocalizedName()), glass);
            OreDictionary.registerOre("paneGlass" + WordUtils.capitalize(oreColours[i].getUnlocalizedName()), pane);
        }*/
    }
}