package com.teammetallurgy.atum.utils;

import com.mojang.brigadier.context.CommandContext;
import com.teammetallurgy.atum.Atum;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LootTableGenerator {
    private static final String LOOT_TABLE_BLOCK_DROP_SELF = "{\"type\":\"minecraft:block\",\"pools\":[{\"rolls\":1,\"entries\":[{\"type\":\"minecraft:item\",\"name\":\"%output%\"}],\"conditions\":[{\"condition\":\"minecraft:survives_explosion\"}]}]}";
    private static final File OUTPUT_DIR = new File("dump/atum");

    public static int validateLootTables() {
        final String modId = "atum";
        int foundIssues = 0;

        for (final Block block : ForgeRegistries.BLOCKS) {
            if ((block.getRegistryName().getNamespace().equals(modId))) {
                final ResourceLocation id = block.getRegistryName();
                final File outputFile = new File(OUTPUT_DIR, "data/" + id.getNamespace() + "/loot_tables/blocks/" + id.getPath() + ".json");

                try {
                    FileUtils.forceMkdir(outputFile.getParentFile());
                    try (FileWriter writer = new FileWriter(outputFile)) {
                        writer.write(LOOT_TABLE_BLOCK_DROP_SELF.replace("%output%", id.toString()));
                    }
                } catch (final IOException e) {
                    Atum.LOG.error("Failed to write dummy block loot table.", e);
                }
                foundIssues++;
            }
        }
        Atum.LOG.warn(new TranslationTextComponent("commands.bookshelf.loot_tables", foundIssues, modId));
        return 0;
    }
}