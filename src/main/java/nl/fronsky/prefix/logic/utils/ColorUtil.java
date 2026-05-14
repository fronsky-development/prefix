package nl.fronsky.prefix.logic.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorUtil {
    /**
     * Converts RGB values to a Color object.
     *
     * @param r The red component, an integer between 0 and 255.
     * @param g The green component, an integer between 0 and 255.
     * @param b The blue component, an integer between 0 and 255.
     * @return Returns a Color object corresponding to the specified RGB values.
     */
    public static Color rgbToColor(int r, int g, int b) {
        return Color.fromRGB(r, g, b);
    }

    /**
     * Converts a Color object to an array of RGB values.
     *
     * @param color The Color object to be converted.
     * @return Returns an integer array where the first element is the red component, the second element is the green component, and the third element is the blue component.
     */
    public static int[] colorToRGB(Color color) {
        return new int[] { color.getRed(), color.getGreen(), color.getBlue() };
    }

    /**
     * Converts a Color object to its hexadecimal representation.
     *
     * @param color The Color object to be converted.
     * @return Returns a String containing the hexadecimal representation of the color, in the format "#RRGGBB".
     */
    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    /**
     * Translates alternate color codes in the given message string.
     *
     * @param message The string containing the message with alternate color codes, typically using the '&' character.
     * @return Returns a string where the alternate color codes have been replaced by ChatColor codes.
     */
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Translates alternate color codes in the given message string using a specified alternate color character.
     *
     * @param message The string containing the message with alternate color codes.
     * @param altColorChar The alternate color character used for translating color codes.
     * @return Returns a string where the alternate color codes have been replaced by ChatColor codes.
     */
    public static String colorize(String message, char altColorChar) {
        return ChatColor.translateAlternateColorCodes(altColorChar, message);
    }

    /**
     * Strips color codes from the given message string.
     *
     * @param message The string containing the message with ChatColor codes.
     * @return Returns a string where all ChatColor codes have been removed.
     */
    public static String decolorize(String message) {
        return ChatColor.stripColor(message);
    }

    /**
     * Retrieves the ChatColor associated with a specific code.
     * <p>
     * This method searches for a ChatColor constant that is mapped to the given code.
     * If the code does not correspond to any ChatColor, it defaults to returning ChatColor.WHITE.
     *
     * @param code the character code that represents a ChatColor
     * @return the ChatColor associated with the given code, or ChatColor.WHITE if no match is found
     */
    public static ChatColor getChatColor(String code) {
        ChatColor chatColor = ChatColor.getByChar(code);
        if (chatColor == null) {
            return ChatColor.WHITE;
        }
        return chatColor;
    }
}
