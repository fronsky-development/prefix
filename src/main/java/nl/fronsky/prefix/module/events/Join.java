package nl.fronsky.prefix.module.events;

import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PPlayer;
import nl.fronsky.prefix.module.models.Tablist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;

public class Join implements Listener {
    private final Data data;

    public Join(Data data) {
        this.data = data;
    }

    @EventHandler
    public void playerJoinEvent(final PlayerJoinEvent event) {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }
}

