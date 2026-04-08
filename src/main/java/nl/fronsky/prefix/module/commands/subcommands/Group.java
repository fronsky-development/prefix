package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Group implements SubCommand {
    private final Data data;

    public Group(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            if (args.length == 1 && sender instanceof Player player) {
                var pplayer = new PPlayer(player, data);
                if (pplayer.setGroup(args[0])) {
                    Logger.sendMessage(sender, "&aSuccessfully changed group for '" + pplayer.getPlayer().getDisplayName() + "'.");
                } else {
                    Logger.sendMessage(sender, "&cGroup '" + args[0] + "' does not exist.");
                }
                return;
            }
            Logger.sendMessage(sender, "&cIncorrect format. Usage: /prefix group <player or group> <group or empty>");
        } else {
            var player = Bukkit.getPlayer(args[0]);
            if (player == null) {
                Logger.sendMessage(sender, "&cPlayer not found. Please ensure that the player is online.");
                return;
            }
            var pplayer = new PPlayer(player, data);
            if (pplayer.setGroup(args[1])) {
                Logger.sendMessage(sender, "&aSuccessfully changed group for '" + pplayer.getPlayer().getDisplayName() + "'.");
            } else {
                Logger.sendMessage(sender, "&cGroup '" + args[1] + "' does not exist.");
            }
        }
    }
}
