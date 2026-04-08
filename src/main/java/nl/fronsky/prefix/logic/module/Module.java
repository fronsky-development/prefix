package nl.fronsky.prefix.logic.module;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import nl.fronsky.prefix.Main;
import nl.fronsky.prefix.logic.commands.CommandHandler;
import nl.fronsky.prefix.logic.logging.Logger;
import nl.fronsky.prefix.logic.module.interfaces.IModule;
import nl.fronsky.prefix.logic.tasks.ITask;
import nl.fronsky.prefix.logic.utils.Result;
import nl.fronsky.prefix.logic.utils.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class Module implements IModule {
    private final Main mainClass;
    private final String moduleName;
    private final List<Listener> events;
    private final List<CommandHandler> commands;
    private final List<ITask> tasks;
    private final CommandMap commandMap;
    private final String STATUS_NOT_ENABLING;
    private Status moduleStatus;

    protected Module() {
        mainClass = Main.getInstance();
        moduleName = this.getClass().getSimpleName();
        STATUS_NOT_ENABLING = "The " + moduleName + " status is not ENABLING.";

        // CQ-010: Reflection is required because Bukkit.getCommandMap() is not in the Spigot 1.20 API.
        // This will be replaced with a direct API call when the method is available in the target API version.
        CommandMap tempCommandMap;
        try {
            var method = Bukkit.getServer().getClass().getDeclaredMethod("getCommandMap");
            tempCommandMap = (CommandMap) method.invoke(Bukkit.getServer());
        } catch (ReflectiveOperationException e) {
            tempCommandMap = null;
            Logger.severe("Failed to obtain CommandMap.", e);
        }

        if (tempCommandMap == null) {
            moduleStatus = Status.DISABLED;
            Logger.severe("Failed to obtain CommandMap. Shutting down server.");
            Bukkit.shutdown();
            // BUG-005: Assign fields to safe defaults and return early to prevent further initialization
            commandMap = null;
            events = new ArrayList<>();
            commands = new ArrayList<>();
            tasks = new ArrayList<>();
            return;
        }

        commandMap = tempCommandMap;
        events = new ArrayList<>();
        commands = new ArrayList<>();
        tasks = new ArrayList<>();
        moduleStatus = Status.IDLE;
    }

    /**
     * Loads the module and returns the result.
     *
     * @return The result of the load operation.
     */
    public Result<String> load() {
        if (!moduleStatus.equals(Status.IDLE)) {
            return Result.fail(new Exception("An attempt was made to load the " + moduleName + " while it was not idle."));
        }

        moduleStatus = Status.LOADING;
        Logger.info("Loading " + moduleName + "...");
        onLoad();
        moduleStatus = Status.LOADED;
        return Result.ok("Module has been successfully loaded.");
    }

    /**
     * Enables the module and returns the result.
     *
     * @return The result of the enable operation.
     */
    public Result<String> enable() {
        if (!moduleStatus.equals(Status.LOADED)) {
            return Result.fail(new Exception("An attempt was made to enable the " + moduleName + " while it was not loaded."));
        }

        moduleStatus = Status.ENABLING;
        Logger.info("Enabling " + moduleName + "...");
        try {
            onEnable();
            moduleStatus = Status.ENABLED;
            return Result.ok("Module has been successfully enabled.");
        } catch (Exception e) {
            moduleStatus = Status.DISABLING;
            Logger.severe(e.getMessage(), e);
            Bukkit.shutdown();
            moduleStatus = Status.DISABLED;
            return Result.fail(e);
        }
    }

    /**
     * Disables the module and returns the result.
     *
     * @return The result of the disable operation.
     */
    public Result<String> disable() {
        if (!moduleStatus.equals(Status.ENABLED)) {
            return Result.fail(new Exception("An attempt was made to disable the " + moduleName + " while it was not enabled."));
        }

        moduleStatus = Status.DISABLING;
        int amountOfComponents = events.size() + commands.size();
        Logger.info("Disabling " + moduleName + ", removing " + amountOfComponents + " components...");

        events.forEach(HandlerList::unregisterAll);
        for (CommandHandler commandHandler : commands) {
            PluginCommand pluginCommand = mainClass.getCommand(commandHandler.getName());
            if (pluginCommand != null) {
                pluginCommand.unregister(commandMap);
            }
        }

        for (ITask task : tasks) {
            task.disable();
        }

        events.clear();
        commands.clear();
        tasks.clear();
        onDisable();
        moduleStatus = Status.DISABLED;
        return Result.ok("Module has been successfully disabled.");
    }

    /**
     * Registers an event listener supplied by the given supplier.
     *
     * @param supplier the supplier of the event listener.
     * @throws RuntimeException if the module is not in the enabling state.
     */
    protected void event(@NonNull Supplier<? extends Listener> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new IllegalStateException(STATUS_NOT_ENABLING);
        }

        Listener listener = supplier.get();
        Bukkit.getServer().getPluginManager().registerEvents(listener, mainClass);
        events.add(listener);
    }

    /**
     * Registers a command handler supplied by the given supplier.
     *
     * @param supplier the supplier of the command handler.
     * @throws RuntimeException if the module is not in the enabling state or if the plugin command is null.
     */
    protected void command(@NonNull Supplier<? extends CommandHandler> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new IllegalStateException(STATUS_NOT_ENABLING);
        }

        CommandHandler commandHandler = supplier.get();
        PluginCommand pluginCommand = mainClass.getCommand(commandHandler.getName());
        if (pluginCommand == null) {
            throw new NullPointerException("The plugin command is null.");
        }

        pluginCommand.setExecutor(commandHandler);
        pluginCommand.setTabCompleter(commandHandler);
        commands.add(commandHandler);
    }

    /**
     * Schedules a task provided by the given supplier.
     *
     * @param supplier the supplier of the task.
     * @throws RuntimeException if the module is not in the enabling state.
     */
    protected void task(@NonNull Supplier<? extends ITask> supplier) {
        if (!moduleStatus.equals(Status.ENABLING)) {
            throw new IllegalStateException(STATUS_NOT_ENABLING);
        }

        ITask task = supplier.get();
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                task.run();
            }
        };

        long delay = task.getDelay();
        long period = task.getPeriod();
        boolean isAsync = task.isAsync();
        if (isAsync) {
            runnable.runTaskTimerAsynchronously(mainClass, delay, period);
        } else {
            runnable.runTaskTimer(mainClass, delay, period);
        }

        tasks.add(task);
    }
}
