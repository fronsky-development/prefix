package nl.fronsky.prefix;

import nl.fronsky.prefix.logic.module.ModuleManager;
import nl.fronsky.prefix.module.PrefixModule;
import org.bukkit.plugin.java.JavaPlugin;
import nl.fronsky.prefix.logic.logging.Logger;

public class Main extends JavaPlugin {
    private final ModuleManager moduleManager = new ModuleManager();


    @Override
    public void onLoad() {
        moduleManager.prepare(new PrefixModule());
        try {
            moduleManager.load();
        } catch (Exception exception) {
            Logger.severe("Failed to load modules!", exception);
        }
    }

    @Override
    public void onEnable() {
        try {
            moduleManager.enable();
        } catch (Exception exception) {
            Logger.severe("Failed to enable modules!", exception);
        }
    }

    @Override
    public void onDisable() {
        try {
            moduleManager.disable();
        } catch (Exception exception) {
            Logger.severe("Failed to disable modules!", exception);
        }
    }

    public static Main getInstance() {
        return getPlugin(Main.class);
    }
}
