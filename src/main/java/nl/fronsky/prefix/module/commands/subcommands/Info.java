package nl.fronsky.prefix.module.commands.subcommands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import nl.fronsky.prefix.Main;
import nl.fronsky.prefix.logic.commands.interfaces.SubCommand;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.utils.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.util.Properties;

public class Info implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        var plugin = Main.getInstance();
        var version = plugin != null ? plugin.getDescription().getVersion() : "unknown";
        var buildId = "unknown";

        if (plugin != null) {
            try (InputStream in = plugin.getResource("prefix-build.properties")) {
                if (in != null) {
                    var props = new Properties();
                    props.load(in);
                    version = props.getProperty("version", version);
                    buildId = props.getProperty("buildId", buildId);
                }
            } catch (Exception ignored) {
            }
        }

        var buildNumber = "FP-" + version.replace(".", "") + "." + buildId;
        var author = "Fronsky";
        var url = "https://fronsky.nl/projects/prefix";

        Logger.sendMessage(sender, "&8<--------- &c&lInfo &r&8--------->");
        Logger.sendMessage(sender, "&7Version: &f" + version);
        Logger.sendMessage(sender, "&7Build: &f" + buildNumber);
        Logger.sendMessage(sender, "&7Author: &f" + author);

        if (sender instanceof Player player) {
            var message = new TextComponent(ColorUtil.colorize("&7Website: &f" + url));
            message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
            player.spigot().sendMessage(message);
        } else {
            Logger.sendMessage(sender, "&7Website: &f" + url);
        }

        Logger.sendMessage(sender, "&8<----------------------------->");
    }
}

