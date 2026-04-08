package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.file.interfaces.IFile;
import org.bukkit.configuration.file.FileConfiguration;
import lombok.Getter;

@Getter
public class Data {
    private final IFile<FileConfiguration> groups;
    private final IFile<FileConfiguration> players;

    public Data(IFile<FileConfiguration> groups, IFile<FileConfiguration> players) {
        this.groups = groups;
        this.players = players;
    }
}
