package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PGroup;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

/**
 * Subcommand handler for {@code /prefix weight <group> <weight>}.
 * Sets the tab weight (sort order) for the specified group.
 */
@RequiredArgsConstructor
public class Weight implements SubCommand {
    private final Data data;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, Messages.get("invalid-format-weight"));
            return;
        }
        if (!PGroup.isValidGroupName(args[0])) {
            Logger.sendMessage(sender, Messages.get("group-invalid-name"));
            return;
        }
        int weight;
        try {
            weight = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            Logger.sendMessage(sender, Messages.get("invalid-number"));
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        pgroup.setTabWeight(weight);
        Logger.sendMessage(sender, Messages.get("weight-changed", "{group}", pgroup.getName()));
    }
}
