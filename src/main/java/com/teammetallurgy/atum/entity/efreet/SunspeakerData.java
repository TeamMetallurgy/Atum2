package com.teammetallurgy.atum.entity.efreet;

import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SunspeakerData {
    private static final int[] LIST = new int[]{0, 10, 70, 150, 250};
    private final int level;

    public SunspeakerData(int level) {
        this.level = Math.max(1, level);
    }

    public SunspeakerData(Dynamic<?> dynamic) {
        this(dynamic.get("level").asInt(1));
    }

    public int getLevel() {
        return this.level;
    }

    public SunspeakerData withLevel(int level) {
        return new SunspeakerData(level);
    }

    public <T> T serialize(DynamicOps<T> dynamicOps) {
        return dynamicOps.createInt(this.level);
    }

    @OnlyIn(Dist.CLIENT)
    public static int checkA(int i) { //func_221133_b
        return checkC(i) ? LIST[i - 1] : 0;
    }

    public static int checkB(int i) { //func_221127_c
        return checkC(i) ? LIST[i] : 0;
    }

    public static boolean checkC(int i) {
        return i >= 1 && i < 5;
    }
}