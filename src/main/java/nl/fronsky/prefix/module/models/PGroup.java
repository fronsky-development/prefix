package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.utils.ColorUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Represents a prefix group with its associated display properties.
 * <p>
 * Use the static factory methods {@link #loadOrCreate(String, Data)} and
 * {@link #create(String, Data, String, String, ChatColor, ChatColor, ChatColor, int)}
 * instead of calling the constructor directly to handle persistence.
 */
public class PGroup {
    private final Data data;
    @Getter
    private final String name;
    @Getter
    private String chatPrefix;
    @Getter
    private String tabPrefix;
    @Getter
    private ChatColor chatNameColor;
    @Getter
    private ChatColor tabNameColor;
    @Getter
    private ChatColor chatColor;
    @Getter
    private int tabWeight;

    /**
     * Internal constructor — no file I/O. Use factory methods instead.
     */
    private PGroup(String name, Data data, String chatPrefix, String tabPrefix,
                   ChatColor chatNameColor, ChatColor tabNameColor, ChatColor chatColor, int tabWeight) {
        this.name = name;
        this.data = data;
        this.chatPrefix = chatPrefix;
        this.tabPrefix = tabPrefix;
        this.chatNameColor = chatNameColor;
        this.tabNameColor = tabNameColor;
        this.chatColor = chatColor;
        this.tabWeight = tabWeight;
    }

    /**
     * Loads an existing group from YAML, or creates a new one with defaults if it doesn't exist.
     *
     * @param name the group name
     * @param data the data source
     * @return the loaded or newly created PGroup
     */
    public static PGroup loadOrCreate(String name, Data data) {
        var groups = data.getGroups().get();
        if (!groups.contains(name)) {
            groups.set(name + ".chatPrefix", "");
            groups.set(name + ".tabPrefix", "");
            groups.set(name + ".chatNameColor", "&7");
            groups.set(name + ".tabNameColor", "&7");
            groups.set(name + ".chatColor", "&7");
            groups.set(name + ".tabWeight", 0);
            data.getGroups().save();
        }

        var chatPrefix = groups.getString(name + ".chatPrefix");
        var tabPrefix = groups.getString(name + ".tabPrefix");

        var chatNameColor = parseColor(groups.getString(name + ".chatNameColor"));
        var tabNameColor = parseColor(groups.getString(name + ".tabNameColor"));
        var chatColor = parseColor(groups.getString(name + ".chatColor"));
        int tabWeight = groups.getInt(name + ".tabWeight");

        return new PGroup(name, data, chatPrefix, tabPrefix, chatNameColor, tabNameColor, chatColor, tabWeight);
    }

    /**
     * Creates a new group with the given properties and persists it immediately.
     *
     * @param name          the group name
     * @param data          the data source
     * @param chatPrefix    the chat prefix
     * @param tabPrefix     the tab prefix
     * @param chatNameColor the chat name color
     * @param tabNameColor  the tab name color
     * @param chatColor     the chat color
     * @param tabWeight     the tab weight
     * @return the newly created PGroup
     */
    public static PGroup create(String name, Data data, String chatPrefix, String tabPrefix,
                                ChatColor chatNameColor, ChatColor tabNameColor, ChatColor chatColor, int tabWeight) {
        var pgroup = new PGroup(name, data, chatPrefix, tabPrefix, chatNameColor, tabNameColor, chatColor, tabWeight);
        var groups = data.getGroups().get();
        groups.set(name + ".chatPrefix", chatPrefix);
        groups.set(name + ".tabPrefix", tabPrefix);
        groups.set(name + ".chatNameColor", "&" + chatNameColor.getChar());
        groups.set(name + ".tabNameColor", "&" + tabNameColor.getChar());
        groups.set(name + ".chatColor", "&" + chatColor.getChar());
        groups.set(name + ".tabWeight", tabWeight);
        data.getGroups().save();
        return pgroup;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
        data.getGroups().get().set(name + ".chatPrefix", chatPrefix);
        data.getGroups().save();
    }

    public void setTabPrefix(String tabPrefix) {
        this.tabPrefix = tabPrefix;
        data.getGroups().get().set(name + ".tabPrefix", tabPrefix);
        data.getGroups().save();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }

    public void setChatNameColor(ChatColor chatNameColor) {
        this.chatNameColor = chatNameColor;
        data.getGroups().get().set(name + ".chatNameColor", "&" + chatNameColor.getChar());
        data.getGroups().save();
    }

    public void setTabNameColor(ChatColor tabNameColor) {
        this.tabNameColor = tabNameColor;
        data.getGroups().get().set(name + ".tabNameColor", "&" + tabNameColor.getChar());
        data.getGroups().save();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }

    public void setChatColor(ChatColor chatColor) {
        this.chatColor = chatColor;
        data.getGroups().get().set(name + ".chatColor", "&" + chatColor.getChar());
        data.getGroups().save();
    }

    public void setTabWeight(int tabWeight) {
        this.tabWeight = tabWeight;
        data.getGroups().get().set(name + ".tabWeight", tabWeight);
        data.getGroups().save();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }

    /**
     * Parses a color code string (e.g. "&7") into a ChatColor.
     *
     * @param colorStr the color string from YAML
     * @return the parsed ChatColor, or ChatColor.GRAY as default
     */
    private static ChatColor parseColor(String colorStr) {
        if (colorStr != null && colorStr.length() >= 2) {
            return ColorUtil.getChatColor(colorStr.substring(1));
        }
        return ChatColor.GRAY;
    }
}
