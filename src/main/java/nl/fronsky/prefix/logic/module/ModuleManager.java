package nl.fronsky.prefix.logic.module;

import lombok.NonNull;
import nl.fronsky.prefix.logic.module.interfaces.IModule;
import nl.fronsky.prefix.logic.utils.Result;
import nl.fronsky.prefix.logic.utils.Status;

import java.util.HashMap;
import java.util.Map;

public class ModuleManager {
    private final Map<Class<? extends IModule>, Module> modules = new HashMap<>();
    private Status moduleStatus = Status.IDLE;

    /**
     * Prepares the given module for use.
     *
     * @param module the module to prepare.
     */
    public void prepare(@NonNull Module module) {
        modules.putIfAbsent(module.getClass(), module);
    }

    /**
     * Loads the module.
     *
     * @throws Exception if an error occurs while loading the module.
     */
    public void load() throws Exception {
        if (!moduleStatus.equals(Status.IDLE)) {
            throw new IllegalStateException("The modules can't be loaded because the ModuleManager is not idle.");
        }
        moduleStatus = Status.LOADING;
        for (Module module : modules.values()) {
            var result = module.load();
            if (!result.isSuccess()) {
                throw result.exception();
            }
        }
        moduleStatus = Status.LOADED;
    }

    /**
     * Enables the module.
     *
     * @throws Exception if an error occurs while enabling the module.
     */
    public void enable() throws Exception {
        if (!moduleStatus.equals(Status.LOADED)) {
            throw new IllegalStateException("The modules can't be enabled because the ModuleManager is not loaded.");
        }
        moduleStatus = Status.ENABLING;
        for (Module module : modules.values()) {
            var result = module.enable();
            if (!result.isSuccess()) {
                throw result.exception();
            }
        }
        moduleStatus = Status.ENABLED;
    }

    /**
     * Enables the module.
     *
     * @throws Exception if an error occurs while enabling the module.
     */
    public void disable() throws Exception {
        if (!moduleStatus.equals(Status.ENABLED)) {
            throw new IllegalStateException("The modules can't be disabled because the ModuleManager is not enabled.");
        }
        moduleStatus = Status.DISABLING;
        for (Module module : modules.values()) {
            var result = module.disable();
            if (!result.isSuccess()) {
                throw result.exception();
            }
        }
        moduleStatus = Status.DISABLED;
    }
}
