package nl.fronsky.prefix.module;

import nl.fronsky.prefix.logic.file.YmlFile;
import nl.fronsky.prefix.logic.module.Module;
import nl.fronsky.prefix.module.commands.Prefix;
import nl.fronsky.prefix.module.events.Chat;
import nl.fronsky.prefix.module.events.Join;
import nl.fronsky.prefix.module.events.Quit;
import nl.fronsky.prefix.module.models.Data;
import nl.fronsky.prefix.module.models.PPlayer;
import nl.fronsky.prefix.module.models.Tablist;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PrefixModule extends Module {
    @Getter @Setter
    private static Data data;

    @Override
    public void onLoad() {
        setData(new Data(new YmlFile("groups"), new YmlFile("players")));
    }

    @Override
    public void onEnable() {
        command(Prefix::new);
        event(() -> new Chat(data));
        event(() -> new Join(data));
        event(Quit::new);

        for (final Player player : Bukkit.getOnlinePlayers()) {
            Tablist.update(new PPlayer(player, data));
        }
    }

    @Override
    public void onDisable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Tablist.remove(player);
        }
    }
}
