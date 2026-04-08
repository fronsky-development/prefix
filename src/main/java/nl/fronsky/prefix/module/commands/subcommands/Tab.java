package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import nl.fronsky.prefix.module.models.PPlayer;
import nl.fronsky.prefix.module.models.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Tab implements SubCommand {
    private final Data data;

    public Tab(Data data) {
        this.data = data;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length < 2) {
            Logger.sendMessage(sender, "&cInvalid command format. Usage: /prefix tab <group> <prefix>");
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
        pgroup.setTabPrefix(prefix.toString());
        Logger.sendMessage(sender, "&aSuccessfully changed tab prefix for group '" + pgroup.getName() + "'.");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }
}

