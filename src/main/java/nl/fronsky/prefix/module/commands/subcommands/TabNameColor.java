package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import org.bukkit.command.CommandSender;

/**
 * Subcommand handler for {@code /prefix tabnamecolor <group> <color>}.
 * Sets the tab name color for the specified group.
 */
public class TabNameColor implements SubCommand {
    private final Data data;

    public TabNameColor(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, "&cInvalid command format. Usage: /prefix tabnamecolor <group> <color>");
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        var colorArg = args[1];
        if (colorArg.length() < 2) {
            Logger.sendMessage(sender, "&cInvalid color code. Usage: &<code> (e.g. &a, &6, &f)");
            return;
        }
        pgroup.setTabNameColor(ColorUtil.getChatColor(colorArg.substring(1, 2)));
        Logger.sendMessage(sender, "&aSuccessfully changed tab name color for group '" + pgroup.getName() + "'.");
    }
}

