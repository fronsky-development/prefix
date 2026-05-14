package nl.fronsky.prefix.logic.tasks;

public interface ITask {
    /**
     * Executes the task. The specific action performed during the execution depends on the implementation.
     */
    void run();

    /**
     * Disables the task, preventing it from being executed if it is scheduled to run in the future.
     * If the task is currently running, implementations may choose to allow it to complete or to terminate it.
     */
    void disable();

    /**
     * Retrieves the delay before the task is scheduled to run.
     *
     * @return the delay in server ticks (20 ticks = 1 second) before the task is scheduled to run.
     */
    long getDelay();

    /**
     * Retrieves the period between consecutive executions of the task.
     *
     * @return the period in server ticks (20 ticks = 1 second) between consecutive executions of the task.
     *         If the task does not repeat, the return value should appropriately indicate this (commonly -1 or 0).
     */
    long getPeriod();

    /**
     * Checks if the task is scheduled to be run asynchronously.
     *
     * @return {@code true} if the task is scheduled to be run asynchronously, {@code false} otherwise.
     */
    boolean isAsync();
}
