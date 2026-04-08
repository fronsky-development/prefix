package nl.fronsky.prefix.logic.file;

import nl.fronsky.prefix.logic.file.interfaces.IFile;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import nl.fronsky.prefix.Main;
import nl.fronsky.prefix.logic.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class YmlFile implements IFile<FileConfiguration> {
    private final Plugin plugin;
    private final String fileName;
    private FileConfiguration configuration = null;
    private File file = null;

    public YmlFile(String fileName) {
        this.fileName = fileName + ".yml";
        this.plugin = Main.getInstance();

        saveDefaultConfig();
    }

    private void saveDefaultConfig() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            plugin.saveResource(fileName, false);
        }
    }

    @Override
    public boolean load() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            return false;
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);
        return true;
    }

    @Override
    public void save() {
        if (this.configuration == null || this.file == null) {
            return;
        }

        try {
            configuration.save(file);
        } catch (IOException e) {
            Logger.severe("Failed to save " + fileName, e);
        }
    }

    @Override
    public void reload() {
        if (this.file == null) {
            this.file = new File(plugin.getDataFolder(), fileName);
        }

        this.configuration = YamlConfiguration.loadConfiguration(file);

        try (InputStream stream = plugin.getResource(fileName)) {
            if (stream != null) {
                try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    var defaults = YamlConfiguration.loadConfiguration(reader);
                    this.configuration.setDefaults(defaults);
                }
            }
        } catch (IOException e) {
            Logger.severe("Failed to reload defaults for " + fileName, e);
        }
    }

    @Override
    public FileConfiguration get() {
        if (this.configuration == null) {
            reload();
        }

        return configuration;
    }
}
