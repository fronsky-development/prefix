package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import org.bukkit.command.CommandSender;

public class ChatNameColor implements SubCommand {
    private final Data data;

    public ChatNameColor(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, "&cInvalid command format. Usage: /prefix chatnamecolor <group> <color>");
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        var colorArg = args[1];
        if (colorArg.length() < 2) {
            Logger.sendMessage(sender, "&cInvalid color code. Usage: &<code> (e.g. &a, &6, &f)");
            return;
        }
        pgroup.setChatNameColor(ColorUtil.getChatColor(colorArg.substring(1, 2)));
        Logger.sendMessage(sender, "&aSuccessfully changed chat name color for group '" + pgroup.getName() + "'.");
    }
}

