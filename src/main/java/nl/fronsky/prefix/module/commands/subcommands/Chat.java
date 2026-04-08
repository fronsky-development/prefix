package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import org.bukkit.command.CommandSender;

public class Chat implements SubCommand {
    private final Data data;

    public Chat(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, "&cInvalid command format. Usage: /prefix chat <group> <prefix>");
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
        Logger.sendMessage(sender, "&aSuccessfully changed chat prefix for group '" + pgroup.getName() + "'.");
    }
}

