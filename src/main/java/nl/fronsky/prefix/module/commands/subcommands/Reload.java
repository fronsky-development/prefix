package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PPlayer;
import nl.fronsky.prefix.module.models.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload implements SubCommand {
    private final Data data;

    public Reload(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        try {
            data.getGroups().reload();
            data.getPlayers().reload();
            for (Player player : Bukkit.getOnlinePlayers()) {
                Tablist.update(new PPlayer(player, data));
            }
            Logger.sendMessage(sender, "&aPrefix has been reloaded successfully.");
            Logger.info("Plugin reloaded by " + sender.getName());
        } catch (Exception e) {
            Logger.exception("Failed to reload plugin configurations", e);
            Logger.sendError(sender, "&cFailed to reload plugin. Check console for details.");
        }
    }
}

