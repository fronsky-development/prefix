package nl.fronsky.prefix.module.models;

import nl.fronsky.prefix.logic.file.interfaces.IFile;
import org.bukkit.configuration.file.FileConfiguration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Holds the plugin's data files for groups, players, config, and messages.
 */
@Getter
@RequiredArgsConstructor
public class Data {
    private final IFile<FileConfiguration> groups;
    private final IFile<FileConfiguration> players;
    private final IFile<FileConfiguration> config;
    private final IFile<FileConfiguration> messages;
}
