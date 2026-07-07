package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PGroup;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

/**
 * Subcommand handler for {@code /prefix chat <group> <prefix>}.
 * Sets the chat prefix for the specified group.
 */
@RequiredArgsConstructor
public class Chat implements SubCommand {
    private final Data data;


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, Messages.get("invalid-format-chat"));
            return;
        }
        if (!PGroup.isValidGroupName(args[0])) {
            Logger.sendMessage(sender, Messages.get("group-invalid-name"));
            return;
        }
        var prefix = new StringBuilder();
        for (int i = 1; i < args.length; ++i) {
            prefix.append(args[i]);
            if (i + 1 < args.length) {
                prefix.append(" ");
            }
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        pgroup.setChatPrefix(prefix.toString());
        Logger.sendMessage(sender, Messages.get("chat-prefix-changed", "{group}", pgroup.getName()));
    }
}

