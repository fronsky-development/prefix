package nl.fronsky.prefix.logic.file.interfaces;

public interface IFile<T> {
    /**
     * Loads the data.
     *
     * @return true if the data was successfully loaded, false otherwise.
     */
    boolean load();

    /**
     * Saves the data.
     */
    void save();

    /**
     * Reloads the data.
     */
    void reload();

    /**
     * Retrieves the value.
     *
     * @return the value of type T.
     */
    T get();
}
