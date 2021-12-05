package com.teammetallurgy.atum.api;

import com.google.common.collect.Maps;
import net.minecraft.util.StringRepresentable;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;
import java.util.HashMap;

public enum God implements StringRepresentable {
    ANPUT("anput", "#46403F"),
    ANUBIS("anubis", "#780162"),
    ATEM("atem", "#229F89"),
    GEB("geb", "#c65e00"),
    HORUS("horus", "#6e60e6"),
    ISIS("isis", "#b853c6"),
    MONTU("montu", "#893c2e"),
    NEPTHYS("nepthys", "#9703c9"),
    NUIT("nuit", "#493e74"),
    OSIRIS("osiris", "#31ab1c"),
    PTAH("ptah", "#b39c05"),
    RA("ra", "#9d0d14"),
    SETH("seth", "#5a7F1a"),
    SHU("shu", "#a6b4bF"),
    TEFNUT("tefnut", "#1a70FF");

    static HashMap<String, God> GOD_BY_NAME;
    static HashMap<Integer, God> MAP;
    private final String name;
    private final TextColor color;
    private final String hex;

    God(String name, String hex) {
        this.name = name;
        this.hex = hex;
        this.color = TextColor.parseColor(hex);
    }

    @Override
    @Nonnull
    public String getSerializedName() {
        return this.getName();
    }

    public String getName() {
        return this.name;
    }

    public TranslatableComponent getDisplayName() {
        return new TranslatableComponent("atum.god." + this.name);
    }

    public TextColor getColor() {
        return this.color;
    }

    public String getHex() {
        return this.hex;
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

    public static God getGodByName(String name) {
        if (GOD_BY_NAME == null) {
            GOD_BY_NAME = Maps.newHashMap();
            for (God god : God.values()) {
                GOD_BY_NAME.put(god.getName(), god);
            }
        }
        God god = GOD_BY_NAME.get(name);
        return god == null ? ANPUT : god;
    }
}