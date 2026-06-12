package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PGroup;
import nl.fronsky.prefix.module.models.PPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Subcommand handler for {@code /prefix group <player or group> <group or empty>}.
 * Assigns a player to a prefix group.
 */
@RequiredArgsConstructor
public class Group implements SubCommand {
    private final Data data;


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            if (args.length == 1 && sender instanceof Player player) {
                if (!PGroup.isValidGroupName(args[0])) {
                    Logger.sendMessage(sender, Messages.get("group-invalid-name"));
                    return;
                }
                var pplayer = new PPlayer(player, data);
                if (pplayer.setGroup(args[0])) {
                    Logger.sendMessage(sender, Messages.get("group-changed", "{player}", pplayer.getPlayer().getDisplayName()));
                } else {
                    Logger.sendMessage(sender, Messages.get("group-not-found", "{group}", args[0]));
                }
                return;
            }
            Logger.sendMessage(sender, Messages.get("invalid-format-group"));
        } else {
            if (!PGroup.isValidGroupName(args[1])) {
                Logger.sendMessage(sender, Messages.get("group-invalid-name"));
                return;
            }
            var player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Logger.sendMessage(sender, Messages.get("player-not-found"));
                return;
            }
            var pplayer = new PPlayer(player, data);
            if (pplayer.setGroup(args[1])) {
                Logger.sendMessage(sender, Messages.get("group-changed", "{player}", pplayer.getPlayer().getDisplayName()));
            } else {
                Logger.sendMessage(sender, Messages.get("group-not-found", "{group}", args[1]));
            }
        }
    }
}
