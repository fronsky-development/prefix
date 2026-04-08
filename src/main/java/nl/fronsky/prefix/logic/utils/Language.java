package nl.fronsky.prefix.logic.utils;

import lombok.Getter;

@Getter
public enum Language {
    DEFAULT("Please choose a language message. The current message is a default message."),
    NO_PERMISSION("&cYou do not have permissions to perform this action. Please contact your system administrator for assistance.");

    private final String message;

    Language(String message) {
        this.message = message;
    }

    /**
     * Returns the message string after applying colorization.
     *
     * @return a colorized version of the message string using ChatColor codes
     */
    public String getMessageWithColor() {
        return ColorUtil.colorize(message);
    }

    /**
     * Gets the Language enum constant corresponding to the given name.
     * If the name doesn't match any, returns the DEFAULT language.
     *
     * @param name the name of the Language enum constant
     * @return the matching Language constant, or Language.DEFAULT if no match
     */
    public static Language getLanguage(String name) {
        for (Language lang : Language.values()) {
            if (lang.name().equalsIgnoreCase(name)) {
                return lang;
            }
        }
        return Language.DEFAULT;
    }
}
