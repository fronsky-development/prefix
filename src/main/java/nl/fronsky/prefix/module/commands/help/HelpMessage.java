package nl.fronsky.prefix.module.commands.help;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Data-driven help message system. Each command is defined once and rendered uniformly.
 */
public class HelpMessage {

    private record HelpEntry(String command, String description, String permission, ClickEvent.Action clickAction) {}

    private static final HelpEntry[] ENTRIES = {
            new HelpEntry("/prefix",
                    "Displays your prefix information.",
                    "prefix.command.prefix",
                    ClickEvent.Action.RUN_COMMAND),
            new HelpEntry("/prefix <player>",
                    "Displays prefix information for the specified player.",
                    "prefix.command.prefix.others",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix help",
                    "Displays this help message.",
                    "prefix.command.prefix.help",
                    ClickEvent.Action.RUN_COMMAND),
            new HelpEntry("/prefix chat <group> <prefix>",
                    "Sets the chat prefix for the specified group.",
                    "prefix.command.prefix.chat",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix tab <group> <prefix>",
                    "Sets the tab prefix for the specified group.",
                    "prefix.command.prefix.tab",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix chatnamecolor <group> <color>",
                    "Sets the chat name color for the specified group.",
                    "prefix.command.prefix.chatnamecolor",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix tabnamecolor <group> <color>",
                    "Sets the tab name color for the specified group.",
                    "prefix.command.prefix.tabnamecolor",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix chatcolor <group> <color>",
                    "Sets the chat color for the specified group.",
                    "prefix.command.prefix.chatcolor",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix weight <group> <weight>",
                    "Sets the tab weight for the specified group.",
                    "prefix.command.prefix.weight",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix group <player or group> <group or empty>",
                    "Set the group for the specified player, or for yourself if no second argument is provided.",
                    "prefix.command.prefix.group",
                    ClickEvent.Action.SUGGEST_COMMAND),
            new HelpEntry("/prefix reload",
                    "Reloads the configuration files.",
                    "prefix.command.prefix.reload",
                    ClickEvent.Action.RUN_COMMAND),
    };

    /**
     * Sends all help entries to the given player with hover and click events.
     *
     * @param player the player to send help messages to
     */
    public static void sendAll(Player player) {
        for (HelpEntry entry : ENTRIES) {
            sendEntry(player, entry);
        }
    }

    private static void sendEntry(Player player, HelpEntry entry) {
        var hoverText = new ComponentBuilder(
                ChatColor.WHITE + entry.command() + "\n\n" + ChatColor.GRAY + entry.description() + "\n")
                .append(ChatColor.RED + entry.permission())
                .create();
        var message = new TextComponent(ChatColor.GRAY + "- " + entry.command());
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
        message.setClickEvent(new ClickEvent(entry.clickAction(), entry.command()));
        player.spigot().sendMessage(message);
    }
}
