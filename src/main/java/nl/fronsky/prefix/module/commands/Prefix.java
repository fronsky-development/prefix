package nl.fronsky.prefix.module.commands;

import nl.fronsky.prefix.logic.commands.CommandHandler;
import nl.fronsky.prefix.logic.commands.annotations.CommandClass;
import nl.fronsky.prefix.logic.commands.annotations.SubCommandMethod;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.PrefixModule;
import nl.fronsky.prefix.module.commands.subcommands.*;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import nl.fronsky.prefix.module.models.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandClass(name = "prefix", permission = "prefix.command.prefix")
public class Prefix extends CommandHandler {
    private final Data data;
    private final Chat chatCmd;
    private final Tab tabCmd;
    private final ChatNameColor chatNameColorCmd;
    private final TabNameColor tabNameColorCmd;
    private final ChatColorCmd chatColorCmd;
    private final Weight weightCmd;
    private final Group groupCmd;
    private final Reload reloadCmd;
    private final Help helpCmd;
    private final Info infoCmd;

    public Prefix() {
        data = PrefixModule.getData();
        chatCmd = new Chat(data);
        tabCmd = new Tab(data);
        chatNameColorCmd = new ChatNameColor(data);
        tabNameColorCmd = new TabNameColor(data);
        chatColorCmd = new ChatColorCmd(data);
        weightCmd = new Weight(data);
        groupCmd = new Group(data);
        reloadCmd = new Reload(data);
        helpCmd = new Help();
        infoCmd = new Info();
    }

    @Override
    public void onCommand(CommandSender sender, String label, String[] args) {
        if (args.length > 0) {
            var target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                if (sender instanceof Player p && !hasPermission(p, "prefix.command.prefix.others")) {
                    return;
                }
                others(sender, target);
            } else {
                Logger.sendMessage(sender, "&cPlayer not found. Please ensure that the player is online.");
            }
            return;
        }
        if (!(sender instanceof Player player)) {
            Logger.sendMessage(sender, "&cThis command can only be executed by players.");
            return;
        }
        var pplayer = new PPlayer(player, data);
        var result = pplayer.getGroup();
        if (!result.isSuccess()) {
            Logger.warning(result.exception().getMessage());
            Logger.sendWarning(sender, "&cIt seems you are not part of a recognized group, please contact an administrator.");
            return;
        }
        displayPrefixInfo(sender, pplayer, result.value());
    }

    private void others(CommandSender sender, Player target) {
        var pplayer = new PPlayer(target, data);
        var result = pplayer.getGroup();
        if (!result.isSuccess()) {
            Logger.warning(result.exception().getMessage());
            Logger.sendWarning(sender, "&cIt seems that this player is not part of a recognized group.");
            return;
        }
        displayPrefixInfo(sender, pplayer, result.value());
    }

    private void displayPrefixInfo(CommandSender sender, PPlayer pplayer, PGroup pgroup) {
        Logger.sendMessage(sender, "&8<----------------- &cPrefix &8----------------->");
        Logger.sendMessage(sender, "&fPlayer: &7" + pplayer.getPlayer().getDisplayName());
        Logger.sendMessage(sender, "&fChat Prefix: " + pgroup.getChatPrefix());
        Logger.sendMessage(sender, "&fChat Name Color: &" + pgroup.getChatNameColor().getChar() + pgroup.getChatNameColor().name());
        Logger.sendMessage(sender, "&fChat Color: &" + pgroup.getChatColor().getChar() + pgroup.getChatColor().name());
        Logger.sendMessage(sender, "&fTab Prefix: " + pgroup.getTabPrefix());
        Logger.sendMessage(sender, "&fTab Name Color: &" + pgroup.getTabNameColor().getChar() + pgroup.getTabNameColor().name());
        Logger.sendMessage(sender, "&fTab Weight: &7" + pgroup.getTabWeight());
        Logger.sendMessage(sender, "&fGroup: &7&n" + pgroup.getName());
        Logger.sendMessage(sender, "&8<----------------------------------------->");
    }

    @SubCommandMethod
    public void help(CommandSender sender, String label, String[] args) {
        helpCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void info(CommandSender sender, String label, String[] args) {
        infoCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void chat(CommandSender sender, String label, String[] args) {
        chatCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void tab(CommandSender sender, String label, String[] args) {
        tabCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void chatnamecolor(CommandSender sender, String label, String[] args) {
        chatNameColorCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void tabnamecolor(CommandSender sender, String label, String[] args) {
        tabNameColorCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void chatcolor(CommandSender sender, String label, String[] args) {
        chatColorCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void weight(CommandSender sender, String label, String[] args) {
        weightCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void group(CommandSender sender, String label, String[] args) {
        groupCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void reload(CommandSender sender, String label, String[] args) {
        reloadCmd.execute(sender, args);
    }
}
