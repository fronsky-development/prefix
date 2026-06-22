package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PPlayer;
import nl.fronsky.prefix.module.models.Tablist;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Subcommand handler for {@code /prefix reload}.
 * Reloads all configuration files and refreshes the tablist.
 */
@RequiredArgsConstructor
public class Reload implements SubCommand {
    private final Data data;


    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            data.getGroups().reload();
            data.getPlayers().reload();
            data.getConfig().reload();
            data.getMessages().reload();
            for (Player player : Bukkit.getOnlinePlayers()) {
                Tablist.update(new PPlayer(player, data));
            }
            Logger.sendMessage(sender, Messages.get("reload-success"));
            Logger.info("Plugin reloaded by " + sender.getName());
        } catch (Exception e) {
            Logger.exception("Failed to reload plugin configurations", e);
            Logger.sendError(sender, Messages.get("reload-fail"));
        }
    }
}

