package nl.fronsky.prefix.module.events;

import nl.fronsky.prefix.module.models.Tablist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Quit implements Listener {
    @EventHandler
    public void playerQuitEvent(final PlayerQuitEvent event) {
        Tablist.remove(event.getPlayer());
    }
}
