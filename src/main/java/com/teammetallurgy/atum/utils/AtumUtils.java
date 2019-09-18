package com.teammetallurgy.atum.utils;

import com.google.common.base.CaseFormat;
import net.minecraft.client.resources.I18n;

/**
 * Various string related helper methods
 */
public class AtumUtils {

    /**
     * Turns a unlocalized name into a registry name.
     * <p>
     * Example: Turns exampleName into example_name
     *
     * @param name the unlocalized name to convert into registry name.
     */
    public static String toRegistryName(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name).replace(".", "_");
    }

    /**
     * Turns a string into camel case
     *
     * @param name the string to turn into camel case
     * @return Camel case name
     */
    public static String toCamelCase(String name) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_CAMEL, name).replace("_", "");
    }

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