package nl.fronsky.prefix.module.events;

import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PGroup;
import nl.fronsky.prefix.module.models.PPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Set;

/**
 * Handles chat formatting with prefix and name color support.
 * <p>
 * Uses Spigot's {@link AsyncPlayerChatEvent} which is the standard asynchronous
 * chat event in the Bukkit/Spigot API. This event is NOT deprecated in the
 * Spigot API (1.20.6). It is only deprecated in Paper's fork, where
 * {@code io.papermc.paper.event.player.AsyncChatEvent} is the replacement.
 * <p>
 * Paper servers still fire this event for backwards compatibility.
 */
public class Chat implements Listener {
    private final Data data;

    public Chat(Data data) {
        this.data = data;
    }

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent event) {
        var pplayer = new PPlayer(event.getPlayer(), data);
        var message = event.getMessage();
        var messageParts = message.split(" ");
        var result = pplayer.getGroup();

        if (!result.isSuccess()) {
            Logger.severe(result.exception().getMessage());
            event.getPlayer().sendMessage(ColorUtil.colorize("&cAn error occurred while retrieving your group information. Please contact an administrator."));
            return;
        }

        var pgroup = result.value();
        var chatColor = pgroup.getChatColor();
        var format = buildFormat(pplayer, pgroup);

        handleMentions(event.getRecipients(), format, messageParts, chatColor);

        // BUG-003: Escape % characters to prevent format injection exploit
        event.setFormat((format + message).replace("%", "%%"));
    }

    /**
     * Builds the chat format string based on the player's group settings.
     */
    private String buildFormat(PPlayer pplayer, PGroup pgroup) {
        var displayName = pplayer.getPlayer().getDisplayName();

        if (pgroup.getChatPrefix() == null) {
            return ChatColor.GRAY + displayName + ": ";
        } else if (pgroup.getChatPrefix().isEmpty()) {
            return pgroup.getChatNameColor() + displayName + ": " + pgroup.getChatColor();
        } else {
            return ColorUtil.colorize(pgroup.getChatPrefix()) + " "
                    + pgroup.getChatNameColor() + displayName + ": " + pgroup.getChatColor();
        }
    }

    /**
     * Handles @mention highlighting — mentioned players receive a bold version
     * of their name and are removed from the default recipients set.
     */
    private void handleMentions(Set<Player> recipients, String format, String[] messageParts, ChatColor chatColor) {
        var mentionedRecipients = new ArrayList<Player>();
        for (final Player recipient : recipients) {
            var messageFormat = new StringBuilder(format);
            boolean foundName = false;
            for (final String part : messageParts) {
                if (part.equalsIgnoreCase(recipient.getName())) {
                    foundName = true;
                    messageFormat.append(ChatColor.BOLD).append(part)
                            .append(ChatColor.RESET).append(chatColor).append(" ");
                } else {
                    messageFormat.append(part).append(" ");
                }
            }
            if (foundName) {
                mentionedRecipients.add(recipient);
                recipient.sendMessage(messageFormat.toString());
            }
        }
        recipients.removeAll(mentionedRecipients);
    }
}
