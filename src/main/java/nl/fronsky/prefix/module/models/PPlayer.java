package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.utils.Result;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PPlayer {
    private final Data data;
    @Getter @Setter
    private Player player;
    @Getter
    private String groupName;

    public PPlayer(Player player, Data data) {
        this.player = player;
        this.data = data;
        var players = data.getPlayers().get();
        var uuid = player.getUniqueId().toString();
        if (!players.contains(uuid)) {
            players.set(uuid + ".group", "");
            data.getPlayers().save();
            groupName = "";
        } else {
            groupName = players.getString(uuid + ".group");
        }
    }

    /**
     * Gets the player's group.
     *
     * @return a Result containing the PGroup, or an error if the group doesn't exist
     */
    public Result<PGroup> getGroup() {
        if (groupName == null || groupName.isEmpty() || !data.getGroups().get().contains(groupName)) {
            return Result.fail(new Exception("The group '" + groupName + "' does not exist."));
        }
        return Result.ok(PGroup.loadOrCreate(groupName, data));
    }

    /**
     * Sets the player's group.
     *
     * @param groupName the name of the group to assign
     * @return true if the group was set, false if it doesn't exist
     */
    public boolean setGroup(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            this.groupName = "";
            data.getPlayers().get().set(player.getUniqueId() + ".group", "");
            data.getPlayers().save();
            updateAllTablists();
            return true;
        }
        if (!data.getGroups().get().contains(groupName)) {
            return false;
        }
        this.groupName = groupName;
        data.getPlayers().get().set(player.getUniqueId() + ".group", groupName);
        data.getPlayers().save();
        updateAllTablists();
        return true;
    }

    private void updateAllTablists() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(online, data));
        }
    }
}
