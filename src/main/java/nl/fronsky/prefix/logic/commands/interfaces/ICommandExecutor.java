package nl.fronsky.prefix.logic.commands.interfaces;

import org.bukkit.command.CommandSender;

public interface ICommandExecutor {
    /**
     * Executes the given command
     *
     * @param sender Source of the command
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     */
    void onCommand(CommandSender sender, String label, String[] args);
}
