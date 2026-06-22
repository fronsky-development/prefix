package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.PrefixModule;

/**
 * Utility class for retrieving configurable messages from messages.yml.
 */
public class Messages {

    public static String get(String key) {
        var messages = PrefixModule.getData().getMessages().get();
        String msg = messages.getString(key);
        if (msg == null) return key;
        return ColorUtil.colorize(msg);
    }

    public static String get(String key, String placeholder, String value) {
        return get(key).replace(placeholder, value);
    }
}

