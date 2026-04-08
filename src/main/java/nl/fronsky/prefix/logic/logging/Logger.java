package nl.fronsky.prefix.logic.logging;

import nl.fronsky.prefix.Main;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

/**
 * Centralized logging utility that delegates to the plugin's logger
 * for proper [Prefix] tagging in console output.
 * <p>
 * All messages are automatically sanitized (color codes stripped) before
 * being written to the console log for readability.
 */
public class Logger {
    private static boolean debugEnabled = false;

    private static java.util.logging.Logger getLogger() {
        var plugin = Main.getInstance();
        if (plugin != null) {
            return plugin.getLogger();
        }
        return java.util.logging.Logger.getLogger("Prefix");
    }

    /**
     * Checks if debug logging is enabled.
     *
     * @return {@code true} if debug logging is enabled, {@code false} otherwise
     */
    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    /**
     * Enables or disables debug logging.
     *
     * @param enabled {@code true} to enable debug logging, {@code false} to disable
     */
    public static void setDebugEnabled(boolean enabled) {
        debugEnabled = enabled;
    }

    /**
     * Logs an informational message.
     *
     * @param message the message to be logged
     */
    public static void info(String message) {
        getLogger().log(Level.INFO, sanitize(message));
    }

    /**
     * Logs a debug message (only if debug mode is enabled).
     *
     * @param message the message to be logged
     */
    public static void debug(String message) {
        if (debugEnabled) {
            getLogger().log(Level.INFO, "[DEBUG] " + sanitize(message));
        }
    }

    /**
     * Logs a warning message.
     *
     * @param message the message to be logged
     */
    public static void warning(String message) {
        getLogger().log(Level.WARNING, sanitize(message));
    }

    /**
     * Logs a severe (error) message.
     *
     * @param message the message to be logged
     */
    public static void severe(String message) {
        getLogger().log(Level.SEVERE, sanitize(message));
    }

    /**
     * Logs a severe (error) message with exception details.
     *
     * @param message   the message to be logged
     * @param throwable the exception to log
     */
    public static void severe(String message, Throwable throwable) {
        getLogger().log(Level.SEVERE, sanitize(message), throwable);
    }

    /**
     * Logs an exception with its message as context.
     *
     * @param message   the context message to be logged
     * @param throwable the exception to log
     */
    public static void exception(String message, Throwable throwable) {
        getLogger().log(Level.SEVERE, sanitize(message) + ": " + (throwable != null ? throwable.getMessage() : ""), throwable);
    }

    /**
     * Sends a colored message to the sender. If the sender is a player, the message
     * is sent with color codes applied. If the sender is the console, the message
     * is logged via {@link #info(String)} with color codes stripped.
     *
     * @param sender  the command sender (player or console)
     * @param message the message with {@code &} color codes
     */
    public static void sendMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ColorUtil.colorize(message));
        } else {
            info(message);
        }
    }

    /**
     * Sends a colored warning message to the sender. If the sender is a player, the message
     * is sent with color codes applied. If the sender is the console, the message
     * is logged via {@link #warning(String)} with color codes stripped.
     *
     * @param sender  the command sender (player or console)
     * @param message the warning message with {@code &} color codes
     */
    public static void sendWarning(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ColorUtil.colorize(message));
        } else {
            warning(message);
        }
    }

    /**
     * Sends a colored error message to the sender. If the sender is a player, the message
     * is sent with color codes applied. If the sender is the console, the message
     * is logged via {@link #severe(String)} with color codes stripped.
     * <p>
     * Use this only for real errors (e.g. failed operations). For user input errors
     * (wrong format, player not found), use {@link #sendMessage} instead.
     *
     * @param sender  the command sender (player or console)
     * @param message the error message with {@code &} color codes
     */
    public static void sendError(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ColorUtil.colorize(message));
        } else {
            severe(message);
        }
    }

    private static String sanitize(String message) {
        if (message == null) {
            return "";
        }
        // First translate &-codes to §-codes, then strip all color codes
        return ColorUtil.decolorize(ColorUtil.colorize(message));
    }
}
