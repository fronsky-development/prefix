package nl.fronsky.prefix.module.commands.subcommands;

import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;

import java.util.Set;

/**
 * Subcommand handler for {@code /prefix groups}.
 * Lists all groups with their prefixes, colors, and player counts.
 */
@RequiredArgsConstructor
public class Groups implements SubCommand {
    private final Data data;


    @Override
    public void execute(CommandSender sender, String[] args) {
        var groups = data.getGroups().get();
        Set<String> groupNames = groups.getKeys(false);

        if (groupNames.isEmpty()) {
            Logger.sendMessage(sender, "&cNo groups have been configured.");
            return;
        }

        Logger.sendMessage(sender, "&8<--------------- &cPrefix Groups &8-------------->");

        for (String groupName : groupNames) {
            var pgroup = PGroup.loadOrCreate(groupName, data);
            int playerCount = countPlayersInGroup(groupName);
            String chatPrefix = pgroup.getChatPrefix() != null && !pgroup.getChatPrefix().isEmpty()
                    ? ColorUtil.colorize(pgroup.getChatPrefix())
                    : "&7(none)";

            Logger.sendMessage(sender, "&f" + groupName
                    + " &8| &fPrefix: " + chatPrefix
                    + " &8| &fName: &" + pgroup.getChatNameColor().getChar() + "Color"
                    + " &8| &fChat: &" + pgroup.getChatColor().getChar() + "Color"
                    + " &8| &fPlayers: &7" + playerCount);
        }

        Logger.sendMessage(sender, "&8<----------------------------------------->");
    }

    private int countPlayersInGroup(String groupName) {
        var players = data.getPlayers().get();
        int count = 0;
        for (String uuid : players.getKeys(false)) {
            String group = players.getString(uuid + ".group");
            if (groupName.equals(group)) {
                count++;
            }
        }
        return count;
    }
}

