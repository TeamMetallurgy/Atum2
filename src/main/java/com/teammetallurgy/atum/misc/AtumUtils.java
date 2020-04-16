package com.teammetallurgy.atum.misc;

import net.minecraft.client.resources.I18n;

/**
 * Various string related helper methods
 */
public class AtumUtils {
    /**
     * Mirror of {@link I18n#format(String,Object...)}
     *
     * @param key the string to format
     */
    public static String format(String key, Object... objects) {
        return I18n.format(key, objects);
    }

    /**
     * Mirror of {@link I18n#hasKey(String)}
     *
     * @param key the string to check
     */
    public static boolean hasKey(String key) {
        return I18n.hasKey(key);
    }
}