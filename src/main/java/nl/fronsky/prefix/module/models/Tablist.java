package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.Result;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Tablist {

    public static void update(PPlayer pplayer) {
        var player = pplayer.getPlayer();
        Result<Team> result;

        // BUG-020: Call getGroup() only once to avoid double I/O
        if (pplayer.getGroupName() == null || pplayer.getGroupName().isEmpty()) {
            result = getTeam(player.getScoreboard(), 0, "", ChatColor.GRAY, player);
        } else {
            Result<PGroup> groupResult = pplayer.getGroup();
            if (!groupResult.isSuccess()) {
                Logger.severe(groupResult.exception().getMessage());
                return;
            }
            var pgroup = groupResult.value();
            var tabNameColor = pgroup.getTabNameColor() != null ? pgroup.getTabNameColor() : ChatColor.GRAY;
            result = getTeam(player.getScoreboard(), pgroup.getTabWeight(), pgroup.getTabPrefix(), tabNameColor, player);
        }

        if (!result.isSuccess()) {
            Logger.severe(result.exception().getMessage());
            return;
        }

        var team = result.value();
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        if (!team.hasEntry(player.getDisplayName())) {
            team.addEntry(player.getDisplayName());
        }
    }

    public static void remove(Player player) {
        if (player.getScoreboard() == null) {
            return;
        }
        for (Team team : player.getScoreboard().getTeams()) {
            if (team.hasEntry(player.getDisplayName())) {
                team.removeEntry(player.getDisplayName());
            }
        }
    }

    /**
     * Converts weight to a sortable string using zero-padded inverted value.
     * Lower weight = higher priority in tablist.
     */
    private static String getWeight(int weight) {
        return String.format("%010d", Integer.MAX_VALUE - weight);
    }

    private static Result<Team> getTeam(Scoreboard scoreboard, int weight, String prefix, ChatColor nameColor, Player player) {
        if (scoreboard == null) {
            return Result.fail(new Exception("Scoreboard is null!"));
        }

        // UP-006: Removed legacy 15-char limits — modern API supports much longer strings
        var teamName = getWeight(weight) + player.getDisplayName();
        prefix = ChatColor.translateAlternateColorCodes('&', prefix);
        if (!prefix.isEmpty() && !prefix.endsWith(" ")) {
            prefix += " ";
        }

        var team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
        }
        team.setPrefix(prefix);
        team.setColor(nameColor);
        return Result.ok(team);
    }
}
