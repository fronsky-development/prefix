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
 * Subcommand handler for {@code /prefix chatcolor <group> <color>}.
 * Sets the chat message color for the specified group.
 */
@RequiredArgsConstructor
public class ChatColorCmd implements SubCommand {
    private final Data data;

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, Messages.get("invalid-format-chatcolor"));
            return;
        }
        if (!PGroup.isValidGroupName(args[0])) {
            Logger.sendMessage(sender, Messages.get("group-invalid-name"));
            return;
        }
        var color = ColorUtil.parseColorCode(args[1]);
        if (color == null) {
            Logger.sendMessage(sender, Messages.get("invalid-color"));
            return;
        }
        var pgroup = PGroup.loadOrCreate(args[0], data);
        pgroup.setChatColor(color);
        Logger.sendMessage(sender, Messages.get("chat-color-changed", "{group}", pgroup.getName()));
    }
}
