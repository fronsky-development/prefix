package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PGroup;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

/**
 * Subcommand handler for {@code /prefix chatnamecolor <group> <color>}.
 * Sets the chat name color for the specified group.
 */
@RequiredArgsConstructor
public class ChatNameColor implements SubCommand {
    private final Data data;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, Messages.get("invalid-format-chatnamecolor"));
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        var colorArg = args[1];
        if (colorArg.length() < 2) {
            Logger.sendMessage(sender, "&cInvalid color code. Usage: &<code> (e.g. &a, &6, &f)");
            return;
        }
        pgroup.setChatNameColor(ColorUtil.getChatColor(colorArg.substring(1, 2)));
        Logger.sendMessage(sender, Messages.get("chat-name-color-changed", "{group}", pgroup.getName()));
    }
}
