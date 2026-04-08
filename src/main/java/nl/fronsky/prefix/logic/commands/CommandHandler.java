package nl.fronsky.prefix.logic.commands;

import nl.fronsky.prefix.logic.commands.annotations.CommandClass;
import nl.fronsky.prefix.logic.commands.annotations.SubCommandMethod;
import nl.fronsky.prefix.logic.commands.interfaces.ICommandExecutor;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.Language;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CommandHandler implements TabCompleter, CommandExecutor, ICommandExecutor {
    @Getter private final String name;
    @Getter private final String permission;
    @Getter private final List<String> subcommands;
    private final boolean isValid;

    protected CommandHandler() {
        subcommands = new ArrayList<>();
        isValid = getClass().isAnnotationPresent(CommandClass.class);
        if (!isValid) {
            name = "invalid";
            permission = "invalid";
            return;
        }

        var commandClass = getClass().getAnnotation(CommandClass.class);
        name = commandClass.name();
        permission = commandClass.permission();

        for (Method method : getClass().getMethods()) {
            if (method.isAnnotationPresent(SubCommandMethod.class)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 3) continue;
                if (!parameterTypes[0].equals(CommandSender.class)) continue;
                if (!parameterTypes[1].equals(String.class)) continue;
                if (!parameterTypes[2].equals(String[].class)) continue;
                subcommands.add(method.getName());
            }
        }
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String[] args) {
        if (!isValid) return true;

        // BUG-008: Use local variable instead of instance field to prevent race condition
        Player player = (sender instanceof Player p) ? p : null;

        if (!subcommands.isEmpty()) {
            String subcommand = getSubcommand(args);
            if (!subcommand.isEmpty() && hasPermission(player, permission + "." + subcommand.toLowerCase())) {
                try {
                    Method method = this.getClass().getMethod(subcommand, CommandSender.class, String.class, String[].class);
                    method.invoke(this, sender, label, getSubcommandArgs(args));
                    return true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
                    Logger.severe("An error occurred while invoking subcommand method.", exception);
                }
            }
        }

        if (!hasPermission(player, permission)) return true;
        onCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, @NonNull String[] args) {
        if (!isValid) return new ArrayList<>();

        var completions = new ArrayList<String>();
        Player player = (sender instanceof Player p) ? p : null;

        if (args.length == 1) {
            subcommands.stream()
                    .filter(sub -> sub.startsWith(args[0]) && hasPermission(player, permission + "." + sub))
                    .forEach(completions::add);
        }
        return completions;
    }

    /**
     * Checks if the given player has the specified permission.
     *
     * @param player     the player to check, or null for console (always permitted)
     * @param permission the permission string to check
     * @return true if permitted, false otherwise
     */
    protected boolean hasPermission(Player player, String permission) {
        if (player == null) {
            return true;
        }
        if (permission.isEmpty()) {
            Logger.severe("Permissions haven't been set. Make sure to initialize them correctly.");
            return false;
        }
        if (!player.hasPermission(permission)) {
            player.sendMessage(Language.NO_PERMISSION.getMessageWithColor());
            return false;
        }
        return true;
    }

    private String getSubcommand(String[] args) {
        if (args.length == 0 || subcommands.isEmpty()) {
            return "";
        }
        for (String subcommand : subcommands) {
            if (subcommand.equalsIgnoreCase(args[0])) {
                return subcommand;
            }
        }
        return "";
    }

    private String[] getSubcommandArgs(String[] args) {
        if (args.length <= 1) {
            return new String[0];
        }
        return Arrays.copyOfRange(args, 1, args.length);
    }
}
