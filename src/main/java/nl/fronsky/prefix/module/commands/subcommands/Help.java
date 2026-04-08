package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.commands.help.HelpMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Help implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Logger.sendMessage(sender, "&8<--------------- &cPrefix Help &8--------------->");
        Logger.sendMessage(sender, "&fAliases: &7&o/p");
        Logger.sendMessage(sender, "&fCommands: ");
        if (sender instanceof Player player) {
            HelpMessage.sendAll(player);
        } else {
            Logger.sendMessage(sender, "&7- /prefix");
            Logger.sendMessage(sender, "&7- /prefix <player>");
            Logger.sendMessage(sender, "&7- /prefix help");
            Logger.sendMessage(sender, "&7- /prefix chat <group> <prefix>");
            Logger.sendMessage(sender, "&7- /prefix tab <group> <prefix>");
            Logger.sendMessage(sender, "&7- /prefix chatnamecolor <group> <color>");
            Logger.sendMessage(sender, "&7- /prefix tabnamecolor <group> <color>");
            Logger.sendMessage(sender, "&7- /prefix chatcolor <group> <color>");
            Logger.sendMessage(sender, "&7- /prefix weight <group> <weight>");
            Logger.sendMessage(sender, "&7- /prefix group <player or group> <group or empty>");
            Logger.sendMessage(sender, "&7- /prefix reload");
        }
        Logger.sendMessage(sender, "&8<----------------------------------------->");
    }
}

