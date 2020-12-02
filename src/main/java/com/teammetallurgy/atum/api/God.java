package com.teammetallurgy.atum.api;

import com.google.common.collect.Maps;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.Map;

public enum God implements IStringSerializable {
    ANPUT("anput", TextFormatting.BLACK),
    ANUBIS("anubis", TextFormatting.DARK_PURPLE),
    ATEM("atem", TextFormatting.DARK_AQUA),
    GEB("geb", TextFormatting.GOLD),
    HORUS("horus", TextFormatting.AQUA),
    ISIS("isis", TextFormatting.LIGHT_PURPLE),
    MONTU("montu", TextFormatting.DARK_RED),
    NEPTHYS("nepthys", TextFormatting.DARK_PURPLE),
    NUIT("nuit", TextFormatting.GRAY),
    OSIRIS("osiris", TextFormatting.DARK_GREEN),
    PTAH("ptah", TextFormatting.YELLOW),
    RA("ra", TextFormatting.DARK_RED),
    SETH("seth", TextFormatting.GREEN),
    SHU("shu", TextFormatting.BLUE),
    TEFNUT("tefnut", TextFormatting.DARK_BLUE);

    static Map<Integer, God> MAP;
    private final String name;
    private final TextFormatting color;

    God(String name, TextFormatting color) {
        this.name = name;
        this.color = color;
    }

    @Override
    @Nonnull
    public String getString() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public TextFormatting getColor() {
        return this.color;
    }

    public static God getGod(int godType) {
        if (MAP == null) {
            MAP = Maps.newHashMap();
            for (God g : God.values()) {
                MAP.put(g.ordinal(), g);
            }
        }
        God god = MAP.get(godType);
        return god == null ? ANPUT : god;
    }
}