package nl.fronsky.prefix.module.commands;

import lombok.NonNull;
import nl.fronsky.prefix.logic.commands.CommandHandler;
import nl.fronsky.prefix.logic.commands.annotations.CommandClass;
import nl.fronsky.prefix.logic.commands.annotations.SubCommandMethod;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.module.PrefixModule;
import nl.fronsky.prefix.module.commands.subcommands.*;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.Messages;
import nl.fronsky.prefix.module.models.PGroup;
import nl.fronsky.prefix.module.models.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Main command handler for the {@code /prefix} command.
 * Routes subcommands to their respective handlers and displays prefix info
 * when used without a subcommand.
 */
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
    private final Groups groupsCmd;
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
        groupsCmd = new Groups(data);
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
                Logger.sendMessage(sender, Messages.get("player-not-found"));
            }
            return;
        }
        if (!(sender instanceof Player player)) {
            Logger.sendMessage(sender, Messages.get("player-only"));
            return;
        }
        var pplayer = new PPlayer(player, data);
        var result = pplayer.getGroup();
        if (!result.isSuccess()) {
            Logger.warning(result.exception().getMessage());
            Logger.sendWarning(sender, Messages.get("group-not-exist"));
            return;
        }
        displayPrefixInfo(sender, pplayer, result.value());
    }

    private void others(CommandSender sender, Player target) {
        var pplayer = new PPlayer(target, data);
        var result = pplayer.getGroup();
        if (!result.isSuccess()) {
            Logger.warning(result.exception().getMessage());
            Logger.sendWarning(sender, Messages.get("group-not-exist-other"));
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

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, @NonNull String[] args) {
        var completions = super.onTabComplete(sender, command, alias, args);

        if (args.length >= 2) {
            Player player = (sender instanceof Player p) ? p : null;
            String sub = args[0].toLowerCase();
            if (!hasSilentPermission(player, getPermission() + "." + sub)) {
                return completions;
            }
            String input = args[args.length - 1].toLowerCase();

            if (args.length == 2) {
                switch (sub) {
                    case "chat", "tab", "chatnamecolor", "tabnamecolor", "chatcolor", "weight" -> {
                        completions = getGroupNames(input);
                    }
                    case "group" -> {
                        completions = getOnlinePlayerNames(input);
                    }
                }
            } else if (args.length == 3) {
                switch (sub) {
                    case "chatnamecolor", "tabnamecolor", "chatcolor" -> {
                        completions = getColorCodes(input);
                    }
                    case "group" -> {
                        completions = getGroupNames(input);
                    }
                }
            }
        }
        return completions;
    }

    private List<String> getGroupNames(String input) {
        return data.getGroups().get().getKeys(false).stream()
                .filter(name -> name.toLowerCase().startsWith(input))
                .toList();
    }

    private List<String> getOnlinePlayerNames(String input) {
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .filter(name -> name.toLowerCase().startsWith(input))
                .toList();
    }

    private List<String> getColorCodes(String input) {
        var codes = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f");
        return codes.stream()
                .filter(code -> code.startsWith(input))
                .toList();
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
    public void groups(CommandSender sender, String label, String[] args) {
        groupsCmd.execute(sender, args);
    }

    @SubCommandMethod
    public void reload(CommandSender sender, String label, String[] args) {
        reloadCmd.execute(sender, args);
    }
}
