package com.teammetallurgy.atum.utils;

import com.google.common.base.CaseFormat;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Various string related helper methods
 */
public class AtumUtils {

    /**
     * Turns a registry name into a unlocalized name.
     * Can also turn getUnlocalizedName() into a unlocalized name without mod prefix.
     * <p>
     * Example: Turns example_name into exampleName
     *
     * @param name the registry name to convert into the unlocalized name.
     */
    public static String toUnlocalizedName(String name) {
        return StringUtils.uncapitalize(WordUtils.capitalize(name, '_')).replace("_", "").replace(Constants.MOD_ID + ".", "_").replace("item.", "").replace("tile.", "").replace(":", ".");
    }

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
     * Mirror of {@link I18n#translateToLocal(String)}, to prevent warnings
     *
     * @param key the string to format
     */
    public static String format(String key) {
        return I18n.translateToLocal(key);
    }

    /**
     * Mirror of {@link I18n#canTranslate(String)}, to prevent warnings
     *
     * @param key the string to check
     */
    public static boolean hasKey(String key) {
        return I18n.canTranslate(key);
    }
}