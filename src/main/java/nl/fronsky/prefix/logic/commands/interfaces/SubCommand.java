package nl.fronsky.prefix.logic.commands.interfaces;

import org.bukkit.command.CommandSender;

/**
 * Interface for subcommand execution.
 * Implementations should perform their logic in the {@link #execute} method,
 * not in the constructor.
 */
public interface SubCommand {
    /**
     * Executes the subcommand.
     *
     * @param sender the command sender
     * @param args   the arguments passed to the subcommand
     */
    void execute(CommandSender sender, String[] args);
}

