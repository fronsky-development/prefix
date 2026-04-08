package nl.fronsky.prefix.logic.module.interfaces;

public interface IModule {
    /**
     * Called after a module is loaded but before it has been enabled.
     * <p>
     * When multiple modules are loaded, the onLoad() for all modules is
     * called before any onEnable() is called.
     */
    void onLoad();

    /**
     * Called when this module is enabled.
     */
    void onEnable();

    /**
     * Called when this module is disabled.
     */
    void onDisable();
}
