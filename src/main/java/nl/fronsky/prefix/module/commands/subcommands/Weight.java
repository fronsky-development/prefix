package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import org.bukkit.command.CommandSender;

/**
 * Subcommand handler for {@code /prefix weight <group> <weight>}.
 * Sets the tab weight (sort order) for the specified group.
 */
public class Weight implements SubCommand {
    private final Data data;

    public Weight(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, "&cInvalid command format. Usage: /prefix weight <group> <weight>");
            return;
        }
        int weight;
        try {
            weight = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            Logger.sendMessage(sender, "&cInvalid number format. Please enter a valid integer.");
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        pgroup.setTabWeight(weight);
        Logger.sendMessage(sender, "&aSuccessfully changed tab weight for group '" + pgroup.getName() + "'.");
    }
}

